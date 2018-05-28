package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import miner.log.ActivityEvent;
import miner.log.DataEvent;
import miner.log.ResourceEvent;
import miner.log.Trace;
import miner.rule.SingleRule;
import model.Activity;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.ParsingCache;
import model.constraint.ResourceConstraint;
import model.constraint.existence.ActivityAvailabilitySchedule;
import model.constraint.relation.AtMostLag;
import model.constraint.resource.AtLeastAvailable;
import model.constraint.resource.AtLeastUsage;
import model.constraint.resource.AtMostAvailable;
import model.constraint.resource.AtMostUsage;
import model.constraint.resource.DecisionAuthorization;
import model.constraint.resource.ResourceAvailabilitySchedule;
import model.constraint.resource.ResourceEquality;
import model.constraint.resource.SimultaneousCapacity;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.expression.AtomicResourceExpression;
import model.resource.DirectResource;
import model.resource.Resource;
import model.resource.ResourceRole;

public class DeciClareEngine {

	private ArrayList<Constraint> model;
	private long currentTime;
	private Trace trace;
	private ArrayList<Activity> activities;
	private ArrayList<Resource> resources;
	private Resource activeResource;
	private HashMap<Resource, Integer> resourceUsage_lowerlimits;
	private HashMap<Resource, Integer> resourceUsage_upperlimits;
	private HashMap<Resource, Integer> resourceUsage;
	private ArrayList<BooleanDataAttribute> boolDataElems;
	private ArrayList<CategoricalDataAttribute> catDataElems;
	private String currentRestrictions;

	public DeciClareEngine(ArrayList<Constraint> model) {
		this.model = model;
		this.currentTime = 0;
		trace = new Trace();
		activities = getActivities(model);
		resources = getResources(model);
		setResourceUsage();
	}

	public ArrayList<Constraint> getModel() {
		return model;
	}

	public void setModel(ArrayList<Constraint> model) {
		this.model = model;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public Resource getActiveResource() {
		return activeResource;
	}

	public void setActiveResource(Resource activeResource) {
		this.activeResource = activeResource;
	}

	public HashMap<Resource, Integer> getResourceUsage_lowerlimits() {
		return resourceUsage_lowerlimits;
	}

	public void setResourceUsage_lowerlimits(HashMap<Resource, Integer> resourceUsage_lowerlimits) {
		this.resourceUsage_lowerlimits = resourceUsage_lowerlimits;
	}

	public HashMap<Resource, Integer> getResourceUsage_upperlimits() {
		return resourceUsage_upperlimits;
	}

	public void setResourceUsage_upperlimits(HashMap<Resource, Integer> resourceUsage_upperlimits) {
		this.resourceUsage_upperlimits = resourceUsage_upperlimits;
	}

	public HashMap<Resource, Integer> getResourceUsage() {
		return resourceUsage;
	}

	public void setResourceUsage(HashMap<Resource, Integer> resourceUsage) {
		this.resourceUsage = resourceUsage;
	}

	public ArrayList<BooleanDataAttribute> getBoolDataElems() {
		return boolDataElems;
	}

	public void setBoolDataElems(ArrayList<BooleanDataAttribute> boolDataElems) {
		this.boolDataElems = boolDataElems;
	}

	public ArrayList<CategoricalDataAttribute> getCatDataElems() {
		return catDataElems;
	}

	public void setCatDataElems(ArrayList<CategoricalDataAttribute> catDataElems) {
		this.catDataElems = catDataElems;
	}

	public String getCurrentRestrictions() {
		return currentRestrictions;
	}

	public void setCurrentRestrictions(String currentRestrictions) {
		this.currentRestrictions = currentRestrictions;
	}

	public Trace getTrace() {
		return trace;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}

	public ArrayList<Resource> getResources() {
		return resources;
	}

	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}

	protected void setTrace(Trace trace) {
		this.trace = trace;
		if(trace.getActivityEvents().isEmpty())
			currentTime = 0;
		else
			currentTime = trace.getActivityEvents().get(trace.getActivityEvents().size() - 1).getEnd();
		currentRestrictions = null;
		String restrictions = validate(trace);
		if(restrictions != null)
			currentRestrictions = restrictions.substring(restrictions.indexOf("\n")).trim();
	}

