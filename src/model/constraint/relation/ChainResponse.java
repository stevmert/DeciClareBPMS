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
import model.constraint.TimedConstraint;
import model.data.Decision;
import model.expression.ExistenceExpression;
import model.resource.Resource;
import util.IndexResult;

public class ChainResponse extends RelationConstraint implements TimedConstraint {

	private static final long serialVersionUID = -4833816453287114292L;

	private long timeA;
	private long timeB;

	public ChainResponse(Decision activationDec, Decision deactivationDec,
			ExistenceExpression condition,
			ExistenceExpression consequence, long timeA, long timeB,
			boolean isOptional) {
		super(activationDec, deactivationDec, condition, consequence, isOptional);
		this.timeA = timeA;
		this.timeB = timeB;
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
		return this.getClass().getSimpleName() + "(" + getConditionExpression()
		+ ", " + getConsequenceExpression()
		+ ", " + getTimeA()
		+ ", " + getTimeB() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		String verb;
		if(getConditionExpression().getNrOfElements() == 1)
			verb = " has to";
		else
			verb = " have to";
		return getConditionExpression().toTextualString() + verb + " be directly followed by "
		+ getConsequenceExpression().toTextualString() + getTimedText(this);
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
		return new ChainResponse(null, null, getConditionExpression(), getConsequenceExpression(),
				getTimeA(), getTimeB(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineChainResponse(this.getActivationDecision(), this.getConditionExpression(),
				this.getConsequenceExpression(), log);
	}

	public static Rule mineChainResponse(Decision activationDecision,
			ExistenceExpression conditionExpression, ExistenceExpression consequenceExpression,
			Log log) {
		throw new IllegalArgumentException("TODO: still used?");//TODO: still used?
		//		HashSet<Integer> tracesPositive = new HashSet<>();
		//		HashSet<Integer> tracesNegative = new HashSet<>();
		//		int countOtherViolations_Traces = 0;
		//		int countNrOfConfirmations = 0;
		//		int countNrOfViolations = 0;
		//		//		long timeMin = -1;
		//		//		long timeMax = -1;
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
		//				int indexFrom = indexOf(conditionExpression, acts, -1, -1);
		//				if(indexFrom > -1) {
		//					boolean restIsViolating = false;
		//					boolean hasConformed = false;
		//					boolean hasViolated = false;
		//					while(true) {
		//						int indexTo = indexOf(consequenceExpression, acts
		//								.subList(indexFrom + 1, acts.size()), -1,
		//								acts.get(indexFrom).getEnd());
		//						if(restIsViolating
		//								|| indexTo == -1) {
		//							restIsViolating = true;
		//							hasViolated = true;
		//							countNrOfViolations++;
		//						} else {
		//							long firstTimingAfter = -1;
		//							for(int j = indexFrom+1; j < acts.size(); j++) {
		//								if(acts.get(j).getStart()
		//										>= acts.get(indexFrom).getEnd()) {
		//									firstTimingAfter = acts.get(j).getStart();
		//									break;
		//								}
		//							}
		//							if(firstTimingAfter == -1) {
		//								restIsViolating = true;
		//								hasViolated = true;
		//								countNrOfViolations++;
		//							} else {
		//								indexTo += indexFrom + 1;
		//								if(acts.get(indexTo).getStart() == firstTimingAfter) {
		//									long countDiff = acts.get(indexTo).getStart()
		//											- acts.get(indexFrom).getEnd();
		//									if(countDiff < 0) {
		//										throw new IllegalArgumentException("countDiff < 0???");
		//									} else {
		//										countNrOfConfirmations++;
		//										hasConformed = true;
		//										//										if(timeMin == -1 || timeMin > countDiff)
		//										//											timeMin = countDiff;
		//										//										if(timeMax == -1 || timeMax < countDiff)
		//										//											timeMax = countDiff;
		//									}
		//								} else {
		//									hasViolated = true;
		//									countNrOfViolations++;
		//								}
		//							}
		//						}
		//						indexFrom = indexOf(conditionExpression, acts, indexFrom+1, -1);
		//						if(indexFrom == -1)
		//							break;
		//					}
		//					if(!hasViolated && hasConformed)
		//						tracesPositive.add(i);
		//					else if(hasViolated && !hasConformed)
		//						tracesNegative.add(i);
		//					else
		//						countOtherViolations_Traces++;
		//				}
		//			}
		//		}
		//		//remove redundant decision rules, as these are not used anyway...
		//		if(countNrOfConfirmations+countNrOfViolations > 0
		//				&& activationDecision != null
		//				&& usedDecisions.size() != activationDecision.getRules().size())
		//			activationDecision.setRules(usedDecisions);
		//		Rule pos = null;
		//		Rule neg = null;
		//		if(Config.MINE_CHAINRESPONSE)
		//			pos = new Rule(new ChainResponse(activationDecision, null, conditionExpression, consequenceExpression, 0, -1, true),
		//					countNrOfConfirmations, countNrOfViolations,
		//					tracesPositive, tracesNegative.size() + countOtherViolations_Traces,
		//					log.size());
		//		if(Config.MINE_NOTCHAINRESPONSE)
		//			neg = new Rule(new ChainResponse(activationDecision, null, conditionExpression,
		//					consequenceExpression.getNegation(), 0, -1, false),
		//					countNrOfViolations, countNrOfConfirmations,
		//					tracesNegative, tracesPositive.size() + countOtherViolations_Traces,
		//					log.size());
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
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		long activationTime = getActivationTime(t);
		int countNrOfViolations = 0;
		int countNrOfPossibleViolations = 0;
		if(activationTime != -1) {//not activated by activation decision
			List<ActivityEvent> acts = t.getRemainingActivityList(activationTime);
			IndexResult indexCond = indexOf(getConditionExpression(), acts, -1, -1);
			if(indexCond != null) {
				activationTime = acts.get(indexCond.getIndex_end()).getEnd();
				boolean restIsViolating = false;
				long maxDelay = 0;
				long minDeadline= -1;
				while(true) {
					IndexResult indexConseq = indexOf(getConsequenceExpression(), acts
							.subList(indexCond.getIndex_end() + 1, acts.size()), -1,
							acts.get(indexCond.getIndex_end()).getEnd());
					if(restIsViolating
							|| indexConseq == null
							|| indexConseq.getIndex_start() == -1
							|| indexConseq.getIndex_end() == -1) {
						restIsViolating = true;
						if(isStillPossibleInFuture(getConsequenceExpression())
								&& (acts.size() == indexCond.getIndex_end()+1 || isPartOf(acts.subList(indexCond.getIndex_end()+1, acts.size()), getConsequenceExpression()))) {
							if(activationTime <= currentTime) {
								if(activationTime+timeA-currentTime > maxDelay)
									maxDelay = activationTime+timeA-currentTime;
								if(timeB != -1 && (minDeadline == -1 || activationTime+timeB-currentTime < minDeadline))
									minDeadline = activationTime+timeB-currentTime;
							}
							countNrOfPossibleViolations++;
						} else
							countNrOfViolations++;
					} else {
						long firstTimingAfter = -1;
						for(int j = indexCond.getIndex_end()+1; j < acts.size(); j++) {
							if(acts.get(j).getStart()
									>= acts.get(indexCond.getIndex_end()).getEnd()) {
								firstTimingAfter = acts.get(j).getStart();
								break;
							}
						}
						if(firstTimingAfter == -1) {
							restIsViolating = true;
							countNrOfViolations++;
						} else {
							int indexToStart = indexConseq.getIndex_start() + indexCond.getIndex_end() + 1;
							if(acts.get(indexToStart).getStart() != firstTimingAfter)
								countNrOfViolations++;
						}
					}
					indexCond = indexOf(getConditionExpression(), acts, indexCond.getIndex_start()+1, -1);
					if(indexCond == null)
						break;
					activationTime = acts.get(indexCond.getIndex_end()).getEnd();
				}
				if(countNrOfPossibleViolations > 0 && countNrOfViolations == 0) {
					if(activationTime <= currentTime) {
						if(timeB != -1 && activationTime+timeB < currentTime)
							return ValidationStatus.DEADEND;
						else if(activationTime+timeA > currentTime) {
//							ValidationStatus.TIME_SATISFIABLE.setBound(maxDelay);
//							return ValidationStatus.TIME_SATISFIABLE;
							return ValidationStatus.ACTIVITY_SATISFIABLE;
						} else if(timeB != -1) {
							ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE.setBound(minDeadline);
							return ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE;
						}
					}
					return ValidationStatus.ACTIVITY_SATISFIABLE;
				} else if(countNrOfViolations == 0 && countNrOfPossibleViolations == 0)
					return ValidationStatus.SATISFIED;
				else
					return ValidationStatus.VIOLATED;
			}
		}
		return ValidationStatus.SATISFIED;
	}

	@Override
	public RelationConstraint getShallowCopy(ExistenceExpression condition,
			ExistenceExpression consequence) {
		return new ChainResponse(getActivationDecision(), getDeactivationDecision(), condition, consequence, getTimeA(),
				getTimeB(), isOptional());
	}

	public static ChainResponse parseConstraint(String input, ParsingCache pc, Decision activationDec, Decision deactivationDecision, boolean isOptional) {
		String text = input.substring(ChainResponse.class.getSimpleName().length()+1, input.length()-1);
		String[] split = getSplit(text, 4);
		return new ChainResponse(activationDec, deactivationDecision,
				ExistenceExpression.parseExistenceExpression(split[0], pc),
				ExistenceExpression.parseExistenceExpression(split[1], pc),
				Long.parseLong(split[2]),
				Long.parseLong(split[3]),
				isOptional);
	}
}