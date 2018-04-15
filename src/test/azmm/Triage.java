package test.azmm;

import java.time.LocalDateTime;
import java.util.Arrays;

import test.azmm.textmining.WordProcessor;

//TODO: integer of boolean?
public class Triage extends ReadableItem {
	private int lnkOpnameAfdelingID;//1 //76 tot 118 (TODO: link met dashboardzones?)
	private String afdelingNr;
	//	private int lnkOpnameAfdelingID;//dubbel
	//	private String lnkPersoneelID;//TODO: altijd null?
	private Integer canule;//5 //TODO: altijd 0 of null? -> is blijkbaar een ingebrachte luchtbuis, is 0=true en null=false??? 71442keer 0, 31keer null
	//	private String canuleBeginDatum;//TODO: altijd null?
	//	private String canuleEindDatum;//TODO: altijd null?
	private LocalDateTime vermoedelijkeBevallingsdatum;//vaak null uiteraard
	private String[] opmerking;//text mining...
	private boolean kostenAfgewerkt;//10 //TODO: of de patient betaald heeft? -> niet echt nuttig dan
	private LocalDateTime lastUpdateDate;
	private int lnkLastUpdateUserID;
	private LocalDateTime aandachtspuntenLastUpdateDate;
	private Integer aandachtspuntenLastUpdateUserID;
	private LocalDateTime diagnoseLastUpdateDate;//15
	private Integer lnkDiagnosesLastUpdateUserID;
	private LocalDateTime kostenLastUpdateDate;
	private Integer lnkKostenLastUpdateUserID;
	private LocalDateTime createDate;
	private int lnkCreateUserID;//20 //TODO: arts/verpleger koppelen?
	//	private boolean actief;//altijd true
	private Integer icpcAanmelding1;//TODO: altijd 0 of null //=International Classification of Primary Care? iets van doorverwijzing van huisarts?
	private Integer icpcAanmelding2;//TODO: altijd 0 of null //=International Classification of Primary Care? iets van doorverwijzing van huisarts?
	private Integer icpcDiagnose1;//TODO: altijd 0 of null //=International Classification of Primary Care? iets van doorverwijzing van huisarts?
	private Boolean nietAanwezig;//25 //TODO: kan de patient afwezig zijn bij triage???
	private Integer nietAanwezigReden;//TODO: naam lijkt niet overeen te komen met datatype... of staan de nummers voor vaste redenen? -> welke?
	private Integer triage;//TODO: altijd 0 of null //TODO: naam lijkt niet overeen te komen met datatype of is 0=true en null=false???
	private LocalDateTime afdelingAankomst;
	private Integer lnkZoneID;//TODO: link naar dashboardzones?
	private LocalDateTime zoneAankomst;//30
	private Integer kamer;
	private Integer bedNr;//0 tot 12 of null
	private LocalDateTime bedAankomst;
	private LocalDateTime bedAangevraagdDate;
	private LocalDateTime bedToegewezenDate;//35
	private LocalDateTime afdelingVerwittigdDate;
	private Boolean ramp;//TODO: al dan niet slachtoffer van gekende ramp?
	private Integer rampID;//0, 3646, 3647 of null //TODO: zonder info over de ramp, is dit waarschijnlijk nutteloos. Tenzij we exacte rampID in decision logic toelaten...
	//	private String ontslag;//TODO: altijd null?
	private Boolean afgehaald;//40 //TODO: door andere dienst, binnen spoed of naar huis?
	private Integer lnkTriageUserID;//TODO: is dit verpleegkundige/dokter?
	private LocalDateTime triageDate;
	private Integer triageKleur;//1, 2, 3, 4 of null //TODO: betekenis van cijfers en null
	private Integer lnkTriageFicheID;//TODO: naar wat linkt dit???
	private Integer lnkTriageVraagID;//45 //TODO: naar wat linkt dit???
	private Integer lnkHertriageUserID;//TODO idem
	private LocalDateTime lnkHertriageDate;
	private Integer lnkHertriageKleur;//2, 3, 4 of null //TODO idem
	private Integer lnkHertriageFicheID;//582, 996, 1270, 1310 of null //TODO idem
	private Integer lnkHertriageVraagID;//50 //602, 1031, 1289, 1332 of null //TODO idem
	private Integer lnkGezienDoorArtsUserID;//TODO: id van arts die triage deed of wat? Wat is verschil met lnkTriageUserID?
	private LocalDateTime gezienDoorArtsDate;
	private LocalDateTime herzienDoorArtsDate;
	private Integer lnkHerzienDoorArtsUserID;//111, 194, 2382, 2695 of null //TODO idem
	private String[] opnamereden;//55 //TODO text mining...

