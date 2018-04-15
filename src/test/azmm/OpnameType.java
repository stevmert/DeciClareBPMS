package test.azmm;

public enum OpnameType {
	GEHOSPITALISEERD(1), AMBULANT(2), DAGZIEKENHUIS(3);

	private int internalNr;

	private OpnameType(int internalNr) {
		this.internalNr = internalNr;
	}

	public int getInternalNr() {
		return internalNr;
	}

	public static OpnameType valueOf(int internalNr) {
		for(OpnameType t : values())
			if(t.getInternalNr() == internalNr)
				return t;
		throw new IllegalArgumentException(""+internalNr);
	}
}