	public ArrayList<Resource> getExpandedResources() {
		ArrayList<Resource> tmp = new ArrayList<>();
		for(Resource r : getResources())
			addAllResources(r, tmp);
		return tmp;
	}

	private void addAllResources(Resource r, ArrayList<Resource> tmp) {
		if(r instanceof ResourceRole)
			addAllResources((ResourceRole) r, tmp);
		else
			tmp.add(r);
	}

	private void addAllResources(ResourceRole r, ArrayList<Resource> tmp) {
		tmp.add(r);
		for(DirectResource x : r.getContainedResources())
			addAllResources(x, tmp);
		for(ResourceRole x : r.getSubRoles())
			addAllResources(x, tmp);
	}

	public HashSet<DataAttribute> getCurrentSituation() {
		HashSet<DataAttribute> currentSituation = new HashSet<DataAttribute>();
		for(DataEvent de : trace.getDataEvents()) {
			if(de.isActivated()) {
				if(!currentSituation.contains(de.getDataElement())) {
					if(de.getDataElement() instanceof BooleanDataAttribute) {
						DataAttribute toDelete = null;
						for(DataAttribute x : currentSituation)
							if(x instanceof BooleanDataAttribute
									&& x.getName().equals(de.getDataElement().getName()))
								toDelete = x;
						if(toDelete != null)
							currentSituation.remove(toDelete);
					}
					currentSituation.add(de.getDataElement());
				}
			} else
				currentSituation.remove(de.getDataElement());
		}
		return currentSituation;
	}

	public boolean addDataEvent(BooleanDataAttribute da, Boolean value, long time, ArrayList<DataEvent> cachedDataEvents) {
		ArrayList<DataEvent> tmp;
		ArrayList<DataEvent> searchList;
		if(cachedDataEvents == null) {
			tmp = trace.getDataEvents();
			searchList = trace.getDataEvents();
		} else {
			tmp = cachedDataEvents;
			searchList = new ArrayList<>(trace.getDataEvents());
			searchList.addAll(cachedDataEvents);
		}
		DataEvent same = null;
		for(int i = searchList.size()-1; i >=0; i--) {
			DataEvent de = searchList.get(i);
			if(de.getTime() >= time
					&& de.getDataElement() instanceof BooleanDataAttribute
					&& de.getDataElement().getName().equals(da.getName())) {
				same = de;
				break;
			}
		}
		DataEvent prev = null;
		for(int i = searchList.size()-1; i >=0; i--) {
			DataEvent de = searchList.get(i);
			if(de.getTime() < time
					&& de.getDataElement() instanceof BooleanDataAttribute
					&& de.getDataElement().getName().equals(da.getName())) {
				prev = de;
				break;
			}
		}
		if(same != null) {
			if(value != null
					&& ((BooleanDataAttribute) same.getDataElement()).getValue() == value
					&& same.isActivated())
				return false;
			tmp.remove(same);
			if(value == null && (prev == null || !prev.isActivated())
					|| (!same.isActivated() && value != null && ((BooleanDataAttribute) same.getDataElement()).getValue() == value))
				return cachedDataEvents == null;
		}
		if(prev == null) {
			if(value == null)
				return false;
			if(da.getValue() == value)
				tmp.add(getDataEvent(da, true, time));
			else
				tmp.add(getDataEvent(new BooleanDataAttribute(da.getName(), value, da.getParent()), true, time));
		} else {
			boolean isActivation = true;
			boolean val = ((BooleanDataAttribute) prev.getDataElement()).getValue();
			if(value == null)
				isActivation = false;
			else
				val = value;
			if(((BooleanDataAttribute) prev.getDataElement()).getValue() == val
					&& prev.isActivated() == isActivation)
				;//do nothing
			else if(da.getValue() == val)
				tmp.add(getDataEvent(da, isActivation, time));
			else
				tmp.add(getDataEvent(new BooleanDataAttribute(da.getName(), val, da.getParent()), isActivation, time));
		}
		Collections.sort(tmp);
		return cachedDataEvents == null;
	}

