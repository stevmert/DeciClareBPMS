package test.azmm.textmining;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import util.FileManager;
import util.StringManager;

public class WordProcessor {

	private static HashMap<String, WordProcessor> instances = new HashMap<>();
	public static WordProcessor getInstance(String instanceName) {
		if(instances.get(instanceName) == null)
			instances.put(instanceName, new WordProcessor(instanceName));
		return instances.get(instanceName);
	}

	private final String wordprocessorName;
	private HashSet<String> ignoreWords;
	private HashSet<String> recombineWords;
	private HashMap<String, ArrayList<String>> sameWords;
	private HashMap<String, String> splitWords;

	private WordProcessor(String instanceName) {
		super();
		this.wordprocessorName = instanceName;
		reload();
	}

	public HashSet<String> getIgnoreWords() {
		return ignoreWords;
	}

	public HashMap<String, ArrayList<String>> getSameWords() {
		return sameWords;
	}

	public HashMap<String, String> getSplitWords() {
		return splitWords;
	}

	public void reload() {
		String appendix;
		if(wordprocessorName.equals("alg"))
			appendix = "0";
		else
			throw new IllegalArgumentException();
		ignoreWords = loadIgnoreWords(appendix);
		//		sameWords = loadSameWords(appendix);
		sameWords = loadSynonymWords(appendix, loadGeneralizationWords(appendix));
		splitWords = loadSplitWords(appendix);
		recombineWords = loadRecombineWords(appendix);
	}

