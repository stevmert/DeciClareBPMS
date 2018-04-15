package test.azmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Reader {

	public static long counter_unknownContactID = 0;
	public static long counter_encodingProblem = 0;
	public static long counter_dataProblem = 0;
	public static long counter_contactlijnError = 0;
	public static long counter_spoedNrSkipped = 0;

	private static final String[] FIRST_COLUMN_NAMES = {"contactid", "spoednr", "id", "actief"};

	//"WV.Voelde", "nacht?Haar", "realistisch31/03", "01/07/16mevr", "vroeg?18", "goedBehandeling", "TUSSENDOOROok", "MIDDAGDe"
	private static String clean(String line) {
		if(line.startsWith("ï»¿"))
			line = line.substring("ï»¿".length());
		line = line.replace("pÃ®jn", "pijn");
		line = line.replace("TraumÃ¹a", "Trauma");
		line = line.replace("chocoladeÂ­schilfers", "chocolade­schilfers");
		line = line.replace("hepaÂ­ tic", "hepatic");
		line = line.replace("steroÃden", "steroïden");
		line = line.replace("Ãƒug", "Aug");
		line = line.replace("Â¶", " ");
		line = line.replace("Â²", "²");
		line = line.replace("Â³", "³");
		line = line.replace("Â°", "°");
		line = line.replace("Â´", "'");
		line = line.replace("Â·", "-");
		line = line.replace("Ã˜", "-");
		line = line.replace("Ã“", "-");
		line = line.replace("Â¬", "-");
		line = line.replace("Ã—", "x");
		line = line.replace("Â±", "±");
		line = line.replace("Â§", "§");
		line = line.replace("Â®", "®");
		line = line.replace("Â½", "½");
		line = line.replace("Â¼", "¼");
		line = line.replace("Â¾", "¾");
		line = line.replace("ÃŸ", "ß");
		line = line.replace("Â«", "«");
		line = line.replace("Â»", "»");
		line = line.replace("ÃƒÂ«", "ë");
		line = line.replace("Ãƒ«", "ë");
		line = line.replace("Ã«", "ë");
		line = line.replace("ÃƒÂ©", "é");
		line = line.replace("Ã©", "é");
		line = line.replace("Ã¨", "è");
		line = line.replace("Ãª", "ê");
		line = line.replace("Ã‹", "Ë");
		line = line.replace("ÃŠ", "Ê");
		line = line.replace("Ã‰", "É");
		line = line.replace("ÃƒÂ¶", "ö");
		line = line.replace("Ã¶", "ö");
		line = line.replace("Ãƒ ", "ö");
		line = line.replace("Ã´", "ô");
		line = line.replace("Ã³", "ó");
		line = line.replace("Ã²", "ò");
		line = line.replace("Ã–", "Ö");
		line = line.replace("Ã¯", "ï");
		line = line.replace("Ã®", "î");
		line = line.replace("Ã­", "í");
		line = line.replace("Ã¼", "ü");
		line = line.replace("Ã¹", "ù");
		line = line.replace("Ãº", "ú");
		line = line.replace("Ã»", "û");
		line = line.replace("Ãœ", "Ü");
		line = line.replace("Âµ", "µ");
		line = line.replace("Ã§", "c");//eigenlijk ç
		line = line.replace("Ã‚", "A");
		line = line.replace("Ã„", "A");
		line = line.replace("Ã¢", "â");
		line = line.replace("Ã¤", "ä");
		line = line.replace("Ã¡", "á");
		line = line.replace("Ã¿", "ÿ");
		line = line.replace("Â¨", "");
		line = line.replace("Â£", "");//TODO: is er een reden om '£Rx' of '£RVT' te typen???
		line = line.replace("Ã ", "à");
		line = line.replace("Â–", "à");
		for(String x : new String[]{"de toekomst.COGNITIEF - LINGU",
		">>> LINGU"})
			if(line.contains(x + "Ã")) {
				line = line.substring(0, line.indexOf(x) + x.length()) + "Ï"
						+ line.substring(line.indexOf(x) + x.length() + 2);
				break;
			}
		for(String x : new String[]{"invloed ethyl en coca",
				"co per dag - lage dosis cortico",
				"stase thv intra-aryteno",
				"mogelijkse vooruitgang van mvr te wijten valt aan een objectieve vooruitgang of aan het (tijdelijke) effect van de corticostero",
				"te blijven: geen chemotherapieverder IV vocht en cortico",
				"; doch niet duidelijk of betrouwbaar.Zegt coca",
				"; alles zo veel mogelijk op kamertemperatuur (of hogere temperatuur)starten met cortico",
				". Mevrouw heeft een vierwielrollator toilet binnen ge",
				". Onder het opdrijven van de corticostero",
				"ne en valpro",
				" houding. Neemt nu code",
				" recto-sigmo",
		"Velcade/melphalan/cortico"})
			if(line.contains(x + "Ã")) {
				line = line.substring(0, line.indexOf(x) + x.length()) + "ï"
						+ line.substring(line.indexOf(x) + x.length() + 2);
				break;
			}
		for(String x : new String[]{"34046;88;C005;88;NULL;0;NULL;NULL;NULL;rx li scapho",
				"1891894;beeld van diverticulitis ter hoogte van sigmo",
				"2043608;Op MR prote",
				"2097355;Rolandofractuur metacapraal 1 rechts Voorlopig immobilisatie met scapho",
				"2204620;AAT na sigmo",
		"2399560;Uw 37-jarige patiënte; A"})
			if(line.startsWith(x + "Ã")) {
				line = line.substring(0, line.indexOf(x) + x.length()) + "ï"
						+ line.substring(line.indexOf(x) + x.length() + 2);
				break;
			}
		if(line.contains("Ã") || line.contains("Â") || line.contains("Ä") || line.contains("À") || line.contains("Á"))
			throw new IllegalArgumentException("TODO?");
		return line;
	}

	private static String encode(String line) {
		if(!line.contains("\""))
			return line;
		if(line.startsWith("730;110;D301;"))
			line = line.replace(" te \"fatsoeneren ", " te fatsoeneren ");
		else if(line.startsWith("37081;101;D601;"))
			line = line.replace(" '\"s avonds ", " 's avonds ");
		else if(line.startsWith("43232;88;C005;"))
			line = line.replace("bal te\"gen li ", "bal tegen li ");
		if(getNumberOfOccurences(line, "\"")%2 != 0) {
			counter_encodingProblem++;
			System.out.println("Encoding problem(" + counter_encodingProblem + "): " + line);
			return line;
		}
		String res = line;
		boolean inside = false;
		for(int i = 0; i < res.length(); i++) {
			String l = res.substring(i, i+1);
			if(l.equals("\""))
				inside = !inside;
			else if(inside && l.equals(";")) {
				res = res.substring(0, i) + "%%%DOTCOM%%%" + res.substring(i+1);
				i += 11;
			}
		}
		return res;
	}

	public static int getNumberOfOccurences(String input, String toCheck) {
		if(input == null
				|| toCheck == null
				|| toCheck.equals(""))
			return 0;
		int occ = 0;
		while(input.contains(toCheck)) {
			occ++;
			input = input.substring(input.indexOf(toCheck) + toCheck.length());
		}
		return occ;
	}

	private static String[] decode(String[] split) {
		for(int i = 0; i < split.length; i++)
			if(split[i].contains("%%%DOTCOM%%%")) {
				split[i] = split[i].replace("%%%DOTCOM%%%", ";");
				if(split[i].startsWith("\"") && split[i].endsWith("\""))
					split[i] = split[i].substring(1, split[i].length()-1);
			}
		return split;
	}

	public static HashMap<Integer, User> readResources(File file) throws FileNotFoundException, IOException {
		HashMap<Integer, User> availableResources = new HashMap<>();
		//		ArrayList<String> doctors = new ArrayList<>();
		//		ArrayList<String> other = new ArrayList<>();
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				User r = new User(line.split(";"));
				availableResources.put(r.getId(), r);
				i++;
			}
		}
		System.out.println("Loaded " + i + " users...");
		return availableResources;
	}

	public static HashMap<Integer, SpoedNr> readOpnames(File file) throws FileNotFoundException, IOException {
		HashMap<Integer, SpoedNr> spoedNrs = new HashMap<>();
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String[] split = line.split(";");
				int spoedNr = Integer.parseInt(split[0]);
				if(!spoedNrs.containsKey(spoedNr))
					spoedNrs.put(spoedNr, new SpoedNr(spoedNr));
				Opname_Ontslag o = new Opname_Ontslag(split);
				spoedNrs.get(spoedNr).addOpname(o);
				//				System.out.println(line);
				i++;
			}
		}
		System.out.println("Loaded " + i + " opnames...");
		System.out.println("Loaded " + spoedNrs.size() + " spoedNrs...");
		return spoedNrs;
	}

	public static void readRX(File file, HashMap<Integer, SpoedNr> spoedNrs) throws FileNotFoundException, IOException {
		int added = 0;
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				int spoedNr = Integer.parseInt(split[0]);
				if(!spoedNrs.containsKey(spoedNr)) {
					spoedNrs.put(spoedNr, new SpoedNr(spoedNr));
					added++;
				}
				//				if(!spoedNrs.containsKey(spoedNr)) {
				//					skipped++;
				//					System.out.println("RX with non-existent spoedNr " + spoedNr + " (" + line + ")");//TODO
				//				} else {
				RX r = new RX(split);
				spoedNrs.get(spoedNr).addRX(r);
				//				}
				//				System.out.println(line);
				i++;
			}
		}
		System.out.println("Loaded " + i + " RXs...");
		System.out.println("Added " + added + " spoedNrs...");
	}

	public static void readWachttijden(File file, HashMap<Integer, SpoedNr> spoedNrs) throws FileNotFoundException, IOException {
		int added = 0;
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				int spoedNr = Integer.parseInt(split[0]);
				if(!spoedNrs.containsKey(spoedNr)) {
					spoedNrs.put(spoedNr, new SpoedNr(spoedNr));
					added++;
				}
				Wachtijden w = new Wachtijden(split);
				spoedNrs.get(spoedNr).addWachtijden(w);
				//				System.out.println(line);
				i++;
			}
		}
		System.out.println("Loaded " + i + " wachtijden...");
		System.out.println("Added " + added + " spoedNrs...");
	}

	public static void readTriage(File file, HashMap<Integer, SpoedNr> spoedNrs) throws FileNotFoundException, IOException {
		int added = 0;
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				split = correctTriage(split);
				while(split.length < 55) {
					String line2 = br.readLine();
					line2 = clean(line2);
					String lineEnc2 = encode(line2);
					String[] split2 = decode(lineEnc2.split(";"));
					split = merge(split, split2);
					split = correctTriage(split);
				}
				if(!isValidSplitTriage(split))
					throw new IllegalArgumentException("Invalid triage line: " + Arrays.toString(split));
				int spoedNr = Integer.parseInt(split[0]);
				if(!spoedNrs.containsKey(spoedNr)) {
					spoedNrs.put(spoedNr, new SpoedNr(spoedNr));
					added++;
				}
				Triage t = new Triage(split);
				//				if(t.getVermoedelijkeBevallingsdatum() != null) {
				//					double days = (double) ChronoUnit.DAYS.between(t.getCreateDate(), t.getVermoedelijkeBevallingsdatum());
				//					days = (30*9)-days;//TODO: per 3 maanden? -> 1 iemand 13maanden zwanger???
				//					long m = Math.round(days/30);
				//					if(!test1.containsKey(""+m))//TODO
				//						test1.put(""+m, 1);
				//					else
				//						test1.put(""+m, test1.get(""+m)+1);
				//				}
				spoedNrs.get(spoedNr).addTriage(t);
				//				System.out.println(line);
				i++;
			}
		}
		System.out.println("Loaded " + i + " triages...");
		System.out.println("Added " + added + " spoedNrs...");
	}

	private static String[] correctTriage(String[] split) {
		String[] tmp = split;
		if(split.length == 11
				&& split[0].equals("3648")
				&& split[1].equals("88")) {
			tmp = new String[split.length-1];
			for(int k = 0; k < tmp.length; k++) {
				if(k > 9)
					tmp[k] = split[k+1];
				else if(k == 9)
					tmp[k] = split[k] + "," + split[k+1];
				else
					tmp[k] = split[k];
			}
			split = tmp;
		} else if(split.length > 9
				&& (split[9].equals("0") || split[9].equals("1"))
				&& (split[8].trim().equalsIgnoreCase("NULL"))) {
			tmp = new String[split.length+1];
			for(int k = 0; k < 9; k++)
				tmp[k] = split[k];
			tmp[9] = "NULL";
			for(int k = 9; k < split.length; k++)
				tmp[k+1] = split[k];
			split = tmp;
		}
		while(split.length > 10
				&& !(split[10].equals("0") || split[10].equals("1"))) {
			tmp = new String[split.length-1];
			for(int k = 0; k < 10; k++)
				tmp[k] = split[k];
			int k = 10;
			if(k < split.length && !(split[k].equals("0") || split[k].equals("1")))
				tmp[9] += ";" + split[k];
			k++;
			for(int q = 0; k+q < split.length; q++)
				tmp[10+q] = split[k+q];
			split = tmp;
		}
		while(split.length > 56
				&& (split[10].equals("0") || split[10].equals("1"))
				&& (split[21].equals("0") || split[21].equals("1"))
				&& split[54].equalsIgnoreCase("null")
				&& !split[55].equalsIgnoreCase("null")) {
			tmp = new String[split.length-1];
			for(int k = 0; k < 56; k++)
				tmp[k] = split[k];
			tmp[55] += ";" + split[56];
			for(int k = 57; k < split.length; k++)
				tmp[k-1] = split[k];
			split = tmp;
		}
		return tmp;
	}

	private static boolean isValidSplitTriage(String[] split) {
		boolean firstCheck = (split.length == 55 || split.length == 56)
				&& (split[10].equals("0") || split[10].equals("1"))
				&& split[11].startsWith("201")
				&& split[19].startsWith("201")
				&& (split[21].equals("0") || split[21].equals("1"));
		if(!firstCheck)
			return false;
		for(int x : new int[]{0, 1, 3, 12, 20, 54})
			try {
				if(x == 54 && split[54].equalsIgnoreCase("null"))
					continue;
				Integer.parseInt(split[x].trim());
			} catch(NumberFormatException e) {
				return false;
			}
		return true;
	}

	private static String[] merge(String[] split1, String[] split2) {
		if(split1[split1.length-1].trim().equalsIgnoreCase("null")) {
			String[] res = new String[split1.length + split2.length];
			int index = 0;
			for(int i = 0; i < split1.length; i++) {
				res[index] = split1[i];
				index++;
			}
			for(int i = 0; i < split2.length; i++) {
				res[index] = split2[i];
				index++;
			}
			return res;
		} else {
			String[] res = new String[split1.length + split2.length-1];
			int index = 0;
			for(int i = 0; i < split1.length; i++) {
				res[index] = split1[i];
				index++;
			}
			res[index-1] = res[index-1] + "\n" + split2[0];
			for(int i = 1; i < split2.length; i++) {
				res[index] = split2[i];
				index++;
			}
			return res;
		}
	}

	public static HashMap<Long, Integer> readContactLijn(File file, HashMap<Integer, SpoedNr> spoedNrs) throws FileNotFoundException, IOException {
		HashMap<Long, Integer> contactToSpoedNr = new HashMap<>();
		int added = 0;
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				split = correctContactLijn(split);
				int spoedNr = Integer.parseInt(split[3]);
				if(!spoedNrs.containsKey(spoedNr)) {
					spoedNrs.put(spoedNr, new SpoedNr(spoedNr));
					added++;
				}
				ContactLijn c = new ContactLijn(split);
				spoedNrs.get(spoedNr).addContactLijn(c);
				contactToSpoedNr.put(c.getContactID(), spoedNr);
				//				System.out.println(line);
				i++;
			}
		}
		System.out.println("Loaded " + i + " contactlijnen...");
		System.out.println("Added " + added + " spoedNrs...");
		return contactToSpoedNr;
	}

	private static String[] correctContactLijn(String[] split) {
		String[] tmp = split;
		while(split.length > 6) {
			tmp = new String[split.length-1];
			for(int k = 0; k < 3; k++)
				tmp[k] = split[k];
			tmp[2] += ";" + split[3];
			for(int k = 4; k < split.length; k++)
				tmp[k-1] = split[k];
			split = tmp;
		}
		return tmp;
	}

	public static ArrayList<DashboardZone> readDashboardZone(File file) throws FileNotFoundException, IOException {
		ArrayList<DashboardZone> zones = new ArrayList<>();
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				DashboardZone dz = new DashboardZone(split);
				zones.add(dz);
				i++;
			}
		}
		System.out.println("Loaded " + i + " dashboard zones...");
		return zones;
	}

	public static ArrayList<DashboardManchester> readDashboardManchester(File file) throws FileNotFoundException, IOException {
		ArrayList<DashboardManchester> ms = new ArrayList<>();
		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				DashboardManchester dm = new DashboardManchester(split);
				//				manc.add(dm.getId());
				ms.add(dm);
				i++;
			}
		}
		System.out.println("Loaded " + i + " dashboard manchesters...");
		//		boolean b1 = ficheIds.containsAll(manc);
		//		boolean b2 = manc.containsAll(ficheIds);
		//		boolean b3 = vraagIds.containsAll(manc);
		//		boolean b4 = manc.containsAll(vraagIds);
		//		int minManc = -1;
		//		int maxManc = -1;
		//		for(int x : manc) {
		//			if(minManc == -1 || minManc > x)
		//				minManc = x;
		//			if(maxManc == -1 || maxManc < x)
		//				maxManc = x;
		//		}
		//		int minFicheIds = -1;
		//		int maxFicheIds = -1;
		//		for(int x : ficheIds) {
		//			if(minFicheIds == -1 || minFicheIds > x)
		//				minFicheIds = x;
		//			if(maxFicheIds == -1 || maxFicheIds < x)
		//				maxFicheIds = x;
		//		}
		//		int minVraagIds = -1;
		//		int maxVraagIds = -1;
		//		for(int x : vraagIds) {
		//			if(minVraagIds == -1 || minVraagIds > x)
		//				minVraagIds = x;
		//			if(maxVraagIds == -1 || maxVraagIds < x)
		//				maxVraagIds = x;
		//		}
		return ms;
	}

	public static void readContactData(File file, HashMap<Integer, SpoedNr> spoedNrs, HashMap<Long, Integer> contactToSpoedNr) throws FileNotFoundException, IOException {
		//		//TODO: to create smaller file...
		//		BufferedWriter bufWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file.getParentFile(), "contactD.csv"))));

		int i = 0;
		boolean keepReading = true;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"))) {
			for(String line; (line = br.readLine()) != null && keepReading; ) {
				if(i == 0 && line.contains(";")) {
					String tmp = line.substring(0, line.indexOf(";")).trim();
					boolean skip = false;
					for(String s : FIRST_COLUMN_NAMES)
						if(s.equalsIgnoreCase(tmp)) {
							skip = true;
							break;
						}
					if(skip) {
						i++;
						continue;
					}
				}
				line = clean(line);
				String lineEnc = encode(line);
				String[] split = decode(lineEnc.split(";"));
				split = correctData(split);
				if(split.length != 3
						|| Long.parseLong(split[0].trim()) < 0)
					throw new IllegalArgumentException();
				long cID = Long.parseLong(split[0].trim());
				Integer sNr = contactToSpoedNr.get(cID);
				if(sNr == null) {
					counter_unknownContactID++;
					System.out.println("Unknown contactID(" + counter_unknownContactID + "): " + line);
				} else {
					SpoedNr s = spoedNrs.get(sNr);
					boolean found = false;
					for(ContactLijn cl : s.getContactLijnen())
						if(cl.getContactID() == cID) {
							found = true;
							cl.addContactData(split);

							//							//TODO: to create smaller file...
							//							String newLine = split[0] + ";;" + split[2];
							//							bufWrite.write(newLine);
							//							bufWrite.newLine();

						}
					if(!found)
						throw new IllegalArgumentException();
				}
				i++;
			}
		}
		//		bufWrite.close();
		System.out.println("Loaded " + i + " contact data...");
	}

	private static String[] correctData(String[] split) {
		if(split.length > 3) {
			String[] tmp = new String[3];
			tmp[0] = split[0];
			tmp[2] = split[split.length-1];
			tmp[1] = split[1];
			for(int i = 2; i < split.length-1; i++)
				tmp[1] += ";" + split[i];
			split = tmp;
		}
		return split;
	}
}