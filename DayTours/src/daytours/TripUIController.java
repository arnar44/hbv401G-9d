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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author freyrdanielsson
 */
public class TripUIController implements Initializable {

    @FXML
    private Label jtitle;
    @FXML
    private TextArea jdescription;
    @FXML
    private Label jprice;
    @FXML
    private Label jdifficulty;
    @FXML
    private Label jlocation;
    @FXML
    private Label jduration;
    @FXML
    private TextField jpurchName;
    @FXML
    private TextField jpurchEmail;
    @FXML
    private ComboBox<?> jpurchQuantity;
    @FXML
    private Button jbookButton;
    @FXML
    private ListView<?> jreviewList;
    @FXML
    private Button jsubmitReview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void jbookTrip(ActionEvent event) {
    }

    @FXML
    private void jnewReview(ActionEvent event) {
    }
    
}
