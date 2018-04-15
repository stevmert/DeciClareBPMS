package miner.clustering.kmeansInt;

public abstract class KMeansIntImpl {

	private boolean showMessages;

	public KMeansIntImpl(boolean showMessages) {
		super();
		this.showMessages = showMessages;
	}

	public boolean isShowMessages() {
		return showMessages;
	}

	public void setShowMessages(boolean showMessages) {
		this.showMessages = showMessages;
	}


	protected void printClusters(Cluster[] clusters) {
		printClusters(clusters, 0);
	}

	protected void printClusters(Cluster[] clusters, int nrOfTabs) {
		for(Cluster c : clusters) {
			System.out.println(getTabs(nrOfTabs) + c);
			if(c.hasSubClusters())
				printClusters(c.getSubclusters(), nrOfTabs+1);
		}
	}

	private static final String TAB = "   ";
	protected String getTabs(int nrOfTabs) {
		String res = "";
		for(int i = 0; i < nrOfTabs; i++)
			res += TAB;
		return res;
	}
}