package test.azmm;

public class DashboardManchester extends ReadableItem {

	private int id;//0 //TODO: ergens als foreign key gebruikt? Hoe linkt dit aan ficheID/vraagID???
	private Integer parent;//TODO: om vervolgvragen te definieren?
	private String naam;//TODO: =vraag?
	private String info;//TODO: =antwoord op vraag voor deze specifieke patient?
	private int volgorde;//TODO: volgorde van vraagstelling? nutteloos dan wrs...
	private Integer kleur;//5 //1, 2, 3, 4 of null //TODO: weer triagekleur maar dan per vraag?

	public DashboardManchester(int id, Integer parent, String naam, String info, int volgorde, Integer kleur) {
		super();
		this.id = id;
		this.parent = parent;
		this.naam = naam;
		this.info = info;
		this.volgorde = volgorde;
		this.kleur = kleur;
	}

	public DashboardManchester(String[] splitline) {
		this((int) parseIntegerSplit(splitline[0]), parseIntegerSplit(splitline[1]), parseStringSplit(splitline[2]),
				parseStringSplit(splitline[3]), (int) parseIntegerSplit(splitline[4]), parseIntegerSplit(splitline[5]));
	}

	public int getId() {
		return id;
	}

	public Integer getParent() {
		return parent;
	}

	public String getNaam() {
		return naam;
	}

	public String getInfo() {
		return info;
	}

	public int getVolgorde() {
		return volgorde;
	}

	public Integer getKleur() {
		return kleur;
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
		DashboardManchester other = (DashboardManchester) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DashboardManchester [" + id + "]";
	}
}