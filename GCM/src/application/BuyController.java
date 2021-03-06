package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import controller.InformationSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.OneTimePurchase;
import objectClasses.Subscription;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author tomer
 * Controller that treat the buy 
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
	 * Open new page
	 * @param FXMLpage new fxml page
	 * @throws IOException cannot open the file
	 */
	void openNewPage(String FXMLpage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainPane.getScene().getWindow());
		stage.setScene(new Scene((Parent) loader.load()));
		stage.setResizable(false);

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}
    
    /**
	 * Initialize variables
     */
    public void initialize() {
    	MonthBox.getItems().addAll(1,2,3,4,5,6);
    	MonthBox.setValue(1);
    	oneTimePrice = Connector.selectedCity.getPriceOneTime();
    	monthPrice = Connector.selectedCity.getPricePeriod();
    	OneTimePrice.setText(String.format("%.02f", oneTimePrice) + "$");
    	SubscriptionPrice.setText(String.format("%.02f", monthPrice) + "$");
    }
    
    /**
     * Handles buy event.
     * @param event user click on buy
     * @throws IOException 
     */
    @SuppressWarnings("deprecation")
	@FXML
    void buy(ActionEvent event) throws IOException {
    	if (RadioOneTime.isSelected())
    	{
    		double price = oneTimePrice;
    		Connector.client.sendToServer(OneTimePurchase._createLocalOneTimePurchase(-1, Connector.selectedCity.getId(), Connector.user.getId(), LocalDate.now(), price, price, true, Connector.selectedCity.getCityName()));
    		Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.OneTimePurcahse);
    		Connector.downloadCity();
    		openNewPage("DownloadCompleteScene.fxml");
    	}
    	else
    	{
			double price = monthPrice * MonthBox.getValue();
    		LocalDate start = LocalDate.now();
    		LocalDate end = start.plusMonths(MonthBox.getValue());
//    		end.plusMonths(MonthBox.getValue());
    		Connector.client.sendToServer(Subscription._createLocalSubscription(-1, Connector.selectedCity.getId(), Connector.user.getId(), start, price, price, end, Connector.selectedCity.getCityName()));
    		Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.Subscription);
    	}
    	Connector.user = Connector.client.fetchUser(Connector.user.getId());
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
     * Go to the previous page
     * @param event user click go previous page
     */
    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }
}
