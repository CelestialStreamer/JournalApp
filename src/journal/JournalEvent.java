package journal;

/**
 * Journal event for loading file in Journal
 * 
 * @author Brian Woodruff
 *
 */
@SuppressWarnings("serial")
public class JournalEvent extends java.util.EventObject {
    private int entries;
    private int scriptures;
    private int topics;

    /**
     * Construct journal event with number of entries, scriptures and topics
     * 
     * @param source
     * @param entries
     * @param scriptures
     * @param topics
     */
    public JournalEvent(Object source, int entries, int scriptures, int topics) {
	super(source);

	this.entries = entries;
	this.scriptures = scriptures;
	this.topics = topics;
    }

    /**
     * Get number of entries
     * 
     * @return entries
     */
    public int getEntries() {
	return entries;
    }

    /**
     * Get number of scriptures
     * 
     * @return scriptures
     */
    public int getScriptures() {
	return scriptures;
    }

    /**
     * Get number of topics
     * 
     * @return topics
     */
    public int getTopics() {
	return topics;
    }
}
