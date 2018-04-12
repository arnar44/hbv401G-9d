


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import database.Gagnagrunnur;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.AnchorPane;
import admin.AdminUIController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import database.Gagnagrunnur;
import java.util.ArrayList;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.stage.WindowEvent;
import model.Trip;

public class DayToursUIController implements Initializable {
    @FXML
    private AnchorPane mainMenu;
    @FXML
    private ComboBox<String> jPrice;
    @FXML
    private ComboBox<String> jLocation;
    @FXML
    private ComboBox<String> jCategory;
    private ObservableList<Ref> tripList = FXCollections.observableArrayList();
    @FXML
    private ListView<Ref> jTripList;
    @FXML
    private TripUIController tripDialogController;
    @FXML
    private AdminUIController adminDialogController;
  
    private ResultSet results;
    private int virkurIndex;
    private Gagnagrunnur gagnagrunnur = new Gagnagrunnur();
    private int rowcount = 0;
    private ArrayList<Ref> refArray;
    private Trip trip;
    @FXML
    private ComboBox<String> jDifficulty;
    @FXML
    private CheckBox jPickup;
    

    /**
     * Initialize fall fyrir controllerinn
     * Setur upp gögn í viðmóti
     * Heldur utanum index á listahlut sem ýtt er á.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){ 
        try {
            updateResults();
            updateUI();
        } catch (SQLException ex) {
            Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Heldur utanum index á listahlut sem er valinn
        MultipleSelectionModel<Ref> lsm = (MultipleSelectionModel<Ref>) jTripList.getSelectionModel();
        lsm.selectedItemProperty().addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                // Indexinn í listanum.             
                virkurIndex = lsm.getSelectedIndex();
            }
        });
    }

    /**
     * Uppfærir öll comboboxes og lista í viðmóti
     * @throws SQLException 
     */
    private void updateUI() throws SQLException {
        categoryBox();
        difficultyBox();
        locationBox();
        priceBox();
        updateList();
    }
    
