package test.azmm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import miner.log.ActivityEvent;
import miner.log.DataEvent;
import miner.log.Log;
import miner.log.ResourceEvent;
import miner.log.Trace;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.data.DataRecord;
import util.FileManager;
import util.StringManager;
import util.xml.ParentNode;
import util.xml.XML;

public class LogMaker {

	public static void main(String[] args) throws Exception {
		createXES_extended2(new File(FileManager.getCurrentDirectory().getParentFile(), "DeciClareMinerV11_copy"),
				"azmmSpoed" + System.currentTimeMillis(), getLog());
	}

	public static Log getLog() throws Exception {
		//		File path = new File("E://Datasets//AZMM Spoedgevallendienst//data//");
		File path = new File("data");
		//eerste lijn begint telkens met een onzichtbaar teken dat als "?" gezien wordt (enkel als kolomnamen niet weggeknipt hoefden te worden) -> wegknippen
		String[] fileNames = {"contactdata",//0
				"contactlijnen",//1
				"dashboard_triage",//2
				"dashboardmanchester",//3
				"dashboardzones",//4
				"opname_ontslag",//5
				"rx",//6
				"SpoedAfdeling_wachttijden",//7
				"users"//8
		};
		File[] files = new File[fileNames.length];
		for(int i = 0; i < files.length; i++)
			files[i] = new File(path, fileNames[i] + ".csv");
		//read
		HashMap<Integer, User> availableResources = Reader.readResources(files[8]);
		HashMap<Integer, SpoedNr> spoedNrs = Reader.readOpnames(files[5]);
		Reader.readRX(files[6], spoedNrs);
		Reader.readWachttijden(files[7], spoedNrs);
		Reader.readTriage(files[2], spoedNrs);
		HashMap<Long, Integer> contactToSpoedNr = Reader.readContactLijn(files[1], spoedNrs);
		ArrayList<DashboardZone> dzs = Reader.readDashboardZone(files[4]);
		ArrayList<DashboardManchester> dms = Reader.readDashboardManchester(files[3]);
		Reader.readContactData(files[0], spoedNrs, contactToSpoedNr);
		//create log
		return new Log("AZMM Spoed", createLog(spoedNrs, availableResources, dzs, dms));
	}

