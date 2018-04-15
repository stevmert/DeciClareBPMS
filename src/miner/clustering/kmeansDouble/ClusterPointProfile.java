package miner.clustering.kmeansDouble;

import java.util.Arrays;

import miner.Config;
import miner.log.DataEvent;
import miner.log.Trace;
import model.Activity;
import model.data.DataAttribute;

public class ClusterPointProfile extends Profile {

	private final int[] activityProfile;
	private final int[] transitionProfile;
	private final int[] dataProfile;

	public ClusterPointProfile(int[] activityProfile, int[] transitionProfile, int[] dataProfile) {
		super();
		this.activityProfile = activityProfile;
		this.transitionProfile = transitionProfile;
		this.dataProfile = dataProfile;
	}

	@Override
	public double[] getActivityProfile() {
		return getDoubleArray(activityProfile);
	}

	public int[] getActivityProfile_int() {
		return activityProfile;
	}

	@Override
	public boolean hasActivityProfile() {
		return activityProfile != null;
	}

	@Override
	public int getActivityProfileSize() {
		if(!hasActivityProfile())
			return 0;
		return activityProfile.length;
	}

	@Override
	public double[] getTransitionProfile() {
		return getDoubleArray(transitionProfile);
	}

	public int[] getTransitionProfile_int() {
		return transitionProfile;
	}

	@Override
	public boolean hasTransitionProfile() {
		return transitionProfile != null;
	}

	@Override
	public int getTransitionProfileSize() {
		if(!hasTransitionProfile())
			return 0;
		return transitionProfile.length;
	}

	@Override
	public double[] getDataProfile() {
		return getDoubleArray(dataProfile);
	}

	public int[] getDataProfile_int() {
		return dataProfile;
	}

	@Override
	public boolean hasDataProfile() {
		return dataProfile != null;
	}

	@Override
	public int getDataProfileSize() {
		if(!hasDataProfile())
			return 0;
		return dataProfile.length;
	}

	private static double[] getDoubleArray(int[] array) {
		if(array == null)
			return null;
		double[] res = new double[array.length];
		for(int i = 0; i < array.length; i++)
			res[i] = array[i];
		return res;
	}

	public static ClusterPointProfile calculateProfile(Trace trace, Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		int[] activityProfile = null;
		if(Config.USE_ACTIVITYPROFILE)
			activityProfile = getActivityProfile(trace, activitiesInScope);
		int[] transitionProfile = null;
		if(Config.USE_TRANSITIONPROFILE)
			transitionProfile = getTransitionProfile(trace, activitiesInScope);
		int[] dataProfile = null;
		if(Config.USE_DATAPROFILE)
			dataProfile = getDataProfile(trace, dataAttributesInScope);
		return new ClusterPointProfile(activityProfile, transitionProfile, dataProfile);
	}

	private static int[] getActivityProfile(Trace trace, Activity[] activitiesInScope) {
		int[] profile = new int[activitiesInScope.length];
		for(int i = 0; i < activitiesInScope.length; i++)
			profile[i] = getActivityProfileItem(trace, activitiesInScope[i]);
		return profile;
	}

	private static int[] getTransitionProfile(Trace trace, Activity[] activitiesInScope) {
		int[] profile = new int[activitiesInScope.length*activitiesInScope.length];
		int index = 0;
		for(int i1 = 0; i1 < activitiesInScope.length; i1++) {
			for(int i2 = 0; i2 < activitiesInScope.length; i2++) {
				profile[index] = getTransitionProfileItem(trace, activitiesInScope[i1], activitiesInScope[i2]);
				index++;
			}
		}
		return profile;
	}

	private static int[] getDataProfile(Trace trace, DataAttribute[] dataAttributesInScope) {
		int[] profile = new int[dataAttributesInScope.length];
		for(int i = 0; i < dataAttributesInScope.length; i++)
			profile[i] = getDataProfileItem(trace, dataAttributesInScope[i]);
		return profile;
	}

	private static int getActivityProfileItem(Trace trace, Activity activity) {
		for(Activity a : trace.getActivityEvents())
			if(activity.equals(a))
				return 1;
		return 0;
	}

	private static int getDataProfileItem(Trace trace, DataAttribute data) {
		for(DataEvent d : trace.getDataEvents())
			if(data.equals(d.getDataElement())) {
				if(d.isActivated())
					return 1;
				else
					return -1;
			}
		return 0;
	}

	private static int getTransitionProfileItem(Trace trace, Activity from, Activity to) {
		for(int i = 1; i < trace.getActivityEvents().size(); i++)
			if(trace.getActivityEvents().get(i).equals(to)
					&& trace.getActivityEvents().get(i-1).equals(from))
				return 1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(activityProfile);
		result = prime * result + Arrays.hashCode(dataProfile);
		result = prime * result + Arrays.hashCode(transitionProfile);
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
		ClusterPointProfile other = (ClusterPointProfile) obj;
		if (!Arrays.equals(activityProfile, other.activityProfile))
			return false;
		if (!Arrays.equals(dataProfile, other.dataProfile))
			return false;
		if (!Arrays.equals(transitionProfile, other.transitionProfile))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "<activityProfile=" + Arrays.toString(activityProfile) + ", transitionProfile="
				+ Arrays.toString(transitionProfile) + ", dataProfile=" + Arrays.toString(dataProfile) + ">";
	}
}