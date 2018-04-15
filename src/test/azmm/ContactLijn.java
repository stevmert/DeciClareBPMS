package test.azmm;

import java.time.LocalDateTime;
import java.util.ArrayList;

import test.azmm.textmining.WordProcessor;

public class ContactLijn extends ReadableItem {

	private long contactID;
	private LocalDateTime contactDatum;
	private String[] omschrijving;//text mining...
	private String DISnaam;//TODO: afdeling of departement?
	private String superviserendeArts;//TODO: achternaam arts?
	private ArrayList<ContactData> contactData;

	public ContactLijn(long contactID, LocalDateTime contactDatum, String omschrijving, String dISnaam,
			String superviserendeArts) {
		super();
		this.contactID = contactID;
		this.contactDatum = contactDatum;
		this.omschrijving = WordProcessor.getInstance("alg").processText(omschrijving);
		DISnaam = dISnaam;
		this.superviserendeArts = superviserendeArts;
		contactData = new ArrayList<>();
	}

	public ContactLijn(String[] splitLine) {
		this(parseLongSplit(splitLine[0]), parseDateTimeSplit(splitLine[1]), parseStringSplit(splitLine[2]),
				parseStringSplit(splitLine[4]), parseStringSplit(splitLine[5]));
	}

	public long getContactID() {
		return contactID;
	}

	public LocalDateTime getContactDatum() {
		return contactDatum;
	}

	public String[] getOmschrijving() {
		return omschrijving;
	}

	public String getDISnaam() {
		return DISnaam;
	}

	public String getSuperviserendeArts() {
		return superviserendeArts;
	}

	public ArrayList<ContactData> getContactData() {
		return contactData;
	}

	public void addContactData(String[] splitline) {
		contactData.add(new ContactData(splitline));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactDatum == null) ? 0 : contactDatum.hashCode());
		result = prime * result + (int) (contactID ^ (contactID >>> 32));
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
		ContactLijn other = (ContactLijn) obj;
		if (contactDatum == null) {
			if (other.contactDatum != null)
				return false;
		} else if (!contactDatum.equals(other.contactDatum))
			return false;
		if (contactID != other.contactID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContactLijn [" + contactID + " " + contactDatum + " " + contactData.size() + "contacten]";
	}
}