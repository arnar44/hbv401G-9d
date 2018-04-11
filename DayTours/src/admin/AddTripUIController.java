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
    private RadioButton jMeet;
    @FXML
    private RadioButton jPickup;
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

    /**
     * Þegar ýtt er á "back" takkan er þessum glugga lokað.
     * @param event 
     */
    @FXML
    private void tilBaka(ActionEvent event) {
        //loka glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Kallað á úr foreldris glugga til að opna þennan dialog (AddTripUI)
     * @param gagnagrunnur
     * @throws SQLException 
     */
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
        jPriceSlider.setBlockIncrement(1000);
        
        //Upphafsstilla duration slider
        jDurationSlider.setMin(1);
        jDurationSlider.setMax(24);
        jDurationSlider.setValue(13);
        jDurationSlider.setShowTickLabels(true);
        jDurationSlider.setShowTickMarks(true);
        jDurationSlider.setMajorTickUnit(6);
        jDurationSlider.setMinorTickCount(1);
        


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
                        // senda í gagnagrunn og loka þessum glugga
                        makeTrip();
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                    } catch (SQLException ex) {
                        Logger.getLogger(AddTripUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // Ef ýtt er á confirm og validate skilar false, litast gluggar rauðir
                // sem ekki var fyllt inní rétt og event er consumaður (ekkert gerist)
                event.consume();
            }
        };
        
        // Setja handler fyrir takkan
        jConfirm.setOnAction(confirmHandler);
        
        // Event listener fyrir Price sliderinn
        jPriceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {
                //Sækja nýju tölu
                int sliderValue = newValue.intValue();
                //Námundum að næsta 1000
                int rounded = (sliderValue + 500) / 1000 * 1000;
                //Setjum í label
                jPrice.setText(String.valueOf(rounded));
            }
        });
        // Event listener fyrir Duration sliderinn
        jDurationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {
                //Sækja nýja tölu
                int sliderValue = newValue.intValue();
                // Setja í label
                jDuration.setText(String.valueOf(sliderValue));
            }
        });
        
    }
    /**
     * Initialize choicebox fyrir categories
     * @throws SQLException 
     */
    private void categoryBox() throws SQLException{
        // Sækjum alla flokka sem eru núþegar í boði
        ResultSet set = db.getCategories();
        ObservableList<String> list = FXCollections.observableArrayList();
        // Setja í lista
        while(set.next()){
            list.add(set.getString("category"));
        }
        // Setjum listan inn í choice-boxið
        jCategory.setItems(list);
    }
    
    /**
     * Initialize choicebox fyrir levels
     * @throws SQLException 
     */
    private void levelBox() throws SQLException{
        // Sækja öll erfiðleikastig sem eru núþegar í boði
        ResultSet set = db.getLevels();
        ObservableList<String> list = FXCollections.observableArrayList();
        // Setja þau í lista
        while(set.next()){
            list.add(set.getString("level"));
        }
        // Setjum listan inn í choice-boxið
        jLevel.setItems(list);
    }
    
    /**
     * Initialize choicebox fyrir tima, frá og til
     * @throws SQLException 
     */
    private void timeBox() throws SQLException{
        // Listi af öllum mánuðum
        ObservableList<String> list = FXCollections.observableArrayList(
            "January", "February", "March", "April", "Mai", "June",
            "July", "August", "September", "Ocktober", "November", "December");
        // Setjum listan inn í bæði choice-boxin
        jFrom.setItems(list);
        jTo.setItems(list);
    }
    
    /**
     * Fall sem bregst við breytingum á radiobutton
     */
    @FXML
    public void timeChange(){
        // Ef jHours takkin er valinn setjum við label jTimeStamp sem Hours (annars Days)
       String display = jHours.isSelected() ? "Hours" : "Days";
       jTimeStamp.setText(display);
    }
    
    /**
     * Fall sem bregst við breytingu í ceckboxi
     */
    @FXML
    public void availabiltyChange(){
       // Ef tikkað er í jAllYear checkboxið disable-um við jFrom og jTo annars ekki 
       Boolean visible = jAllYear.isSelected();
       
       jFromLabel.disableProperty().set(visible);
       jToLabel.disableProperty().set(visible);
       jFrom.disableProperty().set(visible);
       jTo.disableProperty().set(visible);
    }
    
    /**
     * Skoðar Title, Description og departures textfieldin, ef þau eru tóm
     * eru þau lituð rauð annars default litur. Ef eitthvað þeirra er tómt er 
     * false skilað, annars true
     * Skoðar einnig hvort eitthvað var valið í choiceBoxin jLevel og jCategory
     * @return 
     */
    private Boolean validate() {
        // Ef reitur er tómur setjum við þetta sem background-color
        String error = "-fx-control-inner-background: rgba(240, 0, 0, 0.3)";
        // Ef choicebox er tómur setjum við þennan rauða lit sem border-lit
        String borderError = "-fx-border-color: rgba(255, 0, 0, 1)";
        String color;
        
        //Sækjum hvort reitir og choicebox séu tóm
        Boolean title = jTitle.getText().trim().isEmpty();
        Boolean description = jDescription.getText().trim().isEmpty();
        Boolean departures = jDepartures.getText().trim().isEmpty();
        Boolean level = jLevel.getSelectionModel().getSelectedItem() == null;
        Boolean category = jCategory.getSelectionModel().getSelectedItem() == null;
        
        // Setjum lit á reiti og choicebox eftir því hvort þau voru tóm eða ekki
        // error error/borderError ef þeir voru tómir annars null sem setur default lit
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
        
        // Skilar boolean, true ef allir reitir voru ekki tómir (í lagi)
        return !title && !description && !departures && !level && !category;
    }
    
    /**
     * Bætir innslegni ferð í gagnagrunn
     */
    private void makeTrip() throws SQLException{
        db.createTrip(setTrip());
    }
    
    /**
     * Sækir info í dialog og býr til Trip hlut og skilar
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
