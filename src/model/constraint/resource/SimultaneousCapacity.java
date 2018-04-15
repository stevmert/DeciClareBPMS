package model.constraint.resource;

import java.util.HashMap;
import java.util.HashSet;

import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Activity;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.ParsingCache;
import model.constraint.ResourceConstraint;
import model.data.Decision;
import model.resource.Resource;
import model.resource.ResourceRole;

public class SimultaneousCapacity extends ResourceConstraint {

	private static final long serialVersionUID = 1760081722694203060L;

	private Resource resource;
	private int simultaneousCapacity;

	public SimultaneousCapacity(Decision activationDecision, Decision deactivationDec,
			Resource resource, int simultaneousCapacity, boolean isOptional) {
		super(activationDecision, deactivationDec, isOptional);
		this.resource = resource;
		this.simultaneousCapacity = simultaneousCapacity;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getSimultaneousCapacity() {
		return simultaneousCapacity;
	}

	public void setSimultaneousCapacity(int simultaneousCapacity) {
		this.simultaneousCapacity = simultaneousCapacity;
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new SimultaneousCapacity(null, null, getResource(), getSimultaneousCapacity(), isOptional());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + simultaneousCapacity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}

	@Override
	public boolean equals(Object obj, boolean checkDecisions) {
		if (this == obj)
			return true;
		if (!super.equals(obj, checkDecisions))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimultaneousCapacity other = (SimultaneousCapacity) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (simultaneousCapacity != other.simultaneousCapacity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getResource() + ", " + getSimultaneousCapacity()
		+ ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		return getResource() + " can be used by up to " + getSimultaneousCapacity() + " instance(s) simultaneously";
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		return new HashSet<>();
	}

	public static SimultaneousCapacity parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(SimultaneousCapacity.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 2);
		return new SimultaneousCapacity(activationDec, deactivationDecision,
				ResourceRole.parseResource(split[0], pc),
				Integer.parseInt(split[1]),
				isOptional);
	}
}