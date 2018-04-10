/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import database.Gagnagrunnur;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Arnar
 */
public class ReviewNanarUIController implements Initializable {

    @FXML
    private Button jTilBaka;
    @FXML
    private AnchorPane reviewDialog;
    
    private Gagnagrunnur db;
    private int reviewId;
    @FXML
    private TextField jTourID;
    @FXML
    private TextField jNafn;
    @FXML
    private TextField jEmail;
    @FXML
    private TextField jDate;
    @FXML
    private TextArea jReview;
    @FXML
    private Button jHafna;
    @FXML
    private Button jAccept;
    @FXML
    private AdminUIController parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * Birtir Dialog sem sýnir nánari upplýsingar um review
     * @param id
     * @param gagnagrunnur
     * @throws SQLException 
     */
    public void birtaReview(int id, Gagnagrunnur gagnagrunnur, AdminUIController gotParent) throws SQLException {
        parent = gotParent;
        db = gagnagrunnur;
        reviewId = id;
        
        upphafsstilla();
        //Sýna AdminUI diolog-ið
        DialogPane p = new DialogPane();
        reviewDialog.setVisible(true);
        p.setContent(reviewDialog);
        Dialog<ButtonType> d = new Dialog();
        d.setDialogPane(p);
        
        // Loka glugga þegar ýtt er á exit takka í horni
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        
        d.showAndWait();
    }
    
    /**
     * Sækir review-ið ú gagnagrunni og birtir
     * @throws SQLException 
     */
    private void upphafsstilla() throws SQLException{
        ResultSet result = db.getAdminReview(reviewId);
        if(!result.next()){
            //TODO villa kom upp, review finnst ekki
        }
        //Birta review
        jTourID.setText(result.getString("tourId"));
        jNafn.setText(result.getString("name"));
        jEmail.setText(result.getString("email"));
        jDate.setText(result.getString("date"));
        jReview.setText(result.getString("review"));
    }
    
     /**
     * Ef ýtt er á "tilBaka" hnapp lokum við review glugganum
     * @param event
     * @throws IOException 
     */
    @FXML
    public void tilBaka(ActionEvent event) {
        //loka admin glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Ef ýtt er á "accept" samþykkjum við review-ið og lokum glugga
     * @param event
     * @throws IOException 
     */
    @FXML
    public void accept(ActionEvent event) throws SQLException {
        //samþykkja review
        db.confirmReview(reviewId);
        //Erum búin að samþykkja eitt review, það dettur þá úr lista á parent (AdminUI)
        parent.refresh();
        //loka reviewUI glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Ef ýtt er á "decline" höfnum við review-inu og lokum glugga
     * @param event
     * @throws IOException 
     */
    @FXML
    public void decline(ActionEvent event) throws SQLException {
        //samþykkja review
        db.deleteReview(reviewId);
        //Erum búin að samþykkja eitt review, það dettur þá úr lista á parent (AdminUI)
        parent.refresh();
        //loka reviewUI glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
