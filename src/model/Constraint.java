package model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import miner.Config;
import miner.log.ActivityEvent;
import miner.log.DecisionActivation;
import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.constraint.ParsingCache;
import model.constraint.TimedConstraint;
import model.constraint.data.Deletion;
import model.constraint.data.Insertion;
import model.constraint.data.Read;
import model.constraint.data.Update;
import model.constraint.existence.ActivityAvailabilitySchedule;
import model.constraint.existence.AtLeast;
import model.constraint.existence.AtLeastChoice;
import model.constraint.existence.AtMost;
import model.constraint.existence.AtMostChoice;
import model.constraint.existence.extra.First;
import model.constraint.existence.extra.Last;
import model.constraint.relation.AlternatePrecedence;
import model.constraint.relation.AlternateResponse;
import model.constraint.relation.AtLeastLag;
import model.constraint.relation.AtMostLag;
import model.constraint.relation.ChainPrecedence;
import model.constraint.relation.ChainResponse;
import model.constraint.relation.Precedence;
import model.constraint.relation.RespondedPresence;
import model.constraint.relation.Response;
import model.constraint.resource.ActivityAuthorization;
import model.constraint.resource.AtLeastAvailable;
import model.constraint.resource.AtLeastUsage;
import model.constraint.resource.AtMostAvailable;
import model.constraint.resource.AtMostUsage;
import model.constraint.resource.DecisionAuthorization;
import model.constraint.resource.ResourceAvailabilitySchedule;
import model.constraint.resource.ResourceEquality;
import model.constraint.resource.SimultaneousCapacity;
import model.data.DataAttribute;
import model.data.Decision;
import model.expression.ActivityExpression;
import model.expression.AtomicActivityExpression;
import model.expression.AtomicExistenceExpression;
import model.expression.ExistenceExpression;
import model.expression.LogicalOperator;
import model.expression.NonAtomicActivityExpression;
import model.expression.NonAtomicExistenceExpression;
import model.resource.Resource;
import util.IndexResult;

public abstract class Constraint implements Serializable {

	private static final long serialVersionUID = -812539465001823321L;

	private Decision activationDecision;
	private Decision deactivationDecision;
	private boolean isOptional;

	public Constraint(Decision activationDecision, Decision deactivationDecision, boolean isOptional) {
		super();
		this.activationDecision = activationDecision;
		this.deactivationDecision = deactivationDecision;
		this.isOptional = isOptional;
	}

	public Decision getActivationDecision() {
		return activationDecision;
	}

	public void setActivationDecision(Decision activationDecision) {
		this.activationDecision = activationDecision;
	}

	public Decision getDeactivationDecision() {
		return deactivationDecision;
	}

	public void setDeactivationDecision(Decision deactivationDecision) {
		this.deactivationDecision = deactivationDecision;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	//	//	protected ArrayList<ActivityInterval> activityIntervals(Instance instance) {//TODO: needed if data is allowed to change after initial value assigned
	//	protected ActivityInterval activityIntervals(Instance instance) {
	//		int start = -1;
	//		int end = -1;
	//		for(int i = 0; i < instance.getEvents().size(); i++) {
	//			if(start == -1) {
	//				boolean active = false || getActivationDecision().getRules().isEmpty();
	//				for(DecisionRule dr : getActivationDecision().getRules()) {
	//					boolean thisRuleActive = true;
	//					for(Expression e : dr.getTrues())
	//						if(instance.getData().get(e) != true
	//						&& instance.getEvents().get(i).getData().get(e) != true) {
	//							thisRuleActive = false;
	//							break;
	//						}
	//					if(thisRuleActive)
	//						for(Expression e : dr.getFalses())
	//							if(instance.getData().get(e) != false
	//							&& instance.getEvents().get(i).getData().get(e) != false) {
	//								thisRuleActive = false;
	//								break;
	//							}
	//					if(thisRuleActive) {
	//						active = thisRuleActive;
	//						break;
	//					}
	//				}
	//				if(active)
	//					start = i;
	//			}
	//			if(start != -1) {
	//				//TODO
	////				boolean deactive = false || getDeactivationDecision().getRules().isEmpty();
	////				for(DecisionRule dr : getDeactivationDecision().getRules()) {
	////					boolean thisRuleDeactive = true;
	////					for(Expression e : dr.getTrues())
	////						if(instance.getData().get(e) != true
	////						&& instance.getEvents().get(i).getData().get(e) != true) {
	////							thisRuleDeactive = false;
	////							break;
	////						}
	////					if(thisRuleDeactive)
	////						for(Expression e : dr.getFalses())
	////							if(instance.getData().get(e) != false
	////							&& instance.getEvents().get(i).getData().get(e) != false) {
	////								thisRuleDeactive = false;
	////								break;
	////							}
	////					if(thisRuleDeactive) {
	////						deactive = thisRuleDeactive;
	////						break;
	////					}
	////				}
	////				if(deactive) {
	////					end = i;
	////					break;
	////				}
	//			}
	//		}
	//		if(start != -1 && end == -1)
	//			end = instance.getEvents().size() - 1;
	//		return new ActivityInterval(start, end);
	//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activationDecision == null) ? 0 : activationDecision.hashCode());
		result = prime * result + ((deactivationDecision == null) ? 0 : deactivationDecision.hashCode());
		result = prime * result + (isOptional ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}

	public boolean equals(Object obj, boolean checkDecisions) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if(checkDecisions) {
			Constraint other = (Constraint) obj;
			if (activationDecision == null) {
				if (other.activationDecision != null)
					return false;
			} else if (!activationDecision.equals(other.activationDecision))
				return false;
			if (deactivationDecision == null) {
				if (other.deactivationDecision != null)
					return false;
			} else if (!deactivationDecision.equals(other.deactivationDecision))
				return false;
			if (isOptional != other.isOptional)
				return false;
		}
		return true;
	}

