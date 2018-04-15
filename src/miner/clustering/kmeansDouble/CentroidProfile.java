package miner.clustering.kmeansDouble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import miner.Config;
import model.Activity;
import model.data.DataAttribute;

public class CentroidProfile extends Profile {

	private final double[] activityProfile;
	private final double[] transitionProfile;
	private final double[] dataProfile;

	public CentroidProfile(double[] activityProfile, double[] transitionProfile, double[] dataProfile) {
		super();
		this.activityProfile = activityProfile;
		this.transitionProfile = transitionProfile;
		this.dataProfile = dataProfile;
	}

	public CentroidProfile(ArrayList<ClusterPoint> cps,
			Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		if(cps == null || cps.isEmpty())
			throw new IllegalArgumentException();
		else {
			double[] activityProfile = null;
			double[] transitionProfile = null;
			double[] dataProfile = null;
			for(ClusterPoint cp : cps) {
				ClusterPointProfile profile = cp.getProfile(activitiesInScope, dataAttributesInScope);
				if(Config.USE_ACTIVITYPROFILE) {
					if(activityProfile == null)
						activityProfile = new double[profile.getActivityProfileSize()];
					for(int i = 0; i < activityProfile.length; i++)
						activityProfile[i] += profile.getActivityProfile_int()[i];
				}
				if(Config.USE_TRANSITIONPROFILE) {
					if(transitionProfile == null)
						transitionProfile = new double[profile.getTransitionProfileSize()];
					for(int i = 0; i < transitionProfile.length; i++)
						transitionProfile[i] += profile.getTransitionProfile_int()[i];
				}
				if(Config.USE_DATAPROFILE) {
					if(dataProfile == null)
						dataProfile = new double[profile.getDataProfileSize()];
					for(int i = 0; i < dataProfile.length; i++)
						dataProfile[i] += profile.getDataProfile_int()[i];
				}
			}
			if(Config.USE_ACTIVITYPROFILE)
				for(int i = 0; i < activityProfile.length; i++)
					activityProfile[i] = activityProfile[i]/cps.size();
			if(Config.USE_TRANSITIONPROFILE)
				for(int i = 0; i < transitionProfile.length; i++)
					transitionProfile[i] = transitionProfile[i]/cps.size();
			if(Config.USE_DATAPROFILE)
				for(int i = 0; i < dataProfile.length; i++)
					dataProfile[i] = dataProfile[i]/cps.size();
			this.activityProfile = activityProfile;
			this.transitionProfile = transitionProfile;
			this.dataProfile = dataProfile;
		}
	}

	@Override
	public double[] getActivityProfile() {
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

	public static CentroidProfile getRandomProfile(Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		Random r = new Random();
		double[] activityProfile = null;
		if(Config.USE_ACTIVITYPROFILE) {
			activityProfile = new double[activitiesInScope.length];
			for(int i = 0; i < activityProfile.length; i++)
				activityProfile[i] = r.nextDouble();
		}
		double[] transitionProfile = null;
		if(Config.USE_TRANSITIONPROFILE) {
			transitionProfile = new double[activitiesInScope.length * activitiesInScope.length];
			for(int i = 0; i < transitionProfile.length; i++)
				transitionProfile[i] = r.nextDouble();
		}
		double[] dataProfile = null;
		if(Config.USE_DATAPROFILE) {
			dataProfile = new double[dataAttributesInScope.length];
			for(int i = 0; i < dataProfile.length; i++)
				dataProfile[i] = r.nextDouble()*2 - 1;
		}
		return new CentroidProfile(activityProfile, transitionProfile, dataProfile);
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
		CentroidProfile other = (CentroidProfile) obj;
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