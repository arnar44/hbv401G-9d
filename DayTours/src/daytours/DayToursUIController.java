/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daytours;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

import javafx.scene.input.MouseEvent;

import javafx.scene.control.CheckBox;

public class DayToursUIController {
	@FXML
	private Label jSearchParameters;
	@FXML
	private Label jDaytours;
	@FXML
	private Label jPrice;
	@FXML
	private Label jLocation;
	@FXML
	private ComboBox jCategory;
	@FXML
	private CheckBox jOffers;
	@FXML
	private CheckBox jPopular;
	@FXML
	private Button jSearch;
	@FXML
	private Button jShowTrips;

	// Event Listener on MenuItem.onAction
	@FXML
	public void login(ActionEvent event) {
		// TODO Autogenerated
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
		// TODO Autogenerated
	}
}