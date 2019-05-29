package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class RouteEditController {
	
	@FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Name"
    private JFXTextField Name; // Value injected by FXMLLoader

    @FXML // fx:id="InfoBox"
    private TextArea InfoBox; // Value injected by FXMLLoader

    @FXML // fx:id="Accessibility"
    private CheckBox Accessibility; // Value injected by FXMLLoader

    @FXML // fx:id="StopsBox"
    private JFXListView<String> POIBox; // Value injected by FXMLLoader
    
    @FXML // fx:id="StopsBox"
    private JFXListView<String> StopsBox; // Value injected by FXMLLoader

    @FXML // fx:id="AddPoiButton"
    private JFXButton AddPoiButton; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="ApplyChanges"
    private JFXButton ApplyChanges; // Value injected by FXMLLoader
    
    @FXML // fx:id="Time"
    private JFXTextField StopTime; // Value injected by FXMLLoader
    
    @FXML // fx:id="TimeError"
    private Text TimeError; // Value injected by FXMLLoade
    
    @FXML
    public void initialize() {
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    		Accessability.setSelected(true/false)
//			StopsBox.getItems().addAll("");
			POIBox.getItems().addAll("0", "1", "2", "3", "4");
//    	}
    	
    }
    
    @FXML
    void addPOI(ActionEvent event) {
    	String timeS = StopTime.getText();
    	if (!timeS.equals("")) // not empty
    	{
    		int time = Integer.parseInt(timeS);
    		int selectedIdx = POIBox.getSelectionModel().getSelectedIndex();
        	if (selectedIdx >= 0)
        	{
        		POIBox.getItems().remove(selectedIdx);
        		StopsBox.getItems().addAll("" + selectedIdx);
        		StopTime.setText("");
        		TimeError.setOpacity(0);
        	}
    	}
    	else
    		TimeError.setOpacity(1);
    }

    @FXML
    void removePOI(ActionEvent event) {
    	int selectedIdx = StopsBox.getSelectionModel().getSelectedIndex();
    	if (selectedIdx >= 0)
    	{
    		StopsBox.getItems().remove(selectedIdx);
    		POIBox.getItems().addAll("" + selectedIdx);
    	}
    }
    
    @FXML
    void apply(ActionEvent event) {
//    	Name.getText();
//		InfoBox.getText();
//		TypeCombo.getValue();
//    	Accessability.selectedProperty();
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
