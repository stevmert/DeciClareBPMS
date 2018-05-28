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

public class HierarchicalKMeansDouble extends KMeansDoubleImpl {

	public static void main(String[] args) throws Exception {
		//		C:\Users\Steven\Dropbox\workspace\DeciClareMinerV11\logs\test_armFractures(5000)v2.xes
		System.out.print("Loading log...");
		Log log = XESReader.getLog(new File(new File("logs"), "test_armFractures(5000)v2.xes"));
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

		HierarchicalKMeansDouble k = new HierarchicalKMeansDouble(true);
		long t1 = System.currentTimeMillis();
		Cluster[] clusters = k.run(cps, activitiesInScope, dataAttributesInScope, Config.NUM_CLUSTERS_TOP, Config.NUM_CLUSTERS_SUB,
				Config.CLUSTER_SPLIT_THRESHOLD,
				Config.MAX_SEARCHTIME_TOP, Config.MAX_SEARCHTIME_SUB,
				Config.MAX_SEARCH_ITERATIONS_TOP, Config.MAX_SEARCH_ITERATIONS_SUB,
				Config.CLUSTER_RETRIES);
		long t2 = System.currentTimeMillis();

		int nrOfLeafClusters = 0;
		for(Cluster c : clusters) {
			if(c.hasSubClusters())
				nrOfLeafClusters += c.getNumberOfLeafSubclusters();
			else
				nrOfLeafClusters++;
		}
		System.out.println("Clustering " + log.size() + " traces "
				+ "based on a " + CentroidProfile.getRandomProfile(activitiesInScope, dataAttributesInScope).getDimension()
				+ "-dimensional vector took " + (((double) (t2-t1))/1000) + "secs");
		if(nrOfLeafClusters > 0)
			System.out.println("Leaf clusters contain "
					+ (log.size()/nrOfLeafClusters) + " traces on average");
	}

	public HierarchicalKMeansDouble(boolean showMessages) {
		super(showMessages);
	}

	public Cluster[] run(ArrayList<ClusterPoint> cps, Activity[] activitiesInScope,
			DataAttribute[] dataAttributesInScope,
			int numberOfClusters, int numberOfSubclusters,
			double clusterSplitThreshold,
			long maxSearchTimeTop, long maxSearchTimeSub,
			int maxSearchIterationsTop, int maxSearchIterationsSub,
			int nrOfRetries) {
		int iteration = 1;
		KMeansDouble kmeans = new KMeansDouble(isShowMessages());
		ArrayList<Cluster> clusters = new ArrayList<>();

		if(isShowMessages()) {
			System.out.println("=============================================");
			System.out.println("Hierarchical iteration: " + iteration++);
		}
		Cluster[] res = kmeans.run(cps, numberOfClusters, maxSearchTimeTop, maxSearchIterationsTop,
				activitiesInScope, dataAttributesInScope, "", nrOfRetries);
		ArrayList<Cluster> splitableClusters = new ArrayList<>();
		if(res != null)
			for(Cluster c : res)
				if(!c.getClusterPoints().isEmpty()) {
					clusters.add(c);
					if(c.getClusterPoints().size() >= clusterSplitThreshold*cps.size())
						splitableClusters.add(c);
				}
		if(isShowMessages())
			System.out.println("Number of top clusters: " + clusters.size());

		while(!splitableClusters.isEmpty()) {
			Cluster sc = splitableClusters.get(0);
			if(isShowMessages()) {
				System.out.println("=============================================");
				System.out.println("Hierarchical iteration: " + iteration++);
			}
			res = kmeans.run(sc.getClusterPoints(), numberOfSubclusters, maxSearchTimeSub, maxSearchIterationsSub,
					activitiesInScope, dataAttributesInScope, sc.getId(), nrOfRetries);
			ArrayList<Cluster> subclusters = new ArrayList<>();
			ArrayList<Cluster> splittableSubclusters = new ArrayList<>();
			if(res != null)
				for(Cluster c : res)
					if(!c.getClusterPoints().isEmpty()) {
						subclusters.add(c);
						if(c.getClusterPoints().size() >= clusterSplitThreshold*cps.size())
							splittableSubclusters.add(c);
					}
			if(subclusters.size() > 1) {
				sc.setSubclusters(subclusters.toArray(new Cluster[subclusters.size()]));
				splitableClusters.addAll(splittableSubclusters);
				if(isShowMessages())
					System.out.println("Number of subclusters found: " + subclusters.size());
			} else if(isShowMessages())
				System.out.println("No viable subclusters found...");
			splitableClusters.remove(0);
		}
		if(isShowMessages())
			System.out.println("=============================================");
		Cluster[] result = clusters.toArray(new Cluster[clusters.size()]);
		if(isShowMessages()) {
			printClusters(result);
			System.out.println("=============================================");
		}
		int nrOfTotalClusters = result.length;
		int nrOfLeafClusters = 0;
		for(Cluster c : result) {
			nrOfTotalClusters += c.getNumberOfSubclusters();
			if(c.hasSubClusters())
				nrOfLeafClusters += c.getNumberOfLeafSubclusters();
			else
				nrOfLeafClusters++;
		}
		System.out.println(nrOfTotalClusters + " (sub)clusters found");
		System.out.println(nrOfLeafClusters + " leaf clusters found");
		return result;
	}
}