	private HashMap<String, String> loadSplitWords(String appendix) {
		HashMap<String, String> res = new HashMap<>();
		try {
			File f = new File(new File("textmining"), "splitWords" + appendix + ".csv");
			if(!f.exists())
				return res;
			String s = FileManager.readAll(f);
			boolean doWrite = false;
			if(s.contains(";;")) {
				doWrite = true;
				while(s.contains(";;"))
					s = s.replace(";;", ";");
			}
			if(s.contains(";\n")) {
				doWrite = true;
				s = s.replace(";\n", "\n");
			}
			if(doWrite)
				FileManager.writeAll(f, s);
			if(s.contains(":"))
				throw new IllegalArgumentException("Contains ':'!");
			for(String w : s.trim().split("\n")) {
				if(w.trim().length() == 0)
					continue;
				else if(!w.contains(";")) {
					if(w.length() > 0)
						System.out.println("Skipped " + w + "...");
				} else {
					String[] sp = w.split(";");
					if(sp.length < 2)
						throw new IllegalArgumentException("No split: " + w);
					String test = res.put(sp[0].trim().toLowerCase(), sp[1].trim().toLowerCase());
					if(test != null)
						throw new IllegalArgumentException(sp[0]);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private HashSet<String> loadIgnoreWords(String appendix) {
		HashSet<String> res = new HashSet<>();
		try {
			File f = new File(new File("textmining"), "ignoreWords" + appendix + ".csv");
			if(!f.exists())
				return res;
			String s = FileManager.readAll(f);
			if(s.contains(":"))
				throw new IllegalArgumentException("Contains ':'!");
			for(String w : s.split("\n")) {
				String xxx = w.trim().toLowerCase();
				if(xxx.length() > 0 && !res.add(xxx))
					System.out.println("Dubbel in ignoreWords: " + xxx);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	//	private HashMap<String, ArrayList<String>> loadSameWords(String appendix) {
	//		HashMap<String, ArrayList<String>> res = new HashMap<>();
	//		try {
	//			File f = new File(new File("textmining"), "sameWords" + appendix + ".csv");
	//			if(!f.exists())
	//				return res;
	//			String s = FileManager.readAll(f);
	//			if(s.contains(":"))
	//				throw new IllegalArgumentException("Contains ':'!");
	//			for(String w : s.trim().split("\n")) {
	//				if(!w.contains(";")) {
	//					if(w.length() > 0)
	//						System.out.println("Skipped " + w + "...");
	//				} else {
	//					ArrayList<String> set = new ArrayList<>();
	//					for(String ss : w.split(";")) {
	//						String xxx = ss.trim().toLowerCase();
	//						set.add(xxx);
	//						ArrayList<String> test = res.put(xxx, set);
	//						if(test != null)
	//							throw new IllegalArgumentException(xxx);
	//					}
	//				}
	//			}
	//		} catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//		return res;
	//	}

	private HashMap<String, ArrayList<String>> loadGeneralizationWords(String appendix) {
		HashMap<String, ArrayList<String>> res = new HashMap<>();
		try {
			File f = new File(new File("textmining"), "generalizationWords" + appendix + ".csv");
			if(!f.exists())
				return res;
			String s = FileManager.readAll(f);
			boolean doWrite = false;
			if(s.contains(";;")) {
				doWrite = true;
				while(s.contains(";;"))
					s = s.replace(";;", ";");
			}
			if(s.contains(";\n")) {
				doWrite = true;
				s = s.replace(";\n", "\n");
			}
			if(doWrite)
				FileManager.writeAll(f, s);
			if(s.contains(":"))
				throw new IllegalArgumentException("Contains ':'!");
			for(String w : s.trim().split("\n")) {
				if(!w.contains(";")) {
					if(w.length() > 0)
						System.out.println("Skipped " + w + "...");
				} else {
					ArrayList<String> set = new ArrayList<>();
					for(String ss : w.split(";")) {
						String xxx = ss.trim().toLowerCase();
						set.add(xxx);
						ArrayList<String> test = res.put(xxx, set);
						if(test != null)
							throw new IllegalArgumentException(xxx);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private HashMap<String, ArrayList<String>> loadSynonymWords(String appendix,
			HashMap<String, ArrayList<String>> generalizationWords) {
		try {
			File f = new File(new File("textmining"), "synonymWords" + appendix + ".csv");
			if(!f.exists())
				return generalizationWords;
			String s = FileManager.readAll(f);
			boolean doWrite = false;
			if(s.contains(";;")) {
				doWrite = true;
				while(s.contains(";;"))
					s = s.replace(";;", ";");
			}
			if(s.contains(";\n")) {
				doWrite = true;
				s = s.replace(";\n", "\n");
			}
			if(doWrite)
				FileManager.writeAll(f, s);
			if(s.contains(":"))
				throw new IllegalArgumentException("Contains ':'!");
			for(String w : s.trim().split("\n")) {
				if(!w.contains(";")) {
					if(w.length() > 0)
						System.out.println("Skipped " + w + "...");
				} else {
					ArrayList<String> set = new ArrayList<>();
					int k = 0;
					for(String ss : w.split(";")) {
						String xxx = ss.trim().toLowerCase();
						ArrayList<String> tmp = generalizationWords.get(xxx);
						if(tmp == null) {
							set.add(xxx);
							generalizationWords.put(xxx, set);
						} else if(k == 0) {
							//mondkeelneus;neus;keel;mond -> 'neus' = [mondkeelneus,neus,keel,mond]
							//neus;neuzen;neusje
							set = tmp;
						} else {
							//mondkeelneus;neus;keel;mond -> 'neus' = [mondkeelneus,neus,keel,mond]
							//neusje;neuzen;neus
							throw new IllegalArgumentException(xxx);
						}
						k++;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return generalizationWords;
	}

	private HashSet<String> loadRecombineWords(String appendix) {
		HashSet<String> res = new HashSet<>();
		try {
			File f = new File(new File("textmining"), "recombineWords" + appendix + ".csv");
			if(!f.exists())
				return res;
			String s = FileManager.readAll(f);
			for(String w : s.split("\n")) {
				String xxx = w.trim().toLowerCase();
				int x = xxx.split(" ").length;
				if(x < 2)
					throw new IllegalArgumentException("Doesn't contain ' '!");
				if(x > 2)
					throw new IllegalArgumentException("Contains multiple ' '!");
				if(xxx.length() > 0 && !res.add(xxx))
					System.out.println("Dubbel in recombineWords: " + xxx);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String[] processText(String text) {
		String cleanedText = preprocess(text);
		if(cleanedText == null)
			return null;
		LinkedHashSet<String> res = new LinkedHashSet<>();
		String[] split = cleanedText.split(" ");
		for(int i = 0; i < split.length; i++) {
			String s = split[i];
			if(i+1 < split.length && recombineWords.contains(split[i] + " " + split[i+1])) {
				s = split[i] + split[i+1];
				i++;
			}
			ArrayList<String> tmp = processPotentialWord(s);
			if(tmp != null)
				res.addAll(tmp);
		}
		return res.toArray(new String[res.size()]);
	}

	private ArrayList<String> processPotentialWord(String word) {
		if(word.length() == 0
				|| ignoreWords.contains(word))
			return null;
		ArrayList<String> tmp = sameWords.get(word);
		if(tmp != null)//use first in list
			word = tmp.get(0);
		ArrayList<String> res = new ArrayList<>();
		String split1 = splitWords.get(word);
		if(split1 != null) {
			ArrayList<String> tmp1 = processPotentialWord(split1.trim());
			if(tmp1 != null)
				res.addAll(tmp1);
			ArrayList<String> tmp2 = processPotentialWord(word.substring(split1.length()).trim());
			if(tmp2 != null)
				res.addAll(tmp2);
		} else {
			String w = processWord(word);
			if(w != null)
				res.add(w);
		}
		return res;
	}

	private String processWord(String word) {
		if(word.length() == 0
				|| ignoreWords.contains(word))
			return null;
		ArrayList<String> tmp = sameWords.get(word);
		if(tmp != null)//use first in list
			word = tmp.get(0);
		if(Arrays.asList(new String[]{
				//				"pijnvk",
				//				"rxth", "rxtx", "rxr",
				//				"cta", "ctl", "ctt", "cttx",
				//				"vg",
				//				"rr",
				//				"ews",
				//				"hk",
				//				"cva",
				//				"wz",
				//				"ethyl",
				//				"sd",
				//				"echocor", "echolalie", "echografie", "echocardiografie", "echogeleide",
				//				"beenent",
				//				"koffiegruisbraken",
				//				"pm",
				//				"co",
				//				"vko",
				//				"nekmijn",
				//				"nekherniaop",
				//				"nekop",
				//				"neuswieken",
				//				"urinesediment",
				//				"urinekweek",
				//				"urinezak",
				//				"urineblad",
				//				"urinek",
				//				"urinestick",
				//				"urinezuur",
				//				"urineclearance",
				//				"ruoperatie",
				//				"borstcaoperatie",
				//				"co",
				//				"adl",
				//				"lonca",
				//				"paabces",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
				//				"",
		}).contains(word))//TODO: for testing...
			"".length();
		return word;
	}

	public static String preprocess(String input) {
		if(input == null || input.trim().length() == 0)
			return null;
		String cleanedInput = clean(input);
		if(cleanedInput == null || cleanedInput.length() == 0)
			return null;
		return cleanedInput;
	}

	private static String clean(String text) {
		String cleanedT = " " + text.toLowerCase().trim() + " ";
		for(String x : new String[]{
				"\n", "\t", "\r",
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				".", "<", ">", "\\", ",", "?", ";", "/", ":", "=", "+", "~", "^", "¨", "[", "]", "$", "*", "]", "%", "´", "`",
				"µ", "£", "&", "€", "|", "@", "\"", "#", "'", "(", "§", "!", "{", "}", ")", "°", "_", "²", "³",
				"-", "±", "®", "½", "¼", "¾", "â– ", "­", "«", "»", "â”‚", "\f", "", "", "", ""
		})
			cleanedT = cleanedT.replace(x, " ");
		cleanedT = cleanedT.replace("ß", "beta");
		cleanedT = StringManager.respace(cleanedT).trim();
		for(String x : new String[]{
				"â", "ä", "á", "à"
		})
			cleanedT = cleanedT.replace(x, "a");
		for(String x : new String[]{
				"ê", "ë", "é", "è"
		})
			cleanedT = cleanedT.replace(x, "e");
		for(String x : new String[]{
				"ÿ", "ý"
		})
			cleanedT = cleanedT.replace(x, "y");
		for(String x : new String[]{
				"û", "ü", "ú", "ù"
		})
			cleanedT = cleanedT.replace(x, "u");
		for(String x : new String[]{
				"î", "ï", "í", "ì"
		})
			cleanedT = cleanedT.replace(x, "i");
		for(String x : new String[]{
				"ô", "ö", "ó", "ò"
		})
			cleanedT = cleanedT.replace(x, "o");
		for(String x : new String[]{
				"ç"
		})
			cleanedT = cleanedT.replace(x, "c");
		try {
			cleanedT = new String(cleanedT.getBytes("US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < cleanedT.length(); i++) {
			int ch = cleanedT.charAt(i);//a-z == 97-122 en spatie == 32
			if(ch == 31) {//unit separator?
				cleanedT = cleanedT.substring(0, i) + cleanedT.substring(i+1);
				i--;
			} else if(ch != 32 && (ch < 97 || ch > 122))
				new IllegalArgumentException(text
						+ "\n\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n"
						+ cleanedT);
		}
		return cleanedT;
	}
}