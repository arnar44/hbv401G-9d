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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 * Controller fyrir AdminUI
 * @author Arnar
 */
public class AdminUIController implements Initializable {

    @FXML
    private TextField jNotendaNafn;    
    @FXML
    private AnchorPane adminDialog;
    @FXML
    private AddTripUIController addTripDialogController;
    @FXML
    private ListView<Ref> jReviewList;
    @FXML
    private DayToursUIController parent;
    
    private ResultSet results;
    private int virkurIndex;
    private ArrayList<Ref> refArray;
    private ObservableList<Ref> reviewList = FXCollections.observableArrayList();
    private Gagnagrunnur db;

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
    public void tilBaka(ActionEvent event) throws SQLException {
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
    public void buaTilFerd(ActionEvent event) throws SQLException {   
        addTripDialogController.birtaAddTrip(db);
    }
    
    /**
     * Birta AdminUI (sjálfan sig) kallað úr DayToursUI ef notandi gat loggað sig inn
     * @param username 
     * @param gagnagrunnur 
     */
    public void birtaAdminUI(String username, Gagnagrunnur gagnagrunnur){
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
    
    public AnchorPane getDialog(){
        return adminDialog;
    }
    
    public void upphafsstilla(){
        try {
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       jReviewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        ButtonType declineButtonType = new ButtonType("Decline", ButtonBar.ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, declineButtonType, backButtonType);
        
        // Búa til útlit og hluti í útliti
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField tripId = new TextField();
        tripId.setText(result.getString("tourId"));
        TextField name = new TextField();
        name.setText(result.getString("name"));
        TextField email = new TextField();
        email.setText(result.getString("email"));
        TextField date = new TextField();
        date.setText(result.getString("date"));
        TextArea review = new TextArea();
        review.setText(result.getString("review"));
        
        tripId.setEditable(false);
        name.setEditable(false);
        email.setEditable(false);
        date.setEditable(false);
        review.setEditable(false);
        
        review.setPrefColumnCount(20);
        review.setPrefRowCount(6);
        
        // útlit dialogs
        grid.add(new Label("Tour ID"), 0, 0);
        grid.add(tripId, 2, 0);
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
        final Button acceptButton = (Button) dialog.getDialogPane().lookupButton(acceptButtonType);
        final Button declineButton = (Button) dialog.getDialogPane().lookupButton(declineButtonType);
        
        acceptButton.addEventFilter(ActionEvent.ACTION, ae -> { 
            try {
                //samþykkja review
                db.confirmReview(refArray.get(virkurIndex).getId());
                //Erum búin að samþykkja eitt review, það dettur þá úr lista á parent (AdminUI)
                refresh();
            } catch (SQLException ex) {
                Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        declineButton.addEventFilter(ActionEvent.ACTION, ae -> { 
            try {
                db.deleteReview(refArray.get(virkurIndex).getId());
                //Erum búin að samþykkja eitt review, það dettur þá úr lista á parent (AdminUI)
                refresh();
            } catch (SQLException ex) {
                Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        dialog.show();
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
