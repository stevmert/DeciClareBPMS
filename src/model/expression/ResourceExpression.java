package model.expression;

import java.util.HashSet;
import java.util.Set;

import model.constraint.ParsingCache;
import model.resource.Resource;

public interface ResourceExpression {

	public int getNrOfElements();

	public static ResourceExpression parseResourceExpression(String input, ParsingCache pc) {
		if(input.contains(" " + LogicalOperator.AND + " ") || input.contains(" " + LogicalOperator.OR + " "))
			return NonAtomicResourceExpression.parseNonAtomicResourceExpression(input, pc);
		return AtomicResourceExpression.parseAtomicResourceExpression(input, pc);
	}

	public HashSet<Resource> getUsedResources();

	public boolean contains(Resource activeResource, Set<Resource> resourcesCollection);
}