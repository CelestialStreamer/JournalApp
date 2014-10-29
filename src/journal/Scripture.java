package journal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Brian Woodruff
 *
 */
public class Scripture {
	private String book = "";
	private String chapter = "";
	private String verse = "";

	private static String regexBooks = "";

	/**
	 * Call before using the class. Sets a list of valid scriptures for
	 * matching.
	 * 
	 * @param validBooks
	 */
	public static void setValidScriptures(List<String> validBooks) {
		String books = "";
		for (String book : validBooks) {
			if (validBooks.get(0).equals(book)) {
				books += "(" + book + ")";
			} else {
				books += "|(" + book + ")";
			}
		}
		regexBooks = "(" + books + ")";
	}

	/**
	 * Returns a list of scriptures that found in the content.
	 * 
	 * @param content
	 * @return
	 */
	public static List<Scripture> getScriptures(String content) {
		List<Scripture> scriptures = new ArrayList<Scripture>();
		Matcher match = Pattern.compile(
				regexBooks + " ([cC]hapter )?\\d+(:\\d+(\\-\\d+|, \\d+)*)?")
				.matcher(content);
		while (match.find()) {
			scriptures.add(new Scripture(match.group()));
		}
		return scriptures;
	}

	/**
	 * Returns the first scripture that is found in content.
	 * 
	 * @param content
	 * @return
	 */
	public static String getScripture(String content) {
		String scripture = "";
		Matcher match = Pattern.compile(
				regexBooks + " ([cC]hapter )?\\d+(:\\d+(\\-\\d+|, \\d+)*)?")
				.matcher(content);
		if (match.find()) {
			scripture = match.group();
		}
		scripture = scripture.replace("chapter", "");
		return scripture;
	}

	/**
	 * Construct a scripture from a string. Parses string for first valid
	 * scripture and stores book, chapter and verse as applicable.
	 * 
	 * @param content
	 */
	public Scripture(String content) {
		String contentCopy = getScripture(content);

		String chapterKey = "\\d+";
		String verseKey = "(?<=:)(\\d+(\\-\\d+|, \\d+)*)";

		Matcher matcher = Pattern.compile(regexBooks).matcher(contentCopy);
		if (matcher.find()) {
			book = matcher.group();
			contentCopy = contentCopy.replace(book, "");
			// Remove book for cases like '2 Nephi' because chapter will match
			// first integer.
		}

		matcher = Pattern.compile(chapterKey).matcher(contentCopy);
		if (matcher.find()) {
			chapter = matcher.group();
		}

		matcher = Pattern.compile(verseKey).matcher(contentCopy);
		if (matcher.find()) {
			verse = matcher.group();
		}
	}

	/**
	 * Construct a scripture from a book chapter and verse. Chapter and verse
	 * are ints.
	 * 
	 * @param book
	 * @param chapter
	 * @param verse
	 */
	public Scripture(String book, int chapter, int verse) {
		setBook(book);
		setChapter(chapter);
		setVerse(verse);
	}

	/**
	 * Construct a scripture from a book chapter and verse. All parameters are
	 * strings.
	 * 
	 * @param book
	 * @param chapter
	 * @param verse
	 */
	public Scripture(String book, String chapter, String verse) {
		setBook(book);
		setChapter(chapter);
		setVerse(verse);
	}

	/**
	 * Set book
	 * 
	 * @param book
	 */
	public void setBook(String book) {
		Matcher match = Pattern.compile(regexBooks).matcher(book);
		if (match.find()) {
			this.book = match.group();
		} else {
			this.book = "";
		}
	}

	/**
	 * Return book
	 * 
	 * @return
	 */
	public String getBook() {
		return book;
	}

	/**
	 * Set chapter
	 * 
	 * @param chapter
	 */
	public void setChapter(String chapter) {
		if (Integer.parseInt(chapter) > 0) {
			this.chapter = chapter;
		} else {
			this.chapter = "";
		}
	}

	/**
	 * Set chapter
	 * 
	 * @param chapter
	 */
	public void setChapter(int chapter) {
		if (chapter > 0) {
			this.chapter = Integer.toString(chapter);
		} else {
			this.chapter = "";
		}
	}

	/**
	 * Return chapter
	 * 
	 * @return
	 */
	public String getChapter() {
		return chapter;
	}

	/**
	 * Set verse
	 * 
	 * @param verse
	 */
	public void setVerse(String verse) {
		this.verse = verse;
	}

	/**
	 * Set verse Only works for positive integers. Use setVerse(String) if you
	 * need to apply a range of verses like 11-17.
	 * 
	 * @param verse
	 */
	public void setVerse(int verse) {
		if (verse > 0) {
			this.verse = Integer.toString(verse);
		} else {
			this.verse = "";
		}
	}

	/**
	 * Return verse
	 * 
	 * @return
	 */
	public String getVerse() {
		return verse;
	}

	/**
	 * Returns a string that includes verse only if one is provided. And a
	 * chapter if one is provided.
	 * Possible outputs:
	 * John 3:4
	 * John 3
	 * John
	 */
	public String toString() {
		if (chapter.length() > 0) {
			if (verse.length() > 0) {
				return book + " " + chapter + ":" + verse;
			} else {
				return book + " " + chapter;
			}
		} else {
			return book;
		}
	}

}
