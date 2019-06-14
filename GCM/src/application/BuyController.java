package application;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import controller.Downloader;
import controller.InformationSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.OneTimePurchase;
import objectClasses.Subscription;
import javafx.stage.DirectoryChooser;

/**
 * @author tomer
 * controller that treat the buy 
 */
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

    /**
	 * initialize variables
     */
    public void initialize() {
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(1);
    	oneTimePrice = 5;
    	monthPrice = 10;
    	OneTimePrice.setText(String.format("%.02f", oneTimePrice) + "$");
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice) + "$");
    }
    
    /**
     * Handles buy event.
     * @param event user click on buy
     * @throws IOException 
     */
    @FXML
    void buy(ActionEvent event) throws IOException {
    	if (RadioOneTime.isSelected())
    	{
    		double price = oneTimePrice;
    		Connector.client.sendToServer(OneTimePurchase._createOneTimePurchase(-1, Connector.selectedCity.getId(), Connector.user.getId(), new Date(Calendar.getInstance().getTime().getTime()), price, price, true)); // TODO kohavi
    		Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.OneTimePurcahse);
    		Connector.downloadCity();
    	}
    	else
    	{
    		double price = monthPrice * MonthBox.getValue(); // TODO send price to server
    		Date start = new Date(Calendar.getInstance().getTime().getTime());
    		Date end = new Date(Calendar.getInstance().getTime().getTime());
    		end.setMonth(end.getMonth() + MonthBox.getValue());
    		Connector.client.sendToServer(Subscription._createSubscription(-1, Connector.selectedCity.getId(), Connector.user.getId(), start, price, price, end)); // TODO kohavi
    		Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.Subscription);
    	}
    	
    	mainPane.getScene().getWindow().hide();
    }

    /** 
     * Handle One time purchase choose event.
     * @param event user choose the one time
     */
    @FXML
    void chooseOneTime(ActionEvent event) {
    	RadioOneTime.setSelected(true);
    	RadioSubscribe.setSelected(false);
    }

    /**
     * Handle One time purchase choose event.
     * @param event user click on choose subscribe
     */
    @FXML
    void chooseSubscribe(ActionEvent event) {
    	RadioSubscribe.setSelected(true);
    	RadioOneTime.setSelected(false);
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice * MonthBox.getValue()) + "$");
    }
    
    /**
     * Handle price update event.
     * @param event user click on update parice
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