    /**
      * Þegar ýtt er á "login" í menubar er kallað á login() sem
      * Býr til innskáningar-dialog
      * @param event 
    */
    // Event Listener on MenuItem.onAction
    @FXML
    public void login(ActionEvent event) {
        
    // Búa til dialog
    Dialog dialog = new Dialog<>();
    dialog.setTitle("Innskáning");
    dialog.setHeaderText("Vinsamlegast skráðu þig inn");

    // takkar í dialog

    ButtonType loginButtonType = new ButtonType("Innskrá", ButtonData.OK_DONE);
    ButtonType tilBakaButtonType = new ButtonType("Til baka", ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, tilBakaButtonType);

    // Username og psw label og gluggar
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField username = new TextField();
    username.setPromptText("Notendanafn");
    PasswordField password = new PasswordField();
    password.setPromptText("Lykilorð");
    // útlit dialogs
    grid.add(new Label("Notendanafn:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new Label("Lykilorð:"), 0, 1);
    grid.add(password, 1, 1);

    dialog.getDialogPane().setContent(grid);

    final Button loginButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
    //Fylgjumst með þegar ýtt er á "login" takkann
    loginButton.addEventFilter(ActionEvent.ACTION, ae -> {

    //Sækja hvað var slegið inn
    String inputUser = username.getText();
    String inputPSW = password.getText();
    ResultSet user;
    /*
        try {
            // Ath hvort notandi með þetta notendanafn og psw sé til
            user = gagnagrunnur.getUser(inputUser,inputPSW);
            // Ef enginn notandi fannst í gagnagrunni
            if(!user.next()){
                // Latum notanda fá eftirfarandi skilaboð og hreinsum reiti
                dialog.setHeaderText("Rangt notendanafn eða lykilorð");
                username.setText("");
                password.setText("");
                // consumeum-enventinn að ýtt var á login-takkann svo dialogin haldist opinn
                ae.consume();
                return;
            } 
            */
            // EF við komumst hingað var rétt notendanaf & lykilorð slegið inn, birta adminUI   
            adminDialogController.birtaAdminUI(username.getText(), gagnagrunnur);
            try {
                //Refreshum þegar adminDialogUI er lokað þá getur verið að ferð hafi bæst við
                updateResults();
                updateUI();
            } catch (SQLException ex) {
                Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        dialog.show();
    }
  
    /**
     * Lokar forriti.
     * @param event 
     */
    @FXML
    public void closePlatform(ActionEvent event) {
        Platform.exit();
    }

        
    /**
     * Sendir filter parametra á Gagnagrunnur.searchTrips()
     * Fær til baka ResultSet
     * Uppfærir svo lista samkvæmt nýja ResultSet
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void filter(ActionEvent event) throws SQLException {
        String[] filter = new String[5];
        String location = jLocation.getValue();
        String difficulty = jDifficulty.getValue();
        String category = jCategory.getValue();
        String price = jPrice.getValue();
        Boolean pickup = jPickup.isSelected();
        if(price == "All" ||price == null) filter[0] = "0-%";
        else if (price == ">100000") filter[0] = "100000-%";
        else filter[0] = price;
        if(location == "All" ||location == null) filter[1] = "%";
        else filter[1] = location;
        if(difficulty == "All" || difficulty == null) filter[2] = "%";
        else filter[2] = difficulty;     
        if(category == "All" || category == null) filter[3] = "%";
        else filter[3] = category;  
        if(pickup) filter[4] = "Yes";
        else filter[4] = "%";
            
        results = gagnagrunnur.searchTrips(filter);
        updateList();
            
    }

    /**
     * Þegar ýtt er á View Trip
     * Býr til Trip hlut og sendir í TipUIController.
     * @param event
     * @throws SQLException 
     */
    @FXML
    public void showTrips(ActionEvent event) throws SQLException {
        updateResults();
        ResultSet rs = results;
	int myId = refArray.get(virkurIndex).getId();
        while (rs.next()) {
            int id = rs.getInt("Id");
            if(id == myId){
                String title = rs.getString("title");
                String location = rs.getString("departures");
                String price = rs.getString("price");
                String duration = rs.getString("duration");
                String difficulty = rs.getString("level");
                String description = rs.getString("description");
                String meet = rs.getString("meet");
                String pickup = rs.getString("pickup");
                String availability = rs.getString("availability");
                String category = rs.getString("category");
                trip = new Trip(title, location, duration, difficulty
                        , description, id, price, meet, pickup, availability, category);
                break;
            }
        }
        
        tripDialogController.setDb(gagnagrunnur);
        tripDialogController.setTrip(trip);
    }
    
    /**
     * Uppfærir ResultSet frá gagnagrunni með öllum tours.
     * @throws SQLException 
     */
    private void updateResults() throws SQLException {
        results = gagnagrunnur.getTrips();
    }
        
    /**
     * Uppfærir lista með öllum titles úr ResultSet
     * @return ObservableList
     * @throws SQLException 
     */
    private void updateList() throws SQLException {
        tripList.clear();
        refArray = new ArrayList<Ref>(); 
        ResultSet rs = results;
        while (rs.next()) {
            String title = rs.getString("title");
            int id = rs.getInt("Id");
            referanceArray(id, title);
        }
        tripList.addAll(refArray);
        jTripList.setItems(tripList);
    }

    
    /**
     * Setur id og title í array með samsvarandi index, miðað við lista.
     * @param id
     * @param title
     * @param index 
     */
    private void referanceArray(int id, String title){
        refArray.add(new Ref(id, title));
    }
    

    /**
     * Heldur utanum title, id í lista.
     */
    private void categoryBox() throws SQLException{
        ResultSet rs = gagnagrunnur.getCategories();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        while(rs.next()){
            list.add(rs.getString("category"));
        }
        jCategory.setItems(list);
    }
    
     /**
     * Initialize choicebox fyrir difficulty
     * @throws SQLException 
     */
    private void difficultyBox() throws SQLException{
        ResultSet rs = gagnagrunnur.getLevels();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        while(rs.next()){
            list.add(rs.getString("level"));
        }
        jDifficulty.setItems(list);
    }
    
    /**
     * Initialize choicebox fyrir difficulty
     * @throws SQLException 
     */
    private void locationBox() throws SQLException{
        ResultSet rs = gagnagrunnur.getLocation();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("All");
        while(rs.next()){
            list.add(rs.getString("departures"));
        }
        jLocation.setItems(list);
    }
    
     /**
     * Initialize choicebox fyrir price
     * @throws SQLException 
     */
    private void priceBox() throws SQLException{
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("All", "0-15000", "15000-30000", "30000-60000","60000-100000", ">100000");
        jPrice.setItems(list);
    }
}

/**
 * Heldur utanum title, id í lista.
 */
class Ref {

    private int id;
    private String title;

    public Ref(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}