	public abstract Constraint getDecisionlessCopy();

	public abstract ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, Resource activeResource, long currentTime);

	public abstract Rule evaluate(Log log, Rule ancestor);

	public static int count(ActivityExpression actExp, List<ActivityEvent> traceActs) {
		return count(actExp, traceActs, 0, -1);
	}

	public static int count(ActivityExpression actExp, List<ActivityEvent> traceActs, long minTime, long maxTime) {
		if(actExp instanceof AtomicActivityExpression)
			return count(((AtomicActivityExpression) actExp).getActivity(), traceActs, minTime, maxTime);
		NonAtomicActivityExpression x = (NonAtomicActivityExpression) actExp;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			int count = -1;
			for(ActivityExpression a : x.getExpressions()) {
				int c = count(a, traceActs, minTime, maxTime);
				if(count == -1 || c < count)
					count = c;
			}
			return count;
		} else {//or
			int count = 0;
			for(ActivityExpression a : x.getExpressions())
				count += count(a, traceActs, minTime, maxTime);
			return count;
		}
	}

	public static int count(Activity act, List<ActivityEvent> traceActs) {
		return count(act, traceActs, 0, -1);
	}

	public static int count(Activity act, List<ActivityEvent> traceActs, long minTime, long maxTime) {
		int count = 0;
		for(ActivityEvent a : traceActs)
			if(a.getStart() >= minTime
			&& (maxTime == -1 || a.getStart() <= maxTime)
			&& a.equals(act))
				count++;
		return count;
	}

	public static int countChoice(ActivityExpression actExp, List<ActivityEvent> traceActs) {
		if(actExp instanceof AtomicActivityExpression)
			return countChoice(((AtomicActivityExpression) actExp).getActivity(), traceActs)?1:0;
		NonAtomicActivityExpression x = (NonAtomicActivityExpression) actExp;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			throw new UnsupportedOperationException();
			//			int count = -1;
			//			for(ActivityExpression a : x.getExpressions()) {
			//				int c = countChoice(a, traceActs);
			//				if(count == -1 || c < count)
			//					count = c;
			//			}
			//			return count;
		} else {//or
			int count = 0;
			for(ActivityExpression a : x.getExpressions())
				count += countChoice(a, traceActs);
			return count;
		}
	}

	public static boolean countChoice(Activity act, List<ActivityEvent> traceActs) {
		for(Activity a : traceActs)
			if(a.equals(act))
				return true;
		return false;
	}

	public static boolean contains(ActivityExpression actExp,
			List<ActivityEvent> trace) {
		IndexResult index = indexOf(actExp, trace, -1, -1);
		return index != null;
	}

	public static IndexResult indexOf(ActivityExpression actExp,
			List<ActivityEvent> trace) {
		return indexOf(actExp, trace, -1, -1);
	}

	public static IndexResult indexOf(ActivityExpression actExp,
			List<ActivityEvent> trace, int minIndex, long minTime) {
		if(actExp instanceof AtomicActivityExpression) {
			int sub = 0;
			int firstIndex = -1;
			while(sub < trace.size()) {
				int index = trace.subList(sub, trace.size()).indexOf(((AtomicActivityExpression) actExp).getActivity());
				if(index == -1)
					break;
				index = index + sub;
				if((minIndex == -1 || index >= minIndex)
						&& (firstIndex == -1 || firstIndex > index)
						&& (minTime == -1 || trace.get(index).getStart() >= minTime)) {
					firstIndex = index;
					break;
				}
				sub = index+1;
			}
			if(firstIndex == -1)
				return null;
			return new IndexResult(firstIndex, firstIndex);
		}
		IndexResult firstIndex = null;
		NonAtomicActivityExpression x = (NonAtomicActivityExpression) actExp;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			for(ActivityExpression a : x.getExpressions()) {
				IndexResult i = indexOf(a, trace, minIndex, minTime);
				if(i == null)
					return null;
				if(firstIndex == null)
					firstIndex = i;
				else {
					firstIndex.setIndex_start(Math.min(firstIndex.getIndex_start(), i.getIndex_start()));
					firstIndex.setIndex_end(Math.max(firstIndex.getIndex_end(), i.getIndex_end()));
				}
			}
			return firstIndex;
		} else {//or
			for(ActivityExpression a : x.getExpressions()) {
				IndexResult i = indexOf(a, trace, minIndex, minTime);
				if(i != null)
					if(firstIndex == null
					|| i.getIndex_end() < firstIndex.getIndex_end()
					|| (i.getIndex_end() == firstIndex.getIndex_end()
					&& i.getIndex_start() < firstIndex.getIndex_start()))
						firstIndex = i;
			}
			return firstIndex;
		}
	}

	public static IndexResult lastIndexOf(ActivityExpression actExp,
			List<ActivityEvent> trace, int maxIndex, long maxTime) {
		if(actExp instanceof AtomicActivityExpression) {
			int sub = trace.size();
			int lastIndex = -1;
			while(sub > 0) {
				int index = trace.subList(0, sub).lastIndexOf(((AtomicActivityExpression) actExp).getActivity());
				if(index == -1)
					break;
				if((maxIndex == -1 || index <= maxIndex)
						&& (lastIndex == -1 || lastIndex > index)
						&& (maxTime == -1 || trace.get(index).getStart() <= maxTime)) {
					lastIndex = index;
					break;
				}
				sub = index;
			}
			if(lastIndex == -1)
				return null;
			return new IndexResult(lastIndex, lastIndex);
		}
		IndexResult lastIndex = null;
		NonAtomicActivityExpression x = (NonAtomicActivityExpression) actExp;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			for(ActivityExpression a : x.getExpressions()) {
				IndexResult i = lastIndexOf(a, trace, maxIndex, maxTime);
				if(i == null)
					return null;
				if(lastIndex == null)
					lastIndex = i;
				else {
					lastIndex.setIndex_start(Math.min(lastIndex.getIndex_start(), i.getIndex_start()));
					lastIndex.setIndex_end(Math.max(lastIndex.getIndex_end(), i.getIndex_end()));
				}
			}
			return lastIndex;
		} else {//or
			for(ActivityExpression a : x.getExpressions()) {
				IndexResult i = lastIndexOf(a, trace, maxIndex, maxTime);
				if(i != null)
					if(lastIndex == null
					|| i.getIndex_start() > lastIndex.getIndex_start()
					|| (i.getIndex_start() == lastIndex.getIndex_start()
					&& i.getIndex_end() > lastIndex.getIndex_end()))
						lastIndex = i;
			}
			return lastIndex;
		}
	}

	public static boolean contains(ExistenceExpression exExp,
			List<ActivityEvent> trace) {
		return indexOf(exExp, trace, -1, -1) != null;
	}

	public static IndexResult indexOf(ExistenceExpression exExp,
			List<ActivityEvent> trace) {
		return indexOf(exExp, trace, -1, -1);
	}

	public static IndexResult indexOf(ExistenceExpression exExp,
			List<ActivityEvent> trace, int minIndex, long minTime) {
		if(exExp instanceof AtomicExistenceExpression) {
			if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtLeast) {
				AtLeast atL = (AtLeast) ((AtomicExistenceExpression) exExp).getExistenceConstraint();
				IndexResult index = null;
				int minIndex_tmp = minIndex;
				for(int i = 0; i < atL.getBound(); i++) {
					if(i == 0) {
						index = indexOf(atL.getActivityExpression(), trace, minIndex_tmp, minTime);
						if(index == null)
							return null;
						minIndex_tmp = index.getIndex_start()+1;
					} else {
						IndexResult index2 = indexOf(atL.getActivityExpression(), trace, minIndex_tmp, minTime);
						if(index2 == null)
							return null;
						minIndex_tmp = index2.getIndex_start()+1;
						index.setIndex_start(Math.min(index.getIndex_start(), index2.getIndex_start()));
						index.setIndex_end(Math.max(index.getIndex_end(), index2.getIndex_end()));
					}
				}
				return index;
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtMost) {
				AtMost atM = (AtMost) ((AtomicExistenceExpression) exExp).getExistenceConstraint();
				if(atM.getBound() > 0)
					throw new IllegalArgumentException("TODO");
				IndexResult index = indexOf(atM.getActivityExpression(), trace, minIndex, minTime);
				return index;
				//TODO
//				int minIndex_tmp = minIndex;
//				int leftOfBound = atM.getBound();
//				while(true) {
//					IndexResult index = indexOf(atM.getActivityExpression(), trace, minIndex_tmp, minTime);
//					if(index == null)
//						break;
//					else {
//						leftOfBound--;
//						minIndex_tmp = index.getIndex_start()+1;
//					}
//				}
//				if(leftOfBound < 0)
//					return null;
//				return new IndexResult(-2, -2);
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtLeastChoice) {//TODO: other existence expressions
				throw new IllegalArgumentException("TODO");
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtMostChoice) {
				throw new IllegalArgumentException("TODO");
			}
		}
		NonAtomicExistenceExpression x = (NonAtomicExistenceExpression) exExp;
		IndexResult firstIndex = null;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			for(ExistenceExpression a : x.getExpressions()) {
				IndexResult i = indexOf(a, trace, minIndex, minTime);
				if(i == null)
					return null;
				if(i.getIndex_start() != -1 && i.getIndex_end() != -1) {
					if(firstIndex == null)
						firstIndex = i;
					else {
						firstIndex.setIndex_start(Math.min(firstIndex.getIndex_start(), i.getIndex_start()));
						firstIndex.setIndex_end(Math.max(firstIndex.getIndex_end(), i.getIndex_end()));
					}
				}
			}
		} else {//or
			for(ExistenceExpression a : x.getExpressions()) {
				IndexResult i = indexOf(a, trace, minIndex, minTime);
				if(i != null)
					if(firstIndex == null
					|| i.getIndex_end() < firstIndex.getIndex_end()
					|| (i.getIndex_end() == firstIndex.getIndex_end())
					&& (i.getIndex_start() < firstIndex.getIndex_start()))
						firstIndex = i;
			}
		}
		return firstIndex;
	}

	public static IndexResult lastIndexOf(ExistenceExpression exExp,
			List<ActivityEvent> trace) {
		return lastIndexOf(exExp, trace, -1, -1);
	}

	public static IndexResult lastIndexOf(ExistenceExpression exExp,
			List<ActivityEvent> trace, int minIndex, long minTime) {
		//		//TODO test
		//		trace = new ArrayList<>(trace);
		//		trace.addAll(trace);
		//		trace.addAll(trace);
		if(exExp instanceof AtomicExistenceExpression) {
			if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtLeast) {
				AtLeast atL = (AtLeast) ((AtomicExistenceExpression) exExp).getExistenceConstraint();
				IndexResult index = null;
				int minIndex_tmp = minIndex;
				for(int i = 0; i < atL.getBound(); i++) {
					if(i == 0) {
						index = lastIndexOf(atL.getActivityExpression(), trace, minIndex_tmp, minTime);
						if(index == null)
							return null;
						minIndex_tmp = index.getIndex_end()-1;
					} else {
						IndexResult index2 = lastIndexOf(atL.getActivityExpression(), trace, minIndex_tmp, minTime);
						if(index2 == null)
							return null;
						minIndex_tmp = index2.getIndex_end()-1;
						index.setIndex_start(Math.min(index.getIndex_start(), index2.getIndex_start()));
						index.setIndex_end(Math.max(index.getIndex_end(), index2.getIndex_end()));
					}
				}
				return index;
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtMost) {
				AtMost atM = (AtMost) ((AtomicExistenceExpression) exExp).getExistenceConstraint();
				if(atM.getBound() > 0)
					throw new IllegalArgumentException("TODO");
				IndexResult index = lastIndexOf(atM.getActivityExpression(), trace, minIndex, minTime);
//				if(index == null)
//					return new IndexResult(-2, -2);//iets teruggeven om validatie te doen werken
				return index;


				//				AtMost atM = (AtMost) ((AtomicExistenceExpression) exExp).getExistenceConstraint();
				//				IndexResult index = null;//TODO: other existence expressions
				//				int minIndex_tmp = minIndex;
				//				for(int i = 0; i < atM.getBound(); i++) {
				//					if(i == 0) {
				//						index = lastIndexOf(atM.getActivityExpression(), trace, minIndex_tmp, minTime);
				//						if(index == null)
				//							return null;
				//						minIndex_tmp = index.getIndex_end()-1;
				//					} else {
				//						IndexResult index2 = lastIndexOf(atM.getActivityExpression(), trace, minIndex_tmp, minTime);
				//						if(index2 == null)
				//							return null;
				//						minIndex_tmp = index2.getIndex_end()-1;
				//						index.setIndex_start(Math.min(index.getIndex_start(), index2.getIndex_start()));
				//						index.setIndex_end(Math.max(index.getIndex_end(), index2.getIndex_end()));
				//					}
				//				}
				//				return index;
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtLeastChoice) {
				throw new IllegalArgumentException("TODO");
			} else if(((AtomicExistenceExpression) exExp).getExistenceConstraint() instanceof AtMostChoice) {
				throw new IllegalArgumentException("TODO");
			}
		}
		NonAtomicExistenceExpression x = (NonAtomicExistenceExpression) exExp;
		IndexResult lastIndex = null;
		if(x.getOperator().equals(LogicalOperator.AND)) {//and
			for(ExistenceExpression a : x.getExpressions()) {
				IndexResult i = lastIndexOf(a, trace, minIndex, minTime);
				if(i == null)
					return null;
				if(lastIndex == null)
					lastIndex = i;
				else {
					lastIndex.setIndex_start(Math.min(lastIndex.getIndex_start(), i.getIndex_start()));
					lastIndex.setIndex_end(Math.max(lastIndex.getIndex_end(), i.getIndex_end()));
				}
			}
		} else {//or
			for(ExistenceExpression a : x.getExpressions()) {
				IndexResult i = lastIndexOf(a, trace, minIndex, minTime);
				if(i != null)
					if(lastIndex == null
					|| i.getIndex_end() < lastIndex.getIndex_end()
					|| (i.getIndex_end() == lastIndex.getIndex_end())
					&& (i.getIndex_start() < lastIndex.getIndex_start()))
						lastIndex = i;
			}
		}
		return lastIndex;
	}

	@Override
	public String toString() {
		String act = "";
		if(getActivationDecision() != null
				&& !getActivationDecision().getRules().isEmpty())
			act = " activateIf" + getActivationDecision();
		else if(Config.ALWAYS_USE_SHOW_FULL_CONSTRAINT)
			act = " activateIf[True]";
		String deact = "";
		if(getDeactivationDecision() != null
				&& !getDeactivationDecision().getRules().isEmpty())
			deact = " deactivateIf" + getDeactivationDecision();
		else if(Config.ALWAYS_USE_SHOW_FULL_CONSTRAINT)
			deact = " deactivateIf[False]";
		return act + deact;
	}

	public String getTextRepresentation() {
		String optional = "";
		if(isOptional())
			optional += "OPTIONAL ";
		String deact = "";
		if(getDeactivationDecision() != null)
			deact = " unless " + getDeactivationDecision().getTextualVersion();
		if(getActivationDecision() == null
				|| getActivationDecision().getRules().isEmpty())
			return optional + getTextualConstraint() + deact;
		return optional + getActivationDecision().getTextualVersion()
				+ " then " + getTextualConstraint() + deact;
	}

	protected abstract String getTextualConstraint();

	protected static String getTimedText(TimedConstraint c) {
		String res = "";
		if(c.getTimeA() != 0)
			res += " after at least " + getTime(c.getTimeA());
		if(c.getTimeB() != -1) {
			if(res.length() != 0)
				res += " and";
			res += " within at most " + getTime(c.getTimeB());
		}
		return res;
	}

	public static String getTime(long inputSeconds) {
		if(inputSeconds < 60)
			return inputSeconds + " second" + (inputSeconds>1?"s":"");
		long minutes = inputSeconds / 60;
		long seconds = inputSeconds % 60;
		if(minutes < 60) {
			String res = minutes + " minute" + (minutes>1?"s":"");
			if(seconds != 0)
				res += " " + seconds + " second" + (seconds>1?"s":"");
			return res;
		}
		long hours = minutes / 60;
		minutes = minutes % 60;
		if(hours < 24) {
			String res = hours + " hour" + (hours>1?"s":"");
			if(minutes != 0)
				res += " " + minutes + " minute" + (minutes>1?"s":"");
			if(seconds != 0)
				res += " " + seconds + " second" + (seconds>1?"s":"");
			return res;
		}
		long days = hours / 24;
		hours = hours % 24;
		String res = days + " day" + (days>1?"s":"");
		if(hours != 0)
			res += " " + hours + " hour" + (hours>1?"s":"");
		if(minutes != 0)
			res += " " + minutes + " minute" + (minutes>1?"s":"");
		if(seconds != 0)
			res += " " + seconds + " second" + (seconds>1?"s":"");
		return res;
	}

	public boolean isRelatedTo(Constraint c) {
		if(c == null
				|| !this.getClass().equals(c.getClass()))
			return false;
		return true;
	}

	public HashSet<DataAttribute> getUsedDataAttributes() {
		HashSet<DataAttribute> res = new HashSet<>();
		if(activationDecision != null)
			res.addAll(activationDecision.getUsedDataAttributes());
		if(deactivationDecision != null)
			res.addAll(deactivationDecision.getUsedDataAttributes());
		return res;
	}

	public abstract HashSet<Activity> getUsedActivities();

	public abstract HashSet<Resource> getUsedResources();

	public long getActivationTime(Trace t) {
		return getActivationTime(t, getActivationDecision());
	}

	public static long getActivationTime(Trace t, Decision activationDecision) {
		long activationTime = 0;
		if(activationDecision != null) {
			DecisionActivation decisionActivation = t.getDecisionActivation(activationDecision);
			if(decisionActivation != null)
				activationTime = decisionActivation.getTime();
			else
				activationTime = -1;
		}
		return activationTime;
	}

	public boolean isActivated(Trace t) {
		return isActivated(t, getActivationDecision());
	}

	public static boolean isActivated(Trace t, Decision activationDecision) {
		return getActivationTime(t, activationDecision) != -1;
	}

	@SuppressWarnings("unchecked")
	public static Constraint parseConstraint(String input, ParsingCache pc) {
		String text = input.trim();
		if(pc == null)
			pc = new ParsingCache();
		boolean isOptional = false;
		if(text.startsWith("OPTIONAL ")) {
			isOptional = true;
			text = text.substring("OPTIONAL ".length()).trim();
		}
		Decision activationDec = null;
		if(text.contains(" activateIf[") && !text.endsWith(")")) {
			int i = text.indexOf(" activateIf[");
			String dec = text.substring(i + " activateIf".length());
			int bracket = getBracketsEnd(dec);
			text = (text.substring(0, i) + dec.substring(bracket+1)).trim();
			dec = dec.substring(1, bracket).trim();
			activationDec = Decision.parseDecision(dec, pc);
		}
		Decision deactivationDecision = null;
		if(text.contains(" deactivateIf[") && !text.endsWith(")")) {
			int i = text.indexOf(" deactivateIf[");
			String dec = text.substring(i + " deactivateIf[".length(), text.length()-1);
			text = text.substring(0, i);
			deactivationDecision = Decision.parseDecision(dec, pc);
		}
		for(Class<? extends Constraint> c : new Class[]{
				AtLeast.class, AtMost.class, AtLeastChoice.class, AtMostChoice.class, ActivityAvailabilitySchedule.class,
				First.class, Last.class,
				Deletion.class, Insertion.class, Read.class, Update.class,
				AlternatePrecedence.class, AlternateResponse.class, AtLeastLag.class, AtMostLag.class, ChainPrecedence.class, ChainResponse.class, Precedence.class, RespondedPresence.class, Response.class,
				ActivityAuthorization.class, AtLeastAvailable.class, AtLeastUsage.class, AtMostAvailable.class, AtMostUsage.class, DecisionAuthorization.class, ResourceAvailabilitySchedule.class, ResourceEquality.class, SimultaneousCapacity.class}
				)
			if(text.startsWith(c.getSimpleName() + "(")
					|| text.startsWith("Required" + c.getSimpleName() + "(")
					|| text.startsWith("Prohibited" + c.getSimpleName() + "(")
					|| text.startsWith("Not" + c.getSimpleName() + "(")
					|| text.startsWith("No" + c.getSimpleName() + "(")
					|| (c == ResourceEquality.class && text.startsWith("ResourceInequality" + "("))
					|| (c == ActivityAvailabilitySchedule.class && text.startsWith("ActivityUnavailabilitySchedule" + "("))
					|| (c == ResourceAvailabilitySchedule.class && text.startsWith("ResourceUnavailabilitySchedule" + "(")))
				try {
					return (Constraint) c.getDeclaredMethod("parseConstraint", new Class<?>[]{String.class, ParsingCache.class, Decision.class, Decision.class, boolean.class})
							.invoke(null, text, pc, activationDec, deactivationDecision, isOptional);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("Cannot parse (TODO?): " + text);
				}
		throw new UnsupportedOperationException();
	}

	public static String[] getSplit(String input, Integer parts) {
		if(parts != null)
			return getSplit(input, (int) parts);
		return getSplit(input, input.split(",").length);
	}

	public static String[] getSplit(String input, int parts) {
		String[] res = input.split(",");
		if(res.length == parts) {
			for(int i = 0; i < res.length; i++)
				res[i] = res[i].trim();
			return res;
		}
		res = new String[parts];
		int i = 0;
		String text = input;
		String tmp = "";
		while(true) {
			int bracket1 = text.indexOf("(");
			int bracket2 = text.indexOf("[");
			int comma = text.indexOf(",");
			if(comma == -1) {
				res[i] = tmp + text;
				break;
			} else if(bracket1 != -1 && bracket1 < comma && (bracket2 == -1 || bracket1 < bracket2)) {
				int end = getBracketsEnd(text.substring(bracket1), "(", ")");
				String part = text.substring(0, bracket1+end+1);
				text = text.substring(bracket1+end+1);
				res[i] = tmp + part;
				if(text.trim().length() == 0)
					break;
				else if(text.trim().startsWith(",")) {
					text = text.trim().substring(1).trim();
					tmp = "";
				} else {
					tmp += part;
					res[i] = null;
					i--;
					if(text.startsWith(" ")) {
						tmp += " ";
						text = text.trim();
					}
				}
			} else if(bracket2 != -1 && bracket2 < comma) {
				int end = getBracketsEnd(text.substring(bracket2), "[", "]");
				String part = text.substring(0, bracket2+end+1);
				text = text.substring(bracket2+end+1);
				res[i] = tmp + part;
				if(text.trim().length() == 0)
					break;
				else if(text.trim().startsWith(",")) {
					text = text.trim().substring(1).trim();
					tmp = "";
				} else {
					tmp += part;
					res[i] = null;
					i--;
					if(text.startsWith(" ")) {
						tmp += " ";
						text = text.trim();
					}
				}
			} else {
				String part = text.substring(0, comma);
				text = text.substring(comma+1).trim();
				res[i] = tmp + part;
				tmp = "";
			}
			i++;
		}
		return res;
	}

	public static int getBracketsEnd(String text, String bracketStart, String bracketEnd) {
		int count = 1;
		for(int i = 1; i < text.length(); i++) {
			String x = text.substring(i, i+1);
			if(x.equals(bracketStart))
				count++;
			else if(x.equals(bracketEnd)) {
				count--;
				if(count == 0)
					return i;
			}
		}
		throw new RuntimeException();
	}

	private static int getBracketsEnd(String text) {
		return getBracketsEnd(text, "[", "]");
	}
}