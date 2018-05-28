package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import model.Activity;
import model.Constraint;
import model.constraint.ExistenceConstraint;
import model.constraint.Negatable;
import model.constraint.data.Deletion;
import model.constraint.data.Insertion;
import model.constraint.data.Read;
import model.constraint.data.Update;
import model.constraint.existence.ActivityAvailabilitySchedule;
import model.constraint.existence.AtLeast;
import model.constraint.existence.AtLeastChoice;
import model.constraint.existence.AtMost;
import model.constraint.existence.AtMostChoice;
import model.constraint.existence.extra.First;
import model.constraint.existence.extra.Last;
import model.constraint.relation.AlternatePrecedence;
import model.constraint.relation.AlternateResponse;
import model.constraint.relation.AtLeastLag;
import model.constraint.relation.AtMostLag;
import model.constraint.relation.ChainPrecedence;
import model.constraint.relation.ChainResponse;
import model.constraint.relation.Precedence;
import model.constraint.relation.RespondedPresence;
import model.constraint.relation.Response;
import model.constraint.resource.ActivityAuthorization;
import model.constraint.resource.AtLeastAvailable;
import model.constraint.resource.AtLeastUsage;
import model.constraint.resource.AtMostAvailable;
import model.constraint.resource.AtMostUsage;
import model.constraint.resource.DecisionAuthorization;
import model.constraint.resource.ResourceAvailabilitySchedule;
import model.constraint.resource.ResourceEquality;
import model.constraint.resource.Resource_ActivityExpressionPair;
import model.constraint.resource.SimultaneousCapacity;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.data.DataAttributeImpl;
import model.data.DataRecord;
import model.data.DataStructure;
import model.data.Decision;
import model.data.DecisionRule;
import model.expression.ActivityExpression;
import model.expression.AtomicActivityExpression;
import model.expression.AtomicDataExpression;
import model.expression.AtomicExistenceExpression;
import model.expression.AtomicResourceExpression;
import model.expression.DataExpression;
import model.expression.ExistenceExpression;
import model.expression.LogicalOperator;
import model.expression.NonAtomicActivityExpression;
import model.expression.NonAtomicDataExpression;
import model.expression.NonAtomicExistenceExpression;
import model.expression.NonAtomicResourceExpression;
import model.expression.ResourceExpression;
import model.expression.ScheduleStatement;
import model.resource.Resource;
import model.resource.ResourceRole;

public class TestModelV3 {

	public static void main(String[] args) {
		getModel();
		//		for(Constraint c : test())
		//			System.out.println(c.getTextRepresentation());
	}

	//	public static void main(String[] args) throws Exception {
	//		String stringToWrite = "";
	//		for(Constraint c : TestModelV3.getModel())
	//			stringToWrite += "\n" + c.toString();
	//		FileManager.writeAll(new File("ArmFractureCase_DeciClareModel.txt"), stringToWrite.trim());
	//	}

