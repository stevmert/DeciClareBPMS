package test.azmm;

import java.time.LocalDateTime;

public class Wachtijden extends ReadableItem {

	private LocalDateTime datumResponsAfdeling;
	private int lnkRedenNietAfhalenId;
	private boolean actief;
	private LocalDateTime createDate;
	private int lnkCreateUserId;
	private LocalDateTime lastUpdateDate;
	private int lnkLastUpdateUserId;
	private Integer tijdstipAfhalen;
	private int id;//TODO???
	//	private String formNaam;//TODO: nutteloos?
	//	private String controlNaam;//TODO: nutteloos?
	private int volgorde;//TODO???
	private WachttijdType omschrijving;//TODO: nut?

	public Wachtijden(LocalDateTime datumResponsAfdeling, int lnkRedenNietAfhalenId, boolean actief,
			LocalDateTime createDate, int lnkCreateUserId, LocalDateTime lastUpdateDate, int lnkLastUpdateUserId,
			Integer tijdstipAfhalen, int id,
			//			String formNaam, String controlNaam,
			int volgorde, String omschrijving) {
		super();
		this.datumResponsAfdeling = datumResponsAfdeling;
		this.lnkRedenNietAfhalenId = lnkRedenNietAfhalenId;
		this.actief = actief;
		this.createDate = createDate;
		this.lnkCreateUserId = lnkCreateUserId;
		this.lastUpdateDate = lastUpdateDate;
		this.lnkLastUpdateUserId = lnkLastUpdateUserId;
		this.tijdstipAfhalen = tijdstipAfhalen;
		this.id = id;
		//		this.formNaam = formNaam;
		//		this.controlNaam = controlNaam;
		this.volgorde = volgorde;
		this.omschrijving = WachttijdType.parse(omschrijving);
	}

	public Wachtijden(String[] splitLine) {
		this(parseDateTimeSplit(splitLine[2]), parseIntegerSplit(splitLine[3]), parseBooleanSplit(splitLine[4]),
				parseDateTimeSplit(splitLine[5]), parseIntegerSplit(splitLine[6]), parseDateTimeSplit(splitLine[7]),
				parseIntegerSplit(splitLine[8]), parseIntegerSplit(splitLine[9]), parseIntegerSplit(splitLine[10]),
				//				parseStringSplit(splitLine[11]), parseStringSplit(splitLine[12]),
				parseIntegerSplit(splitLine[13]),
				parseStringSplit(splitLine[14]));
	}

	public LocalDateTime getDatumResponsAfdeling() {
		return datumResponsAfdeling;
	}

	public int getLnkRedenNietAfhalenId() {
		return lnkRedenNietAfhalenId;
	}

	public boolean isActief() {
		return actief;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public int getLnkCreateUserId() {
		return lnkCreateUserId;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public int getLnkLastUpdateUserId() {
		return lnkLastUpdateUserId;
	}

	public Integer getTijdstipAfhalen() {
		return tijdstipAfhalen;
	}

	public int getId() {
		return id;
	}

	//	public String getFormNaam() {
	//		return formNaam;
	//	}
	//
	//	public String getControlNaam() {
	//		return controlNaam;
	//	}

	public int getVolgorde() {
		return volgorde;
	}

	public WachttijdType getOmschrijving() {
		return omschrijving;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (actief ? 1231 : 1237);
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((datumResponsAfdeling == null) ? 0 : datumResponsAfdeling.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + lnkCreateUserId;
		result = prime * result + lnkLastUpdateUserId;
		result = prime * result + lnkRedenNietAfhalenId;
		result = prime * result + ((omschrijving == null) ? 0 : omschrijving.hashCode());
		result = prime * result + ((tijdstipAfhalen == null) ? 0 : tijdstipAfhalen.hashCode());
		result = prime * result + volgorde;
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
		Wachtijden other = (Wachtijden) obj;
		if (actief != other.actief)
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (datumResponsAfdeling == null) {
			if (other.datumResponsAfdeling != null)
				return false;
		} else if (!datumResponsAfdeling.equals(other.datumResponsAfdeling))
			return false;
		if (id != other.id)
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (lnkCreateUserId != other.lnkCreateUserId)
			return false;
		if (lnkLastUpdateUserId != other.lnkLastUpdateUserId)
			return false;
		if (lnkRedenNietAfhalenId != other.lnkRedenNietAfhalenId)
			return false;
		if (omschrijving != other.omschrijving)
			return false;
		if (tijdstipAfhalen == null) {
			if (other.tijdstipAfhalen != null)
				return false;
		} else if (!tijdstipAfhalen.equals(other.tijdstipAfhalen))
			return false;
		if (volgorde != other.volgorde)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Wachtijden [" + datumResponsAfdeling + "]";
	}
}