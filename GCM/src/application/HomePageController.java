package application;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import controller.InformationSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.Map;
import objectClasses.PlaceOfInterest;
import objectClasses.Route;
import objectClasses.RouteStop;
import objectClasses.Statistic;
import objectClasses.Subscription;
import objectClasses.Employee.Role;
import objectClasses.Location;
import otherClasses.*;

public class HomePageController
{

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

	@FXML // fx:id="UnpublishSearch"
	private JFXCheckBox UnpublishSearch; // Value injected by FXMLLoader

	@FXML // fx:id="SearchCityButton"
	private JFXButton SearchCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="SearchPOIButton"
	private JFXButton SearchPOIButton; // Value injected by FXMLLoader

	@FXML // fx:id="Info"
	private ListView<String> MainList; // Value injected by FXMLLoader

	@FXML // fx:id="CreateButton"
	private JFXButton CreateButton; // Value injected by FXMLLoader

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

	@FXML // fx:id="RemoveButton"
	private JFXButton RemoveButton; // Value injected by FXMLLoader

	@FXML // fx:id="BuyButton"
	private JFXButton BuyButton; // Value injected by FXMLLoader

	@FXML // fx:id="ReSubscribeButton"
	private JFXButton ReSubscribeButton; // Value injected by FXMLLoader

	@FXML // fx:id="PublishButton"
	private JFXButton PublishButton; // Value injected by FXMLLoader

	@FXML // fx:id="StopsTable"
	private TableView<RouteStop> StopsTable; // Value injected by FXMLLoader

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

	@FXML // fx:id="ViewPurchaseHistoryButton"
	private JFXButton ViewPurchaseHistoryButton; // Value injected by FXMLLoader

	@FXML // fx:id="LoginButton"
	private JFXButton LoginButton; // Value injected by FXMLLoader

	@FXML // fx:id="UserInfoButton"
	private JFXButton UserInfoButton; // Value injected by FXMLLoader

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

	@FXML // fx:id="LoadingGif"
	private ImageView LoadingGif; // Value injected by FXMLLoader

	void startLoad() throws FileNotFoundException
	{
		mainPane.setDisable(true);
		Random r = new Random();
		Image image = new Image(new FileInputStream("Pics\\Gif_" + (r.nextInt(4) + 1) + ".gif"));
		LoadingGif.setImage(image);
		LoadingGif.setVisible(true);
	}

	void endLoad()
	{
		LoadingGif.setVisible(false);
		mainPane.setDisable(false);
	}

