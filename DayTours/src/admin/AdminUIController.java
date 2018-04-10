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
import daytours.DayToursUIController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.stage.Window;
import model.Review;

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
    private Button jSamþykkjaOll;
    @FXML
    private Button jTilBaka;
    
    private ObservableList<Ref> reviewList = FXCollections.observableArrayList();
    private String adminNotandi;
    private String adminpsw;
    private Gagnagrunnur db = new Gagnagrunnur();
    @FXML
    private AnchorPane adminDialog;
    @FXML
    private AddTripUIController addTripDialogController;
    @FXML
    private ReviewNanarUIController reviewDialogController;
    @FXML
    private ListView<Ref> jReviewList;
    @FXML
    private AdminUIController self;
    
    private ResultSet results;
    private int virkurIndex;
    private ArrayList<Ref> refArray;
    private Review review;

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
    @FXML
    public void tilBaka(ActionEvent event) {
        //loka admin glugga
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Þegar ýtt er á "samþykkja öll" hnappinn samþykkjum við öll review-in
     * @param event 
     */
    @FXML
    public void samþykkjaOll(ActionEvent event) throws SQLException {
        db.confirmAllReviews();
        refresh();
    }
    
    /**
     * Birta dialog þar sem er hægt að bæta við ferð
     * @param event 
     */
    @FXML
    public void buaTilFerd(ActionEvent event) {   
        addTripDialogController.birtaAddTrip();
    }
    
    /**
     * Birta AdminUI (sjálfan sig) kallað úr DayToursUI ef notandi gat loggað sig inn
     * @param username 
     * @param gagnagrunnur 
     */
    public void birtaAdminUI(String username, Gagnagrunnur gagnagrunnur, AdminUIController gotSelf){
        self = gotSelf;
        db = gagnagrunnur;
        upphafsstilla();
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
    
    public void upphafsstilla(){
        try {
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MultipleSelectionModel<Ref> lsm = (MultipleSelectionModel<Ref>) jReviewList.getSelectionModel();
        lsm.selectedItemProperty().addListener(new ChangeListener<Ref>() {
            @Override
            public void changed(ObservableValue<? extends Ref> observable, Ref oldValue, Ref newValue) {
                // Indexinn í listanum.             
                virkurIndex = lsm.getSelectedIndex();
                System.out.println(virkurIndex);
                // Skoðum review nánar sem var ýtt á
                if(virkurIndex >= 0){
                    try {
                        reviewDialogController.birtaReview(refArray.get(virkurIndex).getId(), db, self);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }            
            }
        });
    }
    
    /**
     * Uppfærir ResultSet frá gagnagrunni með öllum tours.
     * @throws SQLException 
     */
    private void updateResults() throws SQLException {
        results = db.getAdminReviews();
    }
    
    /**
     * Uppfærir lista með öllum titles úr ResultSet
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
     * @param id
     * @param title
     * @param index 
     */
      private void referanceArray(int id, String title){
          refArray.add(new Ref(id, title));
     }
    
      /**
       * Refreshar lista
       * @throws SQLException 
       */
    public void refresh() throws SQLException{
        
        jReviewList.getItems().clear();
        jReviewList.getSelectionModel().clearSelection();
        List<Ref> selectedItemsCopy = new ArrayList<>(jReviewList.getSelectionModel().getSelectedItems());
        jReviewList.getItems().removeAll(selectedItemsCopy);
        
        updateResults();
        reviewList.addAll(updateList());
        
        jReviewList.setItems(reviewList);
    }
    
}

    /**
     * Heldur utanum title, id í lista.
     */
    class Ref {
        
        private int id;
        private String name;
        
        public Ref(int id, String title) {
            this.id = id;
            this.name = title;
        }
        
        public String getName(){
            return name;
        }
        
        public int getId() {
            return id;
        }
        
         @Override
        public String toString() {
            return getName();
        }
}
