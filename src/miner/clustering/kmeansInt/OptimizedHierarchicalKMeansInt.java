package miner.clustering.kmeansInt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import miner.Config;
import miner.kb.KnowledgeBase;
import miner.log.Log;
import miner.log.Trace;
import model.Activity;
import model.data.DataAttribute;
import util.xes.XESReader;

public class OptimizedHierarchicalKMeansInt extends KMeansIntImpl {

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

		OptimizedHierarchicalKMeansInt k = new OptimizedHierarchicalKMeansInt(true);
		long t1 = System.currentTimeMillis();
		Cluster[] clusters = k.run(cps, activitiesInScope, dataAttributesInScope,
				Config.NUM_CLUSTERS_TOP, Config.NUM_CLUSTERS_SUB,
				Config.CLUSTER_SPLIT_THRESHOLD,
				Config.MAX_SEARCHTIME_TOP, Config.MAX_SEARCHTIME_SUB,
				Config.MAX_SEARCH_ITERATIONS_TOP, Config.MAX_SEARCH_ITERATIONS_SUB,
				Config.CLUSTER_RETRIES);
		long t2 = System.currentTimeMillis();

		int nrOfTotalClusters = clusters.length;
		int nrOfLeafClusters = 0;
		for(Cluster c : clusters) {
			nrOfTotalClusters += c.getNumberOfSubclusters();
			if(c.hasSubClusters())
				nrOfLeafClusters += c.getNumberOfLeafSubclusters();
			else
				nrOfLeafClusters++;
		}
		System.out.println("Clustering " + log.size() + " traces "
				+ "based on a " + Profile.getRandomProfile(activitiesInScope, dataAttributesInScope).getDimension()
				+ "-dimensional vector took " + (((double) (t2-t1))/1000) + "secs");
		System.out.println(nrOfTotalClusters + " (sub)clusters found");
		if(nrOfLeafClusters == 0)
			System.out.println(nrOfLeafClusters + " leaf clusters found");
		else
			System.out.println(nrOfLeafClusters + " leaf clusters found (containing "
					+ (log.size()/nrOfLeafClusters) + " traces on avg)");

		HashSet<int[]> seeds = new HashSet<>();
		for(Cluster c : clusters)
			seeds.add(c.getCentroid().getDataProfile());
		System.out.println();
		System.out.println("Found dataseeds (" + seeds.size() + "):");
		for(int[] s : seeds) {
			//			System.out.println(Arrays.toString(s));
			String d = "";
			for(int i = 0; i < s.length; i++)
				if(s[i] != 0)
					d += " " + dataAttributesInScope[i];
			d = d.trim();
			if(d.length() == 0)
				System.out.println("None");
			else
				System.out.println(d);
		}
		System.out.println();
	}

	public OptimizedHierarchicalKMeansInt(boolean showMessages) {
		super(showMessages);
	}

	public Cluster[] run(ArrayList<ClusterPoint> cps, Activity[] activitiesInScope,
			DataAttribute[] dataAttributesInScope, int numberOfTopClusters, int numberOfSubclusters,
			double clusterSplitThreshold,
			long maxSearchTimeTop, long maxSearchTimeSub,
			int maxSearchIterationsTop, int maxSearchIterationsSub,
			int nrOfRetries) {
		int iteration = 1;
		HierarchicalKMeansInt hierkmeans = new HierarchicalKMeansInt(isShowMessages());
		if(isShowMessages()) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Optimized hierarchical iteration: " + iteration++);
		}
		Cluster[] hc = hierkmeans.run(cps, activitiesInScope, dataAttributesInScope,
				numberOfTopClusters, numberOfSubclusters, clusterSplitThreshold,
				maxSearchTimeTop, maxSearchTimeSub, maxSearchIterationsTop, maxSearchIterationsSub, nrOfRetries);
		ArrayList<Cluster> leafClusters = new ArrayList<>();
		for(Cluster c : hc) {
			if(!c.hasSubClusters())
				leafClusters.add(c);
			else
				leafClusters.addAll(c.getLeafCluster());
		}
		if(leafClusters.isEmpty())
			throw new IllegalArgumentException("TODO: retry?");
		KMeansInt km = new KMeansInt(isShowMessages());
		//TODO: merge threshold? => merge clusters with very similar centroids!!!
		Cluster[] re_hc = km.run(cps, leafClusters.toArray(new Cluster[leafClusters.size()]),
				maxSearchTimeTop, maxSearchIterationsTop, activitiesInScope, dataAttributesInScope, "", nrOfRetries);
		ArrayList<Cluster> clusters = new ArrayList<>();
		if(re_hc != null)
			for(Cluster c : re_hc)
				if(!c.getClusterPoints().isEmpty()) {
					clusters.add(c);
					//					if(c.getClusterPoints().size() >= getClusterSplitThreshold()*cps.size())
					//						splitableClusters.add(c);TODO?
				}
		if(isShowMessages())
			System.out.println("Number of optimized clusters: " + clusters.size());
		Cluster[] result = clusters.toArray(new Cluster[clusters.size()]);
		return result;
	}
}