package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import objectClasses.PlaceOfInterest;
import objectClasses.Route;
import objectClasses.RouteSight;
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

	@FXML // fx:id="UpButton"
	private JFXButton UpButton; // Value injected by FXMLLoader

	@FXML // fx:id="DownButton"
	private JFXButton DownButton; // Value injected by FXMLLoader

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
	public void initialize() throws FileNotFoundException
	{
//		ImageView upImg = new ImageView(new Image(new FileInputStream("Pics\\up_arrow.png")));
//		ImageView downImg = new ImageView(new Image(new FileInputStream("Pics\\down_arrow.png")));
//		upImg.setFitHeight(25);
//		upImg.setFitWidth(25);
//		downImg.setFitHeight(25);
//		downImg.setFitWidth(25);
//		
//		UpButton.setGraphic(upImg);
//		DownButton.setGraphic(downImg);
//		DownButton.setMinWidth(25);
//		DownButton.setMaxWidth(25);
//		DownButton.setMinHeight(25);
//		DownButton.setMaxHeight(25);

		ReadOnlyIntegerProperty selectedIndex = StopsBox.getSelectionModel().selectedIndexProperty();

		UpButton.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));

		DownButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			int index = selectedIndex.get();
			return index < 0 || index + 1 >= StopsBox.getItems().size();
		}, selectedIndex, StopsBox.getItems()));

		if (Connector.isEdit) // if its edit, load the data
		{
			route = Connector.selectedRoute;
			Name.setText("Route " + route.getId());
			InfoBox.setText(route.getInfo());

			stopList = route.getCopyRouteStops();
			StopsBox.setVisible(true);
			ObservableList<RouteStop> stops = FXCollections.observableArrayList(stopList);

			poiColumn = new TableColumn<RouteStop, String>("POI");
			poiColumn.setMinWidth(212);
			poiColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName")); // TODO Fix Sigal stupid
			poiColumn.setSortable(false);

			timeColumn = new TableColumn<RouteStop, Time>("Time");
			timeColumn.setMinWidth(83);
			timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
			timeColumn.setSortable(false);

			StopsBox.setItems(stops);

			StopsBox.getColumns().clear();
			StopsBox.getColumns().addAll(poiColumn, timeColumn);
		}
		else
		{
			stopList = new ArrayList<RouteStop>();
			// TODO KOHAVI INITIALIZE
		}

		POIBox.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));

	}

	@FXML
	void up(ActionEvent event)
	{
		int index = StopsBox.getSelectionModel().getSelectedIndex();
		stopList.add(index - 1, stopList.remove(index));
		StopsBox.getItems().add(index - 1, StopsBox.getItems().remove(index));
		StopsBox.getSelectionModel().clearAndSelect(index - 1);
	}

	@FXML
	void down(ActionEvent event)
	{
		int index = StopsBox.getSelectionModel().getSelectedIndex();
		stopList.add(index + 1, stopList.remove(index));
		StopsBox.getItems().add(index + 1, StopsBox.getItems().remove(index));
		StopsBox.getSelectionModel().clearAndSelect(index + 1);
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
				PlaceOfInterest poi = Connector.searchPOIResult.get(selectedIdx).getCopyPlace();
				RouteStop newRouteStop = RouteStop._createRouteStop(-1, -1, poi.getId(), poi.getName(), 0,
						new Time((time / 60), time % 60, 0)); // TODO DO DO DO TODO DO DO DO DO kohavi add changing
																// orcer
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
			try
			{
				Connector.client.deleteObject(stopList.remove(selectedIdx));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			updateTable();
		}
	}

	@FXML
	void apply(ActionEvent event)
	{
		try
		{
			if (Connector.isEdit)
			{
				route.setInfo(InfoBox.getText());
				Connector.client.update(route);
			}
			else
			{
				RouteSight routeS = Connector.client.createRoute(Connector.selectedCity.getId(), Name.getText(),
						Connector.selectedCity.getCopyUnpublishedVersions().get(0).getId());
				route = routeS.getCopyRoute();
				Connector.searchRouteResult.add(routeS);
			}
			for (RouteStop stop : stopList)
				if (stop.getRouteId() == -1)
					stop._setRouteId(route.getId());
			ArrayList<Integer> stopIdList = Connector.client.createRouteStops(stopList);
			for (int i = 0; i < stopIdList.size(); i++)
				stopList.get(i)._setId(stopIdList.get(i));
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
