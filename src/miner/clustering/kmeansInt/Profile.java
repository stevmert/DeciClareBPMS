package miner.clustering.kmeansInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import miner.Config;
import miner.log.DataEvent;
import miner.log.Trace;
import model.Activity;
import model.data.DataAttribute;

public class Profile {

	//TODO: enkel data int voor centroids?
	private final int[] activityProfile;
	private final int[] transitionProfile;
	private final int[] dataProfile;

	public Profile(int[] activityProfile, int[] transitionProfile, int[] dataProfile) {
		super();
		this.activityProfile = activityProfile;
		this.transitionProfile = transitionProfile;
		this.dataProfile = dataProfile;
	}

	public Profile(ArrayList<ClusterPoint> cps,
			Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		if(cps == null || cps.isEmpty())
			throw new IllegalArgumentException();
		else {
			int[] activityProfile = null;
			int[] transitionProfile = null;
			int[] dataProfile = null;
			for(ClusterPoint cp : cps) {
				Profile profile = cp.getProfile(activitiesInScope, dataAttributesInScope);
				if(Config.USE_ACTIVITYPROFILE) {
					if(activityProfile == null)
						activityProfile = new int[profile.getActivityProfileSize()];
					for(int i = 0; i < activityProfile.length; i++)
						activityProfile[i] += profile.getActivityProfile()[i];
				}
				if(Config.USE_TRANSITIONPROFILE) {
					if(transitionProfile == null)
						transitionProfile = new int[profile.getTransitionProfileSize()];
					for(int i = 0; i < transitionProfile.length; i++)
						transitionProfile[i] += profile.getTransitionProfile()[i];
				}
				if(Config.USE_DATAPROFILE) {
					if(dataProfile == null)
						dataProfile = new int[profile.getDataProfileSize()];
					for(int i = 0; i < dataProfile.length; i++)
						dataProfile[i] += profile.getDataProfile()[i];
				}
			}
			double threshold = ((double) cps.size())/2;
			if(Config.USE_ACTIVITYPROFILE)
				for(int i = 0; i < activityProfile.length; i++)
					activityProfile[i] = (activityProfile[i]>threshold)?1:0;
			if(Config.USE_TRANSITIONPROFILE)
				for(int i = 0; i < transitionProfile.length; i++)
					transitionProfile[i] = (transitionProfile[i]>threshold)?1:0;
			double threshold1 = -((double) cps.size())/3;
			double threshold2 = -threshold1;
			if(Config.USE_DATAPROFILE)
				for(int i = 0; i < dataProfile.length; i++)
					dataProfile[i] = (dataProfile[i]<=threshold1)?-1:((dataProfile[i]>=threshold2)?1:0);
			this.activityProfile = activityProfile;
			this.transitionProfile = transitionProfile;
			this.dataProfile = dataProfile;
		}
	}

	public int[] getActivityProfile() {
		return activityProfile;
	}

	public boolean hasActivityProfile() {
		return activityProfile != null;
	}

	public int getActivityProfileSize() {
		if(!hasActivityProfile())
			return 0;
		return activityProfile.length;
	}

	public int[] getTransitionProfile() {
		return transitionProfile;
	}

	public boolean hasTransitionProfile() {
		return transitionProfile != null;
	}

	public int getTransitionProfileSize() {
		if(!hasTransitionProfile())
			return 0;
		return transitionProfile.length;
	}

	public int[] getDataProfile() {
		return dataProfile;
	}

	public boolean hasDataProfile() {
		return dataProfile != null;
	}

	public int getDataProfileSize() {
		if(!hasDataProfile())
			return 0;
		return dataProfile.length;
	}

