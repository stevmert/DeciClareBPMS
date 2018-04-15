package util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class StringManager {

	public static String clean_normal(String input) {
		if(input == null)
			return null;
		return respace(input
				.replaceAll(" ", " ")
				.replaceAll("\r", "")
				.replaceAll("\t", "")
				.replaceAll("\n", "")
				.replaceAll("_", " ")
				.replaceAll(" - ", " ")
				.replaceAll(" – ", " ")
				.replaceAll("–", " ")
				.replaceAll("-", " ")
				.replaceAll("&ndash;", " ")
				.replaceAll(": ", " ").replaceAll(":", "")
				.replaceAll("\\.", " ")
				.replaceAll("\\(", " ").replaceAll("\\)", " ")
				.replaceAll("\\[", " ").replaceAll("\\]", " ")
				.replaceAll("\\{", " ").replaceAll("\\}", " ")
				.replaceAll("\\^", " ")
				.replaceAll("\\+", " ")
				.replaceAll("\"", " ")
				.replaceAll("!", "")
				.replaceAll("\\?", "")
				.replaceAll(";", " ")
				.trim());
	}

	public static String clean_searchable(String input) {
		if(input == null)
			return null;
		return respace(clean_normal(input)
				.replaceAll("&amp;", " & ")
				.replaceAll("amp;", "&")
				.replaceAll(" â€“ ", " ")
				.replaceAll("â€“", " ")
				.replaceAll(",", " ")
				.replace("=", " ")
				.replaceAll("/", " ")
				.replaceAll("\\\\", " ")
				.replaceAll("\\?", " ")
				.replaceAll("!", "")
				.replaceAll(" swelog ", " ")
				.replaceAll("’", "'")
				.replaceAll("´", "'")
				.replaceAll("&rsquo;", "'")
				.replaceAll("@", " ")
				.trim());
	}

	public static String clean_complete(String input) {
		if(input == null)
			return null;
		input = clean_specialLetters(input).toLowerCase()
				.replaceAll("\\$", "s")
				.replaceAll("&", " and ")
				.replaceAll(" ", " ");
		try {
			input = new String(input.getBytes("US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return respace(clean_searchable(input).replaceAll("'", ""));
	}

	public static String clean_no_formatting(String input) {
		if(input == null)
			return null;
		return respace(clean_complete(input)
				.replaceAll("'", "")
				.trim());
	}

	public static String respace(String input) {
		if(input == null)
			return null;
		while(input.contains("  "))
			input = input.replaceAll("  ", " ");
		return input;
	}

	private static int yearMax = Calendar.getInstance().get(Calendar.YEAR);
	public static String cleanYears(String input) {
		if(input == null || input.length() < 6)
			return input;
		int year = 1960;
		for(int i = year; i < yearMax+1; i++)
			input = input.replaceAll(""+i, " ");
		return respace(input);
	}

	public static String cleanFishHooks(String input) {
		String result = "";
		while(input.contains("<")) {
			result += input.substring(0, input.indexOf("<"));
			input = input.substring(input.indexOf(">")+1);
		}
		return new String(result + " " + input).trim();
	}

	public static String cleanFishHookTags(String input, String... tags) {
		String result = input;
		for(String tag : tags) {
			result = result.replace("<\\" + tag + ">", " ");
			while(result.contains("<" + tag + " ") || result.contains("<" + tag + ">")) {
				int start = result.indexOf("<" + tag + " ");
				int start2 = result.indexOf("<" + tag + ">");
				if(start == -1 && start2 == -1)
					throw new IllegalArgumentException();
				if(start == -1 && start2 != -1)
					start = start2;
				else if(start2 != -1 && start2 < start)
					start = start2;
				int end = result.substring(start+1).indexOf("</" + tag + ">") + ("</" + tag + ">").length() + start+1;
				String removable = result.substring(start, end);
				result = result.replace(removable, " ");
			}
		}
		return new String(result);
	}

	public static String clean_specialLetters(String input) {
		return input.replaceAll("ę", "e")
				.replaceAll("é", "e")
				.replaceAll("è", "e")
				.replaceAll("ê", "e")
				.replaceAll("ë", "e")
				.replaceAll("ã", "a")
				.replaceAll("à", "a")
				.replaceAll("á", "a")
				.replaceAll("ä", "a")
				.replaceAll("â", "a")
				.replaceAll("ç", "c")
				.replaceAll("ù", "u")
				.replaceAll("ú", "u")
				.replaceAll("û", "u")
				.replaceAll("ü", "u")
				.replaceAll("ñ", "n")
				.replaceAll("î", "i")
				.replaceAll("ï", "i")
				.replaceAll("í", "i")
				.replaceAll("ì", "i")
				.replaceAll("ö", "o")
				.replaceAll("ô", "o")
				.replaceAll("ó", "o")
				.replaceAll("ò", "o")
				.replace("ñ", "n");
	}

	public static boolean startsWithTerm(String title, String searchTerm) {
		if(title == null || searchTerm == null)
			return false;
		title = " " + clean_complete(cleanYears(title).toLowerCase()).trim()+ " ";
		if(title.trim().equals("") || searchTerm.equals(""))
			return false;
		if(title.startsWith(" the "))
			title = title.substring(" the".length());
		searchTerm = clean_complete(cleanYears(searchTerm).toLowerCase()).trim();
		if(searchTerm.startsWith("the "))
			searchTerm = searchTerm.substring("the ".length());
		if(title.startsWith(" " + searchTerm + " "))
			return true;
		if(searchTerm.contains(" ") && title.replaceAll(" ", "").startsWith(searchTerm.replaceAll(" ", ""))) {
			String tmp = title.replaceAll(" ", "").substring(title.indexOf(searchTerm.replaceAll(" ", ""))+searchTerm.replaceAll(" ", "").length());
			return title.contains(tmp.substring(0, 1) + " " + tmp.substring(1, 2));
		}
		return false;
	}

	public static boolean containsTerm(String title, String searchTerm) {
		if(title == null || searchTerm == null)
			return false;
		title = " " + clean_complete(title.toLowerCase()).trim()+ " ";
		searchTerm = clean_complete(searchTerm.toLowerCase()).trim();
		if(title.trim().equals("") || searchTerm.equals(""))
			return false;
		if(title.contains(" " + searchTerm + " "))
			return true;
		if(searchTerm.contains(" ") && title.replaceAll(" ", "").contains(searchTerm.replaceAll(" ", ""))) {
			try {
				String tmp = title.replaceAll(" ", "").substring(title.indexOf(searchTerm.replaceAll(" ", ""))+searchTerm.replaceAll(" ", "").length());
				return title.contains(tmp.substring(0, 1) + " " + tmp.substring(1, 2));
			} catch(Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean contains(String cleanWhitespacedTitle, String cleanWhitespacedSearchTerm) {
		return contains_Direct(cleanWhitespacedTitle, cleanWhitespacedSearchTerm)
				|| contains_Indirect(cleanWhitespacedTitle, cleanWhitespacedSearchTerm);
	}

	public static boolean contains_Direct(String title, String searchterm) {
		return containsTerm(cleanYears(title), cleanYears(searchterm));
	}

	public static boolean contains_Indirect(String cleanWhitespacedTitle, String cleanWhitespacedSearchTerm) {
		cleanWhitespacedTitle = cleanYears(cleanWhitespacedTitle);
		cleanWhitespacedSearchTerm = cleanYears(cleanWhitespacedSearchTerm);
		if(cleanWhitespacedTitle == null || cleanWhitespacedSearchTerm == null
				|| (cleanWhitespacedTitle.trim().equals("") && !cleanWhitespacedSearchTerm.equals("")))
			return false;
		if(cleanWhitespacedSearchTerm.equals(""))
			return true;
		String[] searchTerms = cleanWhitespacedSearchTerm.trim().split(" ");
		if(searchTerms.length < 2)
			return false;

		boolean result = true;
		for(String st : searchTerms) {
			if(cleanWhitespacedTitle.contains(" " + st + " "))
				cleanWhitespacedTitle.replaceFirst(" " + st + " ", " ");
			else {
				result = false;
				break;
			}
		}

		return result;
	}

	public static int getNumberOfOccurences(String input, String toCheck) {
		if(input == null
				|| toCheck == null
				|| toCheck.equals(""))
			return 0;
		int occ = 0;
		while(input.contains(toCheck)) {
			occ++;
			input = input.substring(input.indexOf(toCheck) + toCheck.length());
		}
		return occ;
	}

	public static boolean isNumeric(String str) {  
		try {
			Double.parseDouble(str);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String upcaseFirstLetter(String string) {
		String string_onlyLetters = string.replaceAll("[^a-zA-Z0-9'$*çÇIýÿÝŸâäáàÂÄÀÁÊËÉÈêèéëöôòóÒÓÖÔÎÏÌÍîìíïúùûüÚÙÛÜñÑ!?@#]", " ");
		String string_noLetters = string.replaceAll("[a-zA-Z0-9'$*çÇIýÿÝŸâäáàÂÄÀÁÊËÉÈêèéëöôòóÒÓÖÔÎÏÌÍîìíïúùûüÚÙÛÜñÑ!?@#]", " ");

		String result = "";
		String[] split = string_onlyLetters.split(" ");
		for(String tmp : split) {
			result += " ";
			if(tmp.length() > 1) {
				if(tmp.substring(1,2).equals(tmp.substring(1,2).toUpperCase())
						&& isAlphabet(tmp.substring(1,2)))
					result += tmp;
				else
					result += tmp.substring(0,1).toUpperCase() + tmp.substring(1);
			} else
				result += tmp.toUpperCase();
		}
		if(result.length() > 0)
			result = result.substring(1);

		String result2 = "";
		for(int i = 0; i<string.length(); i++) {
			if(i < string_onlyLetters.length() && !" ".equals(""+string_onlyLetters.charAt(i)))
				result2 += result.charAt(i);
			else
				result2 += string_noLetters.charAt(i);
		}
		return respace(result2);
	}

	private static boolean isAlphabet(String name) {
		return name.matches("[a-zA-Z$*çÇIýÿÝŸâäáàÂÄÀÁÊËÉÈêèéëöôòóÒÓÖÔÎÏÌÍîìíïúùûüÚÙÛÜñÑ]+");
	}
}