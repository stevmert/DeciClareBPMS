package model.constraint.resource;

import model.Activity;
import model.constraint.BoundedConstraint;
import model.data.Decision;
import model.expression.ResourceExpression;

public abstract class ResourceUsage extends ResourceParameterConstraint implements BoundedConstraint {

	private static final long serialVersionUID = 2860757604827713976L;

	//	private ActivityExpression activityExpression;
	private Activity activity;
	private int bound;

	public ResourceUsage(Decision activationDecision, Decision deactivationDec, Activity activity,
			ResourceExpression resourceExpression, int bound, boolean isOptional) {
		super(activationDecision, deactivationDec, resourceExpression, isOptional);
		this.activity = activity;
		this.bound = bound;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getBound() {
		return bound;
	}

	@Override
	public void setBound(int bound) {
		this.bound = bound;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + bound;
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
		ResourceUsage other = (ResourceUsage) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (bound != other.bound)
			return false;
		return true;
	}
}