	public static ArrayList<Constraint> getModel() {
		ArrayList<Constraint> model = new ArrayList<>();
		int i = 0;

		//resources
		ArrayList<Resource> resources = new ArrayList<>();
		ResourceRole nurse_scrub = new ResourceRole("scrub nurse");
		ResourceRole nurse_or = new ResourceRole("OR nurse", nurse_scrub);
		ResourceRole nurse = new ResourceRole("nurse", nurse_or);
		resources.add(nurse);
		ResourceRole surgeon = new ResourceRole("orthopedic surgeon");
		ResourceRole anesthesiologist = new ResourceRole("anesthesiologist");
		ResourceRole erDoc = new ResourceRole("emergency doctor");
		ResourceRole doctor = new ResourceRole("doctor", surgeon, anesthesiologist, erDoc);
		resources.add(doctor);
		ResourceRole receptionist = new ResourceRole("receptionist");
		resources.add(receptionist);
		ResourceRole surgicalAss = new ResourceRole("surgical assistant");
		resources.add(surgicalAss);
		ResourceRole receptiondesk = new ResourceRole("reception desk");
		resources.add(receptiondesk);
		ResourceRole examroom = new ResourceRole("examination room");
		resources.add(examroom);
		ResourceRole xRayroom = new ResourceRole("X-ray room");
		resources.add(xRayroom);
		ResourceRole cTroom = new ResourceRole("CT room");
		resources.add(cTroom);
		ResourceRole or = new ResourceRole("operating room");
		resources.add(or);
		ResourceRole patientRoom = new ResourceRole("patient room");
		resources.add(patientRoom);
		ResourceRole patientbed = new ResourceRole("patient bed");
		resources.add(patientbed);
		ResourceRole recoveryRoom = new ResourceRole("recovery room");
		resources.add(recoveryRoom);
		ResourceRole recoverybed = new ResourceRole("recovery bed");
		resources.add(recoverybed);

		//activities
		ArrayList<Activity> acts = new ArrayList<>();
		Activity register = new Activity("Register patient");
		acts.add(register);
		Activity medHis = new Activity("Take medical history of patient");
		acts.add(medHis);
		Activity clExamine = new Activity("Clinical examination of patient");
		acts.add(clExamine);
		Activity consult = new Activity("Doctor consultation");
		acts.add(consult);
		Activity xRay = new Activity("Take X-ray");
		acts.add(xRay);
		Activity ct = new Activity("Take CT scan");
		acts.add(ct);
		Activity painkillersA = new Activity("Prescribe NSAID painkillers");
		acts.add(painkillersA);
		Activity painkillersB = new Activity("Prescribe SAID painkillers");
		acts.add(painkillersB);
		Activity anticoagulants = new Activity("Prescribe anticoagulants");
		acts.add(anticoagulants);
		Activity stomachProt = new Activity("Prescribe stomach protecting drug");
		acts.add(stomachProt);
		Activity noTreatment = new Activity("Prescribe rest (=no treatment)");
		acts.add(noTreatment);
		Activity ice = new Activity("Apply ice");
		acts.add(ice);
		Activity cast = new Activity("Apply cast");
		acts.add(cast);
		Activity remCast = new Activity("Remove cast");
		acts.add(remCast);
		Activity splint = new Activity("Apply splint");
		acts.add(splint);
		Activity sling = new Activity("Apply sling");
		acts.add(sling);
		Activity fixation = new Activity("Apply fixation");
		acts.add(fixation);
		Activity remFixation = new Activity("Remove fixation");
		acts.add(remFixation);
		Activity bandage = new Activity("Apply bandage");
		acts.add(bandage);
		Activity bandageFig8 = new Activity("Apply figure of eight bandage");
		acts.add(bandageFig8);
		Activity surgery = new Activity("Perform surgery");
		acts.add(surgery);
		Activity presMon = new Activity("Apply intra-compartmental pressure monitor");
		acts.add(presMon);
		Activity rest = new Activity("Let patient rest");
		acts.add(rest);
		Activity patientStay = new Activity("Stay in patient room");
		acts.add(patientStay);
		Activity unregister = new Activity("Unregister patient");
		acts.add(unregister);

		//DATA STRUCTURE
		//==============
		//stored data values
		ArrayList<DataAttribute> dataElems = new ArrayList<>();
		DataRecord patientFile = new DataRecord("Patient File");
		DataAttributeImpl patientName = new DataAttributeImpl("name", patientFile);
		dataElems.add(patientName);
		DataAttributeImpl patientStatus = new DataAttributeImpl("status", patientFile);
		dataElems.add(patientStatus);

		DataRecord observations = new DataRecord("Real-time observations");
		//observation data values
		BooleanDataAttribute isBeenBeforeT = new BooleanDataAttribute("Patient has been to this hospital before?", true, observations);
		BooleanDataAttribute isBeenBeforeF = new BooleanDataAttribute(isBeenBeforeT.getName(), false, observations);
		dataElems.add(isBeenBeforeF);
		BooleanDataAttribute isEmergencyT = new BooleanDataAttribute("Is life-threatening emergency?", true, observations);
		BooleanDataAttribute isEmergencyF = new BooleanDataAttribute(isEmergencyT.getName(), false, observations);
		dataElems.add(isEmergencyF);
		BooleanDataAttribute isConsciousT = new BooleanDataAttribute("Is conscious?", true, observations);
		BooleanDataAttribute isConsciousF = new BooleanDataAttribute(isConsciousT.getName(), false, observations);
		dataElems.add(isConsciousF);
		BooleanDataAttribute isComplicationsT = new BooleanDataAttribute("Were there complications during surgery?", true, observations);
		BooleanDataAttribute isComplicationsF = new BooleanDataAttribute(isComplicationsT.getName(), false, observations);
		dataElems.add(isComplicationsF);
		BooleanDataAttribute isDisplaceT = new BooleanDataAttribute("Displace fracture?", true, observations);
		BooleanDataAttribute isDisplaceF = new BooleanDataAttribute(isDisplaceT.getName(), false, observations);
		dataElems.add(isDisplaceF);
		BooleanDataAttribute isUnstableT = new BooleanDataAttribute("Unstable fracture?", true, observations);
		BooleanDataAttribute isUnstableF = new BooleanDataAttribute(isUnstableT.getName(), false, observations);
		dataElems.add(isUnstableF);
		BooleanDataAttribute isOpenT = new BooleanDataAttribute("Open fracture?", true, observations);
		BooleanDataAttribute isOpenF = new BooleanDataAttribute(isOpenT.getName(), false, observations);
		dataElems.add(isOpenF);
		BooleanDataAttribute isMore14T = new BooleanDataAttribute("More than 14yrs old?", true, observations);
		BooleanDataAttribute isMore14F = new BooleanDataAttribute(isMore14T.getName(), false, observations);
		dataElems.add(isMore14F);
		BooleanDataAttribute isBedriddenT = new BooleanDataAttribute("Bedridden patient?", true, observations);
		BooleanDataAttribute isBedriddenF = new BooleanDataAttribute(isBedriddenT.getName(), false, observations);
		dataElems.add(isBedriddenF);
		BooleanDataAttribute isORAvailableT = new BooleanDataAttribute("Operating time available?", true, observations);
		BooleanDataAttribute isORAvailableF = new BooleanDataAttribute(isORAvailableT.getName(), false, observations);
		dataElems.add(isORAvailableF);
		BooleanDataAttribute isInPainT = new BooleanDataAttribute("In pain?", true, observations);
		BooleanDataAttribute isInPainF = new BooleanDataAttribute(isInPainT.getName(), false, observations);
		dataElems.add(isInPainF);
		BooleanDataAttribute isSwollenT = new BooleanDataAttribute("Is swollen?", true, observations);
		BooleanDataAttribute isSwollenF = new BooleanDataAttribute(isSwollenT.getName(), false, observations);
		dataElems.add(isSwollenF);
		BooleanDataAttribute isNumbT = new BooleanDataAttribute("Numbness?", true, observations);
		BooleanDataAttribute isNumbF = new BooleanDataAttribute(isNumbT.getName(), false, observations);
		dataElems.add(isNumbF);
		BooleanDataAttribute isPinsT = new BooleanDataAttribute("Pin-and-needles?", true, observations);
		BooleanDataAttribute isPinsF = new BooleanDataAttribute(isPinsT.getName(), false, observations);
		dataElems.add(isPinsF);
		BooleanDataAttribute isCompartT = new BooleanDataAttribute("Has compartment syndrome?", true, observations);
		BooleanDataAttribute isCompartF = new BooleanDataAttribute(isCompartT.getName(), false, observations);
		dataElems.add(isCompartF);
		BooleanDataAttribute isAcCompartT = new BooleanDataAttribute("Has acute compartment syndrome?", true, observations);
		BooleanDataAttribute isAcCompartF = new BooleanDataAttribute(isAcCompartT.getName(), false, observations);
		dataElems.add(isAcCompartF);
		BooleanDataAttribute isNeedsSurgeryT = new BooleanDataAttribute("Needs surgery?", true, observations);
		BooleanDataAttribute isNeedsSurgeryF = new BooleanDataAttribute(isNeedsSurgeryT.getName(), false, observations);
		dataElems.add(isNeedsSurgeryF);
		BooleanDataAttribute hasCastT = new BooleanDataAttribute("Has cast on?", true, observations);
		BooleanDataAttribute hasCastF = new BooleanDataAttribute(hasCastT.getName(), false, observations);
		dataElems.add(hasCastF);
		BooleanDataAttribute hasFixT = new BooleanDataAttribute("Has fixation on?", true, observations);
		BooleanDataAttribute hasFixF = new BooleanDataAttribute(hasFixT.getName(), false, observations);
		dataElems.add(hasFixT);
		ArrayList<String> values = new ArrayList<>();
		values.add("Forearm");//14
		values.add("Wrist");//15
		values.add("Finger");//16
		values.add("Hand");//17
		values.add("Upper arm");//18
		values.add("Shoulder");//19
		values.add("Collarbone");//20
		HashSet<String> values_hash = new HashSet<>(values);
		CategoricalDataAttribute isForearm = new CategoricalDataAttribute("Confirmed Fracture?", values_hash, values.get(0), observations);
		CategoricalDataAttribute isWrist = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(1), observations);
		CategoricalDataAttribute isFinger = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(2), observations);
		CategoricalDataAttribute isHand = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(3), observations);
		CategoricalDataAttribute isUpperarm = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(4), observations);
		CategoricalDataAttribute isShoulder = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(5), observations);
		CategoricalDataAttribute isCollarbone = new CategoricalDataAttribute(isForearm.getName(), values_hash, values.get(6), observations);
		dataElems.add(isCollarbone);
		CategoricalDataAttribute isForearmSuspect = new CategoricalDataAttribute("Suspected fracture?", values_hash, values.get(0), observations);
		CategoricalDataAttribute isWristSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(1), observations);
		CategoricalDataAttribute isFingerSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(2), observations);
		CategoricalDataAttribute isHandSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(3), observations);
		CategoricalDataAttribute isUpperarmSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(4), observations);
		CategoricalDataAttribute isShoulderSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(5), observations);
		CategoricalDataAttribute isCollarboneSuspect = new CategoricalDataAttribute(isForearmSuspect.getName(), values_hash, values.get(6), observations);
		dataElems.add(isCollarboneSuspect);

		System.out.println("Available resources (" + resources.size() + "): " + resources);
		System.out.println();
		System.out.println("Available activities (" + acts.size() + "): " + acts);
		System.out.println();
		System.out.println("Available data records (2): [" + patientFile + ", " + observations + "]");
		System.out.println();
		System.out.print("Available data attributes (" + (patientFile.getAttributes().size() + observations.getAttributes().size())
				+ "): [");
		boolean addComma = false;
		for(DataAttribute de : dataElems) {
			if(addComma)
				System.out.print(", ");
			else
				addComma = true;
			if(de instanceof BooleanDataAttribute)
				System.out.print(de.getName() + "=(True/False)");
			else if(de instanceof DataAttributeImpl)
				System.out.print(de.getName() + "=String");
			else {
				CategoricalDataAttribute cde = (CategoricalDataAttribute) de;
				String res = null;
				for(String v : ((CategoricalDataAttribute) de).getValues()) {
					if(res != null)
						res += "/" + v;
					else
						res = v;
				}
				System.out.print(cde.getName() + "=(" + res + ")");
			}
		}
		System.out.print("]");
		System.out.println();
		System.out.println();

		//CONSTRAINTS
		//===========

		//DATA
		//----
		//register
		model.add(new Insertion(getDecision(isBeenBeforeF), null, dataExpr(true, patientFile, patientName, patientStatus, isMore14T), actExpr(register), true, false));
		model.add(new Read(getDecision(isBeenBeforeT), null, dataExpr(true, patientFile, patientName), actExpr(register), true, false));
		model.add(new Update(getDecision(isBeenBeforeT), null, dataExpr(true, patientStatus, isMore14T), actExpr(register), true, false));
		//unregister
		model.add(new Update(null, null, dataExpr(patientStatus), actExpr(unregister), true, false));
		//others
		model.add(new Update(null, null, dataExpr(observations), actExpr(false, medHis, clExamine, consult), true, true));
		model.add(new Update(null, null, dataExpr(false, isDisplaceT, isUnstableT, isOpenT, isInPainT, isSwollenT, isNumbT, isPinsT, isCompartT, isAcCompartT, isNeedsSurgeryT, hasCastT),
				actExpr(false, medHis, clExamine, consult), true, true));
		model.add(new Update(null, null, dataExpr(isComplicationsT), actExpr(surgery), true, false));
		model.add(new Update(null, null, dataExpr(hasCastT), actExpr(false, cast, remCast), true, false));
		model.add(new Update(null, null, dataExpr(hasFixF), actExpr(false, fixation, remFixation), true, false));

		System.out.println("Data constraints (" + (model.size()-i) + "):");
		for(; i < model.size(); i++)
			System.out.println(model.get(i).getTextRepresentation());
		//			System.out.println(model.get(i));
		System.out.println();

		//RESOURCE AVAILABILITIES AND CAPACITIES
		//--------------------------------------
		model.add(new AtLeastAvailable(null, null, resExpr(receptionist), 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(receptionist), 2, false));
		model.add(new SimultaneousCapacity(null, null, receptionist, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(nurse), 4, true));
		model.add(new AtMostAvailable(null, null, resExpr(nurse), 10, false));
		model.add(new SimultaneousCapacity(null, null, nurse, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(nurse_or), 1, true));
		model.add(new SimultaneousCapacity(null, null, doctor, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(erDoc), 1, true));
		model.add(new AtMostAvailable(null, null, resExpr(erDoc), 5, false));
		model.add(new AtLeastAvailable(null, null, resExpr(anesthesiologist), 1, true));
		model.add(new AtMostAvailable(null, null, resExpr(anesthesiologist), 2, false));
		model.add(new AtLeastAvailable(null, null, resExpr(surgeon), 1, true));
		model.add(new AtMostAvailable(null, null, resExpr(surgeon), 3, false));
		model.add(new AtLeastAvailable(null, null, resExpr(surgicalAss), 1, true));
		model.add(new AtMostAvailable(null, null, resExpr(surgicalAss), 4, false));
		model.add(new SimultaneousCapacity(null, null, surgicalAss, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(receptiondesk), 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(receptiondesk), 2, false));
		model.add(new SimultaneousCapacity(null, null, receptiondesk, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(examroom), 3, true));
		model.add(new AtMostAvailable(null, null, resExpr(examroom), 15, false));
		model.add(new SimultaneousCapacity(null, null, examroom, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(or), 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(or), 3, false));
		model.add(new SimultaneousCapacity(null, null, or, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(xRayroom), 1, true));
		model.add(new AtMostAvailable(null, null, resExpr(xRayroom), 2, false));
		model.add(new SimultaneousCapacity(null, null, xRayroom, 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(cTroom), 1, false));
		model.add(new SimultaneousCapacity(null, null, cTroom, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(recoverybed), 2, true));
		model.add(new AtMostAvailable(null, null, resExpr(recoverybed), 10, false));
		model.add(new SimultaneousCapacity(null, null, recoverybed, 1, false));
		model.add(new AtLeastAvailable(null, null, resExpr(recoveryRoom), 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(recoveryRoom), 1, false));
		model.add(new SimultaneousCapacity(null, null, recoveryRoom, 10, false));
		model.add(new AtLeastAvailable(null, null, resExpr(patientbed), 5, true));
		model.add(new AtMostAvailable(null, null, resExpr(patientbed), 100, false));
		model.add(new SimultaneousCapacity(null, null, patientbed, 1, false));
		model.add(new AtMostAvailable(null, null, resExpr(patientRoom), 50, false));
		model.add(new SimultaneousCapacity(null, null, patientRoom, 2, false));

		//RESOURCE USAGE/REQUIREMENTS
		//---------------------------
		//register
		model.add(new ActivityAuthorization(null, null, resExpr(receptionist), register, true, false));
		model.add(new AtLeastUsage(null, null, register, resExpr(receptionist), 1, false));
		model.add(new AtMostUsage(null, null, register, resExpr(receptionist), 1, false));
		model.add(new AtLeastUsage(null, null, register, resExpr(receptiondesk), 1, false));
		model.add(new AtMostUsage(null, null, register, resExpr(receptiondesk), 1, false));
		//unregister
		model.add(new ActivityAuthorization(null, null, resExpr(receptionist), unregister, true, false));
		model.add(new AtLeastUsage(null, null, unregister, resExpr(receptionist), 1, false));
		model.add(new AtMostUsage(null, null, unregister, resExpr(receptionist), 1, false));
		model.add(new AtLeastUsage(null, null, unregister, resExpr(receptiondesk), 1, false));
		model.add(new AtMostUsage(null, null, unregister, resExpr(receptiondesk), 1, false));
		//med history
		model.add(new ActivityAuthorization(null, null, resExpr(false, nurse, doctor), medHis, true, false));
		model.add(new AtLeastUsage(null, null, medHis, resExpr(false, nurse, doctor), 1, false));
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), clExamine, true, false));
		//clinical exam
		model.add(new AtLeastUsage(getDecision(isEmergencyF), null, clExamine, resExpr(examroom), 1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeastUsage(null, null, clExamine, resExpr(doctor), 1, false));
		//consult
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), consult, true, false));
		model.add(new AtLeastUsage(null, null, consult, resExpr(examroom), 1, false));
		model.add(new AtLeastUsage(null, null, consult, resExpr(doctor), 1, false));
		//x-ray
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), xRay, true, false));
		model.add(new AtLeastUsage(null, null, xRay, resExpr(xRayroom), 1, false));
		model.add(new AtLeastUsage(null, null, xRay, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, xRay, resExpr(nurse), 1, true));
		//ct
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), ct, true, false));
		model.add(new AtLeastUsage(null, null, ct, resExpr(cTroom), 1, false));
		model.add(new AtMostUsage(null, null, ct, resExpr(cTroom), 1, false));
		model.add(new AtLeastUsage(null, null, ct, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, ct, resExpr(nurse), 1, true));
		//cast
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), cast, true, false));
		model.add(new AtLeastUsage(null, null, cast, resExpr(examroom), 1, false));
		model.add(new AtMostUsage(null, null, cast, resExpr(examroom), 1, false));
		model.add(new AtLeastUsage(null, null, cast, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, cast, resExpr(nurse), 1, true));
		//remove cast
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), remCast, true, false));
		model.add(new AtLeastUsage(null, null, remCast, resExpr(examroom), 1, false));
		model.add(new AtMostUsage(null, null, remCast, resExpr(examroom), 1, false));
		model.add(new AtLeastUsage(null, null, remCast, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, remCast, resExpr(nurse), 1, true));
		//fixation
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), fixation, true, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(or), 1, false));
		model.add(new AtMostUsage(null, null, fixation, resExpr(or), 1, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(surgeon), 1, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(false, surgicalAss, nurse_scrub), 1, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(anesthesiologist), 1, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(nurse_or), 1, false));
		model.add(new AtLeastUsage(null, null, fixation, resExpr(nurse_or), 2, true));
		//remove fixation
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), remFixation, true, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(or), 1, false));
		model.add(new AtMostUsage(null, null, remFixation, resExpr(or), 1, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(surgeon), 1, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(false, surgicalAss, nurse_scrub), 1, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(anesthesiologist), 1, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(nurse_or), 1, false));
		model.add(new AtLeastUsage(null, null, remFixation, resExpr(nurse_or), 2, true));
		//fig8 bandage
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), bandageFig8, true, false));
		model.add(new AtLeastUsage(null, null, bandageFig8, resExpr(examroom), 1, false));
		model.add(new AtMostUsage(null, null, bandageFig8, resExpr(examroom), 1, false));
		model.add(new AtLeastUsage(null, null, bandageFig8, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, bandageFig8, resExpr(nurse), 1, true));
		//bandage
		model.add(new ActivityAuthorization(null, null, resExpr(false, doctor, nurse), bandage, true, false));
		model.add(new AtLeastUsage(null, null, bandage, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, bandage, resExpr(nurse), 1, true));
		//temporary splint
		model.add(new ActivityAuthorization(null, null, resExpr(false, doctor, nurse), splint, true, false));
		model.add(new AtLeastUsage(null, null, splint, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, splint, resExpr(nurse), 1, true));
		//sling
		model.add(new ActivityAuthorization(null, null, resExpr(false, doctor, nurse), sling, true, false));
		model.add(new AtLeastUsage(null, null, sling, resExpr(false, doctor, nurse), 1, false));
		model.add(new AtLeastUsage(null, null, sling, resExpr(nurse), 1, true));
		//ice
		model.add(new ActivityAuthorization(null, null, resExpr(false, doctor, nurse), ice, true, false));
		//surgery
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), surgery, true, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(or), 1, false));
		model.add(new AtMostUsage(null, null, surgery, resExpr(or), 1, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(surgeon), 1, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(false, surgicalAss, nurse_scrub), 1, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(anesthesiologist), 1, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(nurse_or), 1, false));
		model.add(new AtLeastUsage(null, null, surgery, resExpr(nurse_or), 2, true));
		//pressure monitor
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), presMon, true, false));
		model.add(new AtLeastUsage(null, null, presMon, resExpr(false, doctor, nurse), 1, false));
		//rest
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), rest, true, false));
		model.add(new AtLeastUsage(null, null, rest, resExpr(recoverybed), 1, false));
		model.add(new AtMostUsage(null, null, rest, resExpr(recoverybed), 1, false));
		model.add(new AtLeastUsage(null, null, rest, resExpr(recoveryRoom), 1, false));
		model.add(new AtMostUsage(null, null, rest, resExpr(recoveryRoom), 1, false));
		//stay in patient room
		model.add(new AtLeastUsage(null, null, patientStay, resExpr(patientbed), 1, false));
		model.add(new AtMostUsage(null, null, patientStay, resExpr(patientbed), 1, false));
		model.add(new AtLeastUsage(null, null, patientStay, resExpr(patientRoom), 1, false));
		model.add(new AtMostUsage(null, null, patientStay, resExpr(patientRoom), 1, false));
		//prescriptions
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), painkillersA, true, false));
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), painkillersB, true, false));
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), anticoagulants, true, false));
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), stomachProt, true, false));
		model.add(new ActivityAuthorization(null, null, resExpr(doctor), noTreatment, true, false));

		//RESOURCE (IN)EQUALITIES
		//-----------------------
		{
			HashSet<Resource_ActivityExpressionPair> pairs = new HashSet<>();
			pairs.add(new Resource_ActivityExpressionPair(resExpr(surgeon), actExpr(false, surgery, fixation, remFixation)));
			pairs.add(new Resource_ActivityExpressionPair(resExpr(anesthesiologist), actExpr(false, surgery, fixation, remFixation)));
			model.add(new ResourceEquality(null, null, pairs, false, false));
		}

		System.out.println("Resource constraints (" + (model.size()-i) + "):");
		for(; i < model.size(); i++)
			System.out.println(model.get(i).getTextRepresentation());
		//			System.out.println(model.get(i));
		System.out.println();

		//CONTROL-FLOW
		//------------
		//(un)register
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(medHis), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(clExamine), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(consult), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(xRay), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(ct), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(painkillersA), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(painkillersB), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(anticoagulants), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(stomachProt), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(noTreatment), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(ice), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(cast), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(remCast), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(splint), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(sling), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(fixation), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(remFixation), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(bandage), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(bandageFig8), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(surgery), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(presMon), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(rest), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(patientStay), 0, -1, false));
		//		model.add(new Precedence(getDecision(isEmergencyF), null, existExpr(register), existExpr(unregister), 0, -1, false));
		model.add(new First(getDecision(isEmergencyF), null, actExpr(register), false));
		//		model.add(new Response(null, null, existExpr(register), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(medHis), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(clExamine), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(consult), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(xRay), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(ct), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(painkillersA), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(painkillersB), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(anticoagulants), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(stomachProt), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(noTreatment), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(ice), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(cast), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(remCast), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(splint), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(sling), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(fixation), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(remFixation), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(bandage), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(bandageFig8), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(surgery), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(presMon), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(rest), existExpr(unregister), 0, -1, false));
		//		model.add(new Response(null, null, existExpr(patientStay), existExpr(unregister), 0, -1, false));
		model.add(new Last(null, null, actExpr(unregister), false));
		model.add(new Response(null, null, existExpr(unregister),
				existExpr(true, false, register, medHis, clExamine, consult, xRay, ct, painkillersA, painkillersB, anticoagulants, stomachProt, noTreatment, ice, cast,
						remCast, splint, sling, fixation, remFixation, bandage, bandageFig8, surgery, presMon, rest, patientStay),
				0, -1, false));
		model.add(new AtLeast(null, null, actExpr(register), 1, 0, -1, false));
		model.add(new AtMost(null, null, actExpr(register), 1, 0, -1, false));
		model.add(new AtLeast(null, null, actExpr(unregister), 1, 0, -1, false));
		model.add(new AtMost(null, null, actExpr(unregister), 1, 0, -1, false));
		model.add(new Precedence(null, null, existExpr(register), existExpr(unregister), 0, -1, false));
		//admittance
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(patientStay), 0, -1, false));
		//diagnosis - 1st exam
		model.add(new AtLeast(getDecision(isConsciousT), null, actExpr(medHis), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(false, doctor, nurse), model.get(model.size()-1), true, false));
		model.add(new AtLeast(null, null, actExpr(clExamine), 1, 0, -1, false));
		model.add(new Precedence(getDecision(isConsciousT), null, existExpr(medHis), existExpr(clExamine), 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(false, doctor, nurse), model.get(model.size()-1), true, false));
		//diagnosis - scans
		model.add(new AtLeast(getDecision(isEmergencyF), null, actExpr(xRay), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(getDecisionRule(isShoulderSuspect), getDecisionRule(isShoulder, isNeedsSurgeryF)),
				null, actExpr(ct), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		{
			ArrayList<ScheduleStatement> schedule = new ArrayList<>();
			schedule.add(new ScheduleStatement("Monday", "8am and 6pm"));
			schedule.add(new ScheduleStatement("Tuesday", "8am and 6pm"));
			schedule.add(new ScheduleStatement("Wednesday", "8am and 5pm"));
			schedule.add(new ScheduleStatement("Thursday", "8am and 6pm"));
			schedule.add(new ScheduleStatement("Friday", "8am and 6pm"));
			model.add(new ActivityAvailabilitySchedule(null, null, actExpr(ct), schedule, true, false));
		}
		//diagnosis - compartment syndrome
		model.add(new AtLeast(getDecision(
				getDecisionRule(isHandSuspect, isInPainT, isSwollenT, isPinsT),
				getDecisionRule(isForearmSuspect, isInPainT, isSwollenT, isPinsT),
				getDecisionRule(isUpperarmSuspect, isInPainT, isSwollenT, isPinsT)), null,
				actExpr(presMon), 1, 0, 60*30, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//diagnosis - general
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(consult), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(xRay), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(ct), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(presMon), 0, -1, false));
		model.add(new Response(null, null, existExpr(false, true, xRay, ct), existExpr(consult),
				0, -1, false));
		//treatment - general
		model.add(new AtLeastChoice(null, null, actExpr(false, noTreatment, painkillersA, painkillersB, ice, splint,
				cast, sling, fixation, bandage, bandageFig8, surgery), 1, 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(painkillersA), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(painkillersB), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(anticoagulants), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(stomachProt), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(noTreatment), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(cast), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(fixation), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(bandageFig8), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(clExamine), existExpr(surgery), 0, -1, false));

		//TODO
		//treatment - ice
		model.add(new AtLeast(getDecision(getDecisionRule(isSwollenT), getDecisionRule(isCompartT)),
				getDecision(isSwollenF, isCompartF), actExpr(ice), 1, 0, -1, true));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - drugs
		model.add(new AtLeastLag(null, null, existExpr(painkillersA),
				existExpr(anticoagulants), 60*60*12, true));//12hours
		model.add(new AtLeastLag(null, null, existExpr(anticoagulants),
				existExpr(painkillersA), 60*60*12, true));//12hours
		model.add(new ChainResponse(null, null, existExpr(anticoagulants), existExpr(stomachProt), 0, -1, true));
		model.add(new AtLeast(getDecision(isInPainT), null,
				actExpr(false, painkillersA, painkillersB), 1, 0, -1, true));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - cast/splint/fix/sling
		model.add(new Response(null, null, existExpr(cast), existExpr(remCast), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(cast), existExpr(remCast), 0, -1, false));
		model.add(new Response(null, null, existExpr(fixation), existExpr(remFixation), 0, -1, false));
		model.add(new Precedence(null, null, existExpr(fixation), existExpr(remFixation), 0, -1, false));
		model.add(new AtLeast(getDecision(isCompartT, hasCastT), null, actExpr(remCast), 1, 0, 60*15, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(isCompartT, hasFixT), null, actExpr(remFixation), 1, 0, 60*15, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new Precedence(getDecision(
				getDecisionRule(isDisplaceT, isORAvailableF, isWrist),
				getDecisionRule(isDisplaceT, isORAvailableF, isForearm)),
				getDecision(isORAvailableT),
				existExpr(true, existExpr(false, true, cast, splint), existExpr(patientStay)), existExpr(surgery), 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(
				getDecisionRule(isFinger),
				getDecisionRule(isHand),
				getDecisionRule(isWrist),
				getDecisionRule(isForearm),
				getDecisionRule(isUpperarm),
				getDecisionRule(isShoulder)), null,
				actExpr(sling), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(false, doctor, nurse), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(
				getDecisionRule(isOpenF, isDisplaceF, isMore14T, isWrist),
				getDecisionRule(isOpenF, isDisplaceF, isShoulder),
				getDecisionRule(isOpenF, isDisplaceF, isForearm)), null,
				actExpr(false, cast, splint), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - finger
		model.add(new AtLeast(getDecision(isOpenF, isDisplaceF, isUnstableF, isFinger), null,
				actExpr(true, splint, bandage), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(
				getDecisionRule(isOpenT, isFinger), getDecisionRule(isDisplaceT, isFinger), getDecisionRule(isUnstableT, isFinger)), null,
				actExpr(fixation), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - hand
		model.add(new AtLeast(getDecision(isOpenF, isDisplaceF, isUnstableF, isHand), null,
				actExpr(false, fixation, splint, cast), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - upper arm
		model.add(new AtLeast(getDecision(
				getDecisionRule(isOpenF, isDisplaceF, isUnstableF, isUpperarm)), null,
				actExpr(false, sling, cast, splint), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - collarbone
		model.add(new AtLeast(getDecision(
				getDecisionRule(isOpenF, isDisplaceF, isUnstableF, isCollarbone)), null,
				actExpr(false, bandageFig8, splint), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(isCollarbone), null,
				actExpr(sling), 1, 0, -1, true));
		model.add(new DecisionAuthorization(null, null, resExpr(false, doctor, nurse), model.get(model.size()-1), true, false));
		//treatment - shoulder
		model.add(new AtLeast(getDecision(isEmergencyF, isShoulderSuspect), null,
				actExpr(false, xRay, ct), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new Precedence(getDecision(isShoulder), null,
				existExpr(ct), existExpr(surgery), 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(isShoulder), null,
				actExpr(xRay), 1, 60*60*24*1, -1, false));//again xray after at least two days
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtMost(getDecision(isShoulder, isOpenF, isDisplaceF), null,
				actExpr(surgery), 0, 0, -1, true));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - surgery
		model.add(new Response(getDecision(isComplicationsF), null, existExpr(surgery), existExpr(rest), 0, 60*5, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(
				getDecisionRule(isOpenT), getDecisionRule(isDisplaceT)), null,
				actExpr(surgery), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new AtLeast(getDecision(
				getDecisionRule(isAcCompartT)), null,
				actExpr(surgery), 1, 0, 60*60, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		//treatment - specific rules
		model.add(new AtLeast(getDecision(isUnstableT), null,
				actExpr(false, actExpr(surgery), actExpr(true, xRay, consult)), 1, 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new Response(null, null, existExpr(surgery),
				existExpr(false, existExpr(painkillersA), existExpr(true, true, anticoagulants, painkillersB)), 0, 60*60*4, false));//4hours
		model.add(new Response(getDecision(isBedriddenT), null, existExpr(surgery),
				existExpr(true, true, anticoagulants, painkillersB), 0, 60*60*4, false));//4hours
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new Response(null, null,
				existExpr(cast), existExpr(false, true, xRay, surgery), 0, -1, false));
		model.add(new Response(getDecision(
				getDecisionRule(isOpenT, isWrist),
				getDecisionRule(isOpenT, isForearm),
				getDecisionRule(isDisplaceT, isWrist),
				getDecisionRule(isDisplaceT, isForearm)), null,
				existExpr(surgery), existExpr(false, true, cast, splint), 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new Precedence(getDecision(
				getDecisionRule(isEmergencyF, isFingerSuspect),
				getDecisionRule(isEmergencyF, isHandSuspect),
				getDecisionRule(isEmergencyF, isWristSuspect),
				getDecisionRule(isEmergencyF, isForearmSuspect),
				getDecisionRule(isEmergencyF, isUpperarmSuspect),
				getDecisionRule(isEmergencyF, isCollarboneSuspect)), null,
				existExpr(xRay), existExpr(false, true, cast, fixation, bandageFig8, surgery), 0, -1, false));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));
		model.add(new ChainResponse(getDecision(isInPainT), null,
				existExpr(true, existExpr(anticoagulants, false), existExpr(painkillersA, false), existExpr(painkillersB, false), existExpr(false, true, clExamine, consult, surgery)), existExpr(painkillersA), 0, -1, true));
		model.add(new DecisionAuthorization(null, null, resExpr(doctor), model.get(model.size()-1), true, false));

		System.out.println("Control-flow constraints (" + (model.size()-i) + "):");
		for(; i < model.size(); i++)
			System.out.println(model.get(i).getTextRepresentation());
		//			System.out.println(model.get(i));

		//test
		ArrayList<Class<? extends Constraint>> allTemplates = new ArrayList<>();
		allTemplates.add(Deletion.class);
		allTemplates.add(Deletion.class);
		allTemplates.add(Insertion.class);
		allTemplates.add(Read.class);
		allTemplates.add(Update.class);
		allTemplates.add(ActivityAvailabilitySchedule.class);
		allTemplates.add(AtLeast.class);
		allTemplates.add(AtLeastChoice.class);
		allTemplates.add(AtMost.class);
		allTemplates.add(AtMostChoice.class);
		allTemplates.add(AlternatePrecedence.class);
		allTemplates.add(AlternateResponse.class);
		allTemplates.add(AtLeastLag.class);
		allTemplates.add(AtMostLag.class);
		allTemplates.add(ChainPrecedence.class);
		allTemplates.add(ChainResponse.class);
		allTemplates.add(Precedence.class);
		allTemplates.add(RespondedPresence.class);
		allTemplates.add(Response.class);
		allTemplates.add(ActivityAuthorization.class);
		allTemplates.add(AtLeastAvailable.class);
		allTemplates.add(AtLeastUsage.class);
		allTemplates.add(AtMostAvailable.class);
		allTemplates.add(AtMostUsage.class);
		allTemplates.add(DecisionAuthorization.class);
		allTemplates.add(ResourceAvailabilitySchedule.class);
		allTemplates.add(ResourceEquality.class);
		allTemplates.add(SimultaneousCapacity.class);
		ArrayList<String> allTemplates_string = new ArrayList<>();
		for(Class<? extends Constraint> c : allTemplates) {
			if(isInstanceOfInterface(c, Negatable.class)) {
				if(!allTemplates_string.contains(c.getSimpleName() + "+")) {
					allTemplates_string.add(c.getSimpleName() + "+");
					allTemplates_string.add(c.getSimpleName() + "-");
				}
			} else if(!allTemplates_string.contains(c.getSimpleName()))
				allTemplates_string.add(c.getSimpleName());
		}
		System.out.println();
		System.out.println("All constraint templates (" + allTemplates_string.size() + ")");
		System.out.println();
		ArrayList<String> usedTemplates = new ArrayList<>();
		for(Constraint c : model) {
			if(c instanceof Negatable) {
				if(((Negatable) c).isPositiveVersion()) {
					if(!usedTemplates.contains(c.getClass().getSimpleName() + "+"))
						usedTemplates.add(c.getClass().getSimpleName() + "+");
				} else {
					if(!usedTemplates.contains(c.getClass().getSimpleName() + "-"))
						usedTemplates.add(c.getClass().getSimpleName() + "-");
				}
			} else if(!usedTemplates.contains(c.getClass().getSimpleName()))
				usedTemplates.add(c.getClass().getSimpleName());
		}
		usedTemplates.remove("Init");
		usedTemplates.remove("Last");
		Collections.sort(usedTemplates);
		System.out.println("Used constraint templates (" + usedTemplates.size() + "):");
		for(String c : usedTemplates)
			System.out.println(c);
		ArrayList<String> unusedTemplates = new ArrayList<>(allTemplates_string);
		unusedTemplates.removeAll(usedTemplates);
		Collections.sort(unusedTemplates);
		System.out.println();
		System.out.println("Unused constraint templates (" + unusedTemplates.size() + ")");
		for(String c : unusedTemplates)
			System.out.println(c);

		//test
		allTemplates = new ArrayList<>();
		allTemplates.add(Deletion.class);
		allTemplates.add(Deletion.class);
		allTemplates.add(Insertion.class);
		allTemplates.add(Read.class);
		allTemplates.add(Update.class);
		allTemplates.add(ActivityAvailabilitySchedule.class);
		allTemplates.add(AtLeast.class);
		allTemplates.add(AtLeastChoice.class);
		allTemplates.add(AtMost.class);
		allTemplates.add(AtMostChoice.class);
		allTemplates.add(AlternatePrecedence.class);
		allTemplates.add(AlternateResponse.class);
		allTemplates.add(AtLeastLag.class);
		allTemplates.add(AtMostLag.class);
		allTemplates.add(ChainPrecedence.class);
		allTemplates.add(ChainResponse.class);
		allTemplates.add(Precedence.class);
		allTemplates.add(RespondedPresence.class);
		allTemplates.add(Response.class);
		allTemplates.add(ActivityAuthorization.class);
		allTemplates.add(AtLeastAvailable.class);
		allTemplates.add(AtLeastUsage.class);
		allTemplates.add(AtMostAvailable.class);
		allTemplates.add(AtMostUsage.class);
		allTemplates.add(DecisionAuthorization.class);
		allTemplates.add(ResourceAvailabilitySchedule.class);
		allTemplates.add(ResourceEquality.class);
		allTemplates.add(SimultaneousCapacity.class);
		allTemplates_string = new ArrayList<>();
		for(Class<? extends Constraint> c : allTemplates)
			if(!allTemplates_string.contains(c.getSimpleName().replace("AtLeast", "AtMost")))
				allTemplates_string.add(c.getSimpleName().replace("AtLeast", "AtMost"));
		System.out.println();
		System.out.println("All constraint templates (" + allTemplates_string.size() + ")");
		System.out.println();
		usedTemplates = new ArrayList<>();
		for(Constraint c : model)
			if(!usedTemplates.contains(c.getClass().getSimpleName().replace("AtLeast", "AtMost")))
				usedTemplates.add(c.getClass().getSimpleName().replace("AtLeast", "AtMost"));
		usedTemplates.remove("Init");
		usedTemplates.remove("Last");
		Collections.sort(usedTemplates);
		System.out.println("Used constraint templates (" + usedTemplates.size() + "):");
		for(String c : usedTemplates)
			System.out.println(c);
		unusedTemplates = new ArrayList<>(allTemplates_string);
		unusedTemplates.removeAll(usedTemplates);
		Collections.sort(unusedTemplates);
		System.out.println();
		System.out.println("Unused constraint templates (" + unusedTemplates.size() + ")");
		for(String c : unusedTemplates)
			System.out.println(c);

		System.out.println("Number of constraints in the model: " + model.size());

		//		//TODO
		//		model.clear();
		//		model.add(new AtMost(null, null, new AtomicActivityExpression(new Activity("Activity1")), 5, 0, -1, false));
		//		model.add(new Response(null, null,
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity2")), 1, 0, -1, false)),
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity3")), 1, 0, -1, false)), 
		//				0, -1, false));
		//		model.add(new Precedence(null, null,
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity2")), 1, 0, -1, false)),
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity4")), 1, 0, -1, false)), 
		//				0, -1, false));
		//		model.add(new AtLeastLag(null, null,
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity1")), 1, 0, -1, false)),
		//				new AtomicExistenceExpression(new AtLeast(null, null, new AtomicActivityExpression(new Activity("Activity3")), 1, 0, -1, false)), 
		//				20, false));
		//		model.add(new AtMost(getDecision(new BooleanDataAttribute("dataValue1", true, null)), null, new AtomicActivityExpression(new Activity("Activity1")), 2, 0, -1, false));
		//		HashSet<String> v = new HashSet<>();
		//		v.add("v1");
		//		v.add("v2");
		//		model.add(new AtMost(getDecision(new CategoricalDataAttribute("dataValue2", v, "v1", null)), null, new AtomicActivityExpression(new Activity("Activity1")), 2, 0, -1, false));
		//		//TODO
		return model;
	}

	private static boolean isInstanceOfInterface(Class<?> c, Class<?> interf) {
		if(c.getInterfaces() != null
				&& Arrays.asList(c.getInterfaces()).contains(interf))
			return true;
		if(c.getSuperclass() != null)
			return isInstanceOfInterface(c.getSuperclass(), interf);
		return false;
	}

	public static Decision getDecision(DataAttribute... dElems) {
		HashSet<DataAttribute> v = new HashSet<>();
		HashSet<DecisionRule> r = new HashSet<>();
		for(DataAttribute de : dElems)
			v.add(de);
		r.add(new DecisionRule(v));
		return new Decision(r);
	}

	public static Decision getDecision(DecisionRule... rules) {
		HashSet<DecisionRule> r = new HashSet<>();
		for(DecisionRule dr : rules)
			r.add(dr);
		return new Decision(r);
	}

	public static DecisionRule getDecisionRule(DataAttribute... dElems) {
		HashSet<DataAttribute> v = new HashSet<>();
		for(DataAttribute de : dElems)
			v.add(de);
		return new DecisionRule(v);
	}

	public static HashSet<Resource> getResources(Resource... rs) {
		HashSet<Resource> v = new HashSet<>();
		for(Resource r : rs)
			v.add(r);
		return v;
	}

	public static AtomicExistenceExpression existExpr(Activity a) {
		return existExpr(a, true);
	}

	public static AtomicExistenceExpression existExpr(Activity a, boolean isPositive) {
		if(isPositive)
			return new AtomicExistenceExpression(new AtLeast(null, null, actExpr(a), 1, 0, -1, false));
		return new AtomicExistenceExpression(new AtMost(null, null, actExpr(a), 0, 0, -1, false));
	}

	public static AtomicExistenceExpression existExpr(ExistenceConstraint c) {
		return new AtomicExistenceExpression(c);
	}

	public static ExistenceExpression existExpr(boolean isAnd, boolean isPositive, Activity... as) {
		ExistenceConstraint x;
		if(isPositive)
			x = new AtLeast(null, null, actExpr(false, as), 1, 0, -1, false);
		else
			x = new AtMost(null, null, actExpr(false, as), 0, 0, -1, false);
		return existExpr(x);
	}

	public static ExistenceExpression existExpr(boolean isAnd, ExistenceConstraint... cs) {
		if(cs.length == 0)
			throw new IllegalArgumentException();
		if(cs.length == 1)
			return existExpr(cs[0]);
		ExistenceExpression[] exprs = new ExistenceExpression[cs.length];
		for(int i = 0; i < cs.length; i++)
			exprs[i] = existExpr(cs[i]);
		return new NonAtomicExistenceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static ExistenceExpression existExpr(boolean isAnd, ExistenceExpression... exprs) {
		return new NonAtomicExistenceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static AtomicActivityExpression actExpr(Activity activity) {
		return new AtomicActivityExpression(activity);
	}

	public static ActivityExpression actExpr(boolean isAnd, Activity... activities) {
		if(activities.length == 0)
			throw new IllegalArgumentException();
		if(activities.length == 1)
			return actExpr(activities[0]);
		ActivityExpression[] exprs = new ActivityExpression[activities.length];
		for(int i = 0; i < activities.length; i++)
			exprs[i] = actExpr(activities[i]);
		return new NonAtomicActivityExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static ActivityExpression actExpr(boolean isAnd, ActivityExpression... exprs) {
		return new NonAtomicActivityExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static ResourceExpression resExpr(Resource resource) {
		return new AtomicResourceExpression(resource);
	}

	public static ResourceExpression resExpr(boolean isAnd, Resource... resources) {
		if(resources.length == 0)
			throw new IllegalArgumentException();
		if(resources.length == 1)
			return resExpr(resources[0]);
		ResourceExpression[] exprs = new ResourceExpression[resources.length];
		for(int i = 0; i < resources.length; i++)
			exprs[i] = resExpr(resources[i]);
		return new NonAtomicResourceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static NonAtomicResourceExpression resExpr(boolean isAnd, ResourceExpression... exprs) {
		return new NonAtomicResourceExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static DataExpression dataExpr(DataStructure data) {
		return new AtomicDataExpression(data);
	}

	public static DataExpression dataExpr(boolean isAnd, DataStructure... datas) {
		if(datas.length == 0)
			throw new IllegalArgumentException();
		if(datas.length == 1)
			return dataExpr(datas[0]);
		DataExpression[] exprs = new DataExpression[datas.length];
		for(int i = 0; i < datas.length; i++)
			exprs[i] = dataExpr(datas[i]);
		return new NonAtomicDataExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}

	public static NonAtomicDataExpression dataExpr(boolean isAnd, DataExpression... exprs) {
		return new NonAtomicDataExpression(isAnd?LogicalOperator.AND:LogicalOperator.OR, exprs);
	}
}