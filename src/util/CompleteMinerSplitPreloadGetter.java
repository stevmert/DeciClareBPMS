package util;

import java.io.File;

import javax.swing.JFileChooser;

public class CompleteMinerSplitPreloadGetter {

	private static String fileNameStart = "Preloaded_Complete_RulesSeed";

	public static void main(String[] args) throws Exception {
		File[] dirs = FileManager.selectOpenFile(null, new File("."), JFileChooser.DIRECTORIES_ONLY, null);
		for(File dir : dirs)
			process(dir);
		System.exit(0);
	}

	private static void process(File dir) throws Exception {
		for(File f : dir.listFiles())
			if(f.getName().startsWith(fileNameStart))
				FileManager.copyfile(f, new File(new File("."), f.getName()));
	}
}