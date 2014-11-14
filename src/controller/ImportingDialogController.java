package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * A dialog window to display entries, scriptures and topics found while loading
 * a journal file.
 * 
 * @author Brian Woodruff
 *
 */
public class ImportingDialogController {
  @FXML
  private Label entries;

  @FXML
  private Label topics;

  @FXML
  private Label scriptures;

  /**
   * Set number of entries found.
   * 
   * @param count
   */
  public void setEntries(int count) {
    entries.setText(Integer.toString(count));
  }

  /**
   * Set number of topics found.
   * 
   * @param count
   */
  public void setTopics(int count) {
    topics.setText(Integer.toString(count));
  }

  /**
   * Set number of scriptures found.
   * 
   * @param count
   */
  public void setScriptures(int count) {
    scriptures.setText(Integer.toString(count));
  }
}
