package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import objectClasses.PlaceOfInterest;

public class POIEditController {

	private PlaceOfInterest poi;

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Name"
    private JFXTextField Name; // Value injected by FXMLLoader

    @FXML // fx:id="InfoBox"
    private TextArea InfoBox; // Value injected by FXMLLoader

    @FXML // fx:id="ApplyChanges"
    private JFXButton ApplyChanges; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="TypeCombo"
    private JFXComboBox<String> TypeCombo; // Value injected by FXMLLoader

    @FXML // fx:id="Accessibility"
    private CheckBox Accessibility; // Value injected by FXMLLoader
    
    @FXML
    public void initialize() {
    	
    	TypeCombo.getItems().addAll(
    			"Historical",
    	        "Museum",
    	        "Hotel",
    	        "Restaurant",
    	        "Public",
    	        "Park",
    	        "Store",
    	        "Cinema"
    		);
    	
    	if (Connector.isEdit)  // if its edit, load the data
    	{
    		poi = Connector.selectedPOI;
    		Name.setText(poi.getName());
    		InfoBox.setText(poi.getPlaceDescription());
//    		TypeCombo.setValue();
    		Accessibility.setSelected(poi.isAccessibilityToDisabled());
    	}
    	else
    		poi = new PlaceOfInterest(Connector.selectedCity.getId(), null, null, null, false);
    	
    }
    

    @FXML
    void apply(ActionEvent event) {
    	poi.setName(Name.getText());
    	poi.setPlaceDescription(InfoBox.getText());
//    	poi.setType(type);
    	poi.setAccessibilityToDisabled(Accessibility.isSelected());
    	try
		{
			Connector.client.update(poi);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    	mainPane.getScene().getWindow().hide();
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
