package entryTree;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.util.Pair;


public class EntryTreeCell extends TreeCell<Pair<String, String>> implements Action {
		
		@Override
		public void startEdit() {
			super.startEdit();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
		}

		@Override
		public ObservableList<Node> getChildren() {
			return super.getChildren();
		}

		@Override
		protected void updateItem(Pair<String, String> item, boolean empty) {
			super.updateItem(item, empty);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object getValue(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void putValue(String key, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setEnabled(boolean b) {
			// TODO Auto-generated method stub
			
		}

		
	}