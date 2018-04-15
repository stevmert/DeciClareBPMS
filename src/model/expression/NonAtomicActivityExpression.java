package model.expression;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import model.Activity;
import model.Constraint;
import model.constraint.ParsingCache;

public class NonAtomicActivityExpression extends NonAtomicExpression implements LogicalExpression, ActivityExpression, Serializable {

	private static final long serialVersionUID = 360797720045791851L;

	public HashSet<ActivityExpression> expressions;

	public NonAtomicActivityExpression(LogicalOperator operator, ActivityExpression... expressions) {
		this(operator, new HashSet<>(Arrays.asList(expressions)));
	}

	public NonAtomicActivityExpression(LogicalOperator operator, Activity... activities) {
		this(operator, convertToExpressions(activities));
	}

	public NonAtomicActivityExpression(LogicalOperator operator, HashSet<ActivityExpression> expressions) {
		super(operator);
		if(expressions == null
				|| expressions.size() < 2)
			throw new IllegalArgumentException();
		this.expressions = expressions;
	}

	private static HashSet<ActivityExpression> convertToExpressions(Activity... activities) {
		HashSet<ActivityExpression> tmp = new HashSet<>();
		for(Activity a : activities)
			tmp.add(new AtomicActivityExpression(a));
		return tmp;
	}

	public HashSet<ActivityExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(HashSet<ActivityExpression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public int getNrOfElements() {
		int total = 0;
		for(ActivityExpression ae : expressions)
			total += ae.getNrOfElements();
		return total;
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
		NonAtomicActivityExpression other = (NonAtomicActivityExpression) obj;
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
		for(ActivityExpression ae : expressions) {
			res += " " + getOperator() + " ";
			if(ae instanceof AtomicActivityExpression)
				res += ae;
			else
				res += "(" + ae + ")";
		}
		return res.substring((" " + getOperator() + " ").length());
	}

	@Override
	public HashSet<Activity> getUsedActivities() {
		HashSet<Activity> res = new HashSet<>();
		for(ActivityExpression ae : expressions)
			res.addAll(ae.getUsedActivities());
		return res;
	}

	public static NonAtomicActivityExpression parseNonAtomicActivityExpression(String input, ParsingCache pc) {
		String text = input.trim();
		String tmp = "";
		HashSet<ActivityExpression> expressions = new HashSet<>();
		LogicalOperator o = null;
		while(text != null) {
			int or = text.indexOf(" " + LogicalOperator.OR + " ");
			int and = text.indexOf(" " + LogicalOperator.AND + " ");
			int bracket = text.indexOf("(");
			if(bracket == -1 && or == -1 && and == -1) {
				Activity a = pc.getActivity(tmp + text);
				if(a == null) {
					a = new Activity(tmp + text);
					pc.addActivity(a);
				}
				tmp = "";
				expressions.add(new AtomicActivityExpression(a));
				text = null;
			} else if(bracket == -1 || (or != -1 && or < bracket) || (and != -1 && and < bracket)) {
				int x = (or==-1?and:(and==-1?or:Math.min(and, or)));
				String name = text.substring(0, x);
				Activity a = pc.getActivity(tmp + name);
				if(a == null) {
					a = new Activity(tmp + name);
					pc.addActivity(a);
				}
				tmp = "";
				expressions.add(new AtomicActivityExpression(a));
				if(o == null) {
					if(x == or)
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				text = text.substring((name + " " + o + " ").length());
			} else if(bracket != 0) {
				int x = Constraint.getBracketsEnd(text.substring(bracket), "(", ")");
				tmp = text.substring(0, bracket+x+1);
				text = text.substring(bracket+x+1);
			} else {
				int x = Constraint.getBracketsEnd(text, "(", ")");
				expressions.add(parseNonAtomicActivityExpression(tmp + text.substring(bracket+1, x), pc));
				tmp = "";
				text = text.substring(x+1).trim();
				if(o == null) {
					if(text.startsWith(LogicalOperator.OR + " "))
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				if(text.length() == 0)
					text = null;
				else
					text = text.substring((o + " ").length());
			}
		}
		return new NonAtomicActivityExpression(o, expressions);
	}
}