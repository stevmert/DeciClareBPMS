package util;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import miner.Config;

public class TestResultsReader implements Runnable {

	public static void main(String[] args) {
		try {
			new TestResultsReader(args).run();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private final String start;
	private final File dir;
	private final File out;
	private String out_tmp;

	public TestResultsReader(String[] args) {
		if(args != null && args.length == 2) {
			dir = new File(args[0]);
			start = args[1];
			out = new File(dir, "0" + start + "status.txt");
		} else {
			start = "";
			dir = FileManager.selectOpenFile(null, new File("."), JFileChooser.DIRECTORIES_ONLY, null)[0];
			out = new File(dir, "0status.txt");
			//		for(File f : dir.listFiles()) {
			//			System.out.println("ALL_3_10_0000" + f.getName().substring("ALL_3_10_".length()));
			//			f.renameTo(new File(dir, "ALL_3_10_0000" + f.getName().substring("ALL_3_10_".length())));
			//		}
		}
		out_tmp = "";
	}

	@Override
	public void run() {
		try {
			String[] wantedRules = {"AtLeast(Perform surgery, 1, 0, -1)",
			"Response(AtLeast(Perform surgery, 1, 0, -1), AtLeast(Apply cast, 1, 0, -1), 0, -1)"};
			ArrayList<int[]> res_total = new ArrayList<>();
			int[] max_total = null;
			ArrayList<Integer> totalRules = new ArrayList<>();
//			ArrayList<Long> durationsP1 = new ArrayList<>();
//			ArrayList<Long> durationsP2 = new ArrayList<>();
			for(File f : dir.listFiles())
				if(f.getName().startsWith(start)
						&& f.getName().endsWith(" - " + Config.FILENAME_RESULT + "." + Config.FILE_CSV)) {
					String testName = f.getName().substring(0, f.getName().indexOf(" - "));
//					File log = new File(f.getParentFile(), testName + " - " + Config.FILENAME_LOGGER + ".csv");
//					{
//						String fileString = FileManager.readAll(log);
//						String durationP1 = fileString.substring(fileString.indexOf("\n" + Config.FILENAME_LOGGER_P1
//								+ ":;") + ("\n" + Config.FILENAME_LOGGER_P1 + ":;").length());
//						durationP1 = durationP1.substring(0, durationP1.indexOf("\n"));
//						if(durationP1.contains(";"))
//							durationP1 = durationP1.substring(0, durationP1.indexOf(";"));
//						durationsP1.add(Long.parseLong(durationP1));
//						String durationP2 = fileString.substring(fileString.indexOf("\n" + Config.FILENAME_LOGGER_P2
//								+ ":;") + ("\n" + Config.FILENAME_LOGGER_P2 + ":;").length());
//						durationP2 = durationP2.substring(0, durationP2.indexOf("\n"));
//						if(durationP2.contains(";"))
//							durationP2 = durationP2.substring(0, durationP2.indexOf(";"));
//						durationsP2.add(Long.parseLong(durationP2));
//					}
					//					if(max_total == null) {//TODOa
					File intermediate = new File(f.getParentFile(), testName + " - " + Config.FILENAME_INTERMEDIATE_P1
							+ ".csv");
					int[] max;
					{
						String fileString = FileManager.readAll(intermediate);
						max = getConformingInstances(fileString, wantedRules);
					}
					if(max_total == null)
						max_total = max;
					else
						for(int i = 0; i < max_total.length; i++)
							if(max_total[i] != max[i])//not made for heterogeneous experiments
								throw new IllegalArgumentException();
					//					}
					int[] res;
					int totalRulesTMP;
					{
						String fileString = FileManager.readAll(f);
						res = getConformingInstances(fileString, wantedRules);
						totalRulesTMP = getTotalRules(fileString);
					}
					res_total.add(res);
					totalRules.add(totalRulesTMP);
					for(int i = 0; i < wantedRules.length; i++) {
						out(testName + ": " + (((double) Math.round(10000d*((double) res[i])/max_total[i]))/100) + "% ("
								+ res[i] + "/" + max_total[i] + ")"
								+ " for " + wantedRules[i] + "...)");
					}
					out(testName + ": " + totalRulesTMP + " rules mined");
//					out(testName + ": phase 1 took " + durationsP1.get(durationsP1.size()-1)
//					+ "ms and phase 2 " + durationsP2.get(durationsP2.size()-1) + "ms");
				}

			double[] avgs = new double[wantedRules.length];
			double[] stanDev = new double[wantedRules.length];
			int n = 0;
			for(int i = 0; i < wantedRules.length; i++) {
				int sum = 0;
				int n2 = 0;
				for(int[] res : res_total) {
					sum += res[i];
					n2++;
				}
				avgs[i] = ((double) sum)/n2;
				n = n2;
			}
			for(int i = 0; i < wantedRules.length; i++) {
				double sum = 0;
				for(int[] res : res_total)
					sum += ((((double) res[i]) - avgs[i]) * (res[i] - avgs[i])) / n;
				stanDev[i] = Math.sqrt(sum);
			}
			for(int i = 0; i < wantedRules.length; i++) {
				out("==> average for " + wantedRules[i] + "...): " + Math.round(avgs[i])
				+ " (" + (((double) Math.round(10000d*avgs[i]/max_total[i]))/100) + "%)"
				+ " with a standard deviation of " + Math.round(stanDev[i])
				+ " (" + (((double) Math.round(stanDev[i]*10000/max_total[i]))/100) + "%)");
			}
			double avg_total = 0;
			for(int i : totalRules)
				avg_total += i;
			avg_total = avg_total / totalRules.size();
			double stanDev_total = 0;
			for(int i : totalRules)
				stanDev_total += ((((double) i) - avg_total) * (i - avg_total)) / totalRules.size();
			stanDev_total = Math.sqrt(stanDev_total);
			out("==> average for total mined rules: " + Math.round(avg_total)
			+ " with a standard deviation of " + Math.round(stanDev_total));
//			double avg_durationP1 = 0;//in seconds
//			for(long i : durationsP1)
//				avg_durationP1 += ((double) i)/1000;
//			avg_durationP1 = avg_durationP1 / durationsP1.size();
//			double stanDev_durP1 = 0;
//			for(long i : durationsP1)
//				stanDev_durP1 += ((((double) i)/1000 - avg_durationP1)
//						* (((double) i)/1000 - avg_durationP1)) / durationsP1.size();
//			stanDev_durP1 = Math.sqrt(stanDev_durP1);
//			out("==> average P1 mining time: " + ((double) Math.round(avg_durationP1*100))/100 + "secs"
//					+ " with a standard deviation of " + ((double) Math.round(stanDev_durP1*100))/100 + "secs");
			if(out_tmp.length() > 0)
				FileManager.writeAll(out, out_tmp.trim());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static int[] getConformingInstances(String fileString, String[] wantedRules) {
		int[] found = new int[wantedRules.length];
		for(int i = 0; i < wantedRules.length; i++) {
			if(fileString.contains(";" + wantedRules[i])) {
				String tmp = fileString.substring(0, fileString.indexOf(";" + wantedRules[i]));
				tmp = tmp.substring(tmp.lastIndexOf("\n"));
				tmp = tmp.substring(tmp.indexOf(";") + 1);
				found[i] = Integer.parseInt(tmp.substring(0, tmp.indexOf(";")));
			} else
				found[i] = 0;
		}
		return found;
	}

	private static int getTotalRules(String fileString) {
		return fileString.split("\n").length - 1;
	}

	private void out(String out) {
		System.out.println(out);
		out_tmp += "\n" + out;
	}
}