	//ClusterPoint
	public static Profile calculateProfile(Trace trace, Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		int[] activityProfile = null;
		if(Config.USE_ACTIVITYPROFILE)
			activityProfile = getActivityProfile(trace, activitiesInScope);
		int[] transitionProfile = null;
		if(Config.USE_TRANSITIONPROFILE)
			transitionProfile = getTransitionProfile(trace, activitiesInScope);
		int[] dataProfile = null;
		if(Config.USE_DATAPROFILE)
			dataProfile = getDataProfile(trace, dataAttributesInScope);
		return new Profile(activityProfile, transitionProfile, dataProfile);
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

	//Centroid
	public static Profile getRandomProfile(Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		Random r = new Random();
		int[] activityProfile = null;
		if(Config.USE_ACTIVITYPROFILE) {
			activityProfile = new int[activitiesInScope.length];
			for(int i = 0; i < activityProfile.length; i++)
				activityProfile[i] = r.nextInt(2);
		}
		int[] transitionProfile = null;
		if(Config.USE_TRANSITIONPROFILE) {
			transitionProfile = new int[activitiesInScope.length * activitiesInScope.length];
			for(int i = 0; i < transitionProfile.length; i++)
				transitionProfile[i] = r.nextInt(2);
		}
		int[] dataProfile = null;
		if(Config.USE_DATAPROFILE) {
			dataProfile = new int[dataAttributesInScope.length];
			for(int i = 0; i < dataProfile.length; i++)
				dataProfile[i] = r.nextInt(3) - 1;
		}
		return new Profile(activityProfile, transitionProfile, dataProfile);
	}

	//general
	public int getDimension() {
		return getActivityProfileSize()+getTransitionProfileSize()+getDataProfileSize();
	}

	public boolean isCompatible(Profile other) {
		if(other == null)
			return false;
		return (this.getActivityProfileSize() == other.getActivityProfileSize()
				&& this.getTransitionProfileSize() == other.getTransitionProfileSize()
				&& this.getDataProfileSize() == other.getDataProfileSize());
	}

	public static double getDistance(Profile profile1, Profile profile2) {
		if(!profile1.isCompatible(profile2))
			throw new IllegalArgumentException();
		return getEuclidianDistance(profile1, profile2)
				+ getHammingDistance(profile1, profile2)
				+ getJaccardDistance(profile1, profile2);
	}

	private static double getEuclidianDistance(Profile profile1, Profile profile2) {
		double distance = 0;
		double div = 0;
		if(Config.USE_ACTIVITYPROFILE) {
			distance += getEuclidianDistance(profile1.getActivityProfile(), profile2.getActivityProfile())
					* Config.ACTIVITYPROFILE_WEIGHT;
			div += Config.ACTIVITYPROFILE_WEIGHT;
		}
		if(Config.USE_TRANSITIONPROFILE) {
			distance += getEuclidianDistance(profile1.getTransitionProfile(), profile2.getTransitionProfile())
					* Config.TRANSITIONPROFILE_WEIGHT;
			div += Config.TRANSITIONPROFILE_WEIGHT;
		}
		if(Config.USE_DATAPROFILE) {
			distance += getEuclidianDistance(profile1.getDataProfile(), profile2.getDataProfile())
					* Config.DATAPROFILE_WEIGHT;
			div += Config.DATAPROFILE_WEIGHT;
		}
		return distance / div;
	}

	private static double getEuclidianDistance(int[] array1, int[] array2) {
		double distance = 0;
		for(int i = 0; i < array1.length; i++)
			distance += Math.pow(array1[i]-array2[i], 2);
		return Math.sqrt(distance);
	}

	private static double getHammingDistance(Profile profile1, Profile profile2) {
		double distance = 0;
		double div = 0;
		if(Config.USE_ACTIVITYPROFILE) {
			distance += getHammingDistance(profile1.getActivityProfile(), profile2.getActivityProfile())
					* Config.ACTIVITYPROFILE_WEIGHT;
			div += Config.ACTIVITYPROFILE_WEIGHT;
		}
		if(Config.USE_TRANSITIONPROFILE) {
			distance += getHammingDistance(profile1.getTransitionProfile(), profile2.getTransitionProfile())
					* Config.TRANSITIONPROFILE_WEIGHT;
			div += Config.TRANSITIONPROFILE_WEIGHT;
		}
		if(Config.USE_DATAPROFILE) {
			distance += getHammingDistance(profile1.getDataProfile(), profile2.getDataProfile())
					* Config.DATAPROFILE_WEIGHT;
			div += Config.DATAPROFILE_WEIGHT;
		}
		return distance / div;
	}

	private static double getHammingDistance(int[] array1, int[] array2) {
		double distance = 0;
		for(int i = 0; i < array1.length; i++)
			distance += (array1[i] == array2[i])?0:1;
		return distance;
	}

	private static double getJaccardDistance(Profile profile1, Profile profile2) {
		double distance = 0;
		double div = 0;
		if(Config.USE_ACTIVITYPROFILE) {
			distance += getJaccardDistance(profile1.getActivityProfile(), profile2.getActivityProfile())
					* Config.ACTIVITYPROFILE_WEIGHT;
			div += Config.ACTIVITYPROFILE_WEIGHT;
		}
		if(Config.USE_TRANSITIONPROFILE) {
			distance += getJaccardDistance(profile1.getTransitionProfile(), profile2.getTransitionProfile())
					* Config.TRANSITIONPROFILE_WEIGHT;
			div += Config.TRANSITIONPROFILE_WEIGHT;
		}
		if(Config.USE_DATAPROFILE) {
			distance += getJaccardDistance(profile1.getDataProfile(), profile2.getDataProfile())
					* Config.DATAPROFILE_WEIGHT;
			div += Config.DATAPROFILE_WEIGHT;
		}
		return distance / div;
	}

	private static double getJaccardDistance(int[] array1, int[] array2) {
		double product = 0;
		double sqr1 = 0;
		double sqr2 = 0;
		for(int i = 0; i < array1.length; i++) {
			product += array1[i]*array2[i];
			sqr1 += Math.pow(array1[i], 2);
			sqr2 += Math.pow(array2[i], 2);
		}
		return 1-(product/(sqr1+sqr2-product));
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
		Profile other = (Profile) obj;
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