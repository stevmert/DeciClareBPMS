package model.expression;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.Constraint;
import model.constraint.ParsingCache;
import model.resource.Resource;
import model.resource.ResourceRole;

public class NonAtomicResourceExpression extends NonAtomicExpression implements LogicalExpression, ResourceExpression, Serializable {

	private static final long serialVersionUID = -4300530372524118215L;

	public HashSet<ResourceExpression> expressions;

	public NonAtomicResourceExpression(LogicalOperator operator, ResourceExpression... expressions) {
		this(operator, new HashSet<>(Arrays.asList(expressions)));
	}

	public NonAtomicResourceExpression(LogicalOperator operator, HashSet<ResourceExpression> expressions) {
		super(operator);
		if(expressions == null
				|| expressions.size() < 2)
			throw new IllegalArgumentException();
		this.expressions = expressions;
	}

	public HashSet<ResourceExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(HashSet<ResourceExpression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public int getNrOfElements() {
		int total = 0;
		for(ResourceExpression re : expressions)
			total += re.getNrOfElements();
		return total;
	}

	@Override
	public HashSet<Resource> getUsedResources() {
		HashSet<Resource> res = new HashSet<>();
		for(ResourceExpression e : expressions)
			res.addAll(e.getUsedResources());
		return res;
	}

	@Override
	public boolean contains(Resource r, Set<Resource> resourcesCollection) {
		if(getOperator().equals(LogicalOperator.AND))
			throw new UnsupportedOperationException();
		else {
			for(ResourceExpression re : expressions)
				if(re.contains(r, resourcesCollection))
					return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expressions == null) ? 0 : expressions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonAtomicResourceExpression other = (NonAtomicResourceExpression) obj;
		if (expressions == null) {
			if (other.expressions != null)
				return false;
		} else if (!expressions.equals(other.expressions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String res = "";
		for(ResourceExpression re : expressions) {
			res += " " + getOperator() + " ";
			if(re instanceof AtomicResourceExpression)
				res += re;
			else
				res += "(" + re + ")";
		}
		return res.substring((" " + getOperator() + " ").length());
	}

	public static NonAtomicResourceExpression parseNonAtomicResourceExpression(String input, ParsingCache pc) {
		String text = input.trim();
		HashSet<ResourceExpression> expressions = new HashSet<>();
		LogicalOperator o = null;
		while(text != null) {
			int or = text.indexOf(" " + LogicalOperator.OR + " ");
			int and = text.indexOf(" " + LogicalOperator.AND + " ");
			int bracket = text.indexOf("(");
			if(bracket == -1 && or == -1 && and == -1) {
				expressions.add(new AtomicResourceExpression(ResourceRole.parseResource(text, pc)));
				text = null;
			} else if(bracket == -1 || (or != -1 && or < bracket) || (and != -1 && and < bracket)) {
				int x = (or==-1?and:(and==-1?or:Math.min(and, or)));
				String name = text.substring(0, x);
				expressions.add(new AtomicResourceExpression(ResourceRole.parseResource(name, pc)));
				if(o == null) {
					if(x == or)
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				text = text.substring((name + " " + o + " ").length());
			} else {
				int x = Constraint.getBracketsEnd(text, "(", ")");
				expressions.add(parseNonAtomicResourceExpression(text.substring(bracket+1, x), pc));
				text = text.substring(x+1).trim();
				if(o == null) {
					if(text.startsWith(LogicalOperator.OR + " "))
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				if(text.trim().length() == 0)
					text = null;
				else
					text = text.substring((o + " ").length());
			}
		}
		return new NonAtomicResourceExpression(o, expressions);
	}
}