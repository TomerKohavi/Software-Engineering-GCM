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
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterest.PlaceType;
import objectClasses.PlaceOfInterestSight;

/**
 * @author tomer
 * controller for edit point of interest
 */
public class POIEditController
{

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

	/**
	 * initialize variables
	 */
	@FXML
	public void initialize()
	{

		TypeCombo.getItems().addAll("Historical", "Museum", "Hotel", "Restaurant", "Public", "Park", "Store", "Cinema");

		if (Connector.isEdit) // if its edit, load the data
		{
			poi = Connector.selectedPOI;
			Name.setText(poi.getName());
			InfoBox.setText(poi.getPlaceDescription());
//    		TypeCombo.setValue();
			Accessibility.setSelected(poi.isAccessibilityToDisabled());
		}
	}

	/**
	 * @param event user click on edit poin of intreset
	 */
	@FXML
	void apply(ActionEvent event)
	{
		try
		{
			if (Connector.isEdit)
			{
				poi.setName(Name.getText());
				poi.setPlaceDescription(InfoBox.getText());
//   	 	poi.setType(type);
				poi.setAccessibilityToDisabled(Accessibility.isSelected());
				Connector.client.update(poi);
			}
			else
			{
				PlaceOfInterestSight poiS = Connector.client.createPOI(Connector.selectedCity.getId(), Name.getText(),
						PlaceType.values()[TypeCombo.getSelectionModel().getSelectedIndex()], InfoBox.getText(), Accessibility.isSelected(),
						Connector.selectedCity.getCopyUnpublishedVersions().get(0).getId());
				Connector.searchPOIResult.add(poiS);
			}
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
	void goBack(ActionEvent event)
	{
		mainPane.getScene().getWindow().hide();
	}

}
