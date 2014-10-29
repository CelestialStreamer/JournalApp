package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class TabPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabpane;

    @FXML
    void initialize() {
        assert tabpane != null : "fx:id=\"tabpane\" was not injected: check your FXML file 'TabPaneView.fxml'.";

    }
}
