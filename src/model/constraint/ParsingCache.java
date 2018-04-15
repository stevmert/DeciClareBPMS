package model.constraint;

import java.util.HashMap;

import model.Activity;
import model.data.DataAttribute;
import model.data.DataStructure;
import model.resource.Resource;

public class ParsingCache {

	private HashMap<String, Activity> activities;
	private HashMap<String, DataAttribute> dataAttributes_toStrings;
	private HashMap<String, DataAttribute> dataAttributes_names;
	private HashMap<String, Resource> resources;
	private HashMap<String, DataStructure> dataStructures;

	public ParsingCache() {
		super();
		this.activities = new HashMap<>();
		this.dataAttributes_toStrings = new HashMap<>();
		this.dataAttributes_names = new HashMap<>();
		this.resources = new HashMap<>();
		this.dataStructures = new HashMap<>();
	}

	public Activity getActivity(String name) {
		return activities.get(name);
	}

	public void addActivity(Activity a) {
		activities.put(a.getName(), a);
	}

	public DataAttribute getDataAttribute_toString(String toString) {
		return dataAttributes_toStrings.get(toString);
	}

	public DataAttribute getDataAttribute_name(String name) {
		return dataAttributes_names.get(name);
	}

	public void addDataAttribute(DataAttribute da) {
		dataAttributes_toStrings.put(da.toString(), da);
		dataAttributes_names.put(da.getName(), da);
	}

	public Resource getResource(String toString) {
		return resources.get(toString);
	}

	public void addResource(Resource r) {
		resources.put(r.toString(), r);
	}

	public DataStructure getDataStructure(String toString) {
		return dataStructures.get(toString);
	}

	public void addDataStructure(DataStructure d) {
		dataStructures.put(d.toString(), d);
	}
}