	public Triage(int lnkOpnameAfdelingID, String afdelingNr, Integer canule,
			LocalDateTime vermoedelijkeBevallingsdatum, String opmerking, boolean kostenAfgewerkt,
			LocalDateTime lastUpdateDate, int lnkLastUpdateUserID, LocalDateTime aandachtspuntenLastUpdateDate,
			Integer aandachtspuntenLastUpdateUserID, LocalDateTime diagnoseLastUpdateDate,
			Integer lnkDiagnosesLastUpdateUserID, LocalDateTime kostenLastUpdateDate, Integer lnkKostenLastUpdateUserID,
			LocalDateTime createDate, int lnkCreateUserID, Integer icpcAanmelding1, Integer icpcAanmelding2,
			Integer icpcDiagnose1, Boolean nietAanwezig, Integer nietAanwezigReden, Integer triage,
			LocalDateTime afdelingAankomst, Integer lnkZoneID, LocalDateTime zoneAankomst, Integer kamer, Integer bedNr,
			LocalDateTime bedAankomst, LocalDateTime bedAangevraagdDate, LocalDateTime bedToegewezenDate,
			LocalDateTime afdelingVerwittigdDate, Boolean ramp, Integer rampID, Boolean afgehaald,
			Integer lnkTriageUserID, LocalDateTime triageDate, Integer triageKleur, Integer lnkTriageFicheID,
			Integer lnkTriageVraagID, Integer lnkHertriageUserID, LocalDateTime lnkHertriageDate,
			Integer lnkHertriageKleur, Integer lnkHertriageFicheID, Integer lnkHertriageVraagID,
			Integer lnkGezienDoorArtsUserID, LocalDateTime gezienDoorArtsDate, LocalDateTime herzienDoorArtsDate,
			Integer lnkHerzienDoorArtsUserID, String opnamereden) {
		super();
		this.lnkOpnameAfdelingID = lnkOpnameAfdelingID;
		this.afdelingNr = afdelingNr;
		this.canule = canule;
		this.vermoedelijkeBevallingsdatum = vermoedelijkeBevallingsdatum;
		this.opmerking = WordProcessor.getInstance("alg").processText(opmerking);
		this.kostenAfgewerkt = kostenAfgewerkt;
		this.lastUpdateDate = lastUpdateDate;
		this.lnkLastUpdateUserID = lnkLastUpdateUserID;
		this.aandachtspuntenLastUpdateDate = aandachtspuntenLastUpdateDate;
		this.aandachtspuntenLastUpdateUserID = aandachtspuntenLastUpdateUserID;
		this.diagnoseLastUpdateDate = diagnoseLastUpdateDate;
		this.lnkDiagnosesLastUpdateUserID = lnkDiagnosesLastUpdateUserID;
		this.kostenLastUpdateDate = kostenLastUpdateDate;
		this.lnkKostenLastUpdateUserID = lnkKostenLastUpdateUserID;
		this.createDate = createDate;
		this.lnkCreateUserID = lnkCreateUserID;
		this.icpcAanmelding1 = icpcAanmelding1;
		this.icpcAanmelding2 = icpcAanmelding2;
		this.icpcDiagnose1 = icpcDiagnose1;
		this.nietAanwezig = nietAanwezig;
		this.nietAanwezigReden = nietAanwezigReden;
		this.triage = triage;
		this.afdelingAankomst = afdelingAankomst;
		this.lnkZoneID = lnkZoneID;
		this.zoneAankomst = zoneAankomst;
		this.kamer = kamer;
		this.bedNr = bedNr;
		this.bedAankomst = bedAankomst;
		this.bedAangevraagdDate = bedAangevraagdDate;
		this.bedToegewezenDate = bedToegewezenDate;
		this.afdelingVerwittigdDate = afdelingVerwittigdDate;
		this.ramp = ramp;
		this.rampID = rampID;
		this.afgehaald = afgehaald;
		this.lnkTriageUserID = lnkTriageUserID;
		this.triageDate = triageDate;
		this.triageKleur = triageKleur;
		this.lnkTriageFicheID = lnkTriageFicheID;
		this.lnkTriageVraagID = lnkTriageVraagID;
		this.lnkHertriageUserID = lnkHertriageUserID;
		this.lnkHertriageDate = lnkHertriageDate;
		this.lnkHertriageKleur = lnkHertriageKleur;
		this.lnkHertriageFicheID = lnkHertriageFicheID;
		this.lnkHertriageVraagID = lnkHertriageVraagID;
		this.lnkGezienDoorArtsUserID = lnkGezienDoorArtsUserID;
		this.gezienDoorArtsDate = gezienDoorArtsDate;
		this.herzienDoorArtsDate = herzienDoorArtsDate;
		this.lnkHerzienDoorArtsUserID = lnkHerzienDoorArtsUserID;
		this.opnamereden = WordProcessor.getInstance("alg").processText(opnamereden);
	}

