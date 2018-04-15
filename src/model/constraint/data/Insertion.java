package model.constraint.data;

import java.util.HashMap;

import miner.log.Log;
import miner.log.Trace;
import miner.rule.Rule;
import model.Constraint;
import model.ValidationStatus;
import model.constraint.DataConstraint;
import model.constraint.ParsingCache;
import model.data.Decision;
import model.expression.ActivityExpression;
import model.expression.DataExpression;
import model.resource.Resource;

public class Insertion extends DataConstraint {

	private static final long serialVersionUID = 2135045120298547994L;

	public Insertion(Decision activationDecision, Decision deactivationDec, DataExpression dataExpression,
			ActivityExpression actExpression, boolean isPositiveVersion, boolean isOptional) {
		super(activationDecision, deactivationDec, dataExpression, actExpression, isPositiveVersion, isOptional);
	}

	@Override
	public String toString() {
		return toString(this.getClass().getSimpleName());
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new Insertion(null, null, getDataExpression(), getActExpression(), isPositiveVersion(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ValidationStatus validate(Trace t, HashMap<Resource, Integer> resourceUsage, long currentTime) {
		return ValidationStatus.SATISFIED;
	}

	@Override
	protected String getTextualConstraintVerb() {
		return "insert";
	}

	public static DataConstraint parseConstraint(String input, ParsingCache pc, Decision activationDecision, Decision deactivationDecision, boolean isOptional) {
		String text = input;
		boolean isPos = text.startsWith("Required");
		text = text.substring(text.indexOf("(")+1, text.length()-1);
		String[] split = getSplit(text, 2);
		return new Insertion(activationDecision, deactivationDecision,
				DataExpression.parseDataExpression(split[1], pc),
				ActivityExpression.parseActivityExpression(split[0], pc),
				isPos, isOptional);
	}
}