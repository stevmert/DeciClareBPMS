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

public class AtMostAvailable extends ResourceAvailability {

	private static final long serialVersionUID = 7367613864327365904L;

	public AtMostAvailable(Decision activationDecision, Decision deactivationDec,
			ResourceExpression resourceExpression, int atMost, boolean isOptional) {
		super(activationDecision, deactivationDec, resourceExpression, atMost, isOptional);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getResourceExpression()
		+ ", " + getBound() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		if(getBound() == 1)
			return "there is at most " + getBound() + " " + getResourceExpression() + " available";
		return "there are at most " + getBound() + " " + getResourceExpression() + "s available";
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new AtMostAvailable(null, null, getResourceExpression(), getBound(), isOptional());
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

	public static AtMostAvailable parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(AtMostAvailable.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 2);
		return new AtMostAvailable(activationDec, deactivationDecision,
				ResourceExpression.parseResourceExpression(split[0], pc),
				Integer.parseInt(split[1]),
				isOptional);
	}
}