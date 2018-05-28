package model.constraint.resource;

import java.util.HashMap;
import java.util.HashSet;

import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Activity;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.Negatable;
import model.constraint.ParsingCache;
import model.data.Decision;
import model.expression.ResourceExpression;
import model.resource.Resource;

public class ResourceAvailabilitySchedule extends ResourceParameterConstraint implements Negatable {

	private static final long serialVersionUID = 707236037076366298L;

	public ResourceAvailabilitySchedule(Decision activationDecision, Decision deactivationDec,
			ResourceExpression resourceExpression, boolean isOptional) {
		super(activationDecision, deactivationDec, resourceExpression, isOptional);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPositiveVersion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsPositiveVersion(boolean isPositiveVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public Constraint getDecisionlessCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, Resource activeResource, long currentTime) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getTextualConstraint() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		return new HashSet<>();
	}

	public static AtMostUsage parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		throw new UnsupportedOperationException();
	}
}