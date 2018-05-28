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

//= starts as first (does not need to be the only one starting at that time!)
public class First extends ExistenceConstraint {

	private static final long serialVersionUID = -3822561394228803678L;

	//TODO: no preceding acts, so can't be deactivated + exps in decision are always instanceData
	public First(Decision activationDec, Decision deactivationDec,
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
			return getActivityExpression() + " has to be performed as the first activity";
		return "one of the following activities has to be performed as the first activity: " + getActivityExpression();
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new First(null, null, getActivityExpression(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineFirst(this.getActivationDecision(), this.getActivityExpression(), log);
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, Resource activeResource, long currentTime) {
		long activationTime = getActivationTime(t);
		if(activationTime != -1) {//activated by activation decision
			List<ActivityEvent> actsRem = t.getRemainingActivityList(activationTime);
			IndexResult index = indexOf(getActivityExpression(), actsRem, -1, -1);
			if(t.getActivityEvents().isEmpty())
				return ValidationStatus.ACTIVITY_SATISFIABLE;
			if(index == null || actsRem.get(index.getIndex_start()).getStart() != actsRem.get(0).getStart())
				return ValidationStatus.VIOLATED;
		}
		return ValidationStatus.SATISFIED;
	}

	public static Rule mineFirst(Decision activationDecision,
			ActivityExpression activityExpression, Log log) {
		throw new IllegalArgumentException("TODO: still used?");//TODO: still used?
		//		Rule r = ExtremitiesConstraint.mineFirstAndLast(activationDecision, activityExpression, log);
		//		if(r == null)
		//			return null;
		//		List<SingleRule> res = r.rules();
		//		for(SingleRule sr : new ArrayList<>(res))
		//			if(!(sr.getConstraint() instanceof First))
		//				res.remove(sr);
		//		if(res.isEmpty())
		//			return null;
		//		else if(res.size() == 1)
		//			return res.get(0);
		//		return new BatchRule(new ExtremitiesConstraint(activationDecision, activationDecision,
		//				activityExpression, false), res.toArray(new Rule[res.size()]));
	}

	public static First parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(First.class.getSimpleName().length()+1, input.length()-1);
		return new First(activationDec, deactivationDecision,
				ActivityExpression.parseActivityExpression(text, pc),
				isOptional);
	}
}