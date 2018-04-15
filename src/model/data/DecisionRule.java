package model.data;

import java.io.Serializable;
import java.util.HashSet;

import model.Constraint;
import model.constraint.ParsingCache;

public class DecisionRule implements Serializable {

	private static final long serialVersionUID = 7585050727146099565L;

	private HashSet<DataAttribute> dataValues;

	public DecisionRule(HashSet<DataAttribute> dataValues) {
		super();
		this.dataValues = dataValues;
	}

	public HashSet<DataAttribute> getDataValues() {
		return dataValues;
	}

	public void setDataValues(HashSet<DataAttribute> dataValues) {
		this.dataValues = dataValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataValues == null) ? 0 : dataValues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DecisionRule other = (DecisionRule) obj;
		if (dataValues == null) {
			if (other.dataValues != null)
				return false;
		} else if (!dataValues.equals(other.dataValues))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return dataValues.toString();
	}

	public String getTextualVersion() {
		String res = null;
		for(DataAttribute de : getDataValues()) {
			if(res == null)
				res = "'" + de + "'";
			else
				res += " and '" + de + "'";
		}
		return "[" + res + "]";
	}

	public static DecisionRule parseDecisionRule(String input, ParsingCache pc) {
		HashSet<DataAttribute> dataValues = new HashSet<>();
		for(String x : Constraint.getSplit(input, null)) {
			DataAttribute da = pc.getDataAttribute_toString(x);
			if(da == null) {
				int i = x.indexOf(" = ");
				String name = x.substring(0, i);
				String value = x.substring(i + 3);
				if(value.equals("true") || value.equals("false"))
					da = new BooleanDataAttribute(name, Boolean.parseBoolean(value), null);
				else {
					DataAttribute da2 = pc.getDataAttribute_name(name);
					HashSet<String> values;
					if(da2 == null)
						values = new HashSet<>();
					else
						values = ((CategoricalDataAttribute) da2).getValues();
					values.add(value);
					da = new CategoricalDataAttribute(name, values, value, null);
				}
				pc.addDataAttribute(da);
			}
			dataValues.add(da);
		}
		return new DecisionRule(dataValues);
	}
}