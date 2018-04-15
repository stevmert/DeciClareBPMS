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
import model.expression.ResourceExpression;
import model.resource.Resource;

public class AtLeastAvailable extends ResourceAvailability {

	private static final long serialVersionUID = 701752749908274232L;

	public AtLeastAvailable(Decision activationDecision, Decision deactivationDec, ResourceExpression resourceExpression,
			int atLeast, boolean isOptional) {
		super(activationDecision, deactivationDec, resourceExpression, atLeast, isOptional);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getResourceExpression() + ", " + getBound() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		if(getBound() == 1)
			return "there is at least " + getBound() + " " + getResourceExpression() + " available";
		return "there are at least " + getBound() + " " + getResourceExpression() + "s available";
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new AtLeastAvailable(null, null, getResourceExpression(), getBound(), isOptional());
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

	public static AtLeastAvailable parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(AtLeastAvailable.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 2);
		return new AtLeastAvailable(activationDec, deactivationDecision,
				ResourceExpression.parseResourceExpression(split[0], pc),
				Integer.parseInt(split[1]),
				isOptional);
	}
}