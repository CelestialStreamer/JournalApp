package entryTree;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.util.Pair;

/**
 * 
 * @author Brian Woodruff
 *
 */
public class EntryTreeCell extends TreeCell<Pair<String, String>> implements Action {

    /**
     * 
     */
    @Override
    public void startEdit() {
	super.startEdit();
    }

    /**
     * 
     */
    @Override
    public void cancelEdit() {
	super.cancelEdit();
    }

    /**
     * 
     */
    @Override
    public ObservableList<Node> getChildren() {
	return super.getChildren();
    }

    /**
     * 
     */
    @Override
    protected void updateItem(Pair<String, String> item, boolean empty) {
	super.updateItem(item, empty);
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * 
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    /**
     * 
     */
    @Override
    public Object getValue(String key) {
	return null;
    }

    /**
     * 
     */
    @Override
    public boolean isEnabled() {
	return false;
    }

    /**
     * 
     */
    @Override
    public void putValue(String key, Object value) {
    }

    /**
     * 
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    /**
     * 
     */
    @Override
    public void setEnabled(boolean b) {
    }
}