	void openNewPage(String FXMLpage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainPane.getScene().getWindow());
		stage.setScene(new Scene((Parent) loader.load()));
		stage.setResizable(false);

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}

	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

	void setMainSideButton(JFXButton button)
	{
		Connector.sideButton.setOpacity(0.5);
		Connector.sideButton = button;
		Connector.sideButton.setOpacity(1);
		clearInfo(true);
	}

	void clearInfo(boolean clearList)
	{
		if (clearList)
			MainList.getItems().clear();
		ResultName.setText("");
		ResultInfo.setText("");
		Text1.setText("");
		Text2.setText("");
		Text3.setText("");
		InfoPane.setVisible(false);
		MapImage.setVisible(false);
		show_map = false;
		ShowMapButton.setText("Show Map");
		ShowMapButton.setVisible(false);
		ViewPurchaseHistoryButton.setVisible(false);
		StopsTable.setVisible(false);
		BuyButton.setVisible(false);
		ReSubscribeButton.setVisible(false);
		for (POIImage img : Connector.imageList)
		{
			mainPane.getChildren().remove(img.image);
		}
		Connector.imageList.clear();
		ReportCityName.setVisible(false);
		ReportInfo.setVisible(false);
		FirstDate.setVisible(false);
		LastDate.setVisible(false);
		WatchButton.setVisible(false);
		PublishButton.setVisible(false);
		if (Connector.unpublished)
		{
			EditButton.setVisible(true);
			RemoveButton.setVisible(true);
			if (Connector.listType.equals("Map") || Connector.listType.equals("POI")
					|| Connector.listType.equals("Route"))
				CreateButton.setVisible(true);
			else
				CreateButton.setVisible(false);
		}
		else
		{
			EditButton.setVisible(false);
			RemoveButton.setVisible(false);
		}
	}

	private void fillCityInfo(City city)
	{
		Connector.selectedCity = city;
		InfoPane.setVisible(true);
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
		if (Connector.user != null)
		{
			if (Connector.user instanceof Employee)
			{
				Role role = ((Employee) Connector.user).getRole();
				if (role == Role.MANAGER || role == Role.CEO)
					BuyButton.setText("Change Price");
				else
					BuyButton.setVisible(false);
			}
			else
			{
				ArrayList<Subscription> subscriptList = ((Customer) Connector.user).getCopyActiveSubscription();
				boolean found = false;
				for (Subscription sub : subscriptList)
					if (sub.getCityId() == city.getId())
					{
						found = true;
						BuyButton.setText("Download");
						if (sub.isGoingToEnd(new Date(new java.util.Date().getTime())))
							ReSubscribeButton.setVisible(true);
						break;
					}
				if (!found)
					BuyButton.setText("Buy");
			}
		}
		else
			BuyButton.setText("Buy");
		ShowMapButton.setVisible(false);
		SideMap.setDisable(false);
		SidePOI.setDisable(false);
		SideRoutes.setDisable(false);
	}

	public void initialize()
	{

		Connector.sideButton = SideSearch;
		Connector.sideButton.setOpacity(1);

		if (Connector.user != null)
		{
			LoginButton.setText("Log Off");
			UserInfoButton.setVisible(true);
		}
		else
			LoginButton.setText("Login");

		if (Connector.user instanceof Employee)
		{ // check if employee -> can edit
			EditButton.setVisible(true);
			UnpublishSearch.setVisible(true);
			SideReport.setVisible(true);
			SideUsers.setVisible(true);
		}
		else
		{
			SideReport.setVisible(false);
			SideUsers.setVisible(false);
		}

		MainList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void handle(MouseEvent click)
			{

				if (click.getClickCount() == 2)
				{
					int selectedIndex = MainList.getSelectionModel().getSelectedIndex();
					if (selectedIndex >= 0)
					{
						InfoPane.setVisible(true);
						if (Connector.listType.equals("City")) // City
						{ 
							Connector.selectedCity = Connector.searchCityResult.get(selectedIndex);
							fillCityInfo(Connector.selectedCity);
						}
						else if (Connector.listType.equals("POI")) // POI
						{ 
							PlaceOfInterest poi = Connector.searchPOIResult.get(selectedIndex).getCopyPlace();
							Connector.selectedPOI = poi;
							ResultName.setText(poi.getName() + ", " + poi.getType());// set name and type
							ResultInfo.setText(poi.getPlaceDescription()); // set info
							Text1.setText((poi.isAccessibilityToDisabled() ? "" : "Not ") + "Accessible to Disabled"); // Accessible
																														// or
																														// not
						}
						else if (Connector.listType.equals("Map")) // map
						{ 
							try
							{
								Map map = Connector.searchMapResult.get(selectedIndex).getCopyMap();
								Connector.selectedMap = map;
								ResultName.setText(map.getName());// set name and type
								ResultInfo.setText(map.getInfo());// set info
								BufferedImage bufIm = Connector.client.getImage("Pics\\" + map.getImgURL());
								Image image = SwingFXUtils.toFXImage(bufIm, null);
								MapImage.setImage(image);
								ShowMapButton.setVisible(true);
								InfoPane.setVisible(true);
								MapImage.setVisible(false);
								ShowMapButton.setText("Show Map");
								for (POIImage img : Connector.imageList)
									mainPane.getChildren().remove(img.image);
								Connector.imageList.clear();
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
						else if (Connector.listType.equals("Route"))
						{ // route
							Route route = Connector.searchRouteResult.get(selectedIndex).getCopyRoute();
							Connector.selectedRoute = route;
							ResultName.setText("Route " + route.getId());// set name and type
							ResultInfo.setText(route.getInfo()); // set info
//							boolean isAccess = route.isAcceptabilityToDisabled();
//							Text1.setText((isAccess ? "" : "Not ") + "Accessible to Disabled");

							ArrayList<RouteStop> list = route.getCopyRouteStops();
							StopsTable.setVisible(true);
							ObservableList<RouteStop> stops = FXCollections.observableArrayList(list);
							System.out.println(list.get(1).tempPlaceName);
							
							TableColumn<RouteStop, String> poiColumn = new TableColumn<>("POI");
							poiColumn.setMinWidth(365);
							poiColumn.setCellValueFactory(new PropertyValueFactory<>("tempPlaceName"));
							
							TableColumn<RouteStop, Time> timeColumn = new TableColumn<>("Time");
							timeColumn.setMinWidth(83);
							timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));
							
							StopsTable.setItems(stops);

							StopsTable.getColumns().clear();
							StopsTable.getColumns().addAll(poiColumn, timeColumn);
							
						}
						else if (Connector.listType.equals("Report")) // reports
						{ 
							Statistic statboi;
							if (selectedIndex == 0) // All
							{
								statboi = InformationSystem.getRangeSumStatistics(null, new java.sql.Date(Date.valueOf(FirstDate.getValue()).getTime()), new java.sql.Date(Date.valueOf(LastDate.getValue()).getTime()));
								ReportCityName.setText("All Cities");
							}
							else
							{
								statboi = InformationSystem.getRangeSumStatistics(Connector.allCities.get(selectedIndex - 1).b, new java.sql.Date(Date.valueOf(FirstDate.getValue()).getTime()), new java.sql.Date(Date.valueOf(LastDate.getValue()).getTime()));
								ReportCityName.setText(Connector.allCities.get(selectedIndex - 1).a);
								
							}
							ReportInfo.setText("Number of Maps: " + 100 + "\n" + "Number of One Time Purchases: " + statboi.getNumOneTimePurchases()
										+ "\n" + "Number of Subscriptions: " + statboi.getNumSubscriptions() + "\n" + "Number of Re-Subscriptions: " + statboi.getNumSubscriptionsRenewal()
										+ "\n" + "Number of Views: " + statboi.getNumVisited() + "\n" + "Number of Downloads: " + statboi.getNumSubDownloads());
							ReportCityName.setVisible(true);
							ReportInfo.setVisible(true);
							InfoPane.setVisible(false);
						}
						else if (Connector.listType.equals("Users")) // users
						{
							Connector.selectedCustomer = Connector.customerList.get(selectedIndex);
							ResultName.setText(Connector.selectedCustomer.getUserName()); // set name and type
							ResultInfo.setText("Name: " + Connector.selectedCustomer.getFirstName() + " " + Connector.selectedCustomer.getLastName() + "\n" + "Email: "
									+ Connector.selectedCustomer.getEmail() + "\n" + "Phone: " + Connector.selectedCustomer.getPhoneNumber());
							ViewPurchaseHistoryButton.setVisible(true);
							EditButton.setVisible(false);
						}
					}
				}
			}
		});
	}

	@FXML
	void search(ActionEvent event) throws IOException
	{
		setMainSideButton(SideSearch);
		Connector.searchedCity = true;
		Connector.listType = "City";
		cityName = CityNameBox.getText();
		cityInfo = CityInfoBox.getText();
		poiName = POINameBox.getText();
		poiInfo = POIInfoBox.getText();
//		startLoad();
		Connector.searchCityResult = Connector.client.search(cityName, cityInfo, poiName, poiInfo);
//		endLoad();
		if (Connector.searchCityResult != null && !Connector.searchCityResult.isEmpty())
		{

			if (UnpublishSearch.isSelected())
			{// search unpublished
				Connector.unpublished = true;
				System.out.println("search unpublished");
			}
			else
			{// search published
				Connector.unpublished = false;
				System.out.println("search published");
			}

			clearInfo(true);

			for (City city : Connector.searchCityResult)
				MainList.getItems().add(city.getCityName());

			NotValid.setOpacity(0);
		}
		else
		{
			NotValid.setOpacity(1);
		}
	}

	@FXML
	void watch(ActionEvent event)
	{
		if (FirstDate.getValue() == null || LastDate.getValue() == null || 0 <= FirstDate.getValue().compareTo(LastDate.getValue())) // date not valid
		{ // date not valid
			DateNotValid.setVisible(true);
		}
		else
		{
			DateNotValid.setVisible(false);
			MainList.getItems().clear();
			MainList.getItems().add("All Cities");
			for (Pair<String, Integer> pair : Connector.allCities)
				MainList.getItems().add(pair.a);
		}

	}

	@FXML
	void viewPurchaseHistory(ActionEvent event) throws IOException
	{
		openNewPage("PurchaseHistoryScene.fxml");
	}

	@FXML
	void showMapImage(ActionEvent event) throws FileNotFoundException
	{
		show_map = !show_map;
		if (show_map)
		{
			InfoPane.setVisible(false);
			MapImage.setVisible(true);
			ShowMapButton.setText("Hide Map");
			Bounds boundsInScene = MapImage.localToScene(MapImage.getBoundsInLocal());
//			List<Point> posList = new ArrayList<Point>();
//			posList.add(new Point((int) (50 + boundsInScene.getMinX()), (int) (50 + boundsInScene.getMinY())));
//			posList.add(new Point((int) (100 + boundsInScene.getMinX()), (int) (100 + boundsInScene.getMinY())));
			List<Location> locList = Connector.selectedMap.getCopyLocations();
			for (Location loc : locList)
			{
				POIImage poiImage = new POIImage(false);
				poiImage.image.setX(loc.getCoordinates()[0] + boundsInScene.getMinX());
				poiImage.image.setY(loc.getCoordinates()[1] + boundsInScene.getMinY());
				Connector.imageList.add(poiImage);
				mainPane.getChildren().add(poiImage.image);
			}
		}
		else
		{
			InfoPane.setVisible(true);
			MapImage.setVisible(false);
			ShowMapButton.setText("Show Map");
			for (POIImage img : Connector.imageList)
				mainPane.getChildren().remove(img.image);
			Connector.imageList.clear();
		}
	}

	@FXML
	void showSearch(ActionEvent event)
	{
		Connector.listType = "City";
		setMainSideButton(SideSearch);
		if (Connector.searchCityResult != null && Connector.searchCityResult.size() != 0) {
			MainList.getItems().addAll(Connector.getCitiesNames(Connector.searchCityResult));
			if (Connector.selectedCity != null)
				fillCityInfo(Connector.selectedCity); // the index of the chosen city
		}
	}

	@FXML
	void login(ActionEvent event) throws IOException
	{
		if (Connector.user == null)
			loadPage("LoginScene.fxml");
		else
		{
			Connector.user = null;
			Connector.client.logoff();
			loadPage("HomePageScene.fxml");
		}

	}

	@FXML
	void editUser(ActionEvent event) throws IOException
	{
		openNewPage("EditUserScene.fxml");
	}

	@FXML
	void showMaps(ActionEvent event) throws FileNotFoundException
	{
		Connector.listType = "Map";
		setMainSideButton(SideMap);
		MainList.getItems().addAll(Connector.getMapsNames(Connector.searchMapResult));
	}

	@FXML
	void showPOI(ActionEvent event)
	{
		Connector.listType = "POI";
		setMainSideButton(SidePOI);
		MainList.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
	}

	@FXML
	void showRoutes(ActionEvent event)
	{
		Connector.listType = "Route";
		setMainSideButton(SideRoutes);
		MainList.getItems().addAll(Connector.getRoutesNames(Connector.searchRouteResult));
	}

	@FXML
	void showReport(ActionEvent event) throws IOException
	{
		Connector.listType = "Report";
		setMainSideButton(SideReport);
		FirstDate.setVisible(true);
		LastDate.setVisible(true);
		WatchButton.setVisible(true);
		if (Connector.allCities == null)
			Connector.allCities = Connector.client.allCitiesRequest();
	}

	@FXML
	void showUsers(ActionEvent event)
	{
		Connector.listType = "Users";
		setMainSideButton(SideUsers);
		try
		{
			Connector.customerList = Connector.client.customersRquest();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		for (Customer cust : Connector.customerList)
			MainList.getItems().add(cust.getUserName());
	}

	@FXML
	void callRemove(ActionEvent event)
	{
		// TODO Sigal need to implement the correct removal
		int index = MainList.getSelectionModel().getSelectedIndex();
		MainList.getItems().remove(index);
		if (Connector.listType.equals("Map"))
			Connector.selectedMap.deleteFromDatabase();
		else if (Connector.listType.equals("POI"))
			Connector.selectedPOI.deleteFromDatabase();
		else if (Connector.listType.equals("Route"))
			Connector.selectedRoute.deleteFromDatabase();
		clearInfo(false);
	}

	@FXML
	void callCreate(ActionEvent event) throws IOException
	{
		Connector.isEdit = false;
		openNewPage(Connector.listType + "EditScene.fxml");
	}
	
	@FXML
	void callEdit(ActionEvent event) throws IOException
	{
		Connector.isEdit = true;
		openNewPage(Connector.listType + "EditScene.fxml");
	}

	@FXML
	void callReSubscribe(ActionEvent event) throws IOException
	{
		openNewPage("ReSubscribeScene.fxml");
	}

	@FXML
	void callPublish(ActionEvent event) throws IOException
	{
		// publish the unpublished version
		System.out.println("Published");
	}

	@FXML
	void openBuyWindodw(ActionEvent event) throws IOException
	{
		if (Connector.user == null) // check if logged in
			openNewPage("LoginErrorScene.fxml");
		else
		{
			if (BuyButton.getText().equals("Buy"))
				openNewPage("BuyScene.fxml");
			else if (BuyButton.getText().equals("Download"))
			{
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("Choose Download Location");
				File defaultDirectory = new File("c:/");
				chooser.setInitialDirectory(defaultDirectory);
				File selectedDirectory = chooser.showDialog(null);
				System.out.println(selectedDirectory.getPath()); // Path to folder
			}
			else if (BuyButton.getText().equals("Change Price"))
				openNewPage("ChangePriceScene.fxml");
		}
	}

}
