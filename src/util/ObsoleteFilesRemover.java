package util;

import java.io.File;

import javax.swing.JFileChooser;

public class ObsoleteFilesRemover {

	private static String[] files = {"Preloaded_test_armFractures(5000)v2.xes.pldata"};
	private static String jar_start = "DeciClareMinerV11_";
	private static String jar_end = ".jar";
	private static String p1_start = "Preloaded_test_armFractures(5000)v2.xes_ResP1";
	private static String p1_end = ".pldata";

	public static void main(String[] args) throws Exception {
		File[] dirs = FileManager.selectOpenFile(null, new File("."), JFileChooser.DIRECTORIES_ONLY, null);
		for(File dir : dirs)
			process(dir);
		System.exit(0);
	}

	private static void process(File dir) throws Exception {
		for(File f : dir.listFiles()) {
			if(f.isDirectory())
				process(f);
			else if(isDeletableFile(f)) {
				System.out.println("Deleting " + f);
				f.delete();
			}
		}
	}

	private static boolean isDeletableFile(File f) {
		for(String s : files)
			if(f.getName().equals(s))
				return true;
		if(f.getName().startsWith(jar_start) && f.getName().endsWith(jar_end))
			return true;
		if(f.getName().startsWith(p1_start) && f.getName().endsWith(p1_end))
			return true;
		return false;
	}
}