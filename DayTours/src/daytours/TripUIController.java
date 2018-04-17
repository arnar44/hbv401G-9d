/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import admin.AdminUIController;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import database.Gagnagrunnur;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    private Gagnagrunnur db;
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
    @FXML
    private DatePicker jpurchDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboList.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        jpurchQuantity.setItems(comboList);
        
        jreviewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    try {
                        makeDialog();
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
    }
    //_------_-_-Þarf að breyta
        private void makeDialog() throws SQLException{
        ResultSet result = db.getAdminReview(refArray.get(virkurIndex).getId());
        if(!result.next()){
            //TODO villa kom upp, review finnst ekki
        }
        
        // Búa til dialog
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Review");
        dialog.setHeaderText("Review details");

        // takkar í dialog
        ButtonType acceptButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType);
        
        // Búa til útlit og hluti í útliti
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField name = new TextField();
        name.setText(result.getString("name"));
        TextField email = new TextField();
        email.setText(result.getString("email"));
        TextField date = new TextField();
        date.setText(result.getString("date"));
        TextArea review = new TextArea();
        review.setText(result.getString("review"));
        
        name.setEditable(false);
        email.setEditable(false);
        date.setEditable(false);
        review.setEditable(false);
        
        review.setPrefColumnCount(20);
        review.setPrefRowCount(6);
        
        grid.add(new Label("Name"), 0, 1);
        grid.add(name, 2, 1);
        grid.add(new Label("Email"), 0, 2);
        grid.add(email, 2, 2);
        grid.add(new Label("Date"), 0, 3);
        grid.add(date, 2, 3);
        grid.add(new Label("Review"), 0,4);
        grid.add(review, 2, 5);
        
        //setja grid i dialog
        dialog.getDialogPane().setContent(grid);
        
        //Handlerar fyrir takka
        // final Button acceptButton = (Button) dialog.getDialogPane().lookupButton(acceptButtonType);
        
        dialog.show();
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
        booking.setDb(this.db);
        Boolean booked = true;

        String name = jpurchName.getText();
        String email = jpurchEmail.getText();
        LocalDate date = jpurchDate.getValue();
        
        
        int nbr = jpurchQuantity.getValue();
        int id = dayTour.getId();

        int[] validation;

        try {
            validation = booking.makeBooking(id, name, email, nbr, date);
        } catch (SQLException ex) {
            alertBooking("We are very sorry, something went wrong. Booking not recived","Booking error","Unsuccessful");
            Logger.getLogger(TripUIController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        if (validation[0] == 1) {
            jpurchName.setStyle("-fx-background-color: red;");
            booked = false;
        } else {
            jpurchName.setStyle("-fx-background-color: white;");
        }
        if (validation[1] == 1) {
            jpurchEmail.setStyle("-fx-background-color: red;");
            booked = false;
        } else {
            jpurchEmail.setStyle("-fx-background-color: white;");
        }
        if (validation[2] == 1) {
            jpurchDate.setStyle("-fx-background-color: red;");
            booked = false;
        } else {
            jpurchDate.setStyle("-fx-background-color: none;");
        }
        
        if(booked){
            alertBooking("Your booking has been recived! Have a nice trip", "Booking recived", "Success");
            jpurchName.setText("");
            jpurchEmail.setText("");
            jpurchDate.setValue(null);
        }
    }
    
    public void setDb(Gagnagrunnur db) {
        this.db = db;
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
        jduration.setText("Duration: " + trip.getDuration());
        jdifficulty.setText("Lelev: " + trip.getDifficulty());
        jlocation.setText("Departure: " + trip.getLocation());
        jprice.setText("Price: " + trip.getPrice() + " ISK");
        jmeet.setText("Meet on location: " + trip.getMeet());
        javailability.setText("Available: " + trip.getAvailability());
        jcategory.setText("Type: " + trip.getCategory());
        jpickup.setText("Pickup: " + trip.getPickup());
    }

    @FXML
    private void bookTrip(ActionEvent event) {
        book();
    }

    @FXML
    private void addReview(ActionEvent event) throws SQLException{
        // Búa til dialog
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Add review");
        dialog.setHeaderText("Review details");

        // takkar í dialog
        ButtonType acceptButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, backButtonType);
        
        // Búa til útlit og hluti í útliti
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField name = new TextField();
        TextField email = new TextField();
        TextArea reviewText = new TextArea();
        
        reviewText.setPrefColumnCount(20);
        reviewText.setPrefRowCount(6);
        
        // útlit dialogs
        grid.add(new Label("Name"), 0, 0);
        grid.add(name, 2, 0);
        grid.add(new Label("Email"), 0, 1);
        grid.add(email, 2, 1);
        grid.add(new Label("Review"), 0,2);
        grid.add(reviewText, 2, 2);
        
        //setja grid i dialog
        dialog.getDialogPane().setContent(grid);
        
        //Handlerar fyrir takka
        final Button acceptButton = (Button) dialog.getDialogPane().lookupButton(acceptButtonType);
        final Button cancel = (Button) dialog.getDialogPane().lookupButton(backButtonType);
        
        acceptButton.addEventFilter(ActionEvent.ACTION, ae -> {
            LocalDate date = LocalDate.now();
            review = new Review(name.getText(), reviewText.getText(), email.getText(), date);
            try {
                //samþykkja review
                db.createReview(dayTour.getId(), review);
                confirm();
            } catch (SQLException ex) {
                Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dialog.showAndWait();
        
    }
    
    private void confirm() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Your review has been received and will be confirmed shortly",
                new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        alert.setHeaderText("Reveiw received");
        alert.setTitle("Success");
        alert.showAndWait();
    }
    
    private void alertBooking(String info, String header, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
        info,
        new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.showAndWait();   
    }
}
