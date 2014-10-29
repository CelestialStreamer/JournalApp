package entryTree;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import journal.Entry;

public class EntryTreeItem extends TreeItem<String> {
	
	private Entry entry;
	
	public EntryTreeItem(Entry entry) {
		this.entry = entry;
	}

	@Override
	public ObservableList<TreeItem<String>> getChildren() {
		return super.getChildren();
	}

	@Override
	public String toString() {
		return entry.getDate();
	}
	
	public Entry getEntry() {
		return entry;
	}
}
