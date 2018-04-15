package miner.clustering.kmeansDouble;

import java.util.ArrayList;

import model.Activity;
import model.data.DataAttribute;

public class Cluster {
	public final String id;
	public ArrayList<ClusterPoint> clusterPoints;
	public final Activity[] activitiesInScope;
	public final DataAttribute[] dataAttributesInScope;
	public CentroidProfile centroid;
	public Cluster[] subclusters;

	public Cluster(String id, Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		super();
		this.id = id;
		this.clusterPoints = new ArrayList<>();
		this.activitiesInScope = activitiesInScope;
		this.dataAttributesInScope = dataAttributesInScope;
		calculateCentroid();
	}

	public String getId() {
		return id;
	}

	public Activity[] getActivitiesInScope() {
		return activitiesInScope;
	}

	public DataAttribute[] getDataAttributesInScope() {
		return dataAttributesInScope;
	}

	public CentroidProfile getCentroid() {
		return centroid;
	}

	public ArrayList<ClusterPoint> getClusterPoints() {
		return clusterPoints;
	}

	public void setClusterPoints(ArrayList<ClusterPoint> clusterPoints) {
		this.clusterPoints = clusterPoints;
	}

	public Cluster[] getSubclusters() {
		return subclusters;
	}

	public void setSubclusters(Cluster[] subclusters) {
		this.subclusters = subclusters;
	}

	public boolean hasSubClusters() {
		return subclusters != null && subclusters.length > 0;
	}

	public int getNumberOfSubclusters() {
		if(!hasSubClusters())
			return 0;
		int nrOfSubclusters = subclusters.length;
		for(Cluster c : subclusters)
			nrOfSubclusters += c.getNumberOfSubclusters();
		return nrOfSubclusters;
	}

	public ArrayList<Cluster> getLeafCluster() {
		ArrayList<Cluster> leafClusters = new ArrayList<>();
		if(getSubclusters() != null)
			for(Cluster c : getSubclusters()) {
				if(!c.hasSubClusters())
					leafClusters.add(c);
				else
					leafClusters.addAll(c.getLeafCluster());
			}
		return leafClusters;
	}

	public int getNumberOfLeafSubclusters() {
		if(!hasSubClusters())
			return 0;
		int nrOfLeafSubclusters = 0;
		for(Cluster c : subclusters) {
			if(c.hasSubClusters())
				nrOfLeafSubclusters += c.getNumberOfLeafSubclusters();
			else
				nrOfLeafSubclusters++;
		}
		return nrOfLeafSubclusters;
	}

	public void calculateCentroid() {
		centroid = null;
		if(getClusterPoints().isEmpty())
			centroid = CentroidProfile.getRandomProfile(activitiesInScope, dataAttributesInScope);
		else
			centroid = new CentroidProfile(getClusterPoints(), activitiesInScope, dataAttributesInScope);
	}

	public void clear() {
		this.clusterPoints.clear();
		this.subclusters = null;
	}

	/**
	 * pre: Centroid should already be calculated
	 */
	public double getInternalVariance() {
		if(getClusterPoints().isEmpty())
			return 0;
		double variance = 0;
		for(ClusterPoint cp : getClusterPoints()) {
			variance += Math.pow(Profile.getDistance(
					cp.getProfile(activitiesInScope, dataAttributesInScope),
					getCentroid()), 2);
		}
		return variance/getClusterPoints().size();
	}

	@Override
	public String toString() {
		return "Cluster" + id + ": clusterpoints=" + (getClusterPoints()==null?0:getClusterPoints().size())
				+ ", internalDiff=" + getInternalVariance()
				+ ", center=" + getCentroid() + "]";
	}
}