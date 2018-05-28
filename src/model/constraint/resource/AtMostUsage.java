package model.constraint.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import miner.log.ActivityEvent;
import miner.log.Log;
import miner.log.ResourceEvent;
import miner.log.Trace;
import miner.rule.Rule;
import model.Activity;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.ParsingCache;
import model.data.Decision;
import model.expression.ResourceExpression;
import model.resource.Resource;

public class AtMostUsage extends ResourceUsage {

	private static final long serialVersionUID = 7965950888239011087L;

	public AtMostUsage(Decision activationDecision, Decision deactivationDec, Activity activity,
			ResourceExpression resourceExpression, int bound, boolean isOptional) {
		super(activationDecision, deactivationDec, activity, resourceExpression, bound, isOptional);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getActivity() + ", "
				+ getResourceExpression() + ", " + getBound() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		return getActivity() + " uses at most " + getBound() + " " + getResourceExpression();
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new AtMostUsage(null, null, getActivity(), getResourceExpression(),
				getBound(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, Resource activeResource, long currentTime) {
		long activationTime = getActivationTime(t);
		if(activationTime != -1) {
			List<ActivityEvent> actsRem = t.getRemainingActivityList(activationTime);
			for(ActivityEvent ae : actsRem)
				if(ae.equals(getActivity())) {
					ArrayList<ResourceEvent> res = t.getResourceEventsBetween(ae.getStart(), ae.getEnd());
					//TODO: not correct for if support for parallel execution is added
					int nrUsed = 0;
					for(ResourceEvent re : res)
						if(this.getResourceExpression().getUsedResources().contains(re.getResource()))
							nrUsed += re.getNrOfInstances();
					if(nrUsed > getBound()) {
						if(ae.getStart() == currentTime)
							return ValidationStatus.SATISFIED;
						return ValidationStatus.VIOLATED;
					}
				}
		}
		return ValidationStatus.SATISFIED;
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		return new HashSet<>();
	}

	public static AtMostUsage parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(AtMostUsage.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 3);
		Activity act = pc.getActivity(split[0]);
		if(act == null) {
			act = new Activity(split[0]);
			pc.addActivity(act);
		}
		return new AtMostUsage(activationDec, deactivationDecision,
				act,
				ResourceExpression.parseResourceExpression(split[1], pc),
				Integer.parseInt(split[2]),
				isOptional);
	}
}