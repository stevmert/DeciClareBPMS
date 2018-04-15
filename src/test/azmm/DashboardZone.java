package test.azmm;

public class DashboardZone extends ReadableItem {

	private int id;//1 tot 169 (uniek) //=vraagID en ficheID, maar hoe te linken aan aparte ids in triage???
	private int lnkDashboardAfdelingId;//1 tot 41 (niet uniek) //TODO: komen niet overeen met andere afdelingIDs in triage (76 tot 118)???
	private String naam;
	//	private int vorm;//0, 1 of 2 //TODO: nutteloos?
	//	private int positionering;//TODO: altijd 0???
	private int aantalKamers;//5 //0 tot 12
	//	private boolean collapsible;//TODO: nutteloos?
	//	private boolean toonNaam;//TODO: nutteloos?
	//	private boolean toonKader;//TODO: nutteloos?
	private int volgnummer;//TODO???
	//	private boolean toonAantalPatienten;//10 //TODO: nutteloos?
	//	private int boven;//0, 25, 26 of 762 //TODO: nutteloos?
	//	private int links;//0, 25, 302 of 1150 //TODO: nutteloos?
	//	private int breed;//0 of 262 //TODO: nutteloos?
	//	private int hoog;//0, 80, 530 of 605 //TODO: nutteloos?
	private boolean actief;//15
	//	private LocalDateTime createDate;//TODO: nutteloos?
	//	private int lnkCreateUserId;//492 of 1106 //TODO: nutteloos?
	//	private LocalDateTime lastUpdateDate;//TODO: nutteloos?
	//	private int lnkLastUpdateUserId;//TODO: nutteloos?
	private Integer overdrachtsbladZone;//20 //1, 2, 5 of null //TODO???

	public DashboardZone(int id, int lnkDashboardAfdelingId, String naam, int aantalKamers, int volgnummer,
			boolean actief, Integer overdrachtsbladZone) {
		super();
		this.id = id;
		this.lnkDashboardAfdelingId = lnkDashboardAfdelingId;
		this.naam = naam;
		this.aantalKamers = aantalKamers;
		this.volgnummer = volgnummer;
		this.actief = actief;
		this.overdrachtsbladZone = overdrachtsbladZone;
	}

	public DashboardZone(String[] splitline) {
		this((int) parseIntegerSplit(splitline[0]), (int) parseIntegerSplit(splitline[1]), parseStringSplit(splitline[2]),
				(int) parseIntegerSplit(splitline[5]), (int) parseIntegerSplit(splitline[9]), (boolean) parseBooleanSplit(splitline[15]),
				parseIntegerSplit(splitline[20]));
	}

	public int getId() {
		return id;
	}

	public int getLnkDashboardAfdelingId() {
		return lnkDashboardAfdelingId;
	}

	public String getNaam() {
		return naam;
	}

	public int getAantalKamers() {
		return aantalKamers;
	}

	public int getVolgnummer() {
		return volgnummer;
	}

	public boolean isActief() {
		return actief;
	}

	public Integer getOverdrachtsbladZone() {
		return overdrachtsbladZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		DashboardZone other = (DashboardZone) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DashboardZone [" + id + "]";
	}
}