package model.expression;

import java.util.HashSet;

import model.Activity;
import model.constraint.ParsingCache;

public interface ExistenceExpression {
	public int getNrOfElements();
	public String toTextualString();
	public ExistenceExpression getNegation();
	public boolean hasAtMost();
	public boolean hasAtLeast();
	public HashSet<Activity> getUsedActivities();

	public static ExistenceExpression parseExistenceExpression(String input, ParsingCache pc) {
		if(input.contains(" " + LogicalOperator.AND + " ") || input.contains(" " + LogicalOperator.OR + " "))
			return NonAtomicExistenceExpression.parseNonAtomicExistenceExpression(input, pc);
		return AtomicExistenceExpression.parseAtomicExistenceExpression(input, pc);
	}
}