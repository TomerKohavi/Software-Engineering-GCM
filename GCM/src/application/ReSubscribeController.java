/**
 * Sample Skeleton for 'ReSubscribeScene.fxml' Controller Class
 */

package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import controller.InformationSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.Customer;
import objectClasses.Subscription;

/**
 * @author tomer
 * give new subscribe from UI to client
 */
public class ReSubscribeController {

	private double monthPrice;
	
	private Subscription sub = null;
	
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
		ArrayList<Subscription> subscriptList = ((Customer) Connector.user).getCopyActiveSubscription();
		for (Subscription searchedSub : subscriptList)
		{
			if (searchedSub.getCityId() == Connector.selectedCity.getId())
			{
				sub = searchedSub;
				break;
			}
		}
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(sub.numMonths);
    	monthPrice = Connector.selectedCity.getPricePeriodWithDiscount();
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice * sub.numMonths) + "$");
    }
    
    /**
     * Handles resubcribe event.
     * @param event user click on re subscribe
     * @throws IOException 
     */
    @FXML
    void ReSubscribe(ActionEvent event) throws IOException {
    	double price = monthPrice * MonthBox.getValue();
    	Connector.client.resubscribe(sub, price, price);
    	Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.SubRenewal);
    	Connector.user = Connector.client.fetchUser(Connector.user.getId());
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
