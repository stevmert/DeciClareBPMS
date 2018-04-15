package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import miner.log.Log;
import miner.log.Trace;
import util.FileManager;
import util.xml.ParentNode;
import util.xml.XML;

public class XESWriter_Extended2_testlog {
//
//	public static void main(String[] args) {
//		try {
//			//			Log log = TestLog.getTestLog_letters();
//			//			createXES_extended2("test_letters", log);
//			Log log = TestLog.getTestLog_armFractures(5000);
//			createXES_extended2("test_armFractures", log);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.exit(0);
//	}
//
//	private static void createXES_extended2(String filename, Log log) {
//		try {
//			XML xml = XML.parse(FileManager.readAll(new File("miner/xes_templates/templateXES_extended2.xes")));
//			ParentNode logNode = (ParentNode) xml.getNodes().get(xml.getNodes().size()-1);
//			for(Trace t : log) {
//				ParentNode newNode = t.getXesNode_extended2();
//				logNode.getChildNodes().add(newNode);
//			}
//			FileWriter fw = new FileWriter(new File(new File("models"), filename + ".xes"));
//			xml.exportXML(new BufferedWriter(fw));
//			fw.close();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
}