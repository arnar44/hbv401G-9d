package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class AdminUIController {

	// Event Listener on Button.onAction
	@FXML
	public void handleButtonAction(ActionEvent event) {
		try {
			Stage addTripStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("AddTripUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			addTripStage.setScene(scene);
			addTripStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
