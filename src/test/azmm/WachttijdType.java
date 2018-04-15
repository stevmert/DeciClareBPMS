package test.azmm;

import test.azmm.textmining.WordProcessor;

public enum WachttijdType {
	PATIENT_NOG_NIET_AFGEWERKT_DOOR_ARTS,
	KAMER_NOG_BEZET,
	VERPLEEGKUNDIGE_IS_BEZET,
	KAMER_NOG_NIET_GEPOETST,
	MEDICATIE_AAN_HET_KLAARZETTEN,
	SPOED_BRENGT_PT_ZELF_NAAR_AFDELING,
	AFDELING_KOMT_PT_ONMIDDELLIJK_HALEN,
	LUNCHPAUZE,
	VERPLEEGKUNDIGE_PATIENTENTOER,
	VERPLEEGKUNDIGE_ZONE_IS_NIET_OP_DIENST;
	
	public static WachttijdType parse(String input) {
		String cleaned = WordProcessor.preprocess(input);
		if(cleaned == null)
			return null;
		cleaned = cleaned.toUpperCase().replace(" ", "_");
		try {
			return WachttijdType.valueOf(cleaned);
		} catch(IllegalArgumentException e) {
			System.out.println("Unknown WachttijdType: " + cleaned);
			return null;
		}
	}
}