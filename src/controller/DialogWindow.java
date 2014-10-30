package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Generate a dialog window that displays a message. Parent window gets focus
 * only after dialog window is closed.
 * 
 * @author Brian Woodruff
 *
 */
public class DialogWindow {

    /**
     * Generate and shows dialog window with message.
     * 
     * @param message
     * @param window
     */
    public static void showDialog(String message, Window window) {
	VBox vbox = new VBox();
	vbox.setAlignment(Pos.CENTER);
	vbox.setPadding(new Insets(0, 0, 10, 0));

	Label messageLabel = new Label(message);
	messageLabel.setPrefSize(400, 100);
	messageLabel.setAlignment(Pos.CENTER);
	messageLabel.setPadding(new Insets(10));
	messageLabel.setWrapText(true);
	

	Button button = new Button("Close");
	button.setPrefSize(75, 25);
	button.setOnAction((e) -> {
	    messageLabel.getScene().getWindow().hide();
	});

	vbox.getChildren().addAll(messageLabel, button);

	Stage dialogStage = new Stage();
	Scene scene = new Scene(vbox);

	dialogStage.initOwner(window);
	dialogStage.initModality(Modality.WINDOW_MODAL);
	dialogStage.setScene(scene);
	dialogStage.show();
    }
}