	private static final DataRecord opnameDossier = new DataRecord("Opname Dossier");
	private static final HashSet<String> values_opnameTypes = new HashSet<>();
	static {
		for(OpnameType t : OpnameType.values())
			values_opnameTypes.add(t.name());
	}
	private static final CategoricalDataAttribute opnameType_hosp = new CategoricalDataAttribute("Opname type?", values_opnameTypes, OpnameType.GEHOSPITALISEERD.toString(), opnameDossier);
	private static final CategoricalDataAttribute opnameType_amb = new CategoricalDataAttribute("Opname type?", values_opnameTypes, OpnameType.AMBULANT.toString(), opnameDossier);
	private static final CategoricalDataAttribute opnameType_dagz = new CategoricalDataAttribute("Opname type?", values_opnameTypes, OpnameType.DAGZIEKENHUIS.toString(), opnameDossier);
	private static final ArrayList<CategoricalDataAttribute> opnameTypes = new ArrayList<>();
	static {
		opnameTypes.add(opnameType_hosp);
		opnameTypes.add(opnameType_amb);
		opnameTypes.add(opnameType_dagz);
	}
	private static final ArrayList<CategoricalDataAttribute> zvvnm_zvnam_combis = new ArrayList<>();
	private static final HashSet<String> values_zvvnm_zvnam_combis = new HashSet<>();
	static {
		for(String s : new String[]{
				"MM VGIND-GOET-CAST-BILL-VOOS",
				"ISABELLE AERS",
				"STEFAN DESMYTER",
				"BARBARA VLIEGHE",
				"MM Endocrinologie",
				"IZ NEURO ASSOCIATIE",
				"FABRICE ROGGE",
				"CATHY VAN GAEVER",
				"PATRICK VAN BOUCHAUTE",
				"IZ GASTRO-ENTERO ASSOCIATIE",
				". ANESTHESIE",
				"LIESBETH DE GROOTE",
				"SABINE CASTRO",
				". Van Overschelde - Goossens",
				"FREDERIK MORTIER",
				"LOTTE NIEUWENHUIS",
				"IZ ENDOCRINO ASSOCIATIE",
				". Neurochirurgie",
				"TOM VANDEKERCKHOVE",
				"CAMPUS MM Oncologie-Hematologie",
				"ANNICK DE CRAENE",
				"CLAUDIA DAELS",
				"Campus MM MAAG-,DARM-,LEVERZIEKTEN",
				"Campus MM OFTALMO",
				". VERHAEGHE-WITTERS-PLATTEAU-HOORNAERT",
				". CARDIOLOGIE MM",
				"JOOST LAGAST",
				"VERONIQUE DE MAEYER",
				"Lieven Otte",
				"Campus MM DEGRA-DHAESE-VLIEGHE-JACO-GOETH",
				"LUC ALGOED",
				"Associatie Stomatologie",
				"DIRK VAN DEN BROECKE",
				". NEFROLOGIE",
				"PAUL VERSTRAETEN",
				". LONGZIEKTEN",
				"BEATRICE LAGAE",
				"Campus MM HEELKUNDE CARDIALE CHIR",
				"PETER BURSSENS",
				"PETER GABRIEL",
				". KINDERARTSEN",
				"neus-keel-oor Associatie",
				"PAUL VANHOUTEGHEM",
				"IZ PNEUMO ASSOCIATIE",
				"Associatie Fysio - neuro",
				". UROLOGIE MM",
				"IZ NEFROLOGIE ASSOCIATIE",
				"IZ ONCO T'KINDT - DE COCK - SCHURGERS",
				"IZ CARDIO ASSOCIATIE",
				"Campus MM TRO-WIT-LOO-BYN-VIL",
				"KATHLEEN PODEVYN",
				"Campus MM HEELKUNDE VAATCHIRURGIE",
				"Campus MM RAEMDONCK-CLAEYS",
				"Campus MM TEMMERM-V.AUTRYVE-DHONDT",
				"MM Geriatrie-ortho",
				"Associatie Fysio - ortho",
				"GERIATRIE MM Geriatrie-Endo-Onco-Hemato",
				"RUDOLF VERTRIEST",
				"Olivier Stevens",
				"ANN GOBERT",
				"Campus MM HEELKUNDE ALGEMEEN",
				"BENOIT DE CORDIER",
				"MIEKE MOERMAN",
				"JORIS BLEYEN"
		}) {
			s = s.toLowerCase().trim();
			values_zvvnm_zvnam_combis.add(s);
			zvvnm_zvnam_combis.add(new CategoricalDataAttribute("zvvnm zvnam?", values_zvvnm_zvnam_combis, s, opnameDossier));
		}
	}
	//TODO: afdelingCode als CategoricalDataAttribute -> alle mogelijke afdelingen enumereren
	//TODO: mogelijk-overlappende en niet-overlappende CategoricalDataAttribute!
	private static final DataRecord triageRapport = new DataRecord("Triage Rapport");
	private static final BooleanDataAttribute isRampT = new BooleanDataAttribute("Is slachtoffer van ramp?", true, triageRapport);
	private static final BooleanDataAttribute isRampF = new BooleanDataAttribute(isRampT.getName(), false, isRampT.getParent());
	private static final BooleanDataAttribute isBedT = new BooleanDataAttribute("Patient heeft bed nodig?", true, triageRapport);
	private static final BooleanDataAttribute isBedF = new BooleanDataAttribute(isBedT.getName(), false, isBedT.getParent());
	private static final BooleanDataAttribute isNtAanwzgT = new BooleanDataAttribute("Patient niet aanwezig?", true, triageRapport);
	private static final BooleanDataAttribute isNtAanwzgF = new BooleanDataAttribute(isNtAanwzgT.getName(), false, isNtAanwzgT.getParent());
	private static final HashSet<String> values_triageKleur = new HashSet<>();//TODO: kleuren? en wat met null?
	static {
		values_triageKleur.add("Wit");
		values_triageKleur.add("Groen");
		values_triageKleur.add("Oranje");
		values_triageKleur.add("Rood");
	}
	private static final CategoricalDataAttribute triageKleur1 = new CategoricalDataAttribute("Triage kleur?", values_triageKleur, "Wit", triageRapport);
	private static final CategoricalDataAttribute triageKleur2 = new CategoricalDataAttribute(triageKleur1.getName(), values_triageKleur, "Groen", triageKleur1.getParent());
	private static final CategoricalDataAttribute triageKleur3 = new CategoricalDataAttribute(triageKleur1.getName(), values_triageKleur, "Oranje", triageKleur1.getParent());
	private static final CategoricalDataAttribute triageKleur4 = new CategoricalDataAttribute(triageKleur1.getName(), values_triageKleur, "Rood", triageKleur1.getParent());
	private static final ArrayList<CategoricalDataAttribute> afdelingCodes_tr = new ArrayList<>();
	private static final HashSet<String> values_afdelingCodes_tr = new HashSet<>();
	static {
		for(String s : new String[]{
				"A101",
				"A102",
				"A201",
				"A202",
				"A203",
				"A262",
				"A301",
				"C005",
				"C113",
				"D202",
				"D203",
				"D301",
				"D302",
				"D303",
				"D401",
				"D402",
				"D402MC",
				"D403",
				"D501",
				"D502",
				"D503",
				"D601",
				"D602",
				"D603",
				"E103",
				"E104CU",
				"E104U1",
				"E104U2",
				"E104U3"
		}) {
			s = s.toLowerCase().trim();
			values_afdelingCodes_tr.add(s);
			afdelingCodes_tr.add(new CategoricalDataAttribute("afdeling code?", values_afdelingCodes_tr, s, triageRapport));
		}
	}
	private static final BooleanDataAttribute isZwangerT = new BooleanDataAttribute("Is zwanger?", true, triageRapport);
	private static final BooleanDataAttribute isZwangerF = new BooleanDataAttribute(isZwangerT.getName(), false, isZwangerT.getParent());
	private static final ArrayList<CategoricalDataAttribute> opmerking_tr = new ArrayList<>();
	private static final HashSet<String> values_opmerking_tr = new HashSet<>();
	static {
		for(String s : new String[]{
				"pijn",
				"rx",
				"wonde",
				"voet",
				"enkel",
				"been",
				"pols",
				"ct",
				"knie",
				"trauma",
				"koorts",
				"vinger",
				"echo",
				"allergie",
				"hoofd",
				"schouder",
				"ontsteking",
				"hand",
				"labo",
				"oog",
				"arm",
				"fractuur",
				"rug",
				"elleboog",
				"kanker",
				"bloed",
				"infuus",
				"ureg",
				"urinair",
				"fibrillatie",
				"braken",
				"buikstreek",
				"crp",
				"heup",
				"abdomen",
				"operatie",
				"teen",
				"dauer",
				"gips",
				"snij",
				"neuro",
				"neus",
				"zwelling",
				"cardio",
				"diarree",
				"hersenen",
				"dyspnoe",
				"verward",
				"kine",
				"depressie",
				"thorax",
				"penicilline",
				"copd",
				"bloeddruk",
				"nek",
				"hardhorig",
				"ekg",
				"gezicht",
				"wondzorg",
				"roodheid",
				"augmentin",
				"mri",
				"mmse",
				"pijnstilling",
				"dafalgan",
				"dementie",
				"nsaids",
				"bewustzijn",
				"mrsa",
				"hoest",
				"hematoom",
				"zwanger",
				"amlor",
				"temperatuur",
				"abces",
				"chemo",
				"parkinson",
				"chronisch",
				"nuchter",
				"palliatief",
		}) {
			s = s.toLowerCase().trim();
			values_opmerking_tr.add(s);
			opmerking_tr.add(new CategoricalDataAttribute("triage opmerking?", values_opmerking_tr, s, triageRapport));
		}
	}
	//TODO: zvnam als CategoricalDataAttribute -> alle mogelijke afdelingen enumereren (of null)
	private static final DataRecord patientDossier = new DataRecord("Patient Dossier");
	private static final ArrayList<CategoricalDataAttribute> afdelingCodes_cl = new ArrayList<>();
	private static final HashSet<String> values_afdelingCodes_cl = new HashSet<>();
	static {
		for(String s : new String[]{
				"klinische biologie",
				"radiologie",
				"pediatrie",
				"kinesitherapie",
				"predialyse coach",
				"orl",
				"stomatologie",
				"oftalmologie",
				"bewegingstherapeut paaz",
				"pall dienst",
				"heelkunde",
				"psy dienst",
				"cardiologie",
				"wondzorg",
				"urologie",
				"vpl interne liaison g",
				"anesthesie",
				"dermatologie",
				"logopedie",
				"ambulante diabetesvpl",
				"nefrologie",
				"fysio revalidatie",
				"nucl geneeskunde",
				"endocrinologie",
				"orthopedie",
				"neurologie",
				"reumatologie",
				"soc dienst",
				"onco hematologie",
				"neuropsychiatrie",
				"diabetesconsulent",
				"ergotherapie",
				"neurochirurgie",
				"pastorale dienst",
				"iz",
				"algologisch vpl",
				"plastische heelkunde",
				"podologie",
				"urozorg",
				"dietisten",
				"geriatrie",
				"pneumologie",
				"oncocoach",
				"spoed",
				"kinderverpleegpost",
				"gynaecologie",
				"gastro enterologie",
				"copd vpl"
		}) {
			s = s.toLowerCase().trim();
			values_afdelingCodes_cl.add(s);
			afdelingCodes_cl.add(new CategoricalDataAttribute("afdeling contact?", values_afdelingCodes_cl, s, patientDossier));
		}
	}
	//TODO: dienst van contact als CategoricalDataAttribute -> alle mogelijke afdelingen enumereren
	//TODO: arts als CategoricalDataAttribute??? -> alle mogelijke afdelingen enumereren
	private static final DataRecord rxRapport = new DataRecord("RX");

