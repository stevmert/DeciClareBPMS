package miner.clustering.kmeansInt;

import miner.log.Trace;
import model.Activity;
import model.data.DataAttribute;

public class ClusterPoint {

	private Trace trace;
	private Profile profile;

	public ClusterPoint(Trace trace) {
		super();
		this.trace = trace;
		this.profile = null;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public Profile getProfile(Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		if(profile == null)
			profile = Profile.calculateProfile(trace, activitiesInScope, dataAttributesInScope);
		return profile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trace == null) ? 0 : trace.hashCode());
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
		ClusterPoint other = (ClusterPoint) obj;
		if (trace == null) {
			if (other.trace != null)
				return false;
		} else if (!trace.equals(other.trace))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return trace.toString();
	}
}