	public Triage(String[] splitLine) {
		this((int) parseIntegerSplit(splitLine[1]), parseStringSplit(splitLine[2]), parseIntegerSplit(splitLine[5]),
				parseDateTimeSplit(splitLine[8]), parseStringSplit(splitLine[9]), (boolean) parseBooleanSplit(splitLine[10]),
				parseDateTimeSplit(splitLine[11]), (int) parseIntegerSplit(splitLine[12]), parseDateTimeSplit(splitLine[13]),
				parseIntegerSplit(splitLine[14]), parseDateTimeSplit(splitLine[15]), parseIntegerSplit(splitLine[16]),
				parseDateTimeSplit(splitLine[17]), parseIntegerSplit(splitLine[18]), parseDateTimeSplit(splitLine[19]),
				(int) parseIntegerSplit(splitLine[20]), parseIntegerSplit(splitLine[22]),
				parseIntegerSplit(splitLine[23]), parseIntegerSplit(splitLine[24]), parseBooleanSplit(splitLine[25]),
				parseIntegerSplit(splitLine[26]), parseIntegerSplit(splitLine[27]), parseDateTimeSplit(splitLine[28]),
				parseIntegerSplit(splitLine[29]), parseDateTimeSplit(splitLine[30]), parseIntegerSplit(splitLine[31]),
				parseIntegerSplit(splitLine[32]), parseDateTimeSplit(splitLine[33]), parseDateTimeSplit(splitLine[34]),
				parseDateTimeSplit(splitLine[35]), parseDateTimeSplit(splitLine[36]), parseBooleanSplit(splitLine[37]),
				parseIntegerSplit(splitLine[38]), parseBooleanSplit(splitLine[40]), parseIntegerSplit(splitLine[41]),
				parseDateTimeSplit(splitLine[42]), parseIntegerSplit(splitLine[43]), parseIntegerSplit(splitLine[44]),
				parseIntegerSplit(splitLine[45]), parseIntegerSplit(splitLine[46]), parseDateTimeSplit(splitLine[47]),
				parseIntegerSplit(splitLine[48]), parseIntegerSplit(splitLine[49]), parseIntegerSplit(splitLine[50]),
				parseIntegerSplit(splitLine[51]), parseDateTimeSplit(splitLine[52]), parseDateTimeSplit(splitLine[53]),
				parseIntegerSplit(splitLine[54]), splitLine.length==55?null:parseStringSplit(splitLine[55]));
	}

	public int getLnkOpnameAfdelingID() {
		return lnkOpnameAfdelingID;
	}

	public String getAfdelingNr() {
		return afdelingNr;
	}

	public Integer getCanule() {
		return canule;
	}

	public LocalDateTime getVermoedelijkeBevallingsdatum() {
		return vermoedelijkeBevallingsdatum;
	}

	public String[] getOpmerking() {
		return opmerking;
	}

