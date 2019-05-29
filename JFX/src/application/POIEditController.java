package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class POIEditController {


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
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    		TypeCombo.setValue("");
//    		Accessibility.setSelected(true/false);
//    	}
    	
    }
    

    @FXML
    void apply(ActionEvent event) {
//    	Name.getText();
//		InfoBox.getText();
//		TypeCombo.getValue();
//    	Accessibility.selectedProperty();
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
