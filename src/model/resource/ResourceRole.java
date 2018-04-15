package model.resource;

import java.util.Arrays;
import java.util.HashSet;

import model.Constraint;
import model.constraint.ParsingCache;

public class ResourceRole extends Resource {

	private static final long serialVersionUID = 6568817569668087074L;

	private HashSet<DirectResource> containedResources;
	private HashSet<ResourceRole> subRoles;

	/**
	 * Resource roles
	 */
	public ResourceRole(String name) {
		super(name);
		this.containedResources = new HashSet<>();
		this.subRoles = new HashSet<>();
	}

	public ResourceRole(String name, HashSet<ResourceRole> subRoles) {
		super(name);
		this.containedResources = new HashSet<>();
		this.subRoles = subRoles;
	}

	public ResourceRole(String name, ResourceRole... subRoles) {
		super(name);
		this.containedResources = new HashSet<>();
		this.subRoles = new HashSet<>(Arrays.asList(subRoles));
	}

	public HashSet<DirectResource> getContainedResources() {
		return containedResources;
	}

	public void setContainedResources(HashSet<DirectResource> containedResources) {
		this.containedResources = containedResources;
	}

	public HashSet<ResourceRole> getSubRoles() {
		return subRoles;
	}

	public void setSubRoles(HashSet<ResourceRole> subRoles) {
		this.subRoles = subRoles;
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean includeSubRoles) {
		if(!includeSubRoles
				|| getSubRoles().isEmpty())
			return super.toString();
		String subs = "";
		for(ResourceRole rr : getSubRoles())
			subs += "/" + rr.toString(includeSubRoles);
		return getName() + "(" + subs.substring(1) + ")";
	}

	public static ResourceRole parseResource(String input, ParsingCache pc) {
		Resource r = pc.getResource(input);
		if(r != null)
			return (ResourceRole) r;
		if(input.contains("(")) {
			int x = input.indexOf("(");
			r = new ResourceRole(input.substring(0, x), parseSubRoles(input.substring(x+1, input.length()-1), pc));
		} else
			r = new ResourceRole(input);
		pc.addResource(r);
		return (ResourceRole) r;
	}

	public static HashSet<ResourceRole> parseSubRoles(String input, ParsingCache pc) {
		HashSet<ResourceRole> res = new HashSet<>();
		if(input.contains("/")) {
			String text = input;
			while(true) {
				int slash = text.indexOf("/");
				int bracket = text.indexOf("(");
				if(bracket == -1 && slash == -1) {
					res.add(parseResource(text, pc));
					break;
				} else if(bracket == -1 || slash < bracket) {
					res.add(parseResource(text.substring(0, slash), pc));
					text = text.substring(slash);
				} else {
					String subs = text.substring(bracket);
					int end = Constraint.getBracketsEnd(subs, "(", ")");
					res.add(parseResource(text.substring(0, bracket+end+1), pc));
					text = text.substring(bracket+end+1);
				}
				if(text.trim().length() == 0)
					break;
				text = text.substring(1);
			}
		} else
			res.add(parseResource(input, pc));
		return res;
	}
}