	public boolean isKostenAfgewerkt() {
		return kostenAfgewerkt;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public int getLnkLastUpdateUserID() {
		return lnkLastUpdateUserID;
	}

	public LocalDateTime getAandachtspuntenLastUpdateDate() {
		return aandachtspuntenLastUpdateDate;
	}

	public Integer getAandachtspuntenLastUpdateUserID() {
		return aandachtspuntenLastUpdateUserID;
	}

	public LocalDateTime getDiagnoseLastUpdateDate() {
		return diagnoseLastUpdateDate;
	}

	public Integer getLnkDiagnosesLastUpdateUserID() {
		return lnkDiagnosesLastUpdateUserID;
	}

	public LocalDateTime getKostenLastUpdateDate() {
		return kostenLastUpdateDate;
	}

	public Integer getLnkKostenLastUpdateUserID() {
		return lnkKostenLastUpdateUserID;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public int getLnkCreateUserID() {
		return lnkCreateUserID;
	}

	public Integer getIcpcAanmelding1() {
		return icpcAanmelding1;
	}

	public Integer getIcpcAanmelding2() {
		return icpcAanmelding2;
	}

	public Integer getIcpcDiagnose1() {
		return icpcDiagnose1;
	}

	public Boolean getNietAanwezig() {
		return nietAanwezig;
	}

	public Integer getNietAanwezigReden() {
		return nietAanwezigReden;
	}

	public Integer getTriage() {
		return triage;
	}

	public LocalDateTime getAfdelingAankomst() {
		return afdelingAankomst;
	}

	public Integer getLnkZoneID() {
		return lnkZoneID;
	}

	public LocalDateTime getZoneAankomst() {
		return zoneAankomst;
	}

	public Integer getKamer() {
		return kamer;
	}

	public Integer getBedNr() {
		return bedNr;
	}

	public LocalDateTime getBedAankomst() {
		return bedAankomst;
	}

	public LocalDateTime getBedAangevraagdDate() {
		return bedAangevraagdDate;
	}

	public LocalDateTime getBedToegewezenDate() {
		return bedToegewezenDate;
	}

	public LocalDateTime getAfdelingVerwittigdDate() {
		return afdelingVerwittigdDate;
	}

	public Boolean getRamp() {
		return ramp;
	}

	public Integer getRampID() {
		return rampID;
	}

	public Boolean getAfgehaald() {
		return afgehaald;
	}

	public Integer getLnkTriageUserID() {
		return lnkTriageUserID;
	}

	public LocalDateTime getTriageDate() {
		return triageDate;
	}

	public Integer getTriageKleur() {
		return triageKleur;
	}

	public Integer getLnkTriageFicheID() {
		return lnkTriageFicheID;
	}

	public Integer getLnkTriageVraagID() {
		return lnkTriageVraagID;
	}

	public Integer getLnkHertriageUserID() {
		return lnkHertriageUserID;
	}

	public LocalDateTime getLnkHertriageDate() {
		return lnkHertriageDate;
	}

	public Integer getLnkHertriageKleur() {
		return lnkHertriageKleur;
	}

	public Integer getLnkHertriageFicheID() {
		return lnkHertriageFicheID;
	}

	public Integer getLnkHertriageVraagID() {
		return lnkHertriageVraagID;
	}

	public Integer getLnkGezienDoorArtsUserID() {
		return lnkGezienDoorArtsUserID;
	}

	public LocalDateTime getGezienDoorArtsDate() {
		return gezienDoorArtsDate;
	}

	public LocalDateTime getHerzienDoorArtsDate() {
		return herzienDoorArtsDate;
	}

	public Integer getLnkHerzienDoorArtsUserID() {
		return lnkHerzienDoorArtsUserID;
	}

	public String[] getOpnamereden() {
		return opnamereden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aandachtspuntenLastUpdateDate == null) ? 0 : aandachtspuntenLastUpdateDate.hashCode());
		result = prime * result
				+ ((aandachtspuntenLastUpdateUserID == null) ? 0 : aandachtspuntenLastUpdateUserID.hashCode());
		result = prime * result + ((afdelingAankomst == null) ? 0 : afdelingAankomst.hashCode());
		result = prime * result + ((afdelingNr == null) ? 0 : afdelingNr.hashCode());
		result = prime * result + ((afdelingVerwittigdDate == null) ? 0 : afdelingVerwittigdDate.hashCode());
		result = prime * result + ((afgehaald == null) ? 0 : afgehaald.hashCode());
		result = prime * result + ((bedAangevraagdDate == null) ? 0 : bedAangevraagdDate.hashCode());
		result = prime * result + ((bedAankomst == null) ? 0 : bedAankomst.hashCode());
		result = prime * result + ((bedNr == null) ? 0 : bedNr.hashCode());
		result = prime * result + ((bedToegewezenDate == null) ? 0 : bedToegewezenDate.hashCode());
		result = prime * result + ((canule == null) ? 0 : canule.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((diagnoseLastUpdateDate == null) ? 0 : diagnoseLastUpdateDate.hashCode());
		result = prime * result + ((gezienDoorArtsDate == null) ? 0 : gezienDoorArtsDate.hashCode());
		result = prime * result + ((herzienDoorArtsDate == null) ? 0 : herzienDoorArtsDate.hashCode());
		result = prime * result + ((icpcAanmelding1 == null) ? 0 : icpcAanmelding1.hashCode());
		result = prime * result + ((icpcAanmelding2 == null) ? 0 : icpcAanmelding2.hashCode());
		result = prime * result + ((icpcDiagnose1 == null) ? 0 : icpcDiagnose1.hashCode());
		result = prime * result + ((kamer == null) ? 0 : kamer.hashCode());
		result = prime * result + (kostenAfgewerkt ? 1231 : 1237);
		result = prime * result + ((kostenLastUpdateDate == null) ? 0 : kostenLastUpdateDate.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + lnkCreateUserID;
		result = prime * result
				+ ((lnkDiagnosesLastUpdateUserID == null) ? 0 : lnkDiagnosesLastUpdateUserID.hashCode());
		result = prime * result + ((lnkGezienDoorArtsUserID == null) ? 0 : lnkGezienDoorArtsUserID.hashCode());
		result = prime * result + ((lnkHertriageDate == null) ? 0 : lnkHertriageDate.hashCode());
		result = prime * result + ((lnkHertriageFicheID == null) ? 0 : lnkHertriageFicheID.hashCode());
		result = prime * result + ((lnkHertriageKleur == null) ? 0 : lnkHertriageKleur.hashCode());
		result = prime * result + ((lnkHertriageUserID == null) ? 0 : lnkHertriageUserID.hashCode());
		result = prime * result + ((lnkHertriageVraagID == null) ? 0 : lnkHertriageVraagID.hashCode());
		result = prime * result + ((lnkHerzienDoorArtsUserID == null) ? 0 : lnkHerzienDoorArtsUserID.hashCode());
		result = prime * result + ((lnkKostenLastUpdateUserID == null) ? 0 : lnkKostenLastUpdateUserID.hashCode());
		result = prime * result + lnkLastUpdateUserID;
		result = prime * result + lnkOpnameAfdelingID;
		result = prime * result + ((lnkTriageFicheID == null) ? 0 : lnkTriageFicheID.hashCode());
		result = prime * result + ((lnkTriageUserID == null) ? 0 : lnkTriageUserID.hashCode());
		result = prime * result + ((lnkTriageVraagID == null) ? 0 : lnkTriageVraagID.hashCode());
		result = prime * result + ((lnkZoneID == null) ? 0 : lnkZoneID.hashCode());
		result = prime * result + ((nietAanwezig == null) ? 0 : nietAanwezig.hashCode());
		result = prime * result + ((nietAanwezigReden == null) ? 0 : nietAanwezigReden.hashCode());
		result = prime * result + Arrays.hashCode(opmerking);
		result = prime * result + Arrays.hashCode(opnamereden);
		result = prime * result + ((ramp == null) ? 0 : ramp.hashCode());
		result = prime * result + ((rampID == null) ? 0 : rampID.hashCode());
		result = prime * result + ((triage == null) ? 0 : triage.hashCode());
		result = prime * result + ((triageDate == null) ? 0 : triageDate.hashCode());
		result = prime * result + ((triageKleur == null) ? 0 : triageKleur.hashCode());
		result = prime * result
				+ ((vermoedelijkeBevallingsdatum == null) ? 0 : vermoedelijkeBevallingsdatum.hashCode());
		result = prime * result + ((zoneAankomst == null) ? 0 : zoneAankomst.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triage other = (Triage) obj;
		if (aandachtspuntenLastUpdateDate == null) {
			if (other.aandachtspuntenLastUpdateDate != null)
				return false;
		} else if (!aandachtspuntenLastUpdateDate.equals(other.aandachtspuntenLastUpdateDate))
			return false;
		if (aandachtspuntenLastUpdateUserID == null) {
			if (other.aandachtspuntenLastUpdateUserID != null)
				return false;
		} else if (!aandachtspuntenLastUpdateUserID.equals(other.aandachtspuntenLastUpdateUserID))
			return false;
		if (afdelingAankomst == null) {
			if (other.afdelingAankomst != null)
				return false;
		} else if (!afdelingAankomst.equals(other.afdelingAankomst))
			return false;
		if (afdelingNr == null) {
			if (other.afdelingNr != null)
				return false;
		} else if (!afdelingNr.equals(other.afdelingNr))
			return false;
		if (afdelingVerwittigdDate == null) {
			if (other.afdelingVerwittigdDate != null)
				return false;
		} else if (!afdelingVerwittigdDate.equals(other.afdelingVerwittigdDate))
			return false;
		if (afgehaald == null) {
			if (other.afgehaald != null)
				return false;
		} else if (!afgehaald.equals(other.afgehaald))
			return false;
		if (bedAangevraagdDate == null) {
			if (other.bedAangevraagdDate != null)
				return false;
		} else if (!bedAangevraagdDate.equals(other.bedAangevraagdDate))
			return false;
		if (bedAankomst == null) {
			if (other.bedAankomst != null)
				return false;
		} else if (!bedAankomst.equals(other.bedAankomst))
			return false;
		if (bedNr == null) {
			if (other.bedNr != null)
				return false;
		} else if (!bedNr.equals(other.bedNr))
			return false;
		if (bedToegewezenDate == null) {
			if (other.bedToegewezenDate != null)
				return false;
		} else if (!bedToegewezenDate.equals(other.bedToegewezenDate))
			return false;
		if (canule == null) {
			if (other.canule != null)
				return false;
		} else if (!canule.equals(other.canule))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (diagnoseLastUpdateDate == null) {
			if (other.diagnoseLastUpdateDate != null)
				return false;
		} else if (!diagnoseLastUpdateDate.equals(other.diagnoseLastUpdateDate))
			return false;
		if (gezienDoorArtsDate == null) {
			if (other.gezienDoorArtsDate != null)
				return false;
		} else if (!gezienDoorArtsDate.equals(other.gezienDoorArtsDate))
			return false;
		if (herzienDoorArtsDate == null) {
			if (other.herzienDoorArtsDate != null)
				return false;
		} else if (!herzienDoorArtsDate.equals(other.herzienDoorArtsDate))
			return false;
		if (icpcAanmelding1 == null) {
			if (other.icpcAanmelding1 != null)
				return false;
		} else if (!icpcAanmelding1.equals(other.icpcAanmelding1))
			return false;
		if (icpcAanmelding2 == null) {
			if (other.icpcAanmelding2 != null)
				return false;
		} else if (!icpcAanmelding2.equals(other.icpcAanmelding2))
			return false;
		if (icpcDiagnose1 == null) {
			if (other.icpcDiagnose1 != null)
				return false;
		} else if (!icpcDiagnose1.equals(other.icpcDiagnose1))
			return false;
		if (kamer == null) {
			if (other.kamer != null)
				return false;
		} else if (!kamer.equals(other.kamer))
			return false;
		if (kostenAfgewerkt != other.kostenAfgewerkt)
			return false;
		if (kostenLastUpdateDate == null) {
			if (other.kostenLastUpdateDate != null)
				return false;
		} else if (!kostenLastUpdateDate.equals(other.kostenLastUpdateDate))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (lnkCreateUserID != other.lnkCreateUserID)
			return false;
		if (lnkDiagnosesLastUpdateUserID == null) {
			if (other.lnkDiagnosesLastUpdateUserID != null)
				return false;
		} else if (!lnkDiagnosesLastUpdateUserID.equals(other.lnkDiagnosesLastUpdateUserID))
			return false;
		if (lnkGezienDoorArtsUserID == null) {
			if (other.lnkGezienDoorArtsUserID != null)
				return false;
		} else if (!lnkGezienDoorArtsUserID.equals(other.lnkGezienDoorArtsUserID))
			return false;
		if (lnkHertriageDate == null) {
			if (other.lnkHertriageDate != null)
				return false;
		} else if (!lnkHertriageDate.equals(other.lnkHertriageDate))
			return false;
		if (lnkHertriageFicheID == null) {
			if (other.lnkHertriageFicheID != null)
				return false;
		} else if (!lnkHertriageFicheID.equals(other.lnkHertriageFicheID))
			return false;
		if (lnkHertriageKleur == null) {
			if (other.lnkHertriageKleur != null)
				return false;
		} else if (!lnkHertriageKleur.equals(other.lnkHertriageKleur))
			return false;
		if (lnkHertriageUserID == null) {
			if (other.lnkHertriageUserID != null)
				return false;
		} else if (!lnkHertriageUserID.equals(other.lnkHertriageUserID))
			return false;
		if (lnkHertriageVraagID == null) {
			if (other.lnkHertriageVraagID != null)
				return false;
		} else if (!lnkHertriageVraagID.equals(other.lnkHertriageVraagID))
			return false;
		if (lnkHerzienDoorArtsUserID == null) {
			if (other.lnkHerzienDoorArtsUserID != null)
				return false;
		} else if (!lnkHerzienDoorArtsUserID.equals(other.lnkHerzienDoorArtsUserID))
			return false;
		if (lnkKostenLastUpdateUserID == null) {
			if (other.lnkKostenLastUpdateUserID != null)
				return false;
		} else if (!lnkKostenLastUpdateUserID.equals(other.lnkKostenLastUpdateUserID))
			return false;
		if (lnkLastUpdateUserID != other.lnkLastUpdateUserID)
			return false;
		if (lnkOpnameAfdelingID != other.lnkOpnameAfdelingID)
			return false;
		if (lnkTriageFicheID == null) {
			if (other.lnkTriageFicheID != null)
				return false;
		} else if (!lnkTriageFicheID.equals(other.lnkTriageFicheID))
			return false;
		if (lnkTriageUserID == null) {
			if (other.lnkTriageUserID != null)
				return false;
		} else if (!lnkTriageUserID.equals(other.lnkTriageUserID))
			return false;
		if (lnkTriageVraagID == null) {
			if (other.lnkTriageVraagID != null)
				return false;
		} else if (!lnkTriageVraagID.equals(other.lnkTriageVraagID))
			return false;
		if (lnkZoneID == null) {
			if (other.lnkZoneID != null)
				return false;
		} else if (!lnkZoneID.equals(other.lnkZoneID))
			return false;
		if (nietAanwezig == null) {
			if (other.nietAanwezig != null)
				return false;
		} else if (!nietAanwezig.equals(other.nietAanwezig))
			return false;
		if (nietAanwezigReden == null) {
			if (other.nietAanwezigReden != null)
				return false;
		} else if (!nietAanwezigReden.equals(other.nietAanwezigReden))
			return false;
		if (!Arrays.equals(opmerking, other.opmerking))
			return false;
		if (!Arrays.equals(opnamereden, other.opnamereden))
			return false;
		if (ramp == null) {
			if (other.ramp != null)
				return false;
		} else if (!ramp.equals(other.ramp))
			return false;
		if (rampID == null) {
			if (other.rampID != null)
				return false;
		} else if (!rampID.equals(other.rampID))
			return false;
		if (triage == null) {
			if (other.triage != null)
				return false;
		} else if (!triage.equals(other.triage))
			return false;
		if (triageDate == null) {
			if (other.triageDate != null)
				return false;
		} else if (!triageDate.equals(other.triageDate))
			return false;
		if (triageKleur == null) {
			if (other.triageKleur != null)
				return false;
		} else if (!triageKleur.equals(other.triageKleur))
			return false;
		if (vermoedelijkeBevallingsdatum == null) {
			if (other.vermoedelijkeBevallingsdatum != null)
				return false;
		} else if (!vermoedelijkeBevallingsdatum.equals(other.vermoedelijkeBevallingsdatum))
			return false;
		if (zoneAankomst == null) {
			if (other.zoneAankomst != null)
				return false;
		} else if (!zoneAankomst.equals(other.zoneAankomst))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Triage [" + createDate + "]";
	}
}