package model.constraint.existence;

import java.util.HashMap;
import java.util.List;

import miner.log.ActivityEvent;
import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.BoundedConstraint;
import model.constraint.ExistenceConstraint;
import model.constraint.ParsingCache;
import model.constraint.TimedConstraint;
import model.data.Decision;
import model.expression.ActivityExpression;
import model.expression.AtomicActivityExpression;
import model.expression.LogicalOperator;
import model.expression.NonAtomicActivityExpression;
import model.resource.Resource;

public class AtLeastChoice extends ExistenceConstraint implements TimedConstraint, BoundedConstraint {

	private static final long serialVersionUID = -7657948176403387095L;

	private int atLeast;
	private long timeA;
	private long timeB;

	public AtLeastChoice(Decision activationDec, Decision deactivationDec,
			ActivityExpression activityExpression, int atLeast,
			long timeA, long timeB, boolean isOptional) {
		super(activationDec, deactivationDec, activityExpression, isOptional);
		if(activityExpression instanceof AtomicActivityExpression
				|| ((NonAtomicActivityExpression) activityExpression).getOperator().equals(LogicalOperator.AND)
				|| atLeast < 1
				|| ((NonAtomicActivityExpression) activityExpression).getExpressions().size() <= atLeast)
			throw new IllegalArgumentException();
		this.atLeast = atLeast;
		this.timeA = timeA;
		this.timeB = timeB;
	}

	@Override
	public int getBound() {
		return atLeast;
	}

	@Override
	public void setBound(int atLeast) {
		this.atLeast = atLeast;
	}

	public long getTimeA() {
		return timeA;
	}

	public void setTimeA(long timeA) {
		this.timeA = timeA;
	}

	public long getTimeB() {
		return timeB;
	}

	public void setTimeB(long timeB) {
		this.timeB = timeB;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getActivityExpression() + ", " + getBound()
		+ ", " + getTimeA()
		+ ", " + getTimeB() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		return "at least " + getBound() + " out of " + getActivityExpression().getNrOfElements()
				+ " of the following activities have to be performed: " + getActivityExpression()
				+ getTimedText(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + atLeast;
		return result;
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
		AtLeastChoice other = (AtLeastChoice) obj;
		if (atLeast != other.atLeast)
			return false;
		return true;
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new AtLeastChoice(null, null, getActivityExpression(),
				getBound(), getTimeA(), getTimeB(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineAtLeastChoice(this.getActivationDecision(), this.getActivityExpression(), log);
	}

	public static Rule mineAtLeastChoice(Decision activationDecision,
			ActivityExpression activityExpression, Log log) {
		throw new UnsupportedOperationException();
		//		ArrayList<Rule> res = AtMostChoice.mineAtLeastAndAtMostChoice(activationDecision, activityExpression, false, log);
		//		return res.toArray(new Rule[res.size()]);
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		long activationTime = getActivationTime(t);
		if(activationTime != -1) {
			List<ActivityEvent> actsRem = t.getRemainingActivityList(activationTime);
			int nrInTrace = countChoice(getActivityExpression(), actsRem);
			if(this.getBound() > nrInTrace)
				return ValidationStatus.ACTIVITY_SATISFIABLE;
		}
		return ValidationStatus.SATISFIED;
	}

	@Override
	public boolean isRelatedTo(Constraint c) {
		if(!super.isRelatedTo(c))
			return false;
		return this.getBound() == ((BoundedConstraint) c).getBound();
	}

	public static AtLeastChoice parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(AtLeastChoice.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 4);
		return new AtLeastChoice(activationDec, deactivationDecision,
				ActivityExpression.parseActivityExpression(split[0], pc),
				Integer.parseInt(split[1]),
				Long.parseLong(split[2]),
				Long.parseLong(split[3]),
				isOptional);
	}
}