package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.Route;

public class RouteEditController
{

	private Route route;

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
	private TableView<String> StopsBox; // Value injected by FXMLLoader

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
	public void initialize()
	{

		if (Connector.isEdit) // if its edit, load the data
		{
			route = Connector.selectedRoute;
			Name.setText("Route " + route.getId());
			InfoBox.setText(route.getInfo());
			Accessibility.setSelected(route.isAcceptabilityToDisabled());
			StopsBox.getItems().addAll(""); // add to table, copy from HomePageController
			POIBox.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
		}
		else
			route = new Route(Connector.selectedCity.getId(), null);

	}

	@FXML
	void addPOI(ActionEvent event)
	{
		String timeS = StopTime.getText();
		if (!timeS.equals("")) // not empty
		{
			int time = Integer.parseInt(timeS);
			int selectedIdx = POIBox.getSelectionModel().getSelectedIndex();
			if (selectedIdx >= 0)
			{
				StopsBox.getItems().addAll("" + selectedIdx); // need to implement
				StopTime.setText("");
				TimeError.setOpacity(0);
				Accessibility.setSelected(route.isAcceptabilityToDisabled());
			}
		}
		else
			TimeError.setOpacity(1);
	}

	@FXML
	void removePOI(ActionEvent event)
	{
		int selectedIdx = StopsBox.getSelectionModel().getSelectedIndex();
		if (selectedIdx >= 0)
		{
			StopsBox.getItems().remove(selectedIdx);
		}
	}

	@FXML
	void apply(ActionEvent event)
	{
		route.setInfo(InfoBox.getText());
		try
		{
			Connector.client.update(route);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		mainPane.getScene().getWindow().hide();
	}

	@FXML
	void goBack(ActionEvent event)
	{
		mainPane.getScene().getWindow().hide();
	}

}
