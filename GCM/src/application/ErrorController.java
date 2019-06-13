package application;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * @author tomer
 * handle errors
 */
public class ErrorController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader
    
    @FXML // fx:id="ErrorMsg"
    private Text ErrorMsg; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

	/**
	 * initialize variables
	 */
    public void initialize ()
    {
    	ErrorMsg.setText(Connector.errorMsg);
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
