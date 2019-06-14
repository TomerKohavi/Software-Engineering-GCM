package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;

/**
 * @author tomer
 * edit route controller
 */
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
	
	@FXML // fx:id="FavoriteBox"
    private JFXCheckBox FavoriteBox; // Value injected by FXMLLoader

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

	/**
	 * initialize variables
	 * @throws IOException in/out exception
	 */
	@FXML
	public void initialize() throws IOException
	{
		ReadOnlyIntegerProperty selectedIndex = StopsBox.getSelectionModel().selectedIndexProperty();

		UpButton.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));

		DownButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			int index = selectedIndex.get();
			return index < 0 || index + 1 >= StopsBox.getItems().size();
		}, selectedIndex, StopsBox.getItems()));

		if (Connector.isEdit) // if its edit, load the data
		{
			route = Connector.selectedRoute;
			Name.setText(route.getName());
			InfoBox.setText(route.getInfo());
//			FavoriteBox.setSelected(route.getIs); // TODO get isFavorite (is in RouteSight but not in Route - Blame Ronen)
			stopList = route.getCopyRouteStops();
		}
		else
		{
			stopList = new ArrayList<RouteStop>();
			Name.setText("New route");
		}

		StopsBox.setVisible(true);
		ObservableList<RouteStop> stops = FXCollections.observableArrayList(stopList);

		poiColumn = new TableColumn<RouteStop, String>("POI");
		poiColumn.setMinWidth(212);
		poiColumn.setCellValueFactory(new PropertyValueFactory<>("placeName"));
		poiColumn.setSortable(false);

		timeColumn = new TableColumn<RouteStop, Time>("Time");
		timeColumn.setMinWidth(83);
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
		timeColumn.setSortable(false);

		StopsBox.setItems(stops);

		StopsBox.getColumns().clear();
		StopsBox.getColumns().addAll(poiColumn, timeColumn);

		if (Connector.searchPOIResult == null)
			Connector.searchPOIResult = (ArrayList<PlaceOfInterestSight>) Connector.client.fetchSights(Connector.cityData.getId(), PlaceOfInterestSight.class);
		POIBox.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));

	}

	/**
	 * Change route order.
	 * @param event user click to up route
	 */
	@FXML
	void up(ActionEvent event)
	{
		int index = StopsBox.getSelectionModel().getSelectedIndex();
		stopList.add(index - 1, stopList.remove(index));
		StopsBox.getItems().add(index - 1, StopsBox.getItems().remove(index));
		StopsBox.getSelectionModel().clearAndSelect(index - 1);
	}

	/**
	 * Change route order.
	 * @param event user click to down route
	 */
	@FXML
	void down(ActionEvent event)
	{
		int index = StopsBox.getSelectionModel().getSelectedIndex();
		stopList.add(index + 1, stopList.remove(index));
		StopsBox.getItems().add(index + 1, StopsBox.getItems().remove(index));
		StopsBox.getSelectionModel().clearAndSelect(index + 1);
	}

	/**
	 * update the table
	 */
	@SuppressWarnings("unchecked")
	private void updateTable()
	{
		poiColumn.setCellValueFactory(new PropertyValueFactory<>("placeName"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
		ObservableList<RouteStop> stops = FXCollections.observableArrayList(stopList);
		StopsBox.setItems(stops);
		StopsBox.getColumns().clear();
		StopsBox.getColumns().addAll(poiColumn, timeColumn);
	}

	/**
	 * Adds a POI to the route.
	 * @param event add the place of interest to the route
	 */
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
						new Time((time / 60), time % 60, 0));
				stopList.add(newRouteStop);
				updateTable();
				StopTime.setText("");
				TimeError.setOpacity(0);
			}
		}
		else
			TimeError.setOpacity(1);
	}

	/**
	 * Removes a POI from a route.
	 * @param event remove the point of interest from the route
	 */
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

	/**
	 * Applies all the changes defined.
	 * @param event appaly changes
	 */
	@FXML
	void apply(ActionEvent event)
	{
		try
		{
			for (int i = 0; i < stopList.size(); i++)
				stopList.get(i).setNumStop(i);

			if (Connector.isEdit)
			{
				route.setName(Name.getText());
				route.setInfo(InfoBox.getText());
				route.setRouteStops(new ArrayList<RouteStop>());
				Connector.client.update(route);
			}
			else
			{
				RouteSight routeS = Connector.client.createRoute(Connector.selectedCity.getId(), Name.getText(), InfoBox.getText(),
						Connector.selectedCity.getCopyUnpublishedVersions().get(0).getId(), FavoriteBox.isSelected());
				Connector.selectedRoute = route = routeS.getCopyRoute();
				Connector.searchRouteResult.add(routeS);
			}

			for (RouteStop stop : stopList)
				if (stop.getRouteId() == -1)
					stop._setRouteId(route.getId());
			ArrayList<Integer> stopIdList = Connector.client.createRouteStops(stopList);
			for (int i = 0; i < stopIdList.size(); i++)
				stopList.get(i)._setId(stopIdList.get(i));
			Connector.selectedRoute.setRouteStops(stopList);
			mainPane.getScene().getWindow().hide();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
