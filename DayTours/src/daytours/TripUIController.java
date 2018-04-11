/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import admin.AdminUIController;
import database.Gagnagrunnur;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import model.Review;
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
    private ObservableList<Integer> comboList = FXCollections.observableArrayList();
    private ObservableList<Ref> reviewList = FXCollections.observableArrayList();
    private ResultSet results;
    private int virkurIndex;
    private ArrayList<Ref> refArray;
    private Review review;

    @FXML
    private TextField jpurchName;
    @FXML
    private TextField jpurchEmail;
    @FXML
    private ChoiceBox<Integer> jpurchQuantity;

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
    @FXML
    private Label jcategory;
    @FXML
    private Label jmeet;
    @FXML
    private Label jpickup;
    @FXML
    private Label javailability;
    @FXML
    private ListView<Ref> jreviewList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboList.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        jpurchQuantity.setItems(comboList);
    }

    private void jbookTrip(ActionEvent event) {
        book();
    }

    private void jnewReview(ActionEvent event) {
        makeReview();
    }

    private void makeReview() {

    }

    /**
     * Refreshar lista
     *
     * @throws SQLException
     */
    public void refresh() throws SQLException {

        jreviewList.getItems().clear();
        jreviewList.getSelectionModel().clearSelection();
        List<Ref> selectedItemsCopy = new ArrayList<>(jreviewList.getSelectionModel().getSelectedItems());
        jreviewList.getItems().removeAll(selectedItemsCopy);

        updateResults();
        reviewList.addAll(updateList());

        jreviewList.setItems(reviewList);
    }

    /**
     * Uppfærir ResultSet frá gagnagrunni með öllum tours.
     *
     * @throws SQLException
     */
    private void updateResults() throws SQLException {
        results = db.getReviews(dayTour.getId());
    }

    /**
     * Uppfærir lista með öllum titles úr ResultSet
     *
     * @return ObservableList
     * @throws SQLException
     */
    private ArrayList<Ref> updateList() throws SQLException {
        refArray = new ArrayList<Ref>();
        ResultSet rs = results;
        while (rs.next()) {
            String name = rs.getString("name");
            int id = rs.getInt("id");
            String date = rs.getString("date");
            referanceArray(id, name + " - " + date);
        }
        return refArray;
    }

    /**
     * Setur id og title í array með samsvarandi index, miðað við lista.
     *
     * @param id
     * @param title
     * @param index
     */
    private void referanceArray(int id, String title) {
        refArray.add(new Ref(id, title));
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

        if (validation[0] == 1) {
            jpurchName.setStyle("-fx-background-color: red;");
        }
        if (validation[1] == 1) {
            jpurchEmail.setStyle("-fx-background-color: red;");
        }
    }

    public void setTrip(Trip trip) {
        jpurchQuantity.getSelectionModel().selectFirst();
        dayTour = trip;

        DialogPane p = new DialogPane();
        tripDialog.setVisible(true);

        updateInfo(trip);
        try {
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        initializeRevList();

        p.setContent(tripDialog);

        Dialog<ButtonType> d = new Dialog();
        d.setDialogPane(p);

        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        d.showAndWait();
    }

    private void initializeRevList() {
        MultipleSelectionModel<Ref> lsm = (MultipleSelectionModel<Ref>) jreviewList.getSelectionModel();
        lsm.selectedItemProperty().addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                // Indexinn í listanum.             
                virkurIndex = lsm.getSelectedIndex();
            }
        });
    }

    public void updateInfo(Trip trip) {
        jtitle.setText(trip.getTitle());
        jtitle.setWrapText(true);
        jdescription.setText(trip.getItinirary());
        jduration.setText(trip.getDuration());
        jdifficulty.setText(trip.getDifficulty());
        jlocation.setText(trip.getLocation());
        jprice.setText(trip.getPrice());
        jmeet.setText(trip.getMeet());
        javailability.setText(trip.getAvailability());
        jcategory.setText(trip.getCategory());
        jpickup.setText(trip.getPickup());
    }

    @FXML
    private void bookTrip(ActionEvent event) {
        book();
    }

    @FXML
    private void addReview(ActionEvent event) {
    }
}
