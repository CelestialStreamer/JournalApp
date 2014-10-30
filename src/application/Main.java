package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * 
 * @author Brian Woodruff
 *
 */
public class Main extends Application {
    /**
     * This is where the application starts.
     * 
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
	try {
	    Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Journal Application");
	    primaryStage.show();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * 
     */
    @Override
    public void init() throws Exception {
	super.init();
    }

    /**
     * 
     */
    @Override
    public void stop() throws Exception {
	super.stop();
    }

    /**
     * Application is launched form here.
     * 
     * @param args
     */
    public static void main(String[] args) {
	launch(args);
    }
}
