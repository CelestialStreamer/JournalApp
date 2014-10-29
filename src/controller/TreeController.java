package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<String> treeview;

    public void setTreeRoot(TreeItem<String> root) {
    	treeview.setRoot(root);
    }
    
    @FXML
    void initialize() {
        assert treeview != null : "fx:id=\"treeview\" was not injected: check your FXML file 'TreeView.fxml'.";
    }
}
