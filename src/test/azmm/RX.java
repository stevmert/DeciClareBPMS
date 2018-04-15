package test.azmm;

import java.time.LocalDateTime;
import java.util.Arrays;

import test.azmm.textmining.WordProcessor;

public class RX extends ReadableItem {

	private LocalDateTime dateTime;//meestal enkel dag -> onbetrouwbaar...
	private String[] beschrijving;//text mining

	public RX(LocalDateTime dateTime, String beschrijving) {
		super();
		this.dateTime = dateTime;
		this.beschrijving = WordProcessor.getInstance("alg").processText(beschrijving);
	}

	public RX(String[] splitLine) {
		this(parseDateTimeSplit(splitLine[3]), parseStringSplit(splitLine[2]));
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String[] getBeschrijving() {
		return beschrijving;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(beschrijving);
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
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
		RX other = (RX) obj;
		if (!Arrays.equals(beschrijving, other.beschrijving))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RX [" + dateTime + "]";
	}
}