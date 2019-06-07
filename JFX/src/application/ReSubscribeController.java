/**
 * Sample Skeleton for 'ReSubscribeScene.fxml' Controller Class
 */

package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ReSubscribeController {

	private double monthPrice;
	
    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="MonthBox"
    private JFXComboBox<Integer> MonthBox; // Value injected by FXMLLoader

    @FXML // fx:id="SubscriptionPrice"
    private Text SubscriptionPrice; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="ReSubscribeButton"
    private JFXButton ReSubscribeButton; // Value injected by FXMLLoader

    public void initialize() {
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(1);
    	monthPrice = 9;
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice) + "$");
    }
    
    @FXML
    void ReSubscribe(ActionEvent event) {
    	double price = monthPrice * MonthBox.getValue();
    	// send price to server
    	mainPane.getScene().getWindow().hide();
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
