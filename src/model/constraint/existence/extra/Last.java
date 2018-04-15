package model.constraint.existence.extra;

import java.util.HashMap;
import java.util.List;

import miner.log.ActivityEvent;
import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.ExistenceConstraint;
import model.constraint.ParsingCache;
import model.data.Decision;
import model.expression.ActivityExpression;
import model.resource.Resource;
import util.IndexResult;

//= starts as last (does not need to end at same time or after all others!)
public class Last extends ExistenceConstraint {

	private static final long serialVersionUID = 1968190760603471733L;

	public Last(Decision activationDec, Decision deactivationDec,
			ActivityExpression activityExpression, boolean isOptional) {
		super(activationDec, deactivationDec, activityExpression, isOptional);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getActivityExpression() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		if(getActivityExpression().getNrOfElements() == 1)
			return getActivityExpression() + " has to be performed as the last activity";
		return "one of the following activities has to be performed as the last activity: " + getActivityExpression();
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new Last(null, null, getActivityExpression(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineLast(this.getActivationDecision(), this.getActivityExpression(), log);
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		long activationTime = getActivationTime(t);
		if(activationTime != -1) {//activated by activation decision
			List<ActivityEvent> actsRem = t.getRemainingActivityList(activationTime);
			IndexResult index = lastIndexOf(getActivityExpression(), actsRem, -1, -1);
			if(index == null || actsRem.get(index.getIndex_start()).getStart() != actsRem.get(actsRem.size()-1).getStart())
				return ValidationStatus.ACTIVITY_SATISFIABLE;
		}
		return ValidationStatus.SATISFIED;
	}

	public static Rule mineLast(Decision activationDecision,
			ActivityExpression activityExpression, Log log) {
		throw new IllegalArgumentException("TODO: still used?");//TODO: still used?
		//		Rule r = ExtremitiesConstraint.mineFirstAndLast(activationDecision, activityExpression, log);
		//		if(r == null)
		//			return null;
		//		List<SingleRule> res = r.rules();
		//		for(SingleRule sr : new ArrayList<>(res))
		//			if(!(sr.getConstraint() instanceof Last))
		//				res.remove(sr);
		//		if(res.isEmpty())
		//			return null;
		//		else if(res.size() == 1)
		//			return res.get(0);
		//		return new BatchRule(new ExtremitiesConstraint(activationDecision, activationDecision,
		//				activityExpression, false), res.toArray(new Rule[res.size()]));
	}

	public static Last parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(Last.class.getSimpleName().length()+1, input.length()-1);
		return new Last(activationDec, deactivationDecision,
				ActivityExpression.parseActivityExpression(text, pc),
				isOptional);
	}
}