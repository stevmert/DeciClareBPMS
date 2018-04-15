package model.expression;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import model.Constraint;
import model.constraint.ParsingCache;
import model.data.DataRecord;
import model.data.DataStructure;

public class NonAtomicDataExpression extends NonAtomicExpression implements LogicalExpression, DataExpression, Serializable {

	private static final long serialVersionUID = 8102081596777488317L;

	public HashSet<DataExpression> expressions;

	public NonAtomicDataExpression(LogicalOperator operator, DataExpression... expressions) {
		this(operator, new HashSet<>(Arrays.asList(expressions)));
	}

	public NonAtomicDataExpression(LogicalOperator operator, HashSet<DataExpression> expressions) {
		super(operator);
		if(expressions == null
				|| expressions.size() < 2)
			throw new IllegalArgumentException();
		this.expressions = expressions;
	}

	public HashSet<DataExpression> getExpressions() {
		return expressions;
	}

	public void setExpressions(HashSet<DataExpression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public int getNrOfElements() {
		int total = 0;
		for(DataExpression ae : expressions)
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
		NonAtomicDataExpression other = (NonAtomicDataExpression) obj;
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
		for(DataExpression ae : expressions) {
			res += " " + getOperator() + " ";
			if(ae instanceof AtomicDataExpression)
				res += ae;
			else
				res += "(" + ae + ")";
		}
		return res.substring((" " + getOperator() + " ").length());
	}

	public static NonAtomicDataExpression parseNonAtomicDataExpression(String input, ParsingCache pc) {
		String text = input.trim();
		HashSet<DataExpression> expressions = new HashSet<>();
		LogicalOperator o = null;
		while(text != null) {
			int or = text.indexOf(" " + LogicalOperator.OR + " ");
			int and = text.indexOf(" " + LogicalOperator.AND + " ");
			int bracket = text.indexOf("(");
			if(bracket == -1 && or == -1 && and == -1) {
				DataStructure d = pc.getDataStructure(text);
				if(d == null) {
					d = new DataRecord(text);//TODO: incorrect...
					pc.addDataStructure(d);
				}
				expressions.add(new AtomicDataExpression(d));
				text = null;
			} else if(bracket == -1 || (or != -1 && or < bracket) || (and != -1 && and < bracket)) {
				int x = (or==-1?and:(and==-1?or:Math.min(and, or)));
				String name = text.substring(0, x);
				DataStructure d = pc.getDataStructure(name);
				if(d == null) {
					d = new DataRecord(name);//TODO: incorrect...
					pc.addDataStructure(d);
				}
				expressions.add(new AtomicDataExpression(d));
				if(o == null) {
					if(x == or)
						o = LogicalOperator.OR;
					else
						o = LogicalOperator.AND;
				}
				text = text.substring((name + " " + o + " ").length());
			} else {
				int x = Constraint.getBracketsEnd(text, "(", ")");
				expressions.add(parseNonAtomicDataExpression(text.substring(bracket+1, x), pc));
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
		return new NonAtomicDataExpression(o, expressions);
	}
}