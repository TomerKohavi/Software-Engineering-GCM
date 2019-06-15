package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * @author tomer
 * send message to the client with change price request
 */
public class CheckoutPriceController {
	
	private double oneTimePrice, monthPrice;

	@FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="OneTimeField"
    private JFXTextField OneTimeField; // Value injected by FXMLLoader

    @FXML // fx:id="MonthField"
    private JFXTextField MonthField; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader
    
    @FXML // fx:id="NotValid"
    private Text NotValid; // Value injected by FXMLLoader

    @FXML // fx:id="BuyButton"
    private JFXButton BuyButton; // Value injected by FXMLLoader
    
    /**
     * Makes sure that str is a legal number.
     * @param str string to format
     * @return true if the string in the good format
     */
    public static boolean isNumeric(String str) {
    	  return str.matches("?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    /**
     * initialize the variables
     */
    public void initialize() {
    	oneTimePrice = Connector.cityData.getPriceOneTime(); // TODO get the ToBe prices
    	monthPrice = Connector.cityData.getPricePeriod();
    	OneTimeField.setText(String.format("%.02f", oneTimePrice));
    	MonthField.setText(String.format("%.02f", monthPrice));
    }
    
    /**
     * Applies the price change event defined.
     * @param event event from the UI that said to change the price
     * @throws IOException 
     */
    @FXML
    void applyChanges(ActionEvent event) throws IOException {
    	if (isNumeric(OneTimeField.getText()) && isNumeric(MonthField.getText()) && Integer.parseInt(OneTimeField.getText()) > 0 && Integer.parseInt(MonthField.getText()) > 0) {  // TODO Change Price to city (Ron)
	    	mainPane.getScene().getWindow().hide();
    		Connector.cityData.setPriceOneTime(Integer.parseInt(OneTimeField.getText()));
    		Connector.cityData.setPricePeriod(Integer.parseInt(MonthField.getText()));
    		Connector.client.update(Connector.selectedCity);
    	} else {
    		NotValid.setVisible(true);
    	}
    	
    }

    /**
     * go to the back page.
     * @param event user click event
     */
    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
