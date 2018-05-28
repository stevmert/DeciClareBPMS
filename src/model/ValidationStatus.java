package model;

public enum ValidationStatus {
	SATISFIED, TIME_SATISFIABLE, ACTIVITY_SATISFIABLE, ACTIVITY_SATISFIABLE_WITH_DEADLINE,
	VIOLATED, DEADEND, RESOURCE_SATISFIABLE;

	private long bound = -1;

	public long getBound() {
		if(this == TIME_SATISFIABLE || this == ACTIVITY_SATISFIABLE_WITH_DEADLINE)
			return bound;
		throw new UnsupportedOperationException();
	}

	public void setBound(long bound) {
		if(this == TIME_SATISFIABLE || this == ACTIVITY_SATISFIABLE_WITH_DEADLINE)
			this.bound = bound;
		else
			throw new UnsupportedOperationException();
	}
}