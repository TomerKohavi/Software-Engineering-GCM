/**
 * Sample Skeleton for 'ReSubscribeScene.fxml' Controller Class
 */

package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import controller.InformationSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * @author tomer
 * give new subscribe from UI to client
 */
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

    /**
	 * initialize variables
     */
    public void initialize() {
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(1);
    	monthPrice = 9;
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice) + "$");
    }
    
    /**
     * @param event user click on re subscribe
     * @throws IOException 
     */
    @FXML
    void ReSubscribe(ActionEvent event) throws IOException {
    	double price = monthPrice * MonthBox.getValue(); // send price to server
    	Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.Subscription);
    	mainPane.getScene().getWindow().hide();
    }

    /**
     * update price
     * @param event user click on update price
     */
    @FXML
    void updatePrice(ActionEvent event) {
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice * MonthBox.getValue()) + "$");
    }

    /**
     * go to the previous page
     * @param event user click go previous page
     */
    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }
    
}
