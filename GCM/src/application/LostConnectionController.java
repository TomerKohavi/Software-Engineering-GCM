package application;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class LostConnectionController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="ReconnectButton"
    private JFXButton ReconnectButton; // Value injected by FXMLLoader

    /**
	 * Reconnects to the server
	 * @param event reconnect
	 */
    @FXML
    void reconnect(ActionEvent event) {
    	// TODO Sigal do reconnect
    	System.out.println("HOLAAAAAAAA");
    	mainPane.getScene().getWindow().hide();
    }

}
