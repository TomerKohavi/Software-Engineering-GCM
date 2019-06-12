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
import objectClasses.City;

/**
 * @author tomer
 * the controller that treat edit city from the user
 */
public class CityEditController {

	private City city;

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
    
    /**
     * initialize the variables
     */
    @FXML
    public void initialize() {
    	
    	if (Connector.isEdit)
    	{
    		city = Connector.selectedCity;
    		Name.setText(city.getCityName());
    		InfoBox.setText(city.getCityDescription());
    	}
    	else
    		city = new City(null, null); // TODO Sigal
    }
    

    /**
     * apply edit to city as user click
     * @param event user click on edit city
     */
    @FXML
    void apply(ActionEvent event) {
    	Connector.selectedCity.setCityName(Name.getText());
    	Connector.selectedCity.setCityDescription(InfoBox.getText());
    	try
		{
			Connector.client.update(Connector.selectedCity);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    	mainPane.getScene().getWindow().hide();
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
