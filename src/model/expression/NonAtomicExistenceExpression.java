package model.expression;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import model.Activity;
import model.Constraint;
import model.constraint.ExistenceConstraint;
import model.constraint.ParsingCache;

public class NonAtomicExistenceExpression extends NonAtomicExpression implements LogicalExpression, ExistenceExpression, Serializable {

	private static final long serialVersionUID = -4719533727029500747L;

	private HashSet<ExistenceExpression> expressions;

	public NonAtomicExistenceExpression(LogicalOperator operator, ExistenceExpression... expressions) {
		this(operator, new HashSet<>(Arrays.asList(expressions)));
	}

	public NonAtomicExistenceExpression(LogicalOperator operator, HashSet<ExistenceExpression> expressions) {
		super(operator);
		if(expressions == null
				|| expressions.size() < 2)
			throw new IllegalArgumentException();
		this.expressions = expressions;
	}

	public HashSet<ExistenceExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(HashSet<ExistenceExpression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public int getNrOfElements() {
		int total = 0;
		for(ExistenceExpression e : expressions)
			total += e.getNrOfElements();
		return total;
	}

	@Override
	public ExistenceExpression getNegation() {
		LogicalOperator op;
		if(getOperator().equals(LogicalOperator.AND))
			op = LogicalOperator.OR;
		else
			op = LogicalOperator.AND;
		HashSet<ExistenceExpression> newExpressions = new HashSet<>();
		for(ExistenceExpression e : getExpressions())
			newExpressions.add(e.getNegation());
		return new NonAtomicExistenceExpression(op, newExpressions);
	}

	@Override
	public boolean hasAtMost() {
		for(ExistenceExpression x : getExpressions())
			if(x.hasAtMost())
				return true;
		return false;
	}

	@Override
	public boolean hasAtLeast() {
		for(ExistenceExpression x : getExpressions())
			if(x.hasAtLeast())
				return true;
		return false;
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		HashSet<Activity> res = new HashSet<>();
		for(ExistenceExpression e : expressions)
			res.addAll(e.getUsedActivities());
		return res;
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
		NonAtomicExistenceExpression other = (NonAtomicExistenceExpression) obj;
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
		for(ExistenceExpression e : expressions) {
			res += " " + getOperator() + " ";
			if(e instanceof AtomicExistenceExpression)
				res += e;
			else
				res += "(" + e + ")";
		}
		return res.substring((" " + getOperator() + " ").length());
	}

	@Override
	public String toTextualString() {
		String res = "";
		for(ExistenceExpression e : expressions) {
			res += " " + getOperator() + " ";
			if(e instanceof AtomicExistenceExpression)
				res += e.toTextualString();
			else
				res += "(" + e.toTextualString() + ")";
		}
		res = res.substring((" " + getOperator() + " ").length());
		if(!res.startsWith("(") || !res.endsWith(")"))
			res = "(" + res + ")";
		return res;
	}

	public static ExistenceExpression parseNonAtomicExistenceExpression(String input, ParsingCache pc) {
		String text = input.trim();
		HashSet<ExistenceExpression> expressions = new HashSet<>();
		LogicalOperator o = null;
		while(text != null) {
			int or = text.indexOf(" " + LogicalOperator.OR + " ");
			int and = text.indexOf(" " + LogicalOperator.AND + " ");
			int bracket = text.indexOf("(");
			if(bracket == -1 && or == -1 && and == -1) {
				expressions.add(new AtomicExistenceExpression((ExistenceConstraint) Constraint.parseConstraint(text, pc)));
				text = null;
			} else if(bracket == -1 || (or != -1 && or < bracket) || (and != -1 && and < bracket)) {
				int x = (or==-1?and:(and==-1?or:Math.min(and, or)));
				String name = text.substring(0, x);
				expressions.add(new AtomicExistenceExpression((ExistenceConstraint) Constraint.parseConstraint(name, pc)));
				if(o == null) {
					if(x == or)
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				text = text.substring((name + " " + o + " ").length());
			} else if(bracket != 0) {
				int end = Constraint.getBracketsEnd(text.substring(bracket), "(", ")");
				String part = text.substring(0, bracket+end+1);
				text = text.substring(bracket+end+1).trim();
				if(text.length() == 0 && expressions.isEmpty())//is atomic!
					return AtomicExistenceExpression.parseAtomicExistenceExpression(part, pc);
				expressions.add(ExistenceExpression.parseExistenceExpression(part, pc));
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
			} else {
				int x = Constraint.getBracketsEnd(text, "(", ")");
				expressions.add(parseNonAtomicExistenceExpression(text.substring(bracket+1, x), pc));
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
		return new NonAtomicExistenceExpression(o, expressions);
	}
}