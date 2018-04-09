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
import java.util.Set;
import model.Trip;

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
    @FXML
    private TripUIController tripDialogController;

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

        //TODO á að tengjast search klasa sem tengist gagnagrunni
        Gagnagrunnur gagnagrunnur = new Gagnagrunnur();
        //Sækja hvað var slegið inn
        String inputUser = username.getText();
        String inputPSW = password.getText();
        ResultSet user;
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
            } catch (SQLException ex) {
                System.out.println("Tenging við gagnagrunn næst ekki");
                Logger.getLogger(DayToursUIController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            } 
                
                // Ef notandi fannst birtum við notandasíðu
                Stage adminStage = new Stage();
                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/AdminUI.fxml"));
                    root = loader.load();
                    AdminUIController adminController = loader.<AdminUIController>getController();
                    adminController.setPrevoius(mainMenu);
                    // mainMenu.setVisible(false);
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
		// TODO Autogenerated
	}
	// Event Listener on Button[#jShowTrips].onAction
    @FXML
	public void showTrips(ActionEvent event) {
        
	}
        
}
