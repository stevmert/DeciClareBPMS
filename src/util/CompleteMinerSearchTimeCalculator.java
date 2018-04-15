package util;

import java.io.File;

import javax.swing.JFileChooser;

public class CompleteMinerSearchTimeCalculator {

	private static String dirNameEnding = "_seedBlockNr";//_seedBlock _seedBlockNr _seedBlockXXX

	public static void main(String[] args) throws Exception {
		File[] dirs = FileManager.selectOpenFile(null, new File("."), JFileChooser.DIRECTORIES_ONLY, null);
		for(File dir : dirs)
			process(dir);
		System.exit(0);
	}

	private static void process(File dir) {
		File start = new File(dir, "DeciClareMinerV11 - Intermediate_Rules_P1.csv");
		File end = new File(dir, "DeciClareMinerV11 - Result_Rules.csv");
		if(start.exists() && end.exists()) {
			long s = start.lastModified();
			long e = end.lastModified();
			long secs = (e-s)/1000;
			double hours = secs/3600.0;
			System.out.println(hours + "\t"
					+ dir.getName().substring(dir.getName().indexOf(dirNameEnding) + dirNameEnding.length()));
		} else
			System.out.println("\t"
					+ dir.getName().substring(dir.getName().indexOf(dirNameEnding) + dirNameEnding.length()));//not finished yet
	}
}