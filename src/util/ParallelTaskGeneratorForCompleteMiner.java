package util;

import java.io.File;

import javax.swing.JFileChooser;

public class ParallelTaskGeneratorForCompleteMiner {

	//code to generate parallel tasks for complete model
	public static void main(String[] args) throws Exception {
		int nrOfParts = 92;
		File[] dir = FileManager.selectOpenFile(null, new File("."), JFileChooser.DIRECTORIES_ONLY, null);
		File dirToCopy = dir[0];
		String name = dirToCopy.getName().substring(0, dirToCopy.getName().length()-1);
		File parentDir = dirToCopy.getParentFile();
		for(int i = 1; i < nrOfParts; i++) {
			File newDir = new File(parentDir, name + i);
			newDir.mkdir();
			for(File f : dirToCopy.listFiles()) {
				File newFile = new File(newDir, f.getName());
				FileManager.copyfile(f, newFile);
				if(newFile.getName().startsWith("job_")) {
					String tmp = FileManager.readAll(newFile);
					tmp = tmp.replace(".xes\" \"0/" + nrOfParts + "\"", ".xes\" \"" + i + "/" + nrOfParts + "\"");
					tmp = tmp.replace(".xes\" 0/" + nrOfParts, ".xes\" " + i + "/" + nrOfParts);
					FileManager.writeAll(newFile, tmp);
				}
			}
		}
		for(int aaas = 0; aaas < 92; aaas++) {
			if(aaas == 0)
				System.out.println("cd DeciClareMinerV11_Complete_0_seedBlockNr" + aaas);
			else
				System.out.println("cd ../DeciClareMinerV11_Complete_0_seedBlockNr" + aaas);
			System.out.println("qsub job_DeciClareMinerV11_Complete_0_split.pbs");
		}
		System.exit(0);
	}
}