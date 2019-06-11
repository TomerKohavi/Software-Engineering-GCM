package application;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class DownloadCompleteController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="ErrorMsg"
    private Text ErrorMsg; // Value injected by FXMLLoader

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
