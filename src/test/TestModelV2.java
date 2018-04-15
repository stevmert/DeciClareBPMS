package test;

public class TestModelV2 {

	//	public static void main(String[] args) {
	//		test();
	//		//		for(Constraint c : test())
	//		//			System.out.println(c.getTextRepresentation());
	//	}
	//
	//	private static ArrayList<Constraint> test() {
	//		ArrayList<Constraint> model = new ArrayList<>();
	//		int i = 0;
	//
	//		//resources
	//		ArrayList<Resource> resources = new ArrayList<>();
	//		ResourceRole nurse_scrub = new ResourceRole("scrub nurse");//TODO
	//		ResourceRole nurse_or = new ResourceRole("OR nurse");
	//		ResourceRole nurse = new ResourceRole("nurse", nurse_scrub, nurse_or);
	//		resources.add(nurse);
	//		ResourceRole surgeon = new ResourceRole("orthopedic surgeon");
	//		ResourceRole anesthesiologist = new ResourceRole("anesthesiologist");
	//		ResourceRole erDoc = new ResourceRole("emergency doctor");
	//		ResourceRole doctor = new ResourceRole("doctor", surgeon, anesthesiologist, erDoc);
	//		resources.add(doctor);
	//		ResourceRole receptionist = new ResourceRole("receptionist");
	//		resources.add(receptionist);
	//		//		resources.add(surgeon);
	//		ResourceRole surgicalAss = new ResourceRole("surgical assistant");
	//		resources.add(surgicalAss);
	//		//		resources.add(anesthesiologist);
	//		ResourceRole receptiondesk = new ResourceRole("reception desk");
	//		resources.add(receptiondesk);
	//		ResourceRole examroom = new ResourceRole("examination room");
	//		resources.add(examroom);
	//		ResourceRole xRayroom = new ResourceRole("X-ray room");
	//		resources.add(xRayroom);
	//		ResourceRole cTroom = new ResourceRole("CT room");
	//		resources.add(cTroom);
	//		ResourceRole or = new ResourceRole("operating room");
	//		resources.add(or);
	//		//		ResourceRole patientbed = new ResourceRole("patient bed");
	//		//		resources.add(patientbed);//TODO: patient kan opgenomen worden (bij oudere of 'hulploze')
	//		ResourceRole recoverybed = new ResourceRole("recovery room bed");
	//		resources.add(recoverybed);
	//
	//		//activities
	//		ArrayList<Activity> acts = new ArrayList<>();
	//		Activity register = new Activity("Register patient");
	//		acts.add(register);
	//		Activity medHis = new Activity("Take medical history of patient");
	//		acts.add(medHis);
	//		Activity clExamine = new Activity("Clinical examination of patient");
	//		acts.add(clExamine);
	//		Activity consult = new Activity("Doctor consultation");
	//		acts.add(consult);
	//		Activity xRay = new Activity("Take X-ray");
	//		acts.add(xRay);
	//		Activity ct = new Activity("Take CT scan");
	//		acts.add(ct);
	//		Activity painkillersA = new Activity("Prescribe painkillers A");
	//		acts.add(painkillersA);
	//		Activity painkillersB = new Activity("Prescribe painkillers B");
	//		acts.add(painkillersB);
	//		Activity anticoagulants = new Activity("Prescribe anticoagulants");
	//		acts.add(anticoagulants);
	//		Activity stomachProt = new Activity("Prescribe stomach protecting drug");
	//		acts.add(stomachProt);
	//		Activity noTreatment = new Activity("Prescribe rest (=no treatment)");
	//		acts.add(noTreatment);
	//		Activity physiotherapy = new Activity("Prescribe physiotherapy");
	//		acts.add(physiotherapy);
	//		Activity ice = new Activity("Apply ice");
	//		acts.add(ice);
	//		Activity cast = new Activity("Apply cast");
	//		acts.add(cast);
	//		Activity remCast = new Activity("Remove cast");
	//		acts.add(remCast);
	//		Activity corkSpl = new Activity("Apply cork splint");
	//		acts.add(corkSpl);
	//		Activity sling = new Activity("Apply sling");
	//		acts.add(sling);
	//		Activity fixation = new Activity("Apply fixation");
	//		acts.add(fixation);
	//		Activity remFixation = new Activity("Remove fixation");
	//		acts.add(remFixation);
	//		Activity bandage = new Activity("Apply bandage");
	//		acts.add(bandage);
	//		Activity bandageFig8 = new Activity("Apply figure of eight bandage");
	//		acts.add(bandageFig8);
	//		Activity surgery = new Activity("Perform surgery");
	//		acts.add(surgery);
	//		Activity presMon = new Activity("Apply intra-compartmental pressure monitor");
	//		acts.add(presMon);
	//		Activity rest = new Activity("Let patient rest");
	//		acts.add(rest);
	//		Activity unregister = new Activity("Unregister patient");
	//		acts.add(unregister);
	//
	//		//data values
	//		ArrayList<DataAttribute> dataElems = new ArrayList<>();
	//		BooleanDataAttribute isEmergencyT = new BooleanDataAttribute("Is life-threatening emergency?", true);
	//		BooleanDataAttribute isEmergencyF = new BooleanDataAttribute(isEmergencyT.getName(), false);
	//		dataElems.add(isEmergencyF);
	//		BooleanDataAttribute isConsciousT = new BooleanDataAttribute("Is conscious?", true);
	//		BooleanDataAttribute isConsciousF = new BooleanDataAttribute(isConsciousT.getName(), false);
	//		dataElems.add(isConsciousF);
	//		BooleanDataAttribute isComplicationsT = new BooleanDataAttribute("Where there complications during surgery?", true);
	//		BooleanDataAttribute isComplicationsF = new BooleanDataAttribute(isComplicationsT.getName(), false);
	//		dataElems.add(isComplicationsF);
	//		BooleanDataAttribute isOpenComplexT = new BooleanDataAttribute("Open or complex fracture?", true);
	//		BooleanDataAttribute isOpenComplexF = new BooleanDataAttribute(isOpenComplexT.getName(), false);
	//		dataElems.add(isOpenComplexF);
	//		BooleanDataAttribute isExtensiveT = new BooleanDataAttribute("Extensive damage to arteries or nerves?", true);
	//		BooleanDataAttribute isExtensiveF = new BooleanDataAttribute(isExtensiveT.getName(), false);
	//		dataElems.add(isExtensiveF);
	//		BooleanDataAttribute isMore14T = new BooleanDataAttribute("More than 14yrs old?", true);
	//		BooleanDataAttribute isMore14F = new BooleanDataAttribute(isMore14T.getName(), false);
	//		dataElems.add(isMore14F);
	//		BooleanDataAttribute isORAvailableT = new BooleanDataAttribute("OR available?", true);
	//		BooleanDataAttribute isORAvailableF = new BooleanDataAttribute(isORAvailableT.getName(), false);
	//		dataElems.add(isORAvailableF);
	//		BooleanDataAttribute isFarApartT = new BooleanDataAttribute("Bone parts far apart?", true);
	//		BooleanDataAttribute isFarApartF = new BooleanDataAttribute(isFarApartT.getName(), false);
	//		dataElems.add(isFarApartF);
	//		BooleanDataAttribute isInPainT = new BooleanDataAttribute("In pain?", true);
	//		BooleanDataAttribute isInPainF = new BooleanDataAttribute(isInPainT.getName(), false);
	//		dataElems.add(isInPainF);
	//		BooleanDataAttribute isOnAnticoagT = new BooleanDataAttribute("On anticoagulants?", true);
	//		BooleanDataAttribute isOnAnticoagF = new BooleanDataAttribute(isOnAnticoagT.getName(), false);
	//		dataElems.add(isOnAnticoagF);
	//		BooleanDataAttribute isOnAntiInflamT = new BooleanDataAttribute("On anti-inflammatory drugs?", true);
	//		BooleanDataAttribute isOnAntiInflamF = new BooleanDataAttribute(isOnAntiInflamT.getName(), false);
	//		dataElems.add(isOnAntiInflamF);
	//		BooleanDataAttribute isSwollenT = new BooleanDataAttribute("Is swollen?", true);
	//		BooleanDataAttribute isSwollenF = new BooleanDataAttribute(isSwollenT.getName(), false);
	//		dataElems.add(isSwollenF);
	//		BooleanDataAttribute isNumbT = new BooleanDataAttribute("Numbness?", true);
	//		BooleanDataAttribute isNumbF = new BooleanDataAttribute(isNumbT.getName(), false);
	//		dataElems.add(isNumbF);
	//		BooleanDataAttribute isPinsT = new BooleanDataAttribute("Pin-and-needles?", true);
	//		BooleanDataAttribute isPinsF = new BooleanDataAttribute(isPinsT.getName(), false);
	//		dataElems.add(isPinsF);
	//		BooleanDataAttribute isCompartT = new BooleanDataAttribute("Has compartment syndrome?", true);
	//		BooleanDataAttribute isCompartF = new BooleanDataAttribute(isCompartT.getName(), false);
	//		dataElems.add(isCompartF);
	//		BooleanDataAttribute isAcCompartT = new BooleanDataAttribute("Has acute compartment syndrome?", true);
	//		BooleanDataAttribute isAcCompartF = new BooleanDataAttribute(isAcCompartT.getName(), false);
	//		dataElems.add(isAcCompartF);
	//		BooleanDataAttribute isInCastT = new BooleanDataAttribute("Has cast?", true);
	//		BooleanDataAttribute isInCastF = new BooleanDataAttribute(isInCastT.getName(), false);
	//		dataElems.add(isInCastF);
	//		BooleanDataAttribute isInFixT = new BooleanDataAttribute("Has fixation?", true);
	//		BooleanDataAttribute isInFixF = new BooleanDataAttribute(isInFixT.getName(), false);
	//		dataElems.add(isInFixF);
	//		BooleanDataAttribute isNeedsSurgeryT = new BooleanDataAttribute("Needs surgery?", true);
	//		BooleanDataAttribute isNeedsSurgeryF = new BooleanDataAttribute(isNeedsSurgeryT.getName(), false);
	//		dataElems.add(isNeedsSurgeryF);
	//		ArrayList<String> values = new ArrayList<>();
	//		values.add("Forearm");//14
	//		values.add("Wrist");//15
	//		values.add("Finger");//16
	//		values.add("Hand");//17
	//		values.add("Upper arm");//18
	//		values.add("Shoulder");//19
	//		values.add("Collarbone");//20
	//		HashSet<String> values_hash = new HashSet<>(values);
	//		CategoricalDataAttribute isForearm = new CategoricalDataAttribute("Fracture?", values_hash, values.get(0));
	//		CategoricalDataAttribute isWrist = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(1));
	//		CategoricalDataAttribute isFinger = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(2));
	//		CategoricalDataAttribute isHand = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(3));
	//		CategoricalDataAttribute isUpperarm = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(4));
	//		CategoricalDataAttribute isShoulder = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(5));
	//		CategoricalDataAttribute isCollarbone = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(6));
	//		dataElems.add(isCollarbone);
	//		CategoricalDataAttribute isForearmSuspect = new CategoricalDataAttribute("Suspected fracture?", values_hash, values.get(0));
	//		CategoricalDataAttribute isWristSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(1));
	//		CategoricalDataAttribute isFingerSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(2));
	//		CategoricalDataAttribute isHandSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(3));
	//		CategoricalDataAttribute isUpperarmSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(4));
	//		CategoricalDataAttribute isShoulderSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(5));
	//		CategoricalDataAttribute isCollarboneSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(6));
	//		dataElems.add(isCollarboneSuspect);
	//
	//		System.out.println("Available resources: " + resources);
	//		System.out.println();
	//		System.out.println("Available activities: " + acts);
	//		System.out.println();
	//		System.out.print("Available data elements: [");
	//		boolean addComma = false;
	//		for(DataAttribute de : dataElems) {
	//			if(addComma)
	//				System.out.print(", ");
	//			else
	//				addComma = true;
	//			if(de instanceof BooleanDataAttribute)
	//				System.out.print(de.getName() + "=(True/False)");
	//			else {
	//				CategoricalDataAttribute cde = (CategoricalDataAttribute) de;
	//				String res = null;
	//				for(String v : ((CategoricalDataAttribute) de).getValues()) {
	//					if(res != null)
	//						res += "/" + v;
	//					else
	//						res = v;
	//				}
	//				System.out.print(cde.getName() + "=(" + res + ")");
	//			}
	//		}
	//		System.out.print("]");
	//		System.out.println();
	//		System.out.println();
	//
	//		//constraints
	//		model.add(new AtLeastAvailable(null, resExpr(receptionist), 1, false));
	//		model.add(new AtMostAvailable(null, resExpr(receptionist), 2, false));
	//		model.add(new AtLeastAvailable(null, resExpr(nurse), 4, true));
	//		model.add(new AtMostAvailable(null, resExpr(nurse), 10, false));
	//		model.add(new AtLeastAvailable(null, resExpr(doctor), 2, false));
	//		model.add(new AtLeastAvailable(null, resExpr(erDoc), 1, true));
	//		model.add(new AtMostAvailable(null, resExpr(erDoc), 5, false));
	//		model.add(new AtLeastAvailable(null, resExpr(anesthesiologist), 1, true));
	//		model.add(new AtMostAvailable(null, resExpr(anesthesiologist), 2, false));
	//		model.add(new AtLeastAvailable(null, resExpr(surgeon), 1, true));
	//		model.add(new AtMostAvailable(null, resExpr(surgeon), 3, false));
	//		model.add(new AtLeastAvailable(null, resExpr(surgicalAss), 1, true));
	//		model.add(new AtMostAvailable(null, resExpr(surgicalAss), 4, false));
	//		model.add(new AtLeastAvailable(null, resExpr(receptiondesk), 1, false));
	//		model.add(new AtMostAvailable(null, resExpr(receptiondesk), 2, false));
	//		model.add(new AtLeastAvailable(null, resExpr(examroom), 3, true));
	//		model.add(new AtMostAvailable(null, resExpr(examroom), 15, false));
	//		model.add(new AtLeastAvailable(null, resExpr(or), 1, false));
	//		model.add(new AtMostAvailable(null, resExpr(or), 3, false));
	//		model.add(new AtLeastAvailable(null, resExpr(xRayroom), 1, true));
	//		model.add(new AtMostAvailable(null, resExpr(xRayroom), 2, false));
	//		model.add(new AtMostAvailable(null, resExpr(cTroom), 1, false));
	//		model.add(new AtLeastAvailable(null, resExpr(recoverybed), 2, true));
	//		model.add(new AtMostAvailable(null, resExpr(recoverybed), 10, false));
	//
	//		model.add(new ActivityAuthorization(null, resExpr(receptionist), register, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(register), resExpr(receptionist), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(register), resExpr(receptionist), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(register), resExpr(receptiondesk), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(register), resExpr(receptiondesk), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(receptionist), unregister, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(unregister), resExpr(receptionist), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(unregister), resExpr(receptionist), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(unregister), resExpr(receptiondesk), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(unregister), resExpr(receptiondesk), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(false, nurse, doctor), medHis, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(medHis), resExpr(false, nurse, doctor), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), clExamine, true, false));
	//		model.add(new AtLeastUsage(getDecision(isEmergencyF), actExpr(clExamine), resExpr(examroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(clExamine), resExpr(doctor), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), consult, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(consult), resExpr(examroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(consult), resExpr(doctor), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), xRay, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(xRay), resExpr(xRayroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(xRay), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(xRay), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), ct, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(ct), resExpr(cTroom), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(ct), resExpr(cTroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(ct), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(ct), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), cast, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(cast), resExpr(examroom), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(cast), resExpr(examroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(cast), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(cast), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), remCast, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(remCast), resExpr(examroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remCast), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remCast), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), fixation, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(fixation), resExpr(or), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(fixation), resExpr(or), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(fixation), resExpr(surgeon), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(fixation), resExpr(surgicalAss), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(fixation), resExpr(anesthesiologist), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), remFixation, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(remFixation), resExpr(or), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(remFixation), resExpr(or), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remFixation), resExpr(surgeon), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remFixation), resExpr(surgicalAss), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remFixation), resExpr(anesthesiologist), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(remFixation), resExpr(nurse), 2, false));//TODO: scrub & OR
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), bandageFig8, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(bandageFig8), resExpr(examroom), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(bandageFig8), resExpr(examroom), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(bandageFig8), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(bandageFig8), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(false, doctor, nurse), bandage, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(bandage), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(bandage), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(false, doctor, nurse), corkSpl, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(corkSpl), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(corkSpl), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(false, doctor, nurse), sling, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(sling), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(sling), resExpr(nurse), 1, true));
	//		model.add(new ActivityAuthorization(null, resExpr(false, doctor, nurse), ice, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), surgery, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(surgery), resExpr(or), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(surgery), resExpr(or), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(surgery), resExpr(surgeon), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(surgery), resExpr(surgicalAss), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(surgery), resExpr(anesthesiologist), 1, false));
	//		model.add(new AtLeastUsage(null, actExpr(surgery), resExpr(nurse), 2, false));//TODO: scrub & OR
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), presMon, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(presMon), resExpr(false, doctor, nurse), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), rest, true, false));
	//		model.add(new AtLeastUsage(null, actExpr(rest), resExpr(recoverybed), 1, false));
	//		model.add(new AtMostUsage(null, actExpr(rest), resExpr(recoverybed), 1, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), physiotherapy, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), painkillersA, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), painkillersB, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), anticoagulants, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), stomachProt, true, false));
	//		model.add(new ActivityAuthorization(null, resExpr(doctor), noTreatment, true, false));
	//
	//
	//		System.out.println("Resource constraints:");
	//		for(; i < model.size(); i++)
	//			System.out.println(model.get(i).getTextRepresentation());
	//		//			System.out.println(model.get(i));
	//		System.out.println();
	//
	//
	//		model.add(new Init(getDecision(isEmergencyF), actExpr(register), false));
	//		model.add(new Last(null, actExpr(unregister), false));
	//		model.add(new AtLeast(null, actExpr(register), 1, 0, -1, false));
	//		model.add(new AtMost(null, actExpr(register), 1, 0, -1, false));
	//		model.add(new AtLeast(null, actExpr(unregister), 1, 0, -1, false));
	//		model.add(new AtMost(null, actExpr(unregister), 1, 0, -1, false));
	//		model.add(new AtLeast(null, actExpr(clExamine), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(isEmergencyF), actExpr(xRay), 1, 0, -1, false));
	//		model.add(new AtLeastChoice(null, actExpr(false, noTreatment, painkillersA, painkillersB, ice, corkSpl,
	//				cast, sling, fixation, bandage, bandageFig8, surgery), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(isConsciousT), actExpr(medHis), 1, 0, -1, false));
	//		model.add(new Precedence(getDecision(isConsciousT), actExpr(medHis), actExpr(clExamine), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(consult), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(xRay), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(ct), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(painkillersA), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(painkillersB), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(anticoagulants), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(stomachProt), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(cast), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(fixation), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(bandageFig8), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(surgery), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(clExamine), actExpr(physiotherapy), 0, -1, true, false));
	//		model.add(new Response(null, actExpr(xRay), actExpr(consult),
	//				0, -1, true, false));
	//		model.add(new Response(null, actExpr(ct), actExpr(consult),
	//				0, -1, true, false));
	//		model.add(new AtLeastLag(null, actExpr(painkillersA),
	//				actExpr(anticoagulants), 60*60*24*5, false));//5days
	//		model.add(new AtLeastLag(null, actExpr(anticoagulants),
	//				actExpr(painkillersA), 60*60*24*5, false));//5days
	//		model.add(new Response(null, actExpr(cast), actExpr(remCast), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(cast), actExpr(remCast), 0, -1, true, false));
	//		model.add(new Response(null, actExpr(fixation), actExpr(remFixation), 0, -1, true, false));
	//		model.add(new Precedence(null, actExpr(fixation), actExpr(remFixation), 0, -1, true, false));
	//		model.add(new AtLeast(getDecision(isSwollenT), actExpr(ice), 1, 0, -1, true));
	//		model.add(new Response(getDecision(isComplicationsF), actExpr(surgery), actExpr(rest), 0, 60*5, true, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isFinger)),
	//				actExpr(false, bandage, fixation), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isHand)),
	//				actExpr(false, fixation, corkSpl, cast), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isUpperarm)),
	//				actExpr(false, sling, cast), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isMore14T, isWrist),
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isShoulder),
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isForearm)),
	//				actExpr(false, cast, corkSpl), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexT, isORAvailableF, isWrist),
	//				getDecisionRule(isOpenComplexT, isORAvailableF, isForearm),
	//				getDecisionRule(isExtensiveT, isORAvailableF, isWrist),
	//				getDecisionRule(isExtensiveT, isORAvailableF, isForearm)),
	//				actExpr(false, cast, corkSpl), 2, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexF, isExtensiveF, isCollarbone)),
	//				actExpr(false, bandageFig8, corkSpl), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isCollarbone)),
	//				actExpr(sling), 1, 0, -1, true));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isOpenComplexT),
	//				getDecisionRule(isExtensiveT)),
	//				actExpr(surgery), 1, 0, -1, false));
	//		model.add(new Response(getDecision(
	//				getDecisionRule(isOpenComplexT, isWrist),
	//				getDecisionRule(isOpenComplexT, isForearm),
	//				getDecisionRule(isExtensiveT, isWrist),
	//				getDecisionRule(isExtensiveT, isForearm)),
	//				actExpr(surgery), actExpr(false, cast, corkSpl), 0, -1, true, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isFinger),
	//				getDecisionRule(isHand),
	//				getDecisionRule(isWrist),
	//				getDecisionRule(isForearm),
	//				getDecisionRule(isUpperarm),
	//				getDecisionRule(isShoulder)),
	//				actExpr(sling), 1, 0, -1, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isHandSuspect, isInPainT, isSwollenT, isPinsT),
	//				getDecisionRule(isForearmSuspect, isInPainT, isSwollenT, isPinsT),
	//				getDecisionRule(isUpperarmSuspect, isInPainT, isSwollenT, isPinsT)),
	//				actExpr(presMon), 1, 0, 60*30, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isInCastT, isCompartT)),
	//				actExpr(remCast), 1, 0, 60*15, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isInFixT, isCompartT)),
	//				actExpr(remFixation), 1, 0, 60*15, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isAcCompartT)),
	//				actExpr(surgery), 1, 0, 60*60, false));
	//		model.add(new Response(getDecision(
	//				getDecisionRule(isInPainT, isOnAnticoagF, isOnAntiInflamF)),
	//				actExpr(false, clExamine, consult, surgery), actExpr(painkillersA), 0, -1, true, false));
	//		model.add(new Response(getDecision(
	//				getDecisionRule(isInPainT, isOnAnticoagT),
	//				getDecisionRule(isInPainT, isOnAntiInflamT)),
	//				actExpr(false, clExamine, consult, surgery), actExpr(painkillersB), 0, -1, true, false));
	//		model.add(new Response(null, actExpr(surgery),
	//				actExpr(true, anticoagulants, painkillersB), 0, 60*60*4, true, false));//4hours
	//		model.add(new ChainResponse(null, actExpr(anticoagulants), actExpr(stomachProt), 0, -1, true, true));
	//		model.add(new Precedence(getDecision(
	//				getDecisionRule(isEmergencyF, isFingerSuspect),
	//				getDecisionRule(isEmergencyF, isHandSuspect),
	//				getDecisionRule(isEmergencyF, isWristSuspect),
	//				getDecisionRule(isEmergencyF, isForearmSuspect),
	//				getDecisionRule(isEmergencyF, isUpperarmSuspect),
	//				getDecisionRule(isEmergencyF, isCollarboneSuspect)),
	//				actExpr(xRay), actExpr(false, cast, fixation, bandageFig8, surgery), 0, -1, true, false));
	//		model.add(new AtLeast(getDecision(
	//				getDecisionRule(isShoulderSuspect),
	//				getDecisionRule(isShoulder, isNeedsSurgeryF)),
	//				actExpr(ct), 1, 0, -1, false));
	//		model.add(new Precedence(getDecision(
	//				getDecisionRule(isEmergencyF, isShoulderSuspect)),
	//				actExpr(false, xRay, ct), actExpr(surgery), 0, -1, true, false));
	//		model.add(new Response(null,
	//				actExpr(cast), actExpr(false, xRay, surgery), 0, -1, true, false));
	//		model.add(new Response(getDecision(isShoulder),
	//				actExpr(surgery), actExpr(physiotherapy), 0, -1, true, false));
	//		model.add(new Response(null,
	//				actExpr(surgery), actExpr(physiotherapy), 0, -1, true, true));
	//
	//		System.out.println("Control-flow constraints:");
	//		for(; i < model.size(); i++)
	//			System.out.println(model.get(i).getTextRepresentation());
	//		//			System.out.println(model.get(i));
	//
	//		return model;
	//	}
	//
	//	public static Decision getDecision(DataAttribute... dElems) {
	//		HashSet<DataAttribute> v = new HashSet<>();
	//		HashSet<DecisionRule> r = new HashSet<>();
	//		for(DataAttribute de : dElems)
	//			v.add(de);
	//		r.add(new DecisionRule(v));
	//		return new Decision(r);
	//	}
	//
	//	public static Decision getDecision(DecisionRule... rules) {
	//		HashSet<DecisionRule> r = new HashSet<>();
	//		for(DecisionRule dr : rules)
	//			r.add(dr);
	//		return new Decision(r);
	//	}
	//
	//	public static DecisionRule getDecisionRule(DataAttribute... dElems) {
	//		HashSet<DataAttribute> v = new HashSet<>();
	//		for(DataAttribute de : dElems)
	//			v.add(de);
	//		return new DecisionRule(v);
	//	}
	//
	//	public static HashSet<Resource> getResources(Resource... rs) {
	//		HashSet<Resource> v = new HashSet<>();
	//		for(Resource r : rs)
	//			v.add(r);
	//		return v;
	//	}
	//
	//	public static AtomicActivityExpression actExpr(Activity activity) {
	//		return new AtomicActivityExpression(activity);
	//	}
	//
	//	public static ActivityExpression actExpr(boolean isAnd, Activity... activities) {
	//		if(activities.length == 0)
	//			throw new IllegalArgumentException();
	//		if(activities.length == 1)
	//			return actExpr(activities[0]);
	//		ActivityExpression[] exprs = new ActivityExpression[activities.length];
	//		for(int i = 0; i < activities.length; i++)
	//			exprs[i] = actExpr(activities[i]);
	//		return new NonAtomicActivityExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	//	}
	//
	//	public static ActivityExpression actExpr(boolean isAnd, ActivityExpression... exprs) {
	//		return new NonAtomicActivityExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	//	}
	//
	//	public static ResourceExpression resExpr(Resource resource) {
	//		return new AtomicResourceExpression(resource);
	//	}
	//
	//	public static ResourceExpression resExpr(boolean isAnd, Resource... resources) {
	//		if(resources.length == 0)
	//			throw new IllegalArgumentException();
	//		if(resources.length == 1)
	//			return resExpr(resources[0]);
	//		ResourceExpression[] exprs = new ResourceExpression[resources.length];
	//		for(int i = 0; i < resources.length; i++)
	//			exprs[i] = resExpr(resources[i]);
	//		return new NonAtomicResourceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	//	}
	//
	//	public static NonAtomicResourceExpression resExpr(boolean isAnd, ResourceExpression... exprs) {
	//		return new NonAtomicResourceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	//	}
}