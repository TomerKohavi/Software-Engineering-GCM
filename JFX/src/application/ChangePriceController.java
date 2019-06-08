/**
 * Sample Skeleton for 'BuyScene.fxml' Controller Class
 */

package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ChangePriceController {
	
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

    public static boolean isNumeric(String str) {
    	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    public void initialize() {
    	oneTimePrice = 5;
    	monthPrice = 10;
    	OneTimeField.setText(String.format("%.02f", oneTimePrice));
    	MonthField.setText(String.format("%.02f", monthPrice));
    }
    
    @FXML
    void applyChanges(ActionEvent event) {
    	if (isNumeric(OneTimeField.getText()) && isNumeric(MonthField.getText())) {
	    	// send price to server
	    	mainPane.getScene().getWindow().hide();
    	} else {
    		NotValid.setVisible(true);
    	}
    	
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
