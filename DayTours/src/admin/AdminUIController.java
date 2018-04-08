/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Arnar
 */
public class AdminUIController implements Initializable {

    @FXML
    private TextField jNotendaNafn;
    @FXML
    private Button jBaetaVid;
    @FXML
    private ListView<?> jUmsagnir;
    @FXML
    private Button jSamþykkjaOll;
    @FXML
    private Button jTilBaka;
    
    private String adminNotandi;
    private String adminpsw;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Ef ýtt er á "tilBaka" hnapp lokum við adminUI glugganum
     * @param event
     * @throws IOException 
     */
    public void tilBaka(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
}
