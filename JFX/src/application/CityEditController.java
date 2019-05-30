package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class CityEditController {


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
    
    @FXML
    public void initialize() {
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    	}
    	
    }
    

    @FXML
    void apply(ActionEvent event) {
//    	Name.getText();
//		InfoBox.getText();
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
