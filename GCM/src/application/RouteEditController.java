package application;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.Route;
import objectClasses.RouteStop;

public class RouteEditController
{

	private Route route;
	
	private ArrayList<RouteStop> stopList;
	
	private TableColumn<RouteStop, String> poiColumn;
	private TableColumn<RouteStop, Time> timeColumn;

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="Name"
	private JFXTextField Name; // Value injected by FXMLLoader

	@FXML // fx:id="InfoBox"
	private TextArea InfoBox; // Value injected by FXMLLoader

	@FXML // fx:id="StopsBox"
	private JFXListView<String> POIBox; // Value injected by FXMLLoader

	@FXML // fx:id="StopsBox"
	private TableView<RouteStop> StopsBox; // Value injected by FXMLLoader

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
			
			stopList = route.getCopyRouteStops();
			StopsBox.setVisible(true);
			ObservableList<RouteStop> stops = FXCollections.observableArrayList(stopList);
			
			poiColumn = new TableColumn<>("POI");
			poiColumn.setMinWidth(212);
			poiColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName"));
			
			timeColumn = new TableColumn<>("Time");
			timeColumn.setMinWidth(83);
			timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
			
			StopsBox.setItems(stops);

			StopsBox.getColumns().clear();
			StopsBox.getColumns().addAll(poiColumn, timeColumn);
		}
		else
			route = new Route(Connector.selectedCity.getId(), null);
		
		POIBox.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));

	}
	
	@SuppressWarnings("unchecked")
	private void updateTable()
	{
		poiColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
		ObservableList<RouteStop> stops = FXCollections.observableArrayList(stopList);
		StopsBox.setItems(stops);
		StopsBox.getColumns().clear();
		StopsBox.getColumns().addAll(poiColumn, timeColumn);
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
				RouteStop newRouteStop = new RouteStop(route, Connector.searchPOIResult.get(selectedIdx).getCopyPlace(), new Time((time/60), time % 60, 0)); // TODO
				stopList.add(newRouteStop);
				updateTable();
				StopTime.setText("");
				TimeError.setOpacity(0);
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
			stopList.remove(selectedIdx);
			updateTable();
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
