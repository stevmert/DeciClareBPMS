package miner.log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import miner.Config;
import model.resource.DirectResource;
import model.resource.Resource;
import model.resource.ResourceRole;
import util.xml.AtomicNode;
import util.xml.Node;
import util.xml.ParentNode;

public class ResourceEvent implements Comparable<ResourceEvent>, Serializable {

	private static final long serialVersionUID = -2682207810555521960L;

	private Resource resource;
	private int nrOfInstances;
	private long start;
	private long end;

	public ResourceEvent(Resource resource, int nrOfInstances, long start, long end) {
		super();
		if(resource == null
				|| nrOfInstances < 1
				|| start < 0
				|| end < 0
				|| start > end)
			throw new IllegalArgumentException("Invalid ResourceEvent: "
					+ resource + " " + nrOfInstances + " " + start + " " + end);
		this.resource = resource;
		this.nrOfInstances = nrOfInstances;
		this.start = start;
		this.end = end;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getNrOfInstances() {
		return nrOfInstances;
	}

	public void setNrOfInstances(int nrOfInstances) {
		this.nrOfInstances = nrOfInstances;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public int compareTo(ResourceEvent otherEvent) {
		if(this.getStart() < otherEvent.getStart())
			return -1;
		if(this.getStart() > otherEvent.getStart())
			return 1;
		if(this.getEnd() < otherEvent.getEnd())
			return -1;
		if(this.getEnd() > otherEvent.getEnd())
			return 1;
		return 0;
	}

	@Override
	public String toString() {
		return "(" + start + "," + end + "): " + getNrOfInstances() + " instances of " + getResource();
	}

	public Node getXesNode() {
		HashMap<String, String> attrs = new HashMap<>();
		if(getResource() instanceof DirectResource)
			attrs.put("key", "\"org:resource\"");
		else
			attrs.put("key", "\"org:role\"");
		attrs.put("value", "\""+getResource().getName()+"\"");
		//TODO: add nrOfInstances...
		//		LocalDateTime time = Config.BASE_DATETIME.plusSeconds(getTime());
		//		attrs.put("time:timestamp", "\""+time+"\"");
		return new AtomicNode("string", attrs);
	}

	public Node getXesNode_extended(int count) {
		HashMap<String, String> attrs = new HashMap<>();
		if(getResource() instanceof DirectResource)
			attrs.put("key", "\"deci:resource" + count + "\"");
		else
			attrs.put("key", "\"deci:role" + count + "\"");
		attrs.put("value", "\""+getResource().getName()+"\"");
		//		LocalDateTime time = Config.BASE_DATETIME.plusSeconds(getTime());
		//		attrs.put("time:timestamp", "\""+time+"\"");
		return new AtomicNode("string", attrs);
	}

	public ParentNode getXesNode_extended2() {
		ParentNode eventNode = new ParentNode("event", new HashMap<String, String>(),
				new ArrayList<Node>());
		{
			HashMap<String, String> attrs = new HashMap<>();
			attrs.put("key", "\"deci2:eventtype\"");
			attrs.put("value", "\"resource\"");
			eventNode.getChildNodes().add(new AtomicNode("string", attrs));
		}
		eventNode.getChildNodes().add(getXesNode());
		{
			HashMap<String, String> attrs = new HashMap<>();
			attrs.put("key", "\"deci2:timestamp_start\"");
			LocalDateTime time = Config.BASE_DATETIME.plusSeconds(getStart());
			attrs.put("value", "\""+time+"\"");
			eventNode.getChildNodes().add(new AtomicNode("date", attrs));
		}
		{
			HashMap<String, String> attrs = new HashMap<>();
			attrs.put("key", "\"deci2:timestamp_end\"");
			LocalDateTime time = Config.BASE_DATETIME.plusSeconds(getEnd());
			attrs.put("value", "\""+time+"\"");
			eventNode.getChildNodes().add(new AtomicNode("date", attrs));
		}
		return eventNode;
	}

	public static ResourceEvent getEvent_XES(ParentNode node, LocalDateTime base_time) {
		String resourceName = node.getValueFromNodeKey("org:resource");//DirectResource
		Resource resource = null;
		if(resourceName != null)
			resource = new DirectResource(resourceName);
		else {
			resourceName = node.getValueFromNodeKey("org:role");
			//			//availability
			//			String avString = null;
			//			for(Node n : node.getChildNodes()) {
			//				AttributeNode an = (AttributeNode) n;
			//				if(an.getAttributes().get("availability") != null) {
			//					avString = an.getAttributes().get("availability").replace("\"", "");
			//					break;
			//				}
			//			}
			//			int availability = 1;//default availability
			//			if(avString != null)
			//				availability = Integer.parseInt(avString);
			resource = new ResourceRole(resourceName);//ResourceRole
		}
		String dateTimeString = node.getValueFromNodeKey("time:timestamp");
		if(dateTimeString.contains("."))
			dateTimeString = dateTimeString.substring(0, dateTimeString.indexOf("."));
		LocalDateTime ldt_end = LocalDateTime.parse(dateTimeString);
		long end = base_time.until(ldt_end, ChronoUnit.SECONDS);
		//start round down
		LocalDateTime ldt_start = ldt_end.minusMinutes(1);
		long start = base_time.until(ldt_start, ChronoUnit.SECONDS);
		//TODO: nrOfInstances
		return new ResourceEvent(resource, 1, start, end);
	}

	public static ResourceEvent getEvent_XESExtended(ParentNode node, LocalDateTime base_time) {
		Resource resource = null;
		if(node.getAttributes().get("key").startsWith("\"deci:resource"))//DirectResource
			resource = new DirectResource(node.getAttributes().get("value").replace("\"", ""));
		else {//ResourceRole
			//			String avString = node.getAttributes().get("availability");
			//			int availability = 1;//default availability
			//			if(avString != null)
			//				availability = Integer.parseInt(avString.replace("\"", ""));
			resource = new ResourceRole(node.getAttributes().get("value").replace("\"", ""));
		}
		//start round down
		String dateTimeString1 = node.getValueFromNodeKey("deci:timestamp_start");
		if(dateTimeString1.contains("."))
			dateTimeString1 = dateTimeString1.substring(0, dateTimeString1.indexOf("."));
		LocalDateTime ldt_start = LocalDateTime.parse(dateTimeString1);
		long start = base_time.until(ldt_start, ChronoUnit.SECONDS);
		//end round up
		String dateTimeString2 = node.getValueFromNodeKey("deci:timestamp_end");
		if(dateTimeString2.contains("."))
			dateTimeString2 = dateTimeString2.substring(0, dateTimeString2.indexOf("."));
		LocalDateTime ldt_end = LocalDateTime.parse(dateTimeString2);
		long end = base_time.until(ldt_end, ChronoUnit.SECONDS);
		//TODO: nrOfInstances
		return new ResourceEvent(resource, 1, start, end);
	}

	//TODO
	public static ResourceEvent getEvent_XESExtended2(ParentNode node, LocalDateTime base_time) {
		String resourceName = node.getValueFromNodeKey("org:resource");//DirectResource
		Resource resource = null;
		if(resourceName != null)
			resource = new DirectResource(resourceName);
		else {
			resourceName = node.getValueFromNodeKey("org:role");
			//			//availability
			//			String avString = null;
			//			for(Node n : node.getChildNodes()) {
			//				AttributeNode an = (AttributeNode) n;
			//				if(an.getAttributes().get("availability") != null) {
			//					avString = an.getAttributes().get("availability").replace("\"", "");
			//					break;
			//				}
			//			}
			//			int availability = 1;//default availability
			//			if(avString != null)
			//				availability = Integer.parseInt(avString);
			resource = new ResourceRole(resourceName);//ResourceRole
		}
		//start round down
		String dateTimeString1 = node.getValueFromNodeKey("deci2:timestamp_start");
		if(dateTimeString1.contains("."))
			dateTimeString1 = dateTimeString1.substring(0, dateTimeString1.indexOf("."));
		LocalDateTime ldt_start = LocalDateTime.parse(dateTimeString1);
		long start = base_time.until(ldt_start, ChronoUnit.SECONDS);
		//end round up
		String dateTimeString2 = node.getValueFromNodeKey("deci2:timestamp_end");
		if(dateTimeString2.contains("."))
			dateTimeString2 = dateTimeString2.substring(0, dateTimeString2.indexOf("."));
		LocalDateTime ldt_end = LocalDateTime.parse(dateTimeString2);
		long end = base_time.until(ldt_end, ChronoUnit.SECONDS);
		//TODO: nrOfInstances
		return new ResourceEvent(resource, 1, start, end);
	}
}