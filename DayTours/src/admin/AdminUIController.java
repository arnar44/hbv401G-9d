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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import database.Gagnagrunnur;
import javafx.stage.Window;

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
    private Gagnagrunnur db;
    @FXML
    private AnchorPane adminDialog;
    @FXML
    private AddTripUIController addTripDialogController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Gagnagrunnur();
    }
    
    /**
     * Ef ýtt er á "tilBaka" hnapp lokum við adminUI glugganum
     * @param event
     * @throws IOException 
     */
    @FXML
    public void tilBaka(ActionEvent event) {
        //loka admin glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    public void buaTilFerd(ActionEvent event) {
        // EF við komumst hingað var rétt notendanaf & lykilorð slegið inn, birta adminUI   
        addTripDialogController.birtaAddTrip();
    }
    
    public void birtaAdminUI(String username){
        //Sýna AdminUI diolog-ið
        DialogPane p = new DialogPane();
        adminDialog.setVisible(true);
        p.setContent(adminDialog);
        Dialog<ButtonType> d = new Dialog();
        d.setDialogPane(p);
        
        // Loka glugga þegar ýtt er á exit takka í horni
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        
        jNotendaNafn.setText(username);
        
        d.showAndWait();
    }
    
}