	public boolean addDataEvent(CategoricalDataAttribute cda, String value, boolean isActivated, long time, ArrayList<DataEvent> cachedDataEvents) {
		ArrayList<DataEvent> tmp;
		if(cachedDataEvents == null)
			tmp = trace.getDataEvents();
		else
			tmp = cachedDataEvents;
		DataEvent same = null;
		for(int i = tmp.size()-1; i >=0; i--) {
			DataEvent de = tmp.get(i);
			if(de.getTime() >= time
					&& de.getDataElement() instanceof CategoricalDataAttribute
					&& de.getDataElement().getName().equals(cda.getName())
					&& ((CategoricalDataAttribute) de.getDataElement()).getValue().equals(value)) {
				same = de;
				break;
			}
		}
		if(same != null) {
			if(same.isActivated() == isActivated)
				return false;
			tmp.remove(same);
		} else if(cda.getValue().equals(value))
			tmp.add(getDataEvent(cda, isActivated, time));
		else
			tmp.add(getDataEvent(new CategoricalDataAttribute(cda.getName(), cda.getValues(), value, cda.getParent()), isActivated, time));
		Collections.sort(tmp);
		return cachedDataEvents == null;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<? extends Constraint>[] getResourceSubmodel(Activity a) {
		ArrayList<Constraint> subModel = new ArrayList<>();
		ArrayList<AtLeastUsage> usages_atL = new ArrayList<>();
		ArrayList<AtMostUsage> usages_atM = new ArrayList<>();
		for(Constraint c : model) {
			if(c instanceof AtLeastUsage
					&& ((AtLeastUsage) c).getActivity().equals(a)
					&& c.getActivationTime(trace) != -1)
				usages_atL.add((AtLeastUsage) c);
			else if(c instanceof AtMostUsage
					&& ((AtMostUsage) c).getActivity().equals(a)
					&& c.getActivationTime(trace) != -1)
				usages_atM.add((AtMostUsage) c);
			else if((c instanceof AtMostAvailable
					|| c instanceof AtLeastAvailable)
					&& c.getActivationTime(trace) != -1)
				subModel.add(c);
		}
		subModel.addAll(0, usages_atM);
		subModel.addAll(0, usages_atL);
		return new ArrayList[]{subModel, usages_atL, usages_atM};
	}

	public int getLowerUsage(Resource r, ArrayList<AtLeastUsage> usages_atL) {
		int low = 0;
		for(AtLeastUsage atL : usages_atL)
			if(atL.getResourceExpression() instanceof AtomicResourceExpression
					&& atL.getUsedResources().contains(r))
				low = Math.max(low, atL.getBound());
		return low;
	}

	public Integer getUpperUsage(Resource r, ArrayList<AtMostUsage> usages_atM) {
		Integer up = resourceUsage_upperlimits.get(r);
		for(AtMostUsage atM : usages_atM)
			if(atM.getResourceExpression() instanceof AtomicResourceExpression
					&& atM.getUsedResources().contains(r)) {
				if(up == null)
					up = atM.getBound();
				else
					up = Math.min(up, atM.getBound());
			}
		return up;
	}

	public void validate(ArrayList<Activity> sectionA_satisfaction, ArrayList<Activity> sectionB_possibleActivitySatisfaction,
			ArrayList<Activity> sectionC_resourceViolation, ArrayList<Activity> sectionD_timeViolation, ArrayList<Activity> sectionE_violation,
			HashMap<Activity, String> explanations, HashSet<Activity> deadlines, HashSet<Activity> delays, HashSet<Constraint> deadendConstraints) {
		for(Activity a : activities) {
			ArrayList<ActivityEvent> potentialTraceActs = new ArrayList<>(trace.getActivityEvents());
			potentialTraceActs.add(getActivityEvent(a, 2));
			ArrayList<ResourceEvent> potentialResourceEvents = new ArrayList<>(trace.getResourceEvents());
			potentialResourceEvents.addAll(getResourceEvents(a, 2));
			Trace potentialTrace = new Trace(potentialTraceActs, trace.getDataEvents(), potentialResourceEvents);
			ArrayList<Constraint> generalViolations = new ArrayList<>();
			ArrayList<Constraint> timeViolations = new ArrayList<>();
			ArrayList<Constraint> possibleActivityViolations = new ArrayList<>();
			ArrayList<Constraint> resourceViolations = new ArrayList<>();
			HashMap<Constraint, Long> deadlinesA = new HashMap<>();
			HashMap<Constraint, Long> delaysA = new HashMap<>();
			String explanation = validate(resourceUsage, sectionA_satisfaction, sectionB_possibleActivitySatisfaction,
					sectionC_resourceViolation, sectionD_timeViolation, sectionE_violation, deadlines, delays, deadendConstraints, a,
					potentialTrace, generalViolations, timeViolations, possibleActivityViolations, resourceViolations,
					deadlinesA, delaysA);
			if(explanation != null)
				explanations.put(a, explanation);
		}
	}

	private String validate(Trace potentialTrace) {
		return validate(new HashMap<>(), null, null, null, null, null, null, null, null, null, potentialTrace,
				new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null);
	}

	private String validate(HashMap<Resource, Integer> resourceUsage, ArrayList<Activity> sectionA_satisfaction,
			ArrayList<Activity> sectionB_possibleActivitySatisfaction, ArrayList<Activity> sectionC_resourceViolation,
			ArrayList<Activity> sectionD_timeViolation, ArrayList<Activity> sectionE_violation, HashSet<Activity> deadlines,
			HashSet<Activity> delays, HashSet<Constraint> deadendConstraints, Activity a, Trace potentialTrace,
			ArrayList<Constraint> generalViolations, ArrayList<Constraint> timeViolations,
			ArrayList<Constraint> possibleActivityViolations, ArrayList<Constraint> resourceViolations,
			HashMap<Constraint, Long> deadlinesA, HashMap<Constraint, Long> delaysA) {
		return validate(model, activeResource, currentTime, resourceUsage, sectionA_satisfaction,
				sectionB_possibleActivitySatisfaction, sectionC_resourceViolation, sectionD_timeViolation, sectionE_violation,
				deadlines, delays, deadendConstraints, a, potentialTrace, generalViolations, timeViolations,
				possibleActivityViolations, resourceViolations, deadlinesA, delaysA);
	}

	private String validate(ArrayList<Constraint> model, long currentTime, Trace potentialTrace) {
		return validate(model, activeResource, currentTime,
				new HashMap<>(), null, null, null, null, null, null, null, null,
				potentialTrace.getActivityEvents().get(potentialTrace.getActivityEvents().size()-1),
				potentialTrace, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null);
	}

	private static String validate(ArrayList<Constraint> model, Resource activeResource, long currentTime,
			HashMap<Resource, Integer> resourceUsage, ArrayList<Activity> sectionA_satisfaction,
			ArrayList<Activity> sectionB_possibleActivitySatisfaction, ArrayList<Activity> sectionC_resourceViolation,
			ArrayList<Activity> sectionD_timeViolation, ArrayList<Activity> sectionE_violation, HashSet<Activity> deadlines,
			HashSet<Activity> delays, HashSet<Constraint> deadendConstraints, Activity a, Trace potentialTrace,
			ArrayList<Constraint> generalViolations, ArrayList<Constraint> timeViolations,
			ArrayList<Constraint> possibleActivityViolations, ArrayList<Constraint> resourceViolations,
			HashMap<Constraint, Long> deadlinesA, HashMap<Constraint, Long> delaysA) {
		int nrOfConstraintsValidated = 0;
		for(Constraint c : model) {
			if(!(c instanceof ActivityAvailabilitySchedule//TODO: add support...
					|| c instanceof AtMostLag
					|| c instanceof ResourceAvailabilitySchedule
					|| c instanceof ResourceEquality
					|| c instanceof SimultaneousCapacity
					|| c instanceof DecisionAuthorization)) {
				ValidationStatus status = c.validate(potentialTrace, resourceUsage, activeResource, currentTime);
				nrOfConstraintsValidated++;
				//TODO: what if constraint is optional???
				if(status.equals(ValidationStatus.VIOLATED))
					generalViolations.add(c);
				else if(status.equals(ValidationStatus.TIME_SATISFIABLE)) {
					timeViolations.add(c);
					delaysA.put(c, status.getBound());
				} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE)) {
					possibleActivityViolations.add(c);
					//TODO: random generation attempt to find working finished trace?
					//		-> warning if no, but keep on list...
					//		-> stop generation attempt when one trace has been found
					//		-> brute force with incremental length?
					//		-> put in separate section? =section of next-activities that are uncertain to lead to finishable trace
				} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE)) {
					possibleActivityViolations.add(c);
					deadlinesA.put(c, status.getBound());
				} else if(status.equals(ValidationStatus.RESOURCE_SATISFIABLE))
					resourceViolations.add(c);
				else if(deadendConstraints != null
						&& status.equals(ValidationStatus.DEADEND))
					deadendConstraints.add(c);
			}
		}
		String explanation = null;
		if(!generalViolations.isEmpty()) {
			if(sectionE_violation != null)
				sectionE_violation.add(a);
			generalViolations.addAll(0, resourceViolations);
			explanation = makeText("'" + a + "' is not allowed as next activity because:", generalViolations);
		} else if(!timeViolations.isEmpty()) {
			if(sectionD_timeViolation != null)
				sectionD_timeViolation.add(a);
			if(deadlines != null
					&& !deadlinesA.isEmpty())
				deadlines.add(a);
			if(delaysA != null && delays != null
					&& !delaysA.isEmpty())
				delays.add(a);
			generalViolations.addAll(resourceViolations);
			explanation = makeText("'" + a + "' is currently not allowed as next activity, but will be in the near future, because:", timeViolations, delaysA, deadlinesA);
		} else if(!resourceViolations.isEmpty()) {
			if(sectionC_resourceViolation != null)
				sectionC_resourceViolation.add(a);
			if(deadlines != null
					&& !deadlinesA.isEmpty())
				deadlines.add(a);
			if(delaysA != null && delays != null
					&& !delaysA.isEmpty())
				delays.add(a);
				generalViolations.addAll(resourceViolations);
				String extra = "";
				if(!possibleActivityViolations.isEmpty())
					extra = " (with additional future requirements)";
				explanation = makeText("'" + a + "' is allowed as next activity" + extra + ", but not for the active resource, because:", resourceViolations);
				if(!possibleActivityViolations.isEmpty()) {
					explanation += "\n---------------------\n\n";
					explanation += makeText("With additional future requirements:", possibleActivityViolations, delaysA, deadlinesA);
				}
		} else if(!possibleActivityViolations.isEmpty()) {
			if(sectionB_possibleActivitySatisfaction != null)
				sectionB_possibleActivitySatisfaction.add(a);
			if(deadlinesA != null && deadlines != null
					&& !deadlinesA.isEmpty())
				deadlines.add(a);
			if(delaysA != null && delays != null
					&& !delaysA.isEmpty())
				delays.add(a);
			if(!resourceViolations.isEmpty())
				explanation = makeText("'" + a + "' is allowed as next activity, but not for the active resource and with additional future requirements, because:", resourceViolations)
				+ "\n---------------------\n\n";
			else
				explanation = "";
			explanation += makeText("'" + a + "' is allowed as next activity, but with additional future requirements, because:", possibleActivityViolations, delaysA, deadlinesA);
		} else {//TODO: add deadlines
			if(sectionA_satisfaction != null) {
				if(!resourceViolations.isEmpty())
					explanation = makeText("'" + a + "' is allowed as next activity, but not for the active resource, because:", resourceViolations);
				sectionA_satisfaction.add(a);
			}
		}
		System.out.println("Nr of constraints validated: " + nrOfConstraintsValidated);
		return explanation;
	}

	private static String makeText(String header, ArrayList<Constraint> constraints, HashMap<Constraint, Long> delays, HashMap<Constraint, Long> deadlines) {
		String res = header + "\n";
		if(delays != null)
			for(Constraint c : delays.keySet()) {
				if(deadlines != null && deadlines.containsKey(c))
					res += "\n!DELAY(" + delays.get(c) + ") AND DEADLINE(" + deadlines.get(c) + ")! " + c;
				else
					res += "\n!DELAY(" + delays.get(c) + ")! " + c;
			}
		if(deadlines != null)
			for(Constraint c : deadlines.keySet())
				if(delays == null || !delays.containsKey(c))
					res += "\n!DEADLINE(" + deadlines.get(c) + ")! " + c;
		for(Constraint c : constraints)
			if((delays == null || !delays.containsKey(c))
					&& (deadlines == null || !deadlines.containsKey(c)))
				res += "\n" + c;
		return res;
	}

	public static String makeText(String header, ArrayList<Constraint> constraints) {
		return makeText(header, constraints, null, null);
	}

	public String getResourceValidationString(Activity a, String restrictions, HashMap<Resource, String> resourceTextInputs,
			long duration, ArrayList<DataEvent> cachedDataEvents, ArrayList<Constraint> submodel) {
		String resourcesOk = null;
		ArrayList<ResourceEvent> res = new ArrayList<>();
		for(Resource r : resourceTextInputs.keySet()) {
			String nrString = resourceTextInputs.get(r);
			if(nrString.length() > 0) {
				try {
					int nr = Integer.parseInt(nrString);
					if(nr > 0)
						res.add(new ResourceEvent(r, nr, currentTime, currentTime+duration));
				} catch(NumberFormatException x) {
					resourcesOk = r + "=" + nrString;
					break;
				}
			}
		}
		if(resourcesOk == null) {
			ArrayList<ActivityEvent> activityEvents = new ArrayList<>(trace.getActivityEvents());
			ActivityEvent newAct = getActivityEvent(a, duration);
			activityEvents.add(newAct);
			ArrayList<ResourceEvent> resourceEvents = new ArrayList<>(trace.getResourceEvents());
			resourceEvents.addAll(res);
			ArrayList<DataEvent> dataEvents = new ArrayList<>(trace.getDataEvents());
			for(DataEvent de : cachedDataEvents)
				de.setTime(currentTime+duration-1);
			dataEvents.addAll(cachedDataEvents);
			resourcesOk = validate(submodel, currentTime+duration, new Trace(activityEvents, dataEvents, resourceEvents));
			if(resourcesOk == null) {
				trace.getActivityEvents().add(newAct);
				trace.getResourceEvents().addAll(res);
				trace.getDataEvents().addAll(cachedDataEvents);
				currentTime += duration;
				currentRestrictions = restrictions;
				return null;
			}
		}
		return resourcesOk;
	}

	public String getRelevantModelString(Activity a) {
		Trace t;
		if(a == null)
			t = trace;
		else {
			ArrayList<ActivityEvent> potentialTraceActs = new ArrayList<>(trace.getActivityEvents());
			potentialTraceActs.add(getActivityEvent(a, currentTime));
			ArrayList<ResourceEvent> potentialResourceEvents = new ArrayList<>(trace.getResourceEvents());
			t = new Trace(potentialTraceActs, trace.getDataEvents(), potentialResourceEvents);
		}
		return getModelText(getRelevantModel(t));
	}

	public String getCurrentRestrictionsString() {
		String curRestr = "";
		for(Constraint c : model) {
			if(!(c instanceof ResourceConstraint
					|| c instanceof ActivityAvailabilitySchedule
					|| c instanceof AtMostLag)) {
				ValidationStatus status = c.validate(trace, null, activeResource, currentTime);
				if(status.equals(ValidationStatus.VIOLATED)
						|| status.equals(ValidationStatus.TIME_SATISFIABLE)
						|| status.equals(ValidationStatus.DEADEND)) {
					curRestr = "VIOLATION FOUND!\n\n" + c.toString();
					break;
				} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE)
						|| status.equals(ValidationStatus.RESOURCE_SATISFIABLE))
					curRestr += "\n" + c.toString();
				else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE))
					curRestr += "\nDEADLINE(" + status.getBound() + ") " + c.toString();
			}
		}
		return curRestr.trim();
	}

	protected ArrayList<Constraint> getRelevantModel(Trace t) {
		ArrayList<Constraint> relModel = new ArrayList<>();
		for(Constraint c : model)
			if(c.isActivated(t))
				relModel.add(c);
		return relModel;
	}

	protected String getModelText(ArrayList<Constraint> model) {
		String res = "";
		for(Constraint c : model)
			res += "\n" + c.toString();
		return res.trim();
	}

	public ArrayList<DataAttribute> getDataAttributes() {
		HashSet<String> names = new HashSet<>();
		ArrayList<DataAttribute> res = new ArrayList<>();
		for(Constraint c : model)
			for(DataAttribute da : c.getUsedDataAttributes())
				if(names.add(da.getName()))
					res.add(da);
		Collections.sort(res);
		return res;
	}

	private static ArrayList<Activity> getActivities(ArrayList<Constraint> model) {
		HashSet<Activity> tmp = new HashSet<>();
		for(Constraint c : model)
			for(Activity a : c.getUsedActivities())
				tmp.add(a);
		ArrayList<Activity> res = new ArrayList<>(tmp);
		Collections.sort(res);
		return res;
	}

	private static ArrayList<Resource> getResources(ArrayList<Constraint> model) {
		ArrayList<Resource> res = new ArrayList<>(new HashSet<>(getResourceMapping(model).values()));
		Collections.sort(res);
		return res;
	}

	public HashMap<Resource, Resource> getResourceMapping() {
		return getResourceMapping(model);
	}

	private static HashMap<Resource, Resource> getResourceMapping(ArrayList<Constraint> model) {
		HashMap<Resource, Resource> tmp = new HashMap<>();
		for(Constraint c : model)
			for(Resource r : c.getUsedResources()) {
				Resource r_ = tmp.get(r);
				if(r_ != null)
					merge(tmp.get(r), r);
				else
					r_ = r;
				addAllResources(r_, tmp);
			}
		return tmp;
	}

	private static void addAllResources(Resource r, HashMap<Resource, Resource> tmp) {
		addAllResources(r, r, tmp);
	}

	private static void addAllResources(Resource r, Resource rSuper, HashMap<Resource, Resource> tmp) {
		tmp.put(r, rSuper);
		if(r instanceof ResourceRole) {
			for(Resource x : ((ResourceRole) r).getContainedResources())
				addAllResources(x, rSuper, tmp);
			for(ResourceRole x : ((ResourceRole) r).getSubRoles())
				addAllResources(x, rSuper, tmp);
		}
	}

	private static void merge(Resource r1, Resource r2) {
		if(r1.getClass().equals(r2.getClass())
				&& r1.toString().equals(r2.toString()))
			return;
		if(r1.getName().equals(r2.getName())) {
			((ResourceRole) r1).getContainedResources().addAll(((ResourceRole) r2).getContainedResources());
			((ResourceRole) r1).getSubRoles().addAll(((ResourceRole) r2).getSubRoles());
		} else//r1=nurse(OR nurse(scrub nurse)) and r2=OR nurse(scrub nurse)
			return;
	}

	private void setResourceUsage() {
		HashMap<Resource, Resource> m = getResourceMapping(model);
		resourceUsage_lowerlimits = new HashMap<>();
		resourceUsage_upperlimits = new HashMap<>();
		resourceUsage = new HashMap<>();
		for(Resource r : new ArrayList<>(m.keySet())) {
			boolean isSetToLimit = false;
			for(Constraint c : model) {
				//TODO: add support for NonAtomicResourceExpressions
				if(c instanceof AtLeastAvailable
						&& c.getActivationDecision() == null
						&& ((AtLeastAvailable) c).getResourceExpression() instanceof AtomicResourceExpression
						&& ((AtomicResourceExpression) ((AtLeastAvailable) c).getResourceExpression()).getResource().equals(r)) {
					resourceUsage_lowerlimits.put(r, ((AtLeastAvailable) c).getBound());
					resourceUsage.put(r, ((AtLeastAvailable) c).getBound());
					isSetToLimit = true;
				} else if(c instanceof AtMostAvailable
						&& c.getActivationDecision() == null
						&& ((AtMostAvailable) c).getResourceExpression() instanceof AtomicResourceExpression
						&& ((AtomicResourceExpression) ((AtMostAvailable) c).getResourceExpression()).getResource().equals(r)) {
					resourceUsage_upperlimits.put(r, ((AtMostAvailable) c).getBound());
					if(((AtMostAvailable) c).getBound() == 0) {
						resourceUsage.put(r, 0);
						isSetToLimit = true;
					}
				}
			}
			if(!isSetToLimit)
				resourceUsage.put(r, 1);
		}
		ArrayList<Resource> todo = new ArrayList<>(resourceUsage.keySet());
		while(!todo.isEmpty())
			correctResourceUsage(todo.get(0), todo);
		//TODO: add limits of super and sub roles...
	}

	private void correctResourceUsage(Resource r, ArrayList<Resource> todo) {
		int index = -1;
		for(int i = 0; i < todo.size(); i++)
			if(todo.get(i).toString().equals(r.toString())) {
				index = i;
				break;
			}
		if(index < 0)
			return;
		todo.remove(index);
		if(r instanceof ResourceRole) {
			if(((ResourceRole) r).getContainedResources().isEmpty()
					&& ((ResourceRole) r).getSubRoles().isEmpty())
				return;
			int sum = resourceUsage.get(r);
			for(Resource x : ((ResourceRole) r).getSubRoles()) {
				correctResourceUsage(x, todo);
				sum += resourceUsage.get(x);
			}
			for(DirectResource x : ((ResourceRole) r).getContainedResources())
				sum += resourceUsage.get(x);
			if(resourceUsage_upperlimits.containsKey(r)
					&& sum > resourceUsage_upperlimits.get(r))
				sum = resourceUsage_upperlimits.get(r);
			resourceUsage.put(r, sum);
		} else
			todo.remove(0);
	}

	private ActivityEvent getActivityEvent(Activity a, long duration) {
		return new ActivityEvent(a.getName(), currentTime, currentTime+duration);
	}

	private ArrayList<ResourceEvent> getResourceEvents(Activity a, long duration) {
		ArrayList<ResourceEvent> res = new ArrayList<>();
		for(Constraint c : model)
			if(c instanceof AtLeastUsage
					&& ((AtLeastUsage) c).getActivity().equals(a)
					//TODO: NonAtomicResourceExpression proberen voldoen tot max eerste resource, dan volgende resource enzovoort
					&& ((AtLeastUsage) c).getResourceExpression() instanceof AtomicResourceExpression
					&& c.getActivationTime(trace) != -1)
				res.add(new ResourceEvent(((AtLeastUsage) c).getUsedResources().iterator().next(),
						((AtLeastUsage) c).getBound(), currentTime, currentTime+duration));
		return res;
	}

	private DataEvent getDataEvent(DataAttribute da, boolean isActivated, long time) {
		return new DataEvent(da, isActivated, time);
	}

	public static ArrayList<Constraint> readModel(File file) throws IOException {
		if(file.getName().endsWith(".data")) {//data file
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				@SuppressWarnings("unchecked")
				ArrayList<SingleRule> rules = (ArrayList<SingleRule>) in.readObject();
				ArrayList<Constraint> constraints = new ArrayList<>();
				for(SingleRule r : rules)
					constraints.add(r.getConstraint());
				return constraints;
			} catch (Exception ex) {
				throw new RuntimeException("An error occurred when reading the data object for "
						+ file.toString() + "!", ex);
			} finally {
				if(in != null)
					in.close();
			}
		}
		int nrInModel = 0;
		ParsingCache pc = new ParsingCache();
		ArrayList<Constraint> model = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while(line != null) {
				if(!file.getName().endsWith(".csv")
						|| !line.startsWith("PoI+;#I+;"))
					try {
						if(file.getName().endsWith(".csv"))
							for(int i = 0; i < 10; i++)
								line = line.substring(line.indexOf(";") + 1);
						nrInModel++;
						model.add(Constraint.parseConstraint(line, pc));
					} catch(Exception e) {
						e.printStackTrace();
					}
				line = reader.readLine();
			}
		} finally {
			if(reader != null)
				reader.close();
		}
		System.out.println("Number of constraints in the model: " + nrInModel);
		System.out.println("Number of constraints loaded by engine: " + model.size());
		return model;
	}
}