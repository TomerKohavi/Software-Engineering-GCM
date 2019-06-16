package application;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class LostConnectionController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="ReconnectButton"
    private JFXButton ReconnectButton; // Value injected by FXMLLoader

    /**
	 * Closes the program
	 * @param event reconnect
	 */
    @FXML
    void closeProgram(ActionEvent event) {
    	Platform.exit();
    }

}
