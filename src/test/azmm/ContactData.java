package test.azmm;

import java.util.Arrays;

import test.azmm.textmining.WordProcessor;

public class ContactData {

	private String[] tekst;//text mining...
	private String contactVak;//TODO: =naam uitgevoerde activiteit?

	public ContactData(String tekst, String contactVak) {
		super();
		this.tekst = WordProcessor.getInstance("alg").processText(tekst);
		this.contactVak = contactVak;
	}

	public ContactData(String[] splitline) {
		this(splitline[1], splitline[2]);
	}

	public String[] getTekst() {
		return tekst;
	}

	public String getContactVak() {
		return contactVak;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactVak == null) ? 0 : contactVak.hashCode());
		result = prime * result + Arrays.hashCode(tekst);
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
		ContactData other = (ContactData) obj;
		if (contactVak == null) {
			if (other.contactVak != null)
				return false;
		} else if (!contactVak.equals(other.contactVak))
			return false;
		if (!Arrays.equals(tekst, other.tekst))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContactData [" + contactVak + "]";
	}
}