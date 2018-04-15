package model.expression;

import model.constraint.ParsingCache;

public interface ResourceExpression {

	public int getNrOfElements();

	public static ResourceExpression parseResourceExpression(String input, ParsingCache pc) {
		if(input.contains(" " + LogicalOperator.AND + " ") || input.contains(" " + LogicalOperator.OR + " "))
			return NonAtomicResourceExpression.parseNonAtomicResourceExpression(input, pc);
		return AtomicResourceExpression.parseAtomicResourceExpression(input, pc);
	}
}