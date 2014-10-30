package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogController {
	
	public static void showDialog(String message) {
		System.out.println("Dialog Controller!");
		
		AnchorPane anchorpane = new AnchorPane();
		
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0,0,5,0));
		
		AnchorPane textPane = new AnchorPane();
		textPane.setPrefSize(200, 44);
		
		
		Label messageLabel = new Label(message);
		messageLabel.setPrefSize(200, 58);
		messageLabel.setAlignment(Pos.CENTER);
		
		Button button = new Button();
		button.setPrefSize(75, 25);
		button.setText("Close");
		button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent arg0) {
	    		messageLabel.getScene().getWindow().hide();
	        }
	    });
		
		anchorpane.getChildren().add(vbox);
		vbox.getChildren().add(textPane);
		vbox.getChildren().add(button);
		textPane.getChildren().add(messageLabel);
		
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setScene(new Scene(anchorpane));
		dialogStage.show();
	}
}
