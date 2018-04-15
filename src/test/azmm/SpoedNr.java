package test.azmm;

import java.util.ArrayList;

public class SpoedNr {

	private final int spoedNr;
	private ArrayList<Opname_Ontslag> opnames;
	private ArrayList<RX> rxs;
	private ArrayList<Wachtijden> wachtijden;
	private ArrayList<Triage> triages;
	private ArrayList<ContactLijn> contactLijnen;

	public SpoedNr(int spoedNr) {
		super();
		this.spoedNr = spoedNr;
		this.opnames = new ArrayList<>();
		this.rxs = new ArrayList<>();
		this.wachtijden = new ArrayList<>();
		this.triages = new ArrayList<>();
		this.contactLijnen = new ArrayList<>();
	}

	public int getSpoedNr() {
		return spoedNr;
	}

	public ArrayList<Opname_Ontslag> getOpnames() {
		return opnames;
	}

	public void addOpname(Opname_Ontslag opname) {
		opnames.add(opname);
	}

	public ArrayList<RX> getRXs() {
		return rxs;
	}

	public void addRX(RX rx) {
		rxs.add(rx);
	}

	public ArrayList<Wachtijden> getWachtijden() {
		return wachtijden;
	}

	public void addWachtijden(Wachtijden wachtijden) {
		this.wachtijden.add(wachtijden);
	}

	public ArrayList<Triage> getTriages() {
		return triages;
	}

	public void addTriage(Triage triage) {
		this.triages.add(triage);
	}

	public ArrayList<ContactLijn> getContactLijnen() {
		return contactLijnen;
	}

	public void addContactLijn(ContactLijn contactLijn) {
		this.contactLijnen.add(contactLijn);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + spoedNr;
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
		SpoedNr other = (SpoedNr) obj;
		if (spoedNr != other.spoedNr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return spoedNr + " ("
				+ opnames.size() + " opnames, "
				+ triages.size() + " triages, "
				+ rxs.size() + " RXs, "
				+ wachtijden.size() + " wachttijden, "
				+ contactLijnen.size() + " contactlijnen)";
	}
}