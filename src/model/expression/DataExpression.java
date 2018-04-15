package model.expression;

import model.constraint.ParsingCache;

public interface DataExpression {

	public int getNrOfElements();

	public static DataExpression parseDataExpression(String input, ParsingCache pc) {
		if(input.contains(" " + LogicalOperator.AND + " ") || input.contains(" " + LogicalOperator.OR + " "))
			return NonAtomicDataExpression.parseNonAtomicDataExpression(input, pc);
		return AtomicDataExpression.parseAtomicDataExpression(input, pc);
	}
}