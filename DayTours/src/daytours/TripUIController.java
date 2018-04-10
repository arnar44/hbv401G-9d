/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import database.Gagnagrunnur;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import model.Trip;
import utils.Book;

/**
 * FXML Controller class
 *
 * @author freyrdanielsson
 */
public class TripUIController implements Initializable {
    
    private Trip dayTour;
    private Gagnagrunnur db = new Gagnagrunnur();

    @FXML
    private TextField jpurchName;
    @FXML
    private TextField jpurchEmail;
    @FXML
    private ComboBox<Integer> jpurchQuantity;
    
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
    private AnchorPane tripDialog;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private void jbookTrip(ActionEvent event) {
        book();
    }

    private void jnewReview(ActionEvent event) {
        makeReview();
    }
    
    private void makeReview() {
        
    }
    
    private void book() {
        Book booking = new Book();
        
        String name = jpurchName.getText();
        String email = jpurchEmail.getText();
        int nbr = jpurchQuantity.getValue();
        int id = dayTour.getId();
        
        int[] validation;
        
        try {
            validation = booking.makeBooking(id, name, email, nbr);
        } catch (SQLException ex) {
            // Hér væri gott að alerta bara
            Logger.getLogger(TripUIController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        if(validation[0] == 1) {
            jpurchName.setStyle("-fx-background-color: red;");
        }
        if(validation[1] == 1) {
            jpurchEmail.setStyle("-fx-background-color: red;");
        }
    }
    
    public void setTrip(Trip trip) {
        dayTour = trip;
        
        DialogPane p = new DialogPane();
        tripDialog.setVisible(true);
        
        updateInfo(trip);
        
        p.setContent(tripDialog);
        
        Dialog<ButtonType> d = new Dialog();
        d.setDialogPane(p);
        
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        
        d.showAndWait();
    }
    
    public void updateInfo(Trip trip) {
        jtitle.setText(trip.getTitle());
        jdescription.setText(trip.getItinirary());
        jduration.setText(trip.getDuration());
        jdifficulty.setText(trip.getDifficulty());
        jlocation.setText(trip.getLocation());
        jprice.setText(trip.getPrice());
    }

    @FXML
    private void bookTrip(ActionEvent event) {
        book();
    }

    @FXML
    private void addReview(ActionEvent event) {
    }
}
