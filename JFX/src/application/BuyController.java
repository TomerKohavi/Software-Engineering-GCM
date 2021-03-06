/**
 * Sample Skeleton for 'BuyScene.fxml' Controller Class
 */

package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class BuyController {
	
	private double oneTimePrice, monthPrice;

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="OneTimePrice"
    private Text OneTimePrice; // Value injected by FXMLLoader

    @FXML // fx:id="RadioOneTime"
    private JFXRadioButton RadioOneTime; // Value injected by FXMLLoader

    @FXML // fx:id="MonthBox"
    private JFXComboBox<Integer> MonthBox; // Value injected by FXMLLoader

    @FXML // fx:id="SubscriptionPrice"
    private Text SubscriptionPrice; // Value injected by FXMLLoader

    @FXML // fx:id="RadioSubscribe"
    private JFXRadioButton RadioSubscribe; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="BuyButton"
    private JFXButton BuyButton; // Value injected by FXMLLoader

    public void initialize() {
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(1);
    	oneTimePrice = 5;
    	monthPrice = 10;
    	OneTimePrice.setText(String.format("%.02f", oneTimePrice) + "$");
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice) + "$");
    }
    
    @FXML
    void buy(ActionEvent event) {
    	double price = RadioOneTime.isSelected() ? oneTimePrice : (monthPrice * MonthBox.getValue());
    	// send price to server
    	mainPane.getScene().getWindow().hide();
    }

    @FXML
    void chooseOneTime(ActionEvent event) {
    	RadioOneTime.setSelected(true);
    	RadioSubscribe.setSelected(false);
    }

    @FXML
    void chooseSubscribe(ActionEvent event) {
    	RadioSubscribe.setSelected(true);
    	RadioOneTime.setSelected(false);
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice * MonthBox.getValue()) + "$");
    }
    
    @FXML
    void updatePrice(ActionEvent event) {
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice * MonthBox.getValue()) + "$");
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
