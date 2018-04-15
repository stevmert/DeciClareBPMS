package test.azmm.textmining;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import test.azmm.ContactData;
import test.azmm.ContactLijn;
import test.azmm.DashboardManchester;
import test.azmm.DashboardZone;
import test.azmm.RX;
import test.azmm.Reader;
import test.azmm.SpoedNr;
import test.azmm.Triage;
import test.azmm.User;

public class Analyzer {

	public static void main(String[] args) throws Exception {
		System.out.println("Loading log...");
		//		HashMap<Integer, SpoedNr> res = getAll();
		HashMap<Integer, SpoedNr> res = getTriages(null);
		//		HashMap<Integer, SpoedNr> res = getWachttijden(null);
		System.out.println("Log loaded");
		System.out.println("Creating tagcloud...");
		for(SpoedNr s : res.values()) {
			for(Triage t : s.getTriages()) {
				TagCloud.getInstance("alg").addProcessedText(t.getOpmerking());
				TagCloud.getInstance("tr_opm").addProcessedText(t.getOpmerking());
				TagCloud.getInstance("alg").addProcessedText(t.getOpnamereden());
				TagCloud.getInstance("tr_opnR").addProcessedText(t.getOpnamereden());
			}
			for(RX r : s.getRXs()) {
				TagCloud.getInstance("alg").addProcessedText(r.getBeschrijving());
				TagCloud.getInstance("rx").addProcessedText(r.getBeschrijving());
			}
			for(ContactLijn c : s.getContactLijnen()) {
				TagCloud.getInstance("alg").addProcessedText(c.getOmschrijving());
				TagCloud.getInstance("c_om").addProcessedText(c.getOmschrijving());
				for(ContactData cd : c.getContactData()) {
					TagCloud.getInstance("alg").addProcessedText(cd.getTekst());
					TagCloud.getInstance("c_t").addProcessedText(cd.getTekst());
				}
			}
		}
		System.out.println("Tagclouds created");
		TagCloud.getInstance("tr_opm").export();
		//		TagCloud.getInstance("tr_opnR").export();
		//		TagCloud.getInstance("rx").export();
		//		TagCloud.getInstance("c_om").export();
		//		TagCloud.getInstance("c_t").export();
		//		TagCloud.getInstance("alg").export();
		//		TagCloud.getInstance("alg").test("alg");
		TagCloud.getInstance("tr_opm").test("alg");
	}

	public static HashMap<Integer, User> getUsers() throws Exception {
		return Reader.readResources(new File(new File("data"), "users" + ".csv"));
	}

	public static HashMap<Integer, SpoedNr> getOpnames() throws Exception {
		return Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
	}

	public static HashMap<Integer, SpoedNr> getRXs(HashMap<Integer, SpoedNr> spoedNrs) throws Exception {
		if(spoedNrs == null)
			spoedNrs =  Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
		Reader.readRX(new File(new File("data"), "rx" + ".csv"), spoedNrs);
		return spoedNrs;
	}

	public static HashMap<Integer, SpoedNr> getWachttijden(HashMap<Integer, SpoedNr> spoedNrs) throws Exception {
		if(spoedNrs == null)
			spoedNrs =  Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
		Reader.readWachttijden(new File(new File("data"), "SpoedAfdeling_wachttijden" + ".csv"), spoedNrs);
		return spoedNrs;
	}

	public static HashMap<Integer, SpoedNr> getTriages(HashMap<Integer, SpoedNr> spoedNrs) throws Exception {
		if(spoedNrs == null)
			spoedNrs =  Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
		Reader.readTriage(new File(new File("data"), "dashboard_triage" + ".csv"), spoedNrs);
		return spoedNrs;
	}

	public static HashMap<Integer, SpoedNr> getContactLijnen(HashMap<Integer, SpoedNr> spoedNrs) throws Exception {
		if(spoedNrs == null)
			spoedNrs =  Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
		Reader.readContactLijn(new File(new File("data"), "contactlijnen" + ".csv"), spoedNrs);
		return spoedNrs;
	}

	public static HashMap<Integer, SpoedNr> getContactData(HashMap<Integer, SpoedNr> spoedNrs) throws Exception {
		if(spoedNrs == null)
			spoedNrs =  Reader.readOpnames(new File(new File("data"), "opname_ontslag" + ".csv"));
		HashMap<Long, Integer> contactToSpoedNr = Reader.readContactLijn(new File(new File("data"), "contactlijnen" + ".csv"), spoedNrs);
		Reader.readContactData(new File(new File("data"), "contactdata" + ".csv"), spoedNrs, contactToSpoedNr);
		return spoedNrs;
	}

	public static ArrayList<DashboardZone> getDashboardZones() throws Exception {
		return Reader.readDashboardZone(new File(new File("data"), "dashboardzones" + ".csv"));
	}

	public static ArrayList<DashboardManchester> getDashboardManchesters() throws Exception {
		return Reader.readDashboardManchester(new File(new File("data"), "dashboardmanchester" + ".csv"));
	}

	public static HashMap<Integer, SpoedNr> getAll() throws Exception {
		//		HashMap<Integer, User> users = getUsers();
		HashMap<Integer, SpoedNr> spoedNrs = getRXs(null);
		getWachttijden(spoedNrs);
		getTriages(spoedNrs);
		//		ArrayList<DashboardZone> dzs = getDashboardZones();
		//		ArrayList<DashboardManchester> dms = getDashboardManchesters();
		getContactData(spoedNrs);
		return spoedNrs;
	}
}