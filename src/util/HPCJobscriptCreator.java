package util;

import java.io.File;

public class HPCJobscriptCreator {

	public static void main(String[] args) throws Exception {
		int nrOfExperiments = 1;
		//		int nrOfInstancesPerExperiment = 2;
		//		int maxMinutesPerInstance = 4;
		//		String jarName = "DeciClareMinerV9";
		int nrOfInstancesPerExperiment = 100;
		int maxMinutesPerInstance = 18;
		String jarName = "DeciClareMinerV11_TARNEG_3_10";

		int nrOfMinutes = (nrOfExperiments * nrOfInstancesPerExperiment * maxMinutesPerInstance) % 60;
		String nrOfMinutes_tmp = ""+nrOfMinutes;
		while(nrOfMinutes_tmp.length() < 2)
			nrOfMinutes_tmp = "0" + nrOfMinutes_tmp;
		int nrOfHours = (nrOfExperiments * nrOfInstancesPerExperiment * maxMinutesPerInstance) / 60;
		String nrOfHours_tmp = ""+nrOfHours;
		while(nrOfHours_tmp.length() < 2)
			nrOfHours_tmp = "0" + nrOfHours_tmp;
		if(nrOfHours_tmp.length() > 2 || nrOfMinutes_tmp.length() > 2)
			throw new IllegalArgumentException();
		String stringToWrite = "#!/bin/bash";
		stringToWrite += "\n#PBS -l walltime=" + nrOfHours_tmp + ":" + nrOfMinutes_tmp + ":00";
		stringToWrite += "\n#PBS -l nodes=1:ppn=2";
		stringToWrite += "\n#PBS -l mem=16b";
		//		stringToWrite += "\n#PBS -M steven.mertens@ugent.be";
		//		stringToWrite += "\nmodule load Java/1.7.0_80";
//		stringToWrite += "\nmodule load Java/1.8.0_77";
		stringToWrite += "\nmodule load Java/1.8.0_112";
		stringToWrite += "\ncd $PBS_O_WORKDIR";
		stringToWrite += "\necho Start Job";
		stringToWrite += "\ndate";
		stringToWrite += "\njava -jar " + jarName + ".jar \"test_armFractures(5000)v2.xes\" "
				+ nrOfInstancesPerExperiment + " 1";
		stringToWrite += "\ndate";
		stringToWrite += "\necho End Job";
		FileManager.writeAll(new File("job_" + jarName + ".pbs"), stringToWrite);
	}
}