package journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A container for a string content with a date and list of scripture matches.
 * Can return a list of matching topics found in content.
 * 
 * @author Brian Woodruff
 *
 */
public class Entry {
	private String content;
	private String date;
	private List<Scripture> scriptures;

	private static Map<String, String> topics = new HashMap<String, String>();

	/**
	 * Call before the class is used to be able to find topics inside content.
	 * Input is a map of phrases that map to a topic.
	 * 
	 * @param topicMap
	 */
	public static void setTopics(Map<String, String> topicMap) {
		topics = topicMap;
	}

	/**
	 * Initialize with string content and string date. No verification of any
	 * kind is made.
	 * 
	 * @param content
	 * @param date
	 */
	public Entry(String content, String date) {
		this.content = content;
		this.date = date;
		scriptures = Scripture.getScriptures(content);
	}

	/**
	 * Returns the date.
	 * 
	 * @return
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns a list of topics. This is the same list that was set when the
	 * class was initialized.
	 * 
	 * @return
	 */
	public List<String> getTopics() {
		return new ArrayList<String>(topics.values());
	}

	/**
	 * Returns a list of all scriptures found in the contents.
	 * 
	 * @return
	 */
	public List<Scripture> getScriptures() {
		return scriptures;
	}

	/**
	 * Returns the content.
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content. Re-parses for scriptures.
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
		scriptures = Scripture.getScriptures(content);
	}

	/**
	 * Returns a string in the format of
	 * Date: <date>
	 * Content:
	 * <content>
	 * 
	 */
	public String toString() {
		return "Date: " + date + "\nContent:\n" + content;
	}
}
