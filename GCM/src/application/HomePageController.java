package application;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import classes.City;
import classes.CityDataVersion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomePageController {

	private String cityName, cityInfo, poiName, poiInfo;

	static private boolean show_map = false;

	@FXML // fx:id="mainPane"
	private AnchorPane mainPane; // Value injected by FXMLLoader

	@FXML // fx:id="MapNotValid"
	private Text NotValid; // Value injected by FXMLLoader

	@FXML // fx:id="CityNameBox"
	private JFXTextField CityNameBox; // Value injected by FXMLLoader

	@FXML // fx:id="CityInfoBox"
	private JFXTextField CityInfoBox; // Value injected by FXMLLoader

	@FXML // fx:id="POINameBox"
	private JFXTextField POINameBox; // Value injected by FXMLLoader

	@FXML // fx:id="POIInfoBox"
	private JFXTextField POIInfoBox; // Value injected by FXMLLoader

	@FXML // fx:id="SearchCityButton"
	private JFXButton SearchCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="SearchPOIButton"
	private JFXButton SearchPOIButton; // Value injected by FXMLLoader

	@FXML // fx:id="Info"
	private ListView<String> MainList; // Value injected by FXMLLoader

	@FXML // fx:id="InfoPane"
	private Pane InfoPane; // Value injected by FXMLLoader

	@FXML // fx:id="ResultName"
	private Text ResultName; // Value injected by FXMLLoader

	@FXML // fx:id="ResultInfo"
	private Text ResultInfo; // Value injected by FXMLLoader

	@FXML // fx:id="NumOfMaps"
	private Text Text1; // Value injected by FXMLLoader

	@FXML // fx:id="NumOfPOI"
	private Text Text2; // Value injected by FXMLLoader

	@FXML // fx:id="NumOfRoutes"
	private Text Text3; // Value injected by FXMLLoader

	@FXML // fx:id="EditButton"
	private JFXButton EditButton; // Value injected by FXMLLoader

	@FXML // fx:id="BuyButton"
	private JFXButton BuyButton; // Value injected by FXMLLoader

	@FXML // fx:id="StopsTable"
	private TableView<String> StopsTable; // Value injected by FXMLLoader

	@FXML // fx:id="ShowMapButton"
	private JFXButton ShowMapButton; // Value injected by FXMLLoader

	@FXML // fx:id="FirstDate"
	private JFXDatePicker FirstDate; // Value injected by FXMLLoader

	@FXML // fx:id="LastDate"
	private JFXDatePicker LastDate; // Value injected by FXMLLoader

	@FXML // fx:id="WatchButton"
	private JFXButton WatchButton; // Value injected by FXMLLoader

	@FXML // fx:id="DateNotValid"
	private Text DateNotValid; // Value injected by FXMLLoader

	@FXML // fx:id="ReportCityName"
	private Text ReportCityName; // Value injected by FXMLLoader

	@FXML // fx:id="ReportInfo"
	private Text ReportInfo; // Value injected by FXMLLoader

	@FXML // fx:id="LoginButton"
	private JFXButton LoginButton; // Value injected by FXMLLoader

	@FXML // fx:id="MapImage"
	private ImageView MapImage; // Value injected by FXMLLoader

	@FXML // fx:id="SideMap"
	private JFXButton SideSearch; // Value injected by FXMLLoader

	@FXML // fx:id="SideMap"
	private JFXButton SideMap; // Value injected by FXMLLoader

	@FXML // fx:id="SidePOI"
	private JFXButton SidePOI; // Value injected by FXMLLoader

	@FXML // fx:id="SideRoutes"
	private JFXButton SideRoutes; // Value injected by FXMLLoader

	@FXML // fx:id="SideReport"
	private JFXButton SideReport; // Value injected by FXMLLoader

	@FXML // fx:id="SideUsers"
	private JFXButton SideUsers; // Value injected by FXMLLoader

	void openNewPage(String FXMLpage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainPane.getScene().getWindow());
		stage.setScene(new Scene((Parent) loader.load()));
		stage.setResizable(false);

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}

	void loadPage(String FXMLpage) throws IOException {
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

	void setMainSideButton(JFXButton button) {
		Connector.sideButton.setOpacity(0.5);
		Connector.sideButton = button;
		Connector.sideButton.setOpacity(1);
		clearInfo();
	}

	void clearInfo() {
		MainList.getItems().clear();
		ResultName.setText("");
		ResultInfo.setText("");
		Text1.setText("");
		Text2.setText("");
		Text3.setText("");
		InfoPane.setVisible(false);
		MapImage.setVisible(false);
		ShowMapButton.setText("Show Map");
		ShowMapButton.setVisible(false);
		StopsTable.setVisible(false);
		BuyButton.setVisible(false);
		for (ImageView img : Connector.imageList) {
			mainPane.getChildren().remove(img);
		}
		Connector.imageList.clear();
		ReportCityName.setVisible(false);
		ReportInfo.setVisible(false);
		FirstDate.setVisible(false);
		LastDate.setVisible(false);
		WatchButton.setVisible(false);
//    	if (employee) { // check if employee -> can edit
//		EditButton.setVisible(true);
//	}
	}

	public void initialize() {

		Connector.sideButton = SideSearch;
		Connector.sideButton.setOpacity(1);

		if (Connector.user != null)
			LoginButton.setText("Log Off");
		else
			LoginButton.setText("Login");

//    	if (employee) { // check if employee -> can edit
//    		EditButton.setVisible(true);
//    	}

		SideReport.setDisable(false);
		SideUsers.setDisable(false);

		MainList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {

				if (click.getClickCount() == 2) {
					int selectedIndex = MainList.getSelectionModel().getSelectedIndex();
					if (selectedIndex >= 0) {
						City city = Connector.searchCityResult.get(selectedIndex);
						InfoPane.setVisible(true);
						if (Connector.listType.equals("City")) { // City
							ResultName.setText(city.getCityName()); // set name
							ResultInfo.setText(city.getCityDescription()); // set info
							// get QUERIES
							CityDataVersion cityData = city.getCopyPublishedVersion();
							Text1.setText("Maps Found: " + cityData.getNumMapSights()); // #Maps for the city
							Text2.setText("POI Found: " + cityData.getNumPlaceOfInterestSights()); // #POI for the city
							Text3.setText("Routes Found: " + cityData.getNumRouteSights()); // #Routes for the city

							Connector.searchMapResult = cityData.getCopyMapSights();
							Connector.searchPOIResult = cityData.getCopyPlaceSights();
							Connector.searchRouteResult = cityData.getCopyRouteSights();

							BuyButton.setVisible(true);
//    	        		   if (Connector.user_id != -1  && subscribed to the map) // check if it bought the map
//        	        		   BuyButton.setText("Download");
//    	        		   else
//    	        			   BuyButton.setText("Buy");
							ShowMapButton.setVisible(false);
							SideMap.setDisable(false);
							SidePOI.setDisable(false);
							SideRoutes.setDisable(false);
						} 
//						else if (Connector.listType.equals("POI")) { // POI
//							ResultName.setText(currentItemSelected + " - " + "Hotel"); // set name and type
//							ResultInfo.setText("Info"); // set info
//							Text1.setText("Accessible to Disabled"); // Accessible or not
//						} else if (Connector.listType.equals("Map")) { // map
//							ResultName.setText(currentItemSelected); // set name
//							ResultInfo.setText("Info"); // set info
//							ShowMapButton.setVisible(true);
//						} else if (Connector.listType.equals("Route")) { // route
//							ResultName.setText(currentItemSelected); // set name and type
//							ResultInfo.setText("Info"); // set info
//							Text1.setText("Accessible to Disabled"); // Accessible or not
//							StopsTable.setVisible(true);
//							ObservableList<String> stops = FXCollections.observableArrayList();
//							StopsTable.setItems(stops);
//						} else if (Connector.listType.equals("Report")) { // users
//							ReportCityName.setVisible(true);
//							ReportInfo.setVisible(true);
//							InfoPane.setVisible(false);
//							ReportCityName.setText(currentItemSelected); // set name and type
//							ReportInfo.setText("Number of Maps: " + 6 + "\n" + "Number of One Time Purchases: " + 6
//									+ "\n" + "Number of Subscriptions: " + 6 + "\n" + "Number of Re-Subscriptions: " + 6
//									+ "\n" + "Number of Views: " + 6 + "\n" + "Number of Downloads: " + 6);
//						} else if (Connector.listType.equals("Users")) { // users
//							ResultName.setText(currentItemSelected); // set name and type
//							ResultInfo.setText("Name: " + "first" + " " + "last" + "\n" + "Email: "
//									+ "coreset@sigal.is.gay" + "\n" + "Phone: " + "0544444444");
//							EditButton.setDisable(false);
//							// add purchase history
//						}
					}
				}
			}
		});

		MapImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				System.out.println(p);
			}
		});

	}

	@FXML
	void search(ActionEvent event) throws IOException {
		setMainSideButton(SideSearch);
		Connector.searchedCity = true;
		Connector.listType = "City";
		cityName = CityNameBox.getText();
		cityInfo = CityInfoBox.getText();
		poiName = POINameBox.getText();
		poiInfo = POIInfoBox.getText();

		Connector.searchCityResult = Connector.client.search(cityName, cityInfo, poiName, poiInfo);

		if (!Connector.searchCityResult.isEmpty()) {
			MainList.getItems().clear();

			for (City city : Connector.searchCityResult)
				MainList.getItems().add(city.getCityName());

			NotValid.setOpacity(0);
		} else {
			NotValid.setOpacity(1);
		}
	}

	@FXML
	void watch(ActionEvent event) {
		if (FirstDate.getValue() == null || LastDate.getValue() == null) { // date not valid
			DateNotValid.setVisible(true);
		} else {
			MainList.getItems().addAll("city1", "city2");
		}

	}

	@FXML
	void showMapImage(ActionEvent event) throws FileNotFoundException {
		show_map = !show_map;
		if (show_map) {
			InfoPane.setVisible(false);
			MapImage.setVisible(true);
			ShowMapButton.setText("Hide Map");
			Image image = new Image(new FileInputStream("Pics\\POI.png"));
			List<Point> posList = new ArrayList<Point>();
			posList.add(new Point(1211 - 334, 578 - 130));
			posList.add(new Point(1211 - 334 + 50, 578 - 130 + 50));
			for (Point p : posList) {
				ImageView img = new ImageView(image);
				img.setX(p.getX());
				img.setY(p.getY());
				Connector.imageList.add(img);
			}
			mainPane.getChildren().addAll(Connector.imageList);
		} else {
			InfoPane.setVisible(true);
			MapImage.setVisible(false);
			ShowMapButton.setText("Show Map");
			for (ImageView img : Connector.imageList) {
				mainPane.getChildren().remove(img);
			}
			Connector.imageList.clear();
		}
	}

	@FXML
	void showSearch(ActionEvent event) {
		Connector.listType = "City";
		setMainSideButton(SideSearch);
		MainList.getItems().addAll("city1", "city2");
	}

	@FXML
	void login(ActionEvent event) throws IOException {
		if (Connector.user == null)
			loadPage("LoginScene.fxml");
		else {
			System.out.println("LOGOFF FX");
			Connector.user = null;
			Connector.client.logoff();
			// send that the user has logged off to the server
			loadPage("HomePageScene.fxml");
		}

	}

	@FXML
	void showMaps(ActionEvent event) throws FileNotFoundException {
		Connector.listType = "Map";
		setMainSideButton(SideMap);
		MainList.getItems().addAll("map1", "map2");
	}

	@FXML
	void showPOI(ActionEvent event) {
		Connector.listType = "POI";
		setMainSideButton(SidePOI);
		MainList.getItems().addAll("POI1", "POI2");
	}

	@FXML
	void showRoutes(ActionEvent event) {
		Connector.listType = "Route";
		setMainSideButton(SideRoutes);
		MainList.getItems().addAll("route1", "route2");
	}

	@FXML
	void showReport(ActionEvent event) {
		Connector.listType = "Report";
		setMainSideButton(SideReport);
		FirstDate.setVisible(true);
		LastDate.setVisible(true);
		WatchButton.setVisible(true);
	}

	@FXML
	void showUsers(ActionEvent event) {
		Connector.listType = "Users";
		setMainSideButton(SideUsers);
		MainList.getItems().addAll("user1", "user2");
	}

	@FXML
	void callEdit(ActionEvent event) throws IOException {
		openNewPage(Connector.listType + "EditScene.fxml");
	}

	@FXML
	void openBuyWindodw(ActionEvent event) throws IOException {
		if (Connector.user == null) // check if logged in
			openNewPage("LoginErrorScene.fxml");
		else {
			if (BuyButton.getText().equals("Buy"))
				openNewPage("BuyScene.fxml");
			// else
			// download

		}
	}

}
