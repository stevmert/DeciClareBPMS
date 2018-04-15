package model.expression;

import java.util.HashSet;

import model.Activity;
import model.constraint.ParsingCache;

public interface ActivityExpression {

	public int getNrOfElements();

	public HashSet<Activity> getUsedActivities();

	public static ActivityExpression parseActivityExpression(String input, ParsingCache pc) {
		if(input.contains(" " + LogicalOperator.AND + " ") || input.contains(" " + LogicalOperator.OR + " "))
			return NonAtomicActivityExpression.parseNonAtomicActivityExpression(input, pc);
		return AtomicActivityExpression.parseAtomicActivityExpression(input, pc);
	}
}