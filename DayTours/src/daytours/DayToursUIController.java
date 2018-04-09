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
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class DayToursUIController {
    @FXML
    private Label jDaytours;
    @FXML
    private AnchorPane mainMenu;
    @FXML
    private Label jSearchParameters;
    @FXML
    private Label jPrice;
    @FXML
    private Label jLocation;
    @FXML
    private ComboBox<?> jCategory;
    @FXML
    private CheckBox jOffers;
    @FXML
    private CheckBox jPopular;
    @FXML
    private Button jSearch;
    @FXML
    private Button jShowTrips;
    private ObservableList<String> tripList;
    @FXML
    private ListView<String> jTripList;

        @Override
        public void initialize(URL url, ResourceBundle rb) {      
            jdagskraListi.setItems(minnKatalogur.getDagskraTitil());
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

            grid.add(new Label("Notendanafn:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Lykilorð:"), 0, 1);
            grid.add(password, 1, 1);
            
            dialog.getDialogPane().setContent(grid);
            
            final Button loginButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.addEventFilter(ActionEvent.ACTION, ae -> {
                /*
                TODO
                Kalla á aðferð í search sem ber notendanafn og psw við notendur í gaggnagrunni,
                sú aðferð mun skila resultset.
                ef resultset er tómt þá skal birta villuskilaboð í dialog annars opna admin gluggan
                */
                
                //Þegar queries eru tilbúnar
                /*
                String inputUser = username.getText();
                String inputPSW = password.getText();
                ResultSet user = kallISerachogGagnagrunn(inputUser,inputPSW);
                // Ef enginn notandi fannst í gagnagrunni
                if(!user.next()){
                    // Latum notanda fá eftirfarandi skilaboð og hreinsum reiti
                    dialog.setHeaderText("Rangt notendanafn eða lykilorð");
                    username.setText("");
                    password.setText("");
                    // consumeum-enventinn að ýtt var á login-takkann svo dialogin helst opinn
                    ae.consume();
                    return;
                }
                */
                
                // Ef notandi fannst birtum við notandasíðu
                Stage adminStage = new Stage();
                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/AdminUI.fxml"));
                    root = loader.load();
                    AdminUIController adminController = loader.<AdminUIController>getController();
                    adminController.setPrevoius(mainMenu);
                    mainMenu.setVisible(false);
                    Scene scene = new Scene(root);
                    adminStage.setScene(scene);
                    adminStage.show();
                } catch (IOException ex) {
                    Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            dialog.show();
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void closePlatform(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on ImageView.onMouseReleased
    @FXML
	public void languageEnglish(MouseEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on ImageView.onMouseReleased
    @FXML
	public void languageIcelandic(MouseEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#jSearch].onAction
    @FXML
	public void filter(ActionEvent event) {
		
	}
	// Event Listener on Button[#jShowTrips].onAction
    @FXML
	public void showTrips(ActionEvent event) {
		// TODO Autogenerated
	}
        
        private void updateList() {
            
        }
        
        
}