	private static ArrayList<Trace> createLog(HashMap<Integer, SpoedNr> spoedNrs, HashMap<Integer, User> availableResources,
			ArrayList<DashboardZone> dzs, ArrayList<DashboardManchester> dms) {
		ArrayList<Trace> log = new ArrayList<>();
		for(SpoedNr s : spoedNrs.values())
			if(!s.getOpnames().isEmpty()
					&& !s.getContactLijnen().isEmpty()
					&& !s.getTriages().isEmpty()) {
				log.add(createTrace(s));
			} else {
				Reader.counter_spoedNrSkipped++;
				System.out.println("Skipped(" + Reader.counter_spoedNrSkipped + "): " + s.getSpoedNr());
			}
		printStats(log);
		return log;
	}

	private static void printStats(ArrayList<Trace> log) {
		System.out.println("##########################################################################");
		System.out.println("#traces;" + log.size());
		System.out.println("counter_unknownContactID;" + Reader.counter_unknownContactID);
		System.out.println("counter_encodingProblem;" + Reader.counter_encodingProblem);
		System.out.println("counter_dataProblem;" + Reader.counter_dataProblem);
		System.out.println("counter_contactlijnError;" + Reader.counter_contactlijnError);
		System.out.println("counter_spoedNrSkipped;" + Reader.counter_spoedNrSkipped);
		HashMap<String, Long> activityCounter = new HashMap<>();
		for(Trace t : log)
			for(ActivityEvent e : t.getActivityEvents()) {
				if(activityCounter.containsKey(e.getName()))
					activityCounter.put(e.getName(), activityCounter.get(e.getName())+1l);
				else
					activityCounter.put(e.getName(), 1l);
			}
		System.out.println("Aantal activiteiten;" + activityCounter.keySet().size());
		HashMap<DataAttribute, Long> dataCounter = new HashMap<>();
		HashSet<String> dataAttributeCounter = new HashSet<>();
		for(Trace t : log)
			for(DataEvent e : t.getDataEvents()) {
				if(dataCounter.containsKey(e.getDataElement()))
					dataCounter.put(e.getDataElement(), dataCounter.get(e.getDataElement())+1l);
				else
					dataCounter.put(e.getDataElement(), 1l);
			}
		BigInteger nrOfCombinations = BigInteger.ONE;
		for(DataAttribute d : dataCounter.keySet())
			if(dataAttributeCounter.add(d.getName())) {
				if(d instanceof BooleanDataAttribute)
					nrOfCombinations = nrOfCombinations.multiply(BigInteger.valueOf(3));
				else
					nrOfCombinations = nrOfCombinations.multiply(BigInteger.valueOf(2).pow(((CategoricalDataAttribute) d).getValues().size()));
			}
		System.out.println("Aantal data attributen;" + dataAttributeCounter.size());
		System.out.println("Aantal data waarden;" + dataCounter.keySet().size());
		System.out.println("Aantal mogelijke data combinaties;" + nrOfCombinations);
		System.out.println("###########################################################################");
		for(String k : activityCounter.keySet())
			System.out.println(k + ";" + activityCounter.get(k));
		System.out.println("###########################################################################");
		for(DataAttribute k : dataCounter.keySet())
			System.out.println(k + ";" + dataCounter.get(k));
	}

	private static Trace createTrace(SpoedNr s) {
		ArrayList<ActivityEvent> activityEvents = new ArrayList<>();
		ArrayList<DataEvent> dataEvents = new ArrayList<>();
		//opname
		Opname_Ontslag eerste = null;
		for(Opname_Ontslag o : s.getOpnames())
			if(eerste == null || eerste.getOpnameDatum().isAfter(o.getOpnameDatum()))
				eerste = o;
		LocalDateTime baselineDateTime = eerste.getOpnameDatum();
		if(eerste.getType().name().equals(opnameType_amb.getValue()))//check of triage in ziekenwagen...
			for(Triage t : s.getTriages())
				if(t.getCreateDate().isBefore(baselineDateTime))
					baselineDateTime = t.getCreateDate();
		long eersteDiff = getTimeDiff(baselineDateTime, eerste.getOpnameDatum());
		activityEvents.add(new ActivityEvent("Opname", eersteDiff));
		for(CategoricalDataAttribute c : opnameTypes)
			if(eerste.getType().name().equals(c.getValue())) {
				dataEvents.add(new DataEvent(c, true, eersteDiff));
				break;
			}
		for(CategoricalDataAttribute c : zvvnm_zvnam_combis)
			if((eerste.getZvvnm() + " " + eerste.getZvnam()).equalsIgnoreCase(c.getValue())) {
				dataEvents.add(new DataEvent(c, true, eersteDiff));
				break;
			}
		if(eerste.getOntslagDatum() != null)
			activityEvents.add(new ActivityEvent("Ontslag", getTimeDiff(baselineDateTime, eerste.getOntslagDatum())));
		for(Opname_Ontslag o : s.getOpnames())
			if(o.getTransferDatum() != null && !o.getTransferDatum().equals(eerste.getTransferDatum())) {
				long ttt = getTimeDiff(baselineDateTime, o.getTransferDatum());
				activityEvents.add(new ActivityEvent("Transfer", ttt));
				for(CategoricalDataAttribute c : opnameTypes)
					if(o.getType().name().equals(c.getValue())) {
						dataEvents.add(new DataEvent(c, true, ttt));
						break;
					}
				for(CategoricalDataAttribute c : zvvnm_zvnam_combis)
					if((o.getZvvnm() + " " + o.getZvnam()).equalsIgnoreCase(c.getValue())) {
						dataEvents.add(new DataEvent(c, true, ttt));
						break;
					}
			}
		//triage
		for(Triage t : s.getTriages()) {
			if(t.getHerzienDoorArtsDate() != null && !t.getHerzienDoorArtsDate().equals(t.getGezienDoorArtsDate())
					&& t.getLnkHertriageDate() != null && !t.getLnkHertriageDate().equals(t.getCreateDate())) {
				long time = getTimeDiff(baselineDateTime, t.getLnkHertriageDate());
				activityEvents.add(new ActivityEvent("Hertriage", time));//TODO: is hertriage echt iets anders of gewoon Triage van maken?
				if(t.getRamp() != null)
					dataEvents.add(new DataEvent(t.getRamp()?isRampT:isRampF, true, time));
				dataEvents.add(new DataEvent(t.getBedNr()==null?isBedF:isBedT, true, time));
				dataEvents.add(new DataEvent(t.getNietAanwezig()==null || !t.getNietAanwezig()?isNtAanwzgF:isNtAanwzgT, true, time));
				if(t.getLnkHertriageKleur() != null) {
					if(t.getLnkHertriageKleur() == 1)
						dataEvents.add(new DataEvent(triageKleur1, true, time));
					else if(t.getLnkHertriageKleur() == 2)
						dataEvents.add(new DataEvent(triageKleur2, true, time));
					else if(t.getLnkHertriageKleur() == 3)
						dataEvents.add(new DataEvent(triageKleur3, true, time));
					else
						dataEvents.add(new DataEvent(triageKleur4, true, time));
				}
			}
			long time = getTimeDiff(baselineDateTime, t.getCreateDate());
			activityEvents.add(new ActivityEvent("Triage", time));
			if(t.getRamp() != null)
				dataEvents.add(new DataEvent(t.getRamp()?isRampT:isRampF, true, time));
			dataEvents.add(new DataEvent(t.getBedNr()==null?isBedF:isBedT, true, time));
			dataEvents.add(new DataEvent(t.getNietAanwezig()==null || !t.getNietAanwezig()?isNtAanwzgF:isNtAanwzgT, true, time));
			if(t.getTriageKleur() != null) {
				if(t.getTriageKleur() == 1)
					dataEvents.add(new DataEvent(triageKleur1, true, time));
				else if(t.getTriageKleur() == 2)
					dataEvents.add(new DataEvent(triageKleur2, true, time));
				else if(t.getTriageKleur() == 3)
					dataEvents.add(new DataEvent(triageKleur3, true, time));
				else
					dataEvents.add(new DataEvent(triageKleur4, true, time));
			}
			for(CategoricalDataAttribute c : afdelingCodes_tr)
				if((t.getAfdelingNr()).equalsIgnoreCase(c.getValue())) {
					dataEvents.add(new DataEvent(c, true, time));
					break;
				}
			if(t.getOpmerking() != null)
				for(CategoricalDataAttribute c : opmerking_tr)
					if(Arrays.asList(t.getOpmerking()).contains(c.getValue()))
						dataEvents.add(new DataEvent(c, true, time));
			//TODO: weken/maanden/fases???
			dataEvents.add(new DataEvent(t.getVermoedelijkeBevallingsdatum()==null?isZwangerF:isZwangerT, true, time));

			//			ArrayList<LocalDateTime> ts = new ArrayList<>();//TODO
			//			ts.add(t.getAandachtspuntenLastUpdateDate());//0
			//			ts.add(t.getAfdelingAankomst());					//=baselineDateTime of null?
			//			ts.add(t.getAfdelingVerwittigdDate());//2
			//			ts.add(t.getBedAangevraagdDate());					//=activiteit?
			//			ts.add(t.getBedAankomst()); 						//-> hoe kan dit na getBedAangevraagdDate???
			//			ts.add(t.getBedToegewezenDate());//5				//null, hoewel vorige 2 niet-null
			//			ts.add(t.getCreateDate());							//=na baselineDateTime! Is dit dan wanneer triage echt start?
			//			ts.add(t.getDiagnoseLastUpdateDate());//7			//null of veel later
			//			ts.add(t.getGezienDoorArtsDate());					//ver na 1 en 6. Is dit dan wanneer triage start/eindigd? soms null!
			//			ts.add(t.getHerzienDoorArtsDate());
			//			ts.add(t.getKostenLastUpdateDate());//10
			//			ts.add(t.getLastUpdateDate());						//nog eens een uur na 8. Is dit dan wanneer triage eindigd?
			//			ts.add(t.getLnkHertriageDate());
			//			ts.add(t.getVermoedelijkeBevallingsdatum());//13
			//			"".length();
		}
		//contactlijnen
		for(ContactLijn cl : s.getContactLijnen())
			for(ContactData c : cl.getContactData()) {
				if(c.getContactVak() == null) {
					Reader.counter_contactlijnError++;
					System.out.println("Error in contactlijn(" + Reader.counter_contactlijnError + "): " + c + " for " + s);
				} else {
					long time = getTimeDiff(baselineDateTime, cl.getContactDatum());
					String name;
					if(c.getContactVak().contains("+"))//Tentatieve diagnose + planning
						name = c.getContactVak().substring(0, c.getContactVak().indexOf("+")).trim();
					else if(c.getContactVak().contains("coloscopie"))//Linker coloscopie
						name = "Coloscopie";
					else if(c.getContactVak().contains("Sociaal"))//Sociaal
						name = "Sociale dienst";
					else
						name = c.getContactVak();
					activityEvents.add(new ActivityEvent(name, time));
					for(CategoricalDataAttribute cda : afdelingCodes_cl)
						if(StringManager.clean_complete(cl.getDISnaam()).equals(cda.getValue())) {
							dataEvents.add(new DataEvent(cda, true, time));
							break;
						}
				}
			}
		//TODO: wachttijden??? Wat zijn deze? Ook transfers? Aparte activiteit of deel van een andere transfer ofzo? Andere naam?
		for(Wachtijden w : s.getWachtijden()) {
			if(w.isActief()) {
				//TODO: welke tijd? w.getCreateDate() / w.getDatumResponsAfdeling() / w.getLastUpdateDate()
				long diff = getTimeDiff(baselineDateTime, w.getDatumResponsAfdeling());
				//				if(w.getTijdstipAfhalen() == null)//TODO: correct?
				activityEvents.add(new ActivityEvent("Transfer met wachttijd", diff));
				//				else
				//					activityEvents.add(new ActivityEvent("Transfer met wachttijd", diff, diff+w.getTijdstipAfhalen()));//TODO: 'diff, diff+w.getTijdstipAfhalen()' correct (probleem met countDiff!!!)? of 'diff-w.getTijdstipAfhalen(), diff'?
				//TODO: text mining voor reden als data attribuut?
			}
		}
		//TODO: RXen
		for(RX r : s.getRXs()) {//TODO: text mining... + wat met tijd (geen uur of minuten!!!)??? Af te leiden van transfers op een of andere manier???
			long t1 = Math.max(0l, getTimeDiff(baselineDateTime, r.getDateTime()));
			long t2 = getTimeDiff(baselineDateTime, r.getDateTime().plusDays(1));
			//			ActivityEvent rx = new ActivityEvent("RX", getTimeDiff(baselineDateTime, r.getDateTime()));
			"".length();
			//				activityEvents.add(new ActivityEvent("Transfer", getTimeDiff(baselineDateTime, o.getTransferDatum())));
		}
		Collections.sort(activityEvents);//TODO: als triage en transfer op zelfde tijdstip, welke is eerste gebeurd? (bv. 25)
		correctSort(activityEvents);
		Collections.sort(dataEvents);
		cleanRedundant(dataEvents);
		ArrayList<ResourceEvent> resourceEvents = new ArrayList<>();
		//		Collections.sort(resourceEvents);
		return new Trace(activityEvents, dataEvents, resourceEvents);
	}

	private static void correctSort(ArrayList<ActivityEvent> activityEvents) {
		//		for(int i1 = 0; i1 < activityEvents.size()-1; i1++) {
		//			ArrayList<ActivityEvent> sameTime = new ArrayList<>();
		//			for(int i2 = i1+1; i2 < activityEvents.size(); i2++)
		//				if(activityEvents.get(i1).getStart() == activityEvents.get(i2).getStart())
		//					sameTime.add(activityEvents.get(i2));
		//			if(!sameTime.isEmpty()) {
		//				sameTime.add(0, activityEvents.get(i1));
		//				if(sameTime.size() == 2
		//						&& sameTime.get(0).getName().equals("Transfer")
		//						&& sameTime.get(1).getName().equals("Triage")) {//TODO: is dit correct?
		//					Collections.swap(activityEvents, i1, i1+1);
		//				} else if(sameTime.size() == 2
		//						&& sameTime.get(1).getName().equals("Transfer")
		//						&& sameTime.get(0).getName().equals("Triage")) {}//correct
		//				else//TODO?
		//					"".length();
		//			}
		//		}
	}

	private static void cleanRedundant(ArrayList<DataEvent> dataEvents) {
		HashSet<String> currentState_elems = new HashSet<>();
		HashSet<DataAttribute> currentState = new HashSet<>();
		for(int i = 0; i < dataEvents.size(); i++) {
			DataAttribute da = dataEvents.get(i).getDataElement();
			if(currentState.contains(da)) {//remove
				dataEvents.remove(i);
				i--;
			} else if(da instanceof BooleanDataAttribute
					&& currentState_elems.contains(da.getName())) {//change
				for(DataAttribute x : currentState)
					if(x.getName().equals(da.getName())) {
						currentState.remove(x);
						break;
					}
				currentState.add(da);
			} else {
				currentState.add(da);
				currentState_elems.add(da.getName());
			}
		}
	}

	private static long getTimeDiff(LocalDateTime t1, LocalDateTime t2) {
		//		//TODO: minuten?
		//		long secs = ChronoUnit.SECONDS.between(t1, t2);
		////		long mins = ChronoUnit.MINUTES.between(t1, t2);
		//		long mins = secs / 60;
		//		if(secs % 60 > 0)
		//			mins++;
		//		return mins;
		//TODO: seconden?
		return ChronoUnit.SECONDS.between(t1, t2);
	}

	private static void createXES_extended2(File minerDir, String filename, Log log) {
		try {
			XML xml = XML.parse(FileManager.readAll(new File(minerDir, "miner/xes_templates/templateXES_extended2.xes")));
			ParentNode logNode = (ParentNode) xml.getNodes().get(xml.getNodes().size()-1);
			for(Trace t : log) {
				ParentNode newNode = t.getXesNode_extended2();
				logNode.getChildNodes().add(newNode);
			}
			FileWriter fw = new FileWriter(new File(new File(minerDir, "models"), filename + ".xes"));
			xml.exportXML(new BufferedWriter(fw));
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}