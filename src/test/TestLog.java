package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import miner.log.ActivityEvent;
import miner.log.DataEvent;
import miner.log.Log;
import miner.log.ResourceEvent;
import miner.log.Trace;
import model.Activity;
import model.constraint.resource.AtMostAvailable;
import model.constraint.resource.ResourceAvailability;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.resource.DirectResource;
import model.resource.ResourceRole;

public class TestLog {

//	//	public static void main(String[] args) {
//	//		getTestLog_armFractures(5);
//	//	}
//
//	//TODO: maakt automatisch? (random iets maken en telkens checken of geen violations...)
//	public static Log getTestLog_armFractures(int nrOfTraces) {
//		if(nrOfTraces < 1)
//			throw new IllegalArgumentException();
//		//based on case from CAiSE15 (BPMDS) paper
//		String[] acts = {
//				"Register patient",//0
//				"Examine patient",//1
//				"Take X-ray",//2
//				"Prescribe weak painkillers",//3 TODO: unused
//				"Prescribe strong painkillers A",//4 TODO: unused
//				"Prescribe strong painkillers B",//5 TODO: unused
//				"Prescribe anti-inflammatory drugs",//6 TODO: unused
//				"Prescribe anticoagulants",//7 TODO: unused
//				"Perform surgery",//8
//				"Apply bandage",//9
//				"Apply fixation",//10
//				"Apply sling",//11
//				"Apply cast",//12
//				"Perform physiotherapy",//13 TODO: unused
//				"Let patient rest"//14
//		};
//		HashMap<String, Integer> durations = new HashMap<>();
//		durations.put(acts[0], 10);
//		durations.put(acts[1], 20);
//		durations.put(acts[2], 30);
//		durations.put(acts[3], 1);
//		durations.put(acts[4], 1);
//		durations.put(acts[5], 1);
//		durations.put(acts[6], 1);
//		durations.put(acts[7], 1);
//		durations.put(acts[8], 120);
//		durations.put(acts[9], 5);
//		durations.put(acts[10], 10);
//		durations.put(acts[11], 3);
//		durations.put(acts[12], 15);
//		durations.put(acts[13], 60);
//		durations.put(acts[14], 180);
//		ArrayList<String> values = new ArrayList<>();
//		values.add("Forearm");//14
//		values.add("Wrist");//15
//		values.add("Finger");//16
//		values.add("Hand");//17
//		values.add("Upper arm");//18
//		values.add("Shoulder");//19
//		values.add("Collarbone");//20
//		HashSet<String> values_hash = new HashSet<>(values);
//		DataAttribute[] dataElems = {
//				new BooleanDataAttribute("Open or complex fracture", true),//0
//				new BooleanDataAttribute("Open or complex fracture", false),//1
//				new BooleanDataAttribute("Extensive damage to arteries or nerves", true),//2
//				new BooleanDataAttribute("Extensive damage to arteries or nerves", false),//3
//				new BooleanDataAttribute("Both arms broken", true),//4
//				new BooleanDataAttribute("Both arms broken", false),//5
//				new BooleanDataAttribute("No improvement in 3 months", true),//6
//				new BooleanDataAttribute("No improvement in 3 months", false),//7
//				new BooleanDataAttribute("More than 16yrs old", true),//8
//				new BooleanDataAttribute("More than 16yrs old", false),//9
//				new BooleanDataAttribute("Periosteum torn", true),//10
//				new BooleanDataAttribute("Periosteum torn", false),//11
//				new BooleanDataAttribute("OR available", true),//12
//				new BooleanDataAttribute("OR available", false),//13
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(0)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(1)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(2)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(3)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(4)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(5)),
//				new CategoricalDataAttribute("Fractured bone", values_hash, values.get(6))
//		};
//		ArrayList<Trace> predefinedCases = new ArrayList<>();
//
//		//no real action cases
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[6], dataElems[7], dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[11]),
//				durations, 1,
//				acts[0], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[6], dataElems[7], dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[11]),
//				durations, 1,
//				acts[0], acts[1], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[6], dataElems[7], dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[11]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[11]));
//
//		//cases that require actions
//		//forearm open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[0], dataElems[12], dataElems[14]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[0], dataElems[13], dataElems[14]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		//wrist open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[0], dataElems[12], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[0], dataElems[13], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		//finger open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//hand open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//finger and hand open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//upper arm open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//shoulder open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//collarbone open
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[20]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[2], dataElems[3], dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[0], dataElems[20]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1]));
//
//		//forearm ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[1], dataElems[2], dataElems[12], dataElems[14]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[1], dataElems[2], dataElems[13], dataElems[14]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		//wrist ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[1], dataElems[2], dataElems[12], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11]),
//				getData(dataElems[1], dataElems[2], dataElems[13], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[8], acts[14], acts[12], acts[2], acts[1], acts[11]));
//		//finger ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//hand ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//finger and hand ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[16], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//upper arm ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//shoulder ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1], acts[11]));
//		//collarbone ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[20]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[2], dataElems[20]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[2], acts[1]));
//
//		//finger !open !ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[16]),
//				durations, 1,
//				acts[0], acts[1], acts[10], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[16]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[10], acts[11]));
//		//hand !open !ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[17]),
//				durations, 1,
//				acts[0], acts[1], acts[10], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[17]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[10], acts[11]));
//		//collarbone !open !ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[20]),
//				durations, 1,
//				acts[0], acts[1], acts[9]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[20]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[9]));
//		//forearm !open !ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[14]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[14]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[12], acts[2], acts[1]));
//		//shoulder !open !ext
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[19]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[6], dataElems[7],
//						dataElems[8], dataElems[9], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[19]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[12], acts[2], acts[1]));
//		//upper arm !open !ext both
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[4], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[4], dataElems[18]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[8], acts[14], acts[11]));
//		//upper arm !open !ext !both 3mon
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[6], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[6], dataElems[18]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[8], acts[14], acts[11]));
//		//upper arm !open !ext !both !3mon
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[7], dataElems[18]),
//				durations, 1,
//				acts[0], acts[1], acts[10], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[8], dataElems[9],
//						dataElems[10], dataElems[11], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[5], dataElems[7], dataElems[18]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[10], acts[11]));
//		//wrist !open !ext !16+ per
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[9], dataElems[10], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[8], acts[14], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[4], dataElems[5], dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[9], dataElems[10], dataElems[15]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[8], acts[14], acts[11]));
//		//wrist !open !ext !16+ !per
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[9], dataElems[11], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[9], dataElems[11], dataElems[15]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[12], acts[2], acts[1], acts[11]));
//		//wrist !open !ext 16+
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[8], dataElems[15]),
//				durations, 1,
//				acts[0], acts[1], acts[12], acts[2], acts[1], acts[11]));
//		predefinedCases.addAll(getTraces(
//				getData(dataElems[12], dataElems[13]),
//				getData(dataElems[1], dataElems[3], dataElems[8], dataElems[15]),
//				durations, 3,
//				acts[0], acts[1], acts[2], acts[1], acts[12], acts[2], acts[1], acts[11]));
//
//		if(nrOfTraces < predefinedCases.size())
//			throw new IllegalArgumentException();
//		int multiple = nrOfTraces / predefinedCases.size();
//
//		//		//TODO
//		//		int test_sur = 0;
//		//		int test_sur_cast = 0;
//		//		for(Trace t : predefinedCases)
//		//			for(int i = 0; i < t.getActivityEvents().size(); i++)
//		//				if(t.getActivityEvents().get(i).getName().equals(acts[8])) {
//		//					test_sur++;
//		//					for(int j = i+1; j < t.getActivityEvents().size(); j++)
//		//						if(t.getActivityEvents().get(j).getName().equals(acts[12]))
//		//							test_sur_cast++;
//		//					break;
//		//				}
//		//		System.out.println(test_sur + " of " + predefinedCases.size() + " traces contain surgery");
//		//		System.out.println(test_sur_cast + " of " + predefinedCases.size() + " traces contain surgery followed by applying a cast");
//
//		//randomly add the predefined traces to the log (each case occurs at least once)
//		ArrayList<Trace> log = new ArrayList<>();
//		Random r = new Random();
//		for(int i = 1; i <= predefinedCases.size()*multiple; i++) {
//			if(i % multiple == 0)
//				log.add(predefinedCases.get((i/multiple) - 1));
//			else
//				log.add(predefinedCases.get(r.nextInt(predefinedCases.size())));
//		}
//		while(log.size() < nrOfTraces)
//			log.add(predefinedCases.get(r.nextInt(predefinedCases.size())));
//		return log;
//	}
//
//	public static HashSet<ResourceAvailability> getResourceAvailabilities_armFractures() {
//		HashSet<ResourceAvailability> res = new HashSet<>();
//		res.add(new AtMostAvailable(null, new ResourceRole("RECEPTIONDESK"), 2));
//		res.add(new AtMostAvailable(null, new ResourceRole("RECEPTIONIST"), 2));
//		res.add(new AtMostAvailable(null, new ResourceRole("EXAMROOM"), 15));
//		res.add(new AtMostAvailable(null, new ResourceRole("DOCTOR"), 3));
//		res.add(new AtMostAvailable(null, new ResourceRole("XRAYROOM"), 1));
//		res.add(new AtMostAvailable(null, new ResourceRole("NURSE"), 10));
//		res.add(new AtMostAvailable(null, new ResourceRole("OPERATINGROOM"), 3));
//		res.add(new AtMostAvailable(null, new ResourceRole("SURGEON"), 2));
//		res.add(new AtMostAvailable(null, new ResourceRole("PHYSIOTHERAPYROOM"), 1));
//		res.add(new AtMostAvailable(null, new ResourceRole("PHYSIOTHERAPIST"), 1));
//		res.add(new AtMostAvailable(null, new ResourceRole("PATIENTBED"), 60));
//		return res;
//	}
//
//	private static DataAttribute[] getData(DataAttribute... data) {
//		return data;
//	}
//
//	private static ArrayList<Trace> getTraces(DataAttribute[] dontCares,
//			DataAttribute[] data, HashMap<String, Integer> durations,
//			int mainDecision, String... acts) {
//		ArrayList<Trace> traces = new ArrayList<>();
//		for(int x = 0; x < Math.pow(2, dontCares.length/2); x++) {
//			int[] dontCareData = new int[dontCares.length/2];
//			for(int k = 0; k < dontCareData.length; k++)
//				dontCareData[k] = k*2;
//			String tmp = Integer.toBinaryString(x);
//			for(int k = 0; k < tmp.length(); k++)
//				if(tmp.charAt(tmp.length()-1-k) == '1')
//					dontCareData[dontCareData.length-1-k]++;
//			DataAttribute[] newData = new DataAttribute[data.length+(dontCares.length/2)];
//			for(int i = 0; i < data.length; i++)
//				newData[i] = data[i];
//			for(int i = 0; i < dontCareData.length; i++)
//				newData[data.length+i] = dontCares[dontCareData[i]];
//			traces.add(getTrace(newData, durations, mainDecision, acts));
//		}
//		return traces;
//	}
//
//	private static Trace getTrace(DataAttribute[] data, HashMap<String, Integer> durations,
//			int mainDecision, String... actsArray) {
//		ArrayList<ActivityEvent> acts = new ArrayList<>();
//		ArrayList<DataEvent> dataEvs = new ArrayList<>();
//		ArrayList<ResourceEvent> resourceEvents = new ArrayList<>();
//		int firstExamIndex = -1;
//		for(int i = 0; i < actsArray.length; i++)
//			if(actsArray[i].equals("Examine patient")) {
//				firstExamIndex = i;
//				break;
//			}
//		for(DataAttribute de : data)
//			if(de.getName().equals("More than 16yrs old"))
//				dataEvs.add(new DataEvent(de, true, 0));
//		int time = 0;
//		for(int i = 0; i < actsArray.length; i++) {
//			String a = actsArray[i];
//			if(a.equals("Register patient")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("RECEPTIONDESK"), time+1));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("RECEPTIONIST"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Examine patient")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("EXAMROOM"), time+1));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("DOCTOR"), time+1));
//				if(i == firstExamIndex)
//					for(DataAttribute de : data)
//						if(de.getName().equals("Open or complex fracture"))
//							dataEvs.add(new DataEvent(de, true, time+1));
//				if(i == mainDecision)
//					for(DataAttribute de : data)
//						if(!de.getName().equals("Open or complex fracture"))
//							dataEvs.add(new DataEvent(de, true, time+1));
//				time += durations.get(a);
//			} else if(a.equals("Take X-ray")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("XRAYROOM"), time+1));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("NURSE"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Prescribe weak painkillers")
//					|| a.equals("Prescribe strong painkillers A")
//					|| a.equals("Prescribe strong painkillers B")
//					|| a.equals("Prescribe anti-inflammatory drugs")
//					|| a.equals("Prescribe anticoagulants")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("DOCTOR"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Perform surgery")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("OPERATINGROOM"), time+1));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("SURGEON"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Apply sling")
//					|| a.equals("Apply cast")
//					|| a.equals("Apply fixation")
//					|| a.equals("Apply bandage")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("EXAMROOM"), time+1));
//				if(new Random().nextBoolean())
//					resourceEvents.add(new ResourceEvent(new ResourceRole("NURSE"), time+1));
//				else
//					resourceEvents.add(new ResourceEvent(new ResourceRole("DOCTOR"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Perform physiotherapy")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("PHYSIOTHERAPYROOM"), time+1));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("PHYSIOTHERAPIST"), time+1));
//				time += durations.get(a);
//			} else if(a.equals("Let patient rest")) {
//				acts.add(new ActivityEvent(a, time, time+durations.get(a)));
//				resourceEvents.add(new ResourceEvent(new ResourceRole("PATIENTBED"), time+1));
//				time += durations.get(a);
//			} else {
//				throw new IllegalArgumentException();
//			}
//		}
//		return new Trace(acts, dataEvs, resourceEvents);
//	}
//
//	public static ArrayList<Trace> getTestLog_letters() {
//		//init A
//		//A at most 1 + A at least 1
//		//B response C
//		//C at least 1
//		//B not coexist D
//		//E at most 2
//		//D or E last
//		// + data
//		// + resources
//		ArrayList<Trace> log = new ArrayList<>();
//		log.add(parseLetterTrace("ACECCE"));
//		log.add(parseLetterTrace("AEBBBCBCCBCCE"));
//		log.add(parseLetterTrace("AEECD"));
//		log.add(parseLetterTrace("ACD"));
//		log.add(parseLetterTrace("ACECCEDDD"));
//		log.add(parseLetterTrace("ACDECE"));
//		log.add(parseLetterTrace("ADCDDCDEE"));
//		log.add(parseLetterTrace("ABEBCCE"));
//		log.add(parseLetterTrace("ACECCEDDDD"));
//		log.add(parseLetterTrace("ACCE"));
//		log.add(parseLetterTrace("ABBECCE"));
//		log.add(parseLetterTrace("ABCE"));
//		log.add(parseLetterTrace("ABCBCCE"));
//		log.add(parseLetterTrace("ACDCDDD"));
//		log.add(parseLetterTrace("ACCDCCEDECCDDD"));
//		log.add(parseLetterTrace("ACE"));
//		log.add(parseLetterTrace("AECEDCD"));
//		log.add(parseLetterTrace("ACBCCE"));
//		log.add(parseLetterTrace("AECCBCCBCE"));
//		log.add(parseLetterTrace("ADDDCDCDCDDCCDD"));
//		log.add(parseLetterTrace("ACE"));
//		log.add(parseLetterTrace("AECCBCCBCE"));
//		log.add(parseLetterTrace("ABCE"));
//		log.add(parseLetterTrace("ADDDCDCDCDDCCDD"));
//		log.add(parseLetterTrace("ACD"));
//		return log;
//	}
//
//	private static Trace parseLetterTrace(String input) {
//		ArrayList<ActivityEvent> acts = new ArrayList<>();
//		ArrayList<DataEvent> data = new ArrayList<>();
//		ArrayList<DataAttribute> dataUsed = new ArrayList<>();
//		ArrayList<ResourceEvent> resourceEvents = new ArrayList<>();
//		if(input.length() < 5) {
//			DataAttribute dv = new CategoricalDataAttribute("test3", categories, "2222");
//			data.add(new DataEvent(dv, true, 0));
//			dataUsed.add(dv);
//		}
//		if(input.substring(input.length()-1, input.length()).equals("D")) {
//			DataAttribute dv = new BooleanDataAttribute("test4", false);
//			data.add(new DataEvent(dv, true, 0));
//			dataUsed.add(dv);
//		} else {
//			DataAttribute dv = new BooleanDataAttribute("test4", true);
//			data.add(new DataEvent(dv, true, 0));
//			dataUsed.add(dv);
//		}
//		int time = 0;
//		for(int i = 0; i < input.length(); i++) {
//			String name = input.substring(i, i+1);
//			//add data
//			if(name.equals("B")) {
//				DataAttribute dv = new BooleanDataAttribute("test1", false);
//				if(!dataUsed.contains(dv)) {
//					data.add(new DataEvent(dv, true, time+1));
//					dataUsed.add(dv);
//				}
//				dv = new BooleanDataAttribute("test2", true);
//				if(!dataUsed.contains(dv)) {
//					data.add(new DataEvent(dv, true, time+1));
//					dataUsed.add(dv);
//				}
//			} else if(name.equals("D")) {
//				DataAttribute dv = new BooleanDataAttribute("test1", true);
//				if(!dataUsed.contains(dv)) {
//					data.add(new DataEvent(dv, true, time+1));
//					dataUsed.add(dv);
//				}
//				dv = new BooleanDataAttribute("test2", false);
//				if(!dataUsed.contains(dv)) {
//					data.add(new DataEvent(dv, true, time+1));
//					dataUsed.add(dv);
//				}
//			}
//			if(name.equals("C") && acts.contains(new Activity("E"))) {
//				DataAttribute dv = new CategoricalDataAttribute("test3", categories, "3333");
//				data.add(new DataEvent(dv, false, time+1));
//				dataUsed.remove(dv);
//			}
//			if(name.equals("E")) {
//				if(acts.contains(new Activity("D"))) {
//					DataAttribute dv = new CategoricalDataAttribute("test3", categories, "1111");
//					if(!dataUsed.contains(dv)) {
//						data.add(new DataEvent(dv, true, time+1));
//						dataUsed.add(dv);
//					}
//				} else {
//					DataAttribute dv = new CategoricalDataAttribute("test3", categories, "3333");
//					if(!dataUsed.contains(dv)) {
//						data.add(new DataEvent(dv, true, time+1));
//						dataUsed.add(dv);
//					}
//				}
//			}
//			if(name.equals("A"))
//				resourceEvents.add(new ResourceEvent(new ResourceRole("R1"), time+1));
//			else if(name.equals("C")) {
//				resourceEvents.add(new ResourceEvent(new ResourceRole("R2"), time+1));
//				resourceEvents.add(new ResourceEvent(new DirectResource("DR1"), time+1));
//				resourceEvents.add(new ResourceEvent(new DirectResource("DR2"), time+1));
//			} else if(name.equals("D")) {
//				resourceEvents.add(new ResourceEvent(new ResourceRole("R3"), time+1));
//				resourceEvents.add(new ResourceEvent(new DirectResource("DR1"), time+1));
//			} else if(name.equals("E")) {
//				if(acts.contains(new Activity("D")))
//					resourceEvents.add(new ResourceEvent(new ResourceRole("R3"), time+1));
//				else
//					resourceEvents.add(new ResourceEvent(new DirectResource("DR3"), time+1));
//			}
//			acts.add(new ActivityEvent(name, time, time+1));
//			time += 2;
//		}
//		return new Trace(acts, data, resourceEvents);
//	}
//
//	private static final HashSet<String> categories = new HashSet<>();
//	static {
//		categories.add("1111");
//		categories.add("2222");
//		categories.add("3333");
//	}
}