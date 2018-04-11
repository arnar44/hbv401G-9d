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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import model.Trip;

/**
 * FXML Controller class
 *
 * @author Arnar
 */
public class AddTripUIController implements Initializable {

    @FXML
    private Button jTilBaka;
    @FXML
    private Button jConfirm;
    @FXML
    private AnchorPane addTripDialog;
    @FXML
    private TextField jTitle;
    @FXML
    private Label jPrice;
    @FXML
    private TextArea jDescription;
    @FXML
    private Slider jPriceSlider;
    @FXML
    private ChoiceBox<String> jCategory;
    @FXML
    private Slider jDurationSlider;
    @FXML
    private Label jTimeStamp;
    @FXML
    private Label jDuration;
    @FXML
    private ChoiceBox<String> jTo;
    @FXML
    private CheckBox jAllYear;
    @FXML
    private Label jFromLabel;
    @FXML
    private ChoiceBox<String> jFrom;
    @FXML
    private Label jToLabel;
    @FXML
    private RadioButton jDays;
    @FXML
    private RadioButton jMeet;
    @FXML
    private RadioButton jPickup;
    @FXML
    private ToggleGroup timeStampGroup;
    @FXML
    private RadioButton jHours;
    @FXML
    private ChoiceBox<String> jLevel;
    @FXML
    private TextField jDepartures;
    
    private Gagnagrunnur db;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void tilBaka(ActionEvent event) {
        //loka glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void birtaAddTrip(Gagnagrunnur gagnagrunnur) throws SQLException{
        db = gagnagrunnur;
        upphafsstilla();
        //Sýna AdminUI diolog-ið
        DialogPane p = new DialogPane();
        addTripDialog.setVisible(true);
        p.setContent(addTripDialog);
        Dialog<ButtonType> d = new Dialog();
        d.setDialogPane(p);
        
        // Loka glugga þegar ýtt er á exit takka í horni
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        
        d.showAndWait();
    }
    
    /**
     * Upphafsstillir valmöguleika á addTripUI
     */
    private void upphafsstilla() throws SQLException{
        
        //Núllstilla TextFields
        jTitle.setText("");
        jDescription.setText("");
        jDepartures.setText("");
        
        // Núllstilla reiti
        jHours.setSelected(true);
        jMeet.setSelected(true);
        jAllYear.setSelected(true);
        
        //Láta label sem svara til reita sýna rétt
        timeChange();
        availabiltyChange();
        
        //Upphafsstilla price slider
        jPriceSlider.setMin(0);
        jPriceSlider.setMax(150000);
        jPriceSlider.setValue(75000);
        jPriceSlider.setShowTickLabels(true);
        jPriceSlider.setShowTickMarks(true);
        jPriceSlider.setMajorTickUnit(25000);
        jPriceSlider.setMinorTickCount(1);
        jPriceSlider.setBlockIncrement(1000);
        
        //Upphafsstilla duration slider
        jDurationSlider.setMin(1);
        jDurationSlider.setMax(24);
        jDurationSlider.setValue(13);
        jDurationSlider.setShowTickLabels(true);
        jDurationSlider.setShowTickMarks(true);
        jDurationSlider.setMajorTickUnit(6);
        jDurationSlider.setMinorTickCount(1);
        
        //Label fyrir slider-a
        priceChange();
        durationChange();

        // Upphafsstilla category
        categoryBox();
        
        // Upphafsstilla level
        levelBox();
        
        // Upphafsstilla tima, fra-til
        timeBox();
        jFrom.getSelectionModel().selectFirst();
        jTo.getSelectionModel().selectFirst();
        
        //Eventhandler á Confirm takka
        EventHandler<ActionEvent> confirmHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(validate()){
                    try {
                        // senda í gagnagrunn og loka
                        makeTrip();
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                    } catch (SQLException ex) {
                        Logger.getLogger(AddTripUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                event.consume();
            }
        };
        
        jConfirm.setOnAction(confirmHandler);
    }
    /**
     * Initialize choicebox fyrir categories
     * @throws SQLException 
     */
    private void categoryBox() throws SQLException{
        ResultSet set = db.getCategories();
        ObservableList<String> list = FXCollections.observableArrayList();
        while(set.next()){
            list.add(set.getString("category"));
        }
        jCategory.setItems(list);
    }
    
    /**
     * Initialize choicebox fyrir levels
     * @throws SQLException 
     */
    private void levelBox() throws SQLException{
        ResultSet set = db.getLevels();
        ObservableList<String> list = FXCollections.observableArrayList();
        while(set.next()){
            list.add(set.getString("level"));
        }
        jLevel.setItems(list);
    }
    
    /**
     * Initialize choicebox fyrir tima, frá - til
     * @throws SQLException 
     */
    private void timeBox() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList(
            "January", "February", "March", "April", "Mai", "June",
            "July", "August", "September", "Ocktober", "November", "December");
        jFrom.setItems(list);
        jTo.setItems(list);
    }
    
    /**
     * Höndlar event þegar price slider er breytt
     */
    @FXML
    public void priceChange(){
        int sliderValue = (int)Math.round(jPriceSlider.getValue());
        jPrice.setText(String.valueOf(sliderValue));
    }
    
       /**
     * Höndlar event þegar duration slider er breytt
     */
    @FXML
    public void durationChange(){
        int sliderValue = (int)Math.round(jDurationSlider.getValue());
        jDuration.setText(String.valueOf(sliderValue));
    }
    
    /**
     * Fall sem bregst við breytingum á radiobutton
     */
    @FXML
    public void timeChange(){
       String display = jHours.isSelected() ? "Hours" : "Days";
       jTimeStamp.setText(display);
    }
    
    /**
     * Fall sem bregst við breytingu í hceckboxi
     */
    @FXML
    public void availabiltyChange(){
        Boolean visible = jAllYear.isSelected();
       
       jFromLabel.disableProperty().set(visible);
       jToLabel.disableProperty().set(visible);
       jFrom.disableProperty().set(visible);
       jTo.disableProperty().set(visible);
    }
    
    /**
     * Skoðar Title, Description og departures textfieldin, ef þau eru tóm
     * eru þau lituð rauð annars default litur, ef eitthvað þeirra er tómt er 
     * false skilað, annars true
     * @return 
     */
    private Boolean validate() {
        String error = "-fx-control-inner-background: rgba(240, 0, 0, 0.2)";
        String borderError = "-fx-border-color: rgba(255, 0, 0, 1)";
        String color;
        
        Boolean title = jTitle.getText().trim().isEmpty();
        Boolean description = jDescription.getText().trim().isEmpty();
        Boolean departures = jDepartures.getText().trim().isEmpty();
        Boolean level = jLevel.getSelectionModel().getSelectedItem() == null;
        Boolean category = jCategory.getSelectionModel().getSelectedItem() == null;
        
        color = title ? error : null;
        jTitle.setStyle(color);
        color = description ? error : null;
        jDescription.setStyle(color);
        color = departures ? error : null;
        jDepartures.setStyle(color);
        color = level ? borderError : null;
        jLevel.setStyle(color);
        color = category ? borderError : null;
        jCategory.setStyle(color);       
        
        return !title && !description && !departures && !level && !category;
    }
    
    /**
     * Bætir innslegni ferð í gagnagrunn
     */
    private void makeTrip() throws SQLException{
        db.createTrip(setTrip());
    }
    
    /**
     * Sækir info í dialog og býr til Trip hlut
     * @return 
     */
    private Trip setTrip(){
        String title = jTitle.getText();
        String price = jPrice.getText();
        String duration = jDuration.getText() + " " + jTimeStamp.getText();
        String category = jCategory.getSelectionModel().getSelectedItem();
        String level = jLevel.getSelectionModel().getSelectedItem();
        String availability = jAllYear.isSelected() ? 
                "All Year" :
                jFrom.getSelectionModel().getSelectedItem() + " - " + jTo.getSelectionModel().getSelectedItem();
        String description = jDescription.getText();
        String departures = jDepartures.getText();
        String meet = jMeet.isSelected() ? "Yes" : "No";
        String pickup = jPickup.isSelected() ? "Yes" : "No";
        Trip trip = new Trip(title, departures, duration, level, 
                description, 0, price, meet, pickup, availability, category);
        return trip;
    }
    
}
