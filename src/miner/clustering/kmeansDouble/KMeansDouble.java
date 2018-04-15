package miner.clustering.kmeansDouble;

import java.io.File;
import java.util.ArrayList;

import miner.Config;
import miner.kb.KnowledgeBase;
import miner.log.Log;
import miner.log.Trace;
import model.Activity;
import model.data.DataAttribute;
import util.xes.XESReader;

public class KMeansDouble extends KMeansDoubleImpl {

	private static final int NUM_CLUSTERS = 10;
	private static final long MAX_SEARCHTIME = 30*1000;//30secs
	private static final int MAX_SEARCH_ITERATIONS = 500;
	public static void main(String[] args) throws Exception {
		//		C:\Users\Steven\Dropbox\workspace\DeciClareMinerV11\models\test_armFractures(5000)v2.xes
		System.out.print("Loading log...");
		Log log = XESReader.getLog(new File(new File("models"), "test_armFractures(5000)v2.xes"));
		System.out.println("Done");
		System.out.print("Setting up KnowledgeBase...");
		KnowledgeBase kb = new KnowledgeBase(log);
		System.out.println("Done");
		Activity[] activitiesInScope = kb.getActivities().toArray(new Activity[kb.getActivities().size()]);
		DataAttribute[] dataAttributesInScope = kb.getDataElementsWithAllUsedValues().toArray(
				new DataAttribute[kb.getDataElementsWithAllUsedValues().size()]);
		ArrayList<ClusterPoint> cps = new ArrayList<>();
		for(Trace t : log)
			cps.add(new ClusterPoint(t));

		KMeansDouble k = new KMeansDouble(true);
		k.run(cps, NUM_CLUSTERS, MAX_SEARCHTIME, MAX_SEARCH_ITERATIONS,
				activitiesInScope, dataAttributesInScope, "", Config.CLUSTER_RETRIES);
	}

	public KMeansDouble(boolean showMessages) {
		super(showMessages);
	}

	public Cluster[] run(ArrayList<ClusterPoint> cps, int numberOfClusters,
			long maxSearchTime, int maxSearchIterations,
			Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope,
			String idPrefix, int nrOfRetriesLeft) {
		Cluster[] clusters = new Cluster[numberOfClusters];
		for (int i = 0; i < numberOfClusters; i++) {
			Cluster cluster = new Cluster(idPrefix+(i+1), activitiesInScope, dataAttributesInScope);
			clusters[i] = cluster;
		}
		return run(cps, clusters, maxSearchTime, maxSearchIterations,
				activitiesInScope, dataAttributesInScope, idPrefix, nrOfRetriesLeft);
	}

	public Cluster[] run(ArrayList<ClusterPoint> cps, Cluster[] clusters,
			long maxSearchTime, int maxSearchIterations,
			Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope,
			String idPrefix, int nrOfRetriesLeft) {
		boolean finish = false;
		int iteration = 0;
		long startTime = System.currentTimeMillis();
		ArrayList<Double> pastDistances = new ArrayList<>(Config.CLUSTER_STOP-1);
		while(!finish) {
			//Clear cluster state
			clearClusters(clusters);

			CentroidProfile[] lastCentroids = getCentroids(clusters);

			//Assign points to the closer cluster
			assignCluster(clusters, cps, activitiesInScope, dataAttributesInScope);

			//Calculate new centroids.
			calculateCentroids(clusters);

			iteration++;

			CentroidProfile[] currentCentroids = getCentroids(clusters);

			//Calculates total distance between new and old centroids
			double distance = 0;
			for(int i = 0; i < lastCentroids.length; i++)
				distance += Profile.getDistance(lastCentroids[i], currentCentroids[i]);
			if(isShowMessages()) {
				System.out.println("#################");
				System.out.println("Iteration: " + iteration);
				System.out.println("Centroid distances: " + distance);
			}

			if(distance == 0)
				finish = true;
			else if(iteration >= maxSearchIterations
					|| System.currentTimeMillis() >= startTime+maxSearchTime) {
				if(isShowMessages())
					System.out.println("!!!ABORTED CLUSTERING!!! (Limit reached)");
				if(nrOfRetriesLeft > 0)//ipv te stoppen, nog eens proberen
					return this.run(cps, clusters.length, maxSearchTime, maxSearchIterations, activitiesInScope,
							dataAttributesInScope, idPrefix, nrOfRetriesLeft--);
				//				if(clusters.length-1 > 1)//ipv te stoppen, nog eens proberen met minder clusters
				//					return this.run(cps, clusters.length-1, maxSearchTime, maxSearchIterations, activitiesInScope,
				//							dataAttributesInScope, idPrefix);
				return null;
			} else {
				if(pastDistances.size() >= Config.CLUSTER_STOP-1
						&& pastDistances.get(0) <= distance) {
					if(isShowMessages())
						System.out.println("!!!ABORTED CLUSTERING!!! (No convergence)");
					if(nrOfRetriesLeft > 0)//ipv te stoppen, nog eens proberen
						return this.run(cps, clusters.length, maxSearchTime, maxSearchIterations, activitiesInScope,
								dataAttributesInScope, idPrefix, nrOfRetriesLeft--);
					//					if(clusters.length-1 > 1)//ipv te stoppen, nog eens proberen met minder clusters
					//						return this.run(cps, clusters.length-1, maxSearchTime, maxSearchIterations, activitiesInScope,
					//								dataAttributesInScope, idPrefix);
					return null;
				}
				pastDistances.add(distance);
				while(pastDistances.size() > Config.CLUSTER_STOP-1)
					pastDistances.remove(0);
			}
		}
		if(isShowMessages()) {
			System.out.println("-----------------------------");
			for(Cluster c : clusters)
				System.out.println(c);
			System.out.println("-----------------------------");
		}
		return clusters;
	}

	private static void clearClusters(Cluster[] clusters) {
		for(Cluster c : clusters)
			c.clear();
	}

	private static void calculateCentroids(Cluster[] clusters) {
		for(Cluster c : clusters)
			c.calculateCentroid();
	}

	private static CentroidProfile[] getCentroids(Cluster[] clusters) {
		CentroidProfile[] centroids = new CentroidProfile[clusters.length];
		for(int i = 0; i < clusters.length; i++)
			centroids[i] = clusters[i].getCentroid();
		return centroids;
	}

	private void assignCluster(Cluster[] clusters, ArrayList<ClusterPoint> cps,
			Activity[] activitiesInScope, DataAttribute[] dataAttributesInScope) {
		for(ClusterPoint cp : cps) {
			int cluster = -1;
			double min = -1;
			for(int i = 0; i < clusters.length; i++) {
				Cluster c = clusters[i];
				double distance = Profile.getDistance(cp.getProfile(activitiesInScope, dataAttributesInScope),
						c.getCentroid());
				if(min == -1 || distance < min) {
					min = distance;
					cluster = i;
				}
			}
			clusters[cluster].getClusterPoints().add(cp);
		}
	}
}