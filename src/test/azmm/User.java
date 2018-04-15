package test.azmm;

public class User extends ReadableItem {

	private final int id;
	private final String voornaam;
	private final String achternaam;

	//		if(split.length != 3
	//						|| split[2].equalsIgnoreCase("null")
	//						|| split[1].equalsIgnoreCase("null"))
	//					other.add(line);//TODO: wegdoen indien null;null??? Worden die gebruikt in andere data?
	//				else
	//					doctors.add(line);
	//				System.out.println(line);
	//
	//TODO: nog in lijst van doctors:
	//3650;ASS.;IZ GERIATRIE
	//210;MM;Sterilisatie
	//214;Spoedafdeling;Sint-Jozef
	//267;ASS;ENDOCRINOLOGIE
	//270;assoc;oncologie-Hematologie
	//380;ASS.;IZ ENDOCRINOLOGIE
	//...

	//86;Fran?ois;Bart
	//		for(String l : doctors) {
	//			String input = new String(l.getBytes("US-ASCII"));
	//			if(input.contains("?"))
	//				System.out.println(l);
	//		}

	public User(int id, String achternaam, String voornaam) {
		super();
		this.id = id;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
	}

	public User(String[] splitLine) {
		this(parseIntegerSplit(splitLine[0]), parseStringSplit(splitLine[1]),
				splitLine.length==3?parseStringSplit(splitLine[2]):null);
	}

	public int getId() {
		return id;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public String getAchternaam() {
		return achternaam;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + ";" + achternaam + ";" + voornaam;
	}
}