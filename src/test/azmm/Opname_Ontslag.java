package test.azmm;

import java.time.LocalDateTime;

public class Opname_Ontslag extends ReadableItem {

	private int teller;
	private OpnameType type;
	private LocalDateTime opnameDatum;
	private LocalDateTime ontslagDatum;
	private LocalDateTime transferDatum;
	private String afdelingCode;
	private String afdeling;//TODO: beide nodig?

	private String zvnam;//achternaam dokter/departement (niet betrouwbaar: voorspelling bij opname, dus kan fout zijn...)
	private String zvvnm;//voornaam dokter/departement (niet betrouwbaar: voorspelling bij opname, dus kan fout zijn...
	private String zvriz;//TODO???
	private String dokzv;//TODO: niet ID van dokter/departement in User-tabel, dus wat is dit dan wel???

	public Opname_Ontslag(int teller, OpnameType type, LocalDateTime opnameDatum, LocalDateTime ontslagDatum,
			LocalDateTime transferDatum, String afdelingCode, String afdeling, String zvnam, String zvvnm,
			String zvriz, String dokzv) {//, HashMap<Integer, Resource> availableResources) {TODO: link zvvnm+zvnam met user?
		this.teller = teller;
		this.type = type;
		this.opnameDatum = opnameDatum;
		this.ontslagDatum = ontslagDatum;
		this.transferDatum = transferDatum;
		this.afdelingCode = afdelingCode;
		this.afdeling = afdeling;
		this.zvnam = zvnam;
		this.zvvnm = zvvnm;
		this.zvriz = zvriz;
		this.dokzv = dokzv;
	}

	public Opname_Ontslag(String[] splitLine) {//, HashMap<Integer, Resource> availableResources) {
		this(parseIntegerSplit(splitLine[1]), OpnameType.valueOf(parseIntegerSplit(splitLine[2])),
				parseDateTimeSplit(splitLine[7]), parseDateTimeSplit(splitLine[8]), parseDateTimeSplit(splitLine[5]),
				parseStringSplit(splitLine[3]), parseStringSplit(splitLine[4]), parseStringSplit(splitLine[9]),
				parseStringSplit(splitLine[10]), parseStringSplit(splitLine[11]), parseStringSplit(splitLine[12]));//,
		//availableResources);
	}

	public int getTeller() {
		return teller;
	}

	public OpnameType getType() {
		return type;
	}

	public LocalDateTime getOpnameDatum() {
		return opnameDatum;
	}

	public LocalDateTime getOntslagDatum() {
		return ontslagDatum;
	}

	public LocalDateTime getTransferDatum() {
		return transferDatum;
	}

	public String getAfdelingCode() {
		return afdelingCode;
	}

	public String getAfdeling() {
		return afdeling;
	}

	public String getZvnam() {
		return zvnam;
	}

	public String getZvvnm() {
		return zvvnm;
	}

	public String getZvriz() {
		return zvriz;
	}

	public String getDokzv() {
		return dokzv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + teller;
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
		Opname_Ontslag other = (Opname_Ontslag) obj;
		if (teller != other.teller)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Opname_Ontslag [teller=" + teller + ", transferDatum=" + transferDatum + "]";
	}
}