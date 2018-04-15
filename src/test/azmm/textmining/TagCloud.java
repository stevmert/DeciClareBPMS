package test.azmm.textmining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class TagCloud {

	private static HashMap<String, TagCloud> instances = new HashMap<>();
	public static TagCloud getInstance(String instanceName) {
		if(instances.get(instanceName) == null)
			instances.put(instanceName, new TagCloud(instanceName));
		return instances.get(instanceName);
	}

	private final String cloudName;
	private final HashMap<String, Long> cloud;

	private TagCloud(String cloudName) {
		super();
		this.cloud = new HashMap<>();
		this.cloudName = cloudName;
	}

	public void addText(String wordprocessorInstanceName, String text) {
		addProcessedText(WordProcessor.getInstance(wordprocessorInstanceName).processText(text));
	}

	public void addProcessedText(String[] processedText) {
		if(processedText == null)
			return;
		for(String word : processedText) {
			Long nr = cloud.get(word);
			if(nr == null)
				nr = 1l;
			else
				nr++;
			cloud.put(word, nr);
		}
	}

	//	public void addText(String text) {
	//		if(text == null || text.trim().length() == 0)
	//			return;
	//		String cleanedText = clean(text);
	//		for(String s : cleanedText.split(" ")) {
	//			String split1 = splitWords.get(s);
	//			if(split1 != null) {
	//				addWord(split1);
	//				addWord(s.substring(split1.length()));
	//			} else
	//				addWord(s);
	//		}
	//	}
	//
	//	private void addWord(String word) {
	//		if(word.length() == 0
	//				|| ignoreWords.contains(word))
	//			return;
	//		ArrayList<String> tmp = sameWords.get(word);
	//		if(tmp != null)//use first in list
	//			word = tmp.get(0);
	//		if(Arrays.asList(new String[]{}).contains(word))//TODO: for testing...
	//			"".length();
	//		Long nr = cloud.get(word);
	//		if(nr == null)
	//			nr = 1l;
	//		else
	//			nr++;
	//		cloud.put(word, nr);
	//	}

	public void test(String wordprocessorInstanceName) {
		Scanner s = new Scanner(System.in);
		boolean doReload = false;
		while(testSub(s, doReload, wordprocessorInstanceName))
			doReload = true;
		s.close();
		System.exit(0);
	}

	private boolean testSub(Scanner s, boolean doReload, String wordprocessorInstanceName) {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("Woord (+synoniemen)? ('EXIT' om af te sluiten)");
		String line = s.nextLine();
		if(line.trim().equals("EXIT"))
			return false;
		if(doReload)
			WordProcessor.getInstance(wordprocessorInstanceName).reload();
		String[] words = line.split(";");
		{
			ArrayList<String> searchWords = new ArrayList<>();
			for(String w : words) {
				w = w.toLowerCase().trim();
				ArrayList<String> x = WordProcessor.getInstance(wordprocessorInstanceName).getSameWords().get(w);
				if(x != null)
					searchWords.addAll(x);
				else
					searchWords.add(w);
			}
			searchWords = new ArrayList<>(new LinkedHashSet<>(searchWords));
			System.out.println();
			String sameString = "";
			for(String sw : searchWords)
				sameString += ";" + sw;
			System.out.println(sameString.substring(1));
			words = searchWords.toArray(new String[searchWords.size()]);
		}
		System.out.println();
		@SuppressWarnings("unchecked")
		ArrayList<String>[] splitWords = new ArrayList[words.length];
		for(int i = 0; i < splitWords.length; i++)
			splitWords[i] = new ArrayList<String>();
		for(String w : cloud.keySet()) {
			for(int i = 0; i < words.length; i++)
				if(!Arrays.asList(words).contains(w)
						&& !w.equals(words[i]) && w.contains(words[i])
						&& !WordProcessor.getInstance(wordprocessorInstanceName).getSplitWords().containsKey(w)) {
					if(w.startsWith(words[i])) {
						String x = w + ";" + words[i];
						if(!contains(splitWords, x))
							splitWords[i].add(x);
					} else if(w.endsWith(words[i])) {
						String x = w + ";" + w.substring(0, w.length()-words[i].length());
						if(!contains(splitWords, x))
							splitWords[i].add(x);
					} else {
						String x = w + ";" + w.substring(0, w.indexOf(words[i]));
						if(!contains(splitWords, x))
							splitWords[i].add(x);
						String rest = w.substring(w.indexOf(words[i]));
						if(!WordProcessor.getInstance(wordprocessorInstanceName).getSplitWords().containsKey(rest)
								&& !WordProcessor.getInstance(wordprocessorInstanceName).getSameWords().containsKey(rest)) {
							String y = rest + ";" + words[i];
							if(!contains(splitWords, y))
								splitWords[i].add(y);
						}
					}
				}
			boolean reset = false;
			if(reset)
				return true;
		}
		int nrFound = 0;
		for(int i = 0; i < splitWords.length; i++)
			for(String x : splitWords[i]) {
				System.out.println(x);
				nrFound++;
			}
		System.out.println("(" + nrFound + " resultaten gevonden)");
		return true;
	}

	private boolean contains(ArrayList<String>[] splitWords, String toTest) {
		for(int i = 0; i < splitWords.length; i++)
			if(splitWords[i].contains(toTest))
				return true;
		return false;
	}

	public void export() {
		export("TagCloud_" + cloudName + ".csv");
	}

	public void export(String fileName) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));
			for(String w : cloud.keySet()) {
				bw.write(w + ";" + cloud.get(w));
				bw.newLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}