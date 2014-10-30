package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import journal.Entry;

/**
 * 
 * Custom TreeItem that stores an Entry item with control over what text is
 * displayed for the 'title' or whatever, and keeps access to the stored entry
 * for future editing.
 * 
 * @author Brian Woodruff
 *
 */
public class EntryTreeItem extends TreeItem<String> {
    private Entry entry;

    /**
     * Constructor that takes an entry as a parameter and stores it inside.
     * 
     * @param entry
     */
    public EntryTreeItem(Entry entry) {
	this.entry = entry;
    }

    /**
     * Return super.getChildren()
     * 
     * @return ObservableList<TreeItem<String>>
     */
    @Override
    public ObservableList<TreeItem<String>> getChildren() {
	return super.getChildren();
    }

    /**
     * Return the date of the entry. This will be the visible text for the tree
     * item.
     * 
     * @return String
     */
    @Override
    public String toString() {
	return entry.getDate();
    }

    /**
     * Returns the entry.
     * 
     * @return Entry
     */
    public Entry getEntry() {
	return entry;
    }
}
