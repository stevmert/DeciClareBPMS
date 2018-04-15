package model.constraint;

import java.util.HashSet;
import java.util.List;

import miner.log.ActivityEvent;
import model.Activity;
import model.Constraint;
import model.constraint.existence.AtLeast;
import model.data.Decision;
import model.expression.ActivityExpression;
import model.expression.AtomicActivityExpression;
import model.expression.AtomicExistenceExpression;
import model.expression.ExistenceExpression;
import model.expression.NonAtomicActivityExpression;
import model.expression.NonAtomicExistenceExpression;

public abstract class RelationConstraint extends Constraint {

	private static final long serialVersionUID = -8651161995964662675L;

	private ExistenceExpression conditionExpression;
	private ExistenceExpression consequenceExpression;

	public RelationConstraint(Decision activationDecision, Decision deactivationDec,
			ExistenceExpression conditionExpression, ExistenceExpression consequenceExpression,
			boolean isOptional) {
		super(activationDecision, deactivationDec, isOptional);
		this.conditionExpression = conditionExpression;
		this.consequenceExpression = consequenceExpression;
	}

	public ExistenceExpression getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(ExistenceExpression conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public ExistenceExpression getConsequenceExpression() {
		return consequenceExpression;
	}

	public void setConsequenceExpression(ExistenceExpression consequenceExpression) {
		this.consequenceExpression = consequenceExpression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conditionExpression == null) ? 0 : conditionExpression.hashCode());
		result = prime * result + ((consequenceExpression == null) ? 0 : consequenceExpression.hashCode());
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
		RelationConstraint other = (RelationConstraint) obj;
		if (conditionExpression == null) {
			if (other.conditionExpression != null)
				return false;
		} else if (!conditionExpression.equals(other.conditionExpression))
			return false;
		if (consequenceExpression == null) {
			if (other.consequenceExpression != null)
				return false;
		} else if (!consequenceExpression.equals(other.consequenceExpression))
			return false;
		return true;
	}

	public abstract RelationConstraint getShallowCopy(ExistenceExpression conditionExpression,
			ExistenceExpression consequenceExpression);

	@Override
	public boolean isRelatedTo(Constraint c) {
		if(!super.isRelatedTo(c))
			return false;
		if(!(this.getConditionExpression() instanceof AtomicExistenceExpression)
				|| !(this.getConsequenceExpression() instanceof AtomicExistenceExpression)
				|| !(((RelationConstraint) c).getConditionExpression() instanceof AtomicExistenceExpression)
				|| !(((RelationConstraint) c).getConsequenceExpression() instanceof AtomicExistenceExpression))
			throw new IllegalArgumentException("TODO: not supported yet!");
		return areCompatible((AtomicExistenceExpression) this.getConditionExpression(),
				(AtomicExistenceExpression) ((RelationConstraint) c).getConditionExpression())
				&& areCompatible((AtomicExistenceExpression) this.getConsequenceExpression(),
						(AtomicExistenceExpression) ((RelationConstraint) c).getConsequenceExpression());
	}

	private static boolean areCompatible(AtomicExistenceExpression aee1, AtomicExistenceExpression aee2) {
		if(!aee1.getExistenceConstraint().getClass().equals(aee2.getExistenceConstraint().getClass()))
			return false;
		if(((BoundedConstraint) aee1.getExistenceConstraint()).getBound()
				!= ((BoundedConstraint) aee2.getExistenceConstraint()).getBound())
			return false;
		return true;
	}

	public boolean hasAtMostConsequence() {
		if(consequenceExpression == null)
			return false;
		return consequenceExpression.hasAtMost();
	}

	public boolean hasAtLeastConsequence() {
		if(consequenceExpression == null)
			return false;
		return consequenceExpression.hasAtLeast();
	}

	public boolean isStillPossibleInFuture(ExistenceExpression consequenceExpression) {
		if(consequenceExpression.hasAtMost() && consequenceExpression.hasAtLeast())
			throw new UnsupportedOperationException();
		if(consequenceExpression.hasAtLeast())
			return true;
		return false;
	}

	public boolean isOk(ExistenceExpression consequenceExpression) {
		if(consequenceExpression.hasAtMost() && consequenceExpression.hasAtLeast())
			throw new UnsupportedOperationException();
		if(consequenceExpression.hasAtMost())
			return true;
		return false;
	}

	public boolean isPartOf(List<ActivityEvent> partialList, ExistenceExpression ee) {
		if(ee instanceof NonAtomicExistenceExpression)
			throw new UnsupportedOperationException();
		else {
			AtomicExistenceExpression aee = (AtomicExistenceExpression) ee;
			if(aee.getExistenceConstraint() instanceof AtLeast) {
				AtLeast atL = (AtLeast) aee.getExistenceConstraint();
				return isPartOf(partialList, atL.getActivityExpression());
			} else
				throw new UnsupportedOperationException();
		}
	}

	public boolean isPartOf(List<ActivityEvent> partialList, ActivityExpression ae) {
		if(ae instanceof AtomicActivityExpression) {
			AtomicActivityExpression aae = (AtomicActivityExpression) ae;
			return partialList.isEmpty() || (partialList.size() == 1 && partialList.get(0).equals(aae.getActivity()));
		} else {
			NonAtomicActivityExpression naae = (NonAtomicActivityExpression) ae;
			for(ActivityExpression x : naae.getExpressions())
				if(isPartOf(partialList, x))
					return true;
			return false;
		}
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		HashSet<Activity> res = conditionExpression.getUsedActivities();
		res.addAll(consequenceExpression.getUsedActivities());
		return res;
	}
}