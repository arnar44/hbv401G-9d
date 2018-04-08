/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author freyrdanielsson
 */
public class TripUIController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label price;
    @FXML
    private Label location;
    @FXML
    private Label duration;
    @FXML
    private Label difficulty;
    @FXML
    private TextArea description;
    @FXML
    private Label duration1;
    @FXML
    private Label duration11;
    @FXML
    private Label duration111;
    @FXML
    private TextField purchName;
    @FXML
    private TextField purchEmail;
    @FXML
    private ComboBox<?> purchQuantity;
    @FXML
    private Button bookButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void bookTrip(ActionEvent event) {
    }
    
}
