package miner.clustering.kmeansDouble;

import miner.Config;

public abstract class Profile {

	public abstract double[] getActivityProfile();

	public abstract boolean hasActivityProfile();

	public abstract int getActivityProfileSize();

	public abstract double[] getTransitionProfile();

	public abstract boolean hasTransitionProfile();

	public abstract int getTransitionProfileSize();

	public abstract double[] getDataProfile();

	public abstract boolean hasDataProfile();

	public abstract int getDataProfileSize();

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

	private static double getEuclidianDistance(double[] array1, double[] array2) {
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

	private static double getHammingDistance(double[] array1, double[] array2) {
		double distance = 0;
		for(int i = 0; i < array1.length; i++)
			distance += (Math.round(array1[i]) == Math.round(array2[i]))?0:1;
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

	private static double getJaccardDistance(double[] array1, double[] array2) {
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
}