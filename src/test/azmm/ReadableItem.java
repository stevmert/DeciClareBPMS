package test.azmm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ReadableItem {

	protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	protected static String parseStringSplit(String s) {
		return (s.trim().length()==0 || s.trim().equalsIgnoreCase("null"))?null:s.trim();
	}

	protected static Integer parseIntegerSplit(String s) {
		return s.trim().equalsIgnoreCase("null")?null:Integer.parseInt(s.trim());
	}

	protected static Long parseLongSplit(String s) {
		return s.trim().equalsIgnoreCase("null")?null:Long.parseLong(s.trim());
	}

	protected static LocalDateTime parseDateTimeSplit(String s) {
		return s.trim().equalsIgnoreCase("null")?null:
			LocalDateTime.parse(s.trim().substring(0, s.trim().lastIndexOf(".")), FORMATTER);
	}

	protected static Boolean parseBooleanSplit(String s) {
		Integer v = parseIntegerSplit(s);
		if(v == null)
			return null;
		return v == 1;
	}
}