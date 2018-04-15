package model.expression;

import java.io.Serializable;

import model.constraint.ParsingCache;
import model.data.DataAttribute;
import model.data.DataRecord;
import model.data.DataStructure;

public class AtomicDataExpression implements LogicalExpression, DataExpression, Serializable {

	private static final long serialVersionUID = -7474607760423596830L;

	private DataStructure data;

	public AtomicDataExpression(DataStructure data) {
		super();
		this.data = data;
	}

	public DataStructure getDataStructure() {
		return data;
	}

	public void setDataStructure(DataStructure data) {
		this.data = data;
	}

	@Override
	public int getNrOfElements() {
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		AtomicDataExpression other = (AtomicDataExpression) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(data instanceof DataAttribute)
			return ((DataAttribute) data).getName();
		return data.toString();
	}

	public static DataExpression parseAtomicDataExpression(String input, ParsingCache pc) {
		DataStructure d = pc.getDataStructure(input);
		if(d == null) {
			d = new DataRecord(input);//TODO: incorrect...
			pc.addDataStructure(d);
		}
		return new AtomicDataExpression(d);
	}
}