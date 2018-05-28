package model.constraint.relation;

import java.util.HashMap;
import java.util.List;

import miner.log.ActivityEvent;
import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.ParsingCache;
import model.constraint.RelationConstraint;
import model.data.Decision;
import model.expression.ExistenceExpression;
import model.resource.Resource;

public class RespondedPresence extends RelationConstraint {

	private static final long serialVersionUID = 284275947115679904L;

	public RespondedPresence(Decision activationDec, Decision deactivationDec,
			ExistenceExpression condition,
			ExistenceExpression consequence, boolean isOptional) {
		super(activationDec, deactivationDec, condition, consequence, isOptional);
	}

	@Override
	public String toString() {
		String name = this.getClass().getSimpleName();
		return name + "(" + getConditionExpression()
		+ ", " + getConsequenceExpression() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		String fromString;
		if(getConditionExpression().getNrOfElements() == 1)
			fromString = "when " + getConditionExpression().toTextualString() + " is performed";
		else
			fromString = "when " + getConditionExpression().toTextualString() + " are performed";
		String toString = getConsequenceExpression().toTextualString()
				+ " will also have been performed before or will be performed thereafter";
		return fromString + ", " + toString;
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}

	public boolean equals(Object obj, boolean checkDecisions) {
		if (this == obj)
			return true;
		if (!super.equals(obj, checkDecisions))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new RespondedPresence(null, null, getConditionExpression(),
				getConsequenceExpression(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineRespondedPresence(this.getActivationDecision(), this.getConditionExpression(),
				this.getConsequenceExpression(), log);
	}

	public static Rule mineRespondedPresence(Decision activationDecision,
			ExistenceExpression actsFrom, ExistenceExpression actsTo,
			Log log) {
		throw new IllegalArgumentException("TODO: still used?");//TODO: still used?
		//		HashSet<Integer> tracesOne = new HashSet<>();
		//		HashSet<Integer> tracesBoth = new HashSet<>();
		//		HashSet<DecisionRule> usedDecisions = new HashSet<>();
		//		for(int i = 0; i < log.size(); i++) {
		//			Trace t = log.get(i);
		//			long activationTime = 0;
		//			if(activationDecision != null) {
		//				DecisionActivation decisionActivation = t.getDecisionActivation(activationDecision);
		//				if(decisionActivation != null) {
		//					activationTime = decisionActivation.getTime();
		//					usedDecisions.add(decisionActivation.getDecisionRule());
		//				} else
		//					activationTime = -1;
		//			}
		//			if(activationTime != -1) {
		//				List<ActivityEvent> acts = t.getRemainingActivityList(activationTime);
		//				boolean containsFrom = contains(actsFrom, acts);
		//				boolean containsTo = contains(actsTo, acts);
		//				if(containsFrom && containsTo)
		//					tracesBoth.add(i);
		//				if(containsFrom && !containsTo)
		//					tracesOne.add(i);
		//			}
		//		}
		//		//remove redundant decision rules, as these are not used anyway...
		//		if(tracesOne.size() > 0
		//				&& activationDecision != null
		//				&& usedDecisions.size() != activationDecision.getRules().size())
		//			activationDecision.setRules(usedDecisions);
		//		Rule pos = null;
		//		Rule neg = null;
		//		if(Config.MINE_RESPONDEDEXISTENCE)
		//			pos = new Rule(new RespondedPresence(activationDecision, null, actsFrom, actsTo, true),
		//					tracesBoth.size(), tracesOne.size(),
		//					tracesBoth, tracesOne.size(), log.size());
		//		if(Config.MINE_NOTRESPONDEDEXISTENCE)
		//			neg = new Rule(new RespondedPresence(activationDecision, null, actsFrom, actsTo, false),
		//					tracesOne.size(), tracesBoth.size(),
		//					tracesOne, tracesBoth.size(), log.size());
		//		if(pos != null && neg != null) {
		//			Rule[] result = {pos, neg};
		//			return result;
		//		} else if(pos != null && neg == null) {
		//			Rule[] result = {pos};
		//			return result;
		//		} else if(pos == null && neg != null) {
		//			Rule[] result = {neg};
		//			return result;
		//		} else
		//			return null;
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, Resource activeResource, long currentTime) {
		long activationTime = getActivationTime(t);
		int countNrOfViolations = 0;
		int countNrOfPossibleViolations = 0;
		if(activationTime != -1) {//not activated by activation decision
			boolean isAtMostConsequence = getConsequenceExpression().hasAtMost();
			List<ActivityEvent> acts = t.getRemainingActivityList(activationTime);
			boolean containsCond = contains(getConditionExpression(), acts);
			boolean containsConseq = contains(getConsequenceExpression(), acts);
			if(isAtMostConsequence) {
				if(containsCond && containsConseq)
					countNrOfViolations++;
			} else if(containsCond && !containsConseq) {
				if(isStillPossibleInFuture(getConsequenceExpression()))
					countNrOfPossibleViolations++;
				else
					countNrOfViolations++;
			}
			//TODO: time_satisfied
			if(countNrOfViolations > 0)
				return ValidationStatus.VIOLATED;
			else if(countNrOfPossibleViolations > 0)
				return ValidationStatus.ACTIVITY_SATISFIABLE;
			else
				return ValidationStatus.SATISFIED;
		}
		return ValidationStatus.SATISFIED;
	}

	@Override
	public RelationConstraint getShallowCopy(ExistenceExpression conditionExpression,
			ExistenceExpression consequenceExpression) {
		return new RespondedPresence(getActivationDecision(), getDeactivationDecision(),
				conditionExpression, consequenceExpression, isOptional());
	}

	public static RespondedPresence parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(RespondedPresence.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 2);
		return new RespondedPresence(activationDec, deactivationDecision,
				ExistenceExpression.parseExistenceExpression(split[0], pc),
				ExistenceExpression.parseExistenceExpression(split[1], pc),
				isOptional);
	}
}