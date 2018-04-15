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
import model.data.Decision;
import model.expression.ActivityExpression;
import model.expression.ResourceExpression;
import model.resource.Resource;

public class AtLeastUsage extends ResourceUsage {

	private static final long serialVersionUID = -8885458154311157987L;

	public AtLeastUsage(Decision activationDecision, Decision deactivationDec, ActivityExpression activityExpression,
			ResourceExpression resourceExpression, int bound, boolean isOptional) {
		super(activationDecision, deactivationDec, activityExpression, resourceExpression, bound, isOptional);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getActivityExpression() + ", "
				+ getResourceExpression() + ", " + getBound() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		return getActivityExpression() + " uses at least " + getBound() + " " + getResourceExpression();
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new AtLeastUsage(null, null, getActivityExpression(), getResourceExpression(),
				getBound(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		return new HashSet<>();
	}

	public static AtLeastUsage parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(AtLeastUsage.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 3);
		return new AtLeastUsage(activationDec, deactivationDecision,
				ActivityExpression.parseActivityExpression(split[0], pc),
				ResourceExpression.parseResourceExpression(split[1], pc),
				Integer.parseInt(split[2]),
				isOptional);
	}
}