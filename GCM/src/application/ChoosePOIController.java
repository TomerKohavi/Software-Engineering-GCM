/**
 * Sample Skeleton for 'ChoosePOIScene.fxml' Controller Class
 */

package application;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ChoosePOIController {

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="POIList"
    private ListView<String> POIList; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="AddButton"
    private JFXButton AddButton; // Value injected by FXMLLoader

    public void initialize() {
    	POIList.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
    }
    
    @FXML
    void add(ActionEvent event) {
		int selectedIdx = POIList.getSelectionModel().getSelectedIndex();
    	if (selectedIdx >= 0)
    	{
    		Connector.choosenPOIInLoc = Connector.searchPOIResult.get(selectedIdx).getCopyPlace();
    		mainPane.getScene().getWindow().hide();
    	}
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

}
