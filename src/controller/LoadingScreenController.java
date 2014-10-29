package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingScreenController {

    @FXML
    private Label entries;

    @FXML
    private Label topics;

    @FXML
    private Label scriptures;

    @FXML
    private ProgressBar progressbar;
    
    public void setEntries(int count) {
    	entries.setText(Integer.toString(count));
    }

    public void setTopics(int count) {
    	topics.setText(Integer.toString(count));
    }
    
    public void setScriptures(int count) {
    	scriptures.setText(Integer.toString(count));
    }
    
    public void setProgress(double progress) {
    	if (progress < 0 || progress > 1) {
    		return;
    	}
    	progressbar.setProgress(progress);
    }
}
