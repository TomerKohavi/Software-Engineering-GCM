package application;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.LocalDate;
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
import javafx.concurrent.Task;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.RouteStop;
import objectClasses.Statistic;
import objectClasses.Subscription;
import objectClasses.Employee.Role;
import objectClasses.Location;
import otherClasses.*;

/**
 * @author tomer
 * the home page xml
 */
/**
 * @author user
 *
 */
public class HomePageController
{

	private String cityName, cityInfo, poiName, poiInfo;

	static private boolean show_map = false;

	static private boolean isLastCityPublish = true;
	
	static private Connector con = new Connector();

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

	@FXML // fx:id="AlertButton"
	private JFXButton AlertButton; // Value injected by FXMLLoader

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

	/**
	 * @author tomer Treat loading animation
	 */
	class LoadingAnimation extends Task<Integer>
	{

		/**
		 * 
		 * @return the value of the call
		 * @throws Exception cannot call
		 */
		@Override
		protected Integer call() throws Exception
		{
			boolean temp = true;
			while (true)
			{
				System.out.println("s");
				if (Connector.loading && temp)
				{
					startLoad();
					temp = false;
					System.out.println("load");
				}
				else if (!Connector.loading && !temp)
				{
					endLoad();
					temp = true;
					System.out.println("unload");
				}
			}
		}

		/**
		 * cancel operation
		 * 
		 * @param mayInterruptIfRunning problem with the cancel
		 * @return if the cancel success
		 */
		@Override
		public boolean cancel(boolean mayInterruptIfRunning)
		{
			updateMessage("Cancelled!");
			return super.cancel(mayInterruptIfRunning);
		}

	}

	/**
	 * Loading page
	 * 
	 * @throws FileNotFoundException the file wasn't found
	 */
	void startLoad() throws FileNotFoundException
	{
		mainPane.setDisable(true);
		Random r = new Random();
		Image image = new Image(new FileInputStream("Pics\\Gif_" + (r.nextInt(4) + 1) + ".gif"));
		LoadingGif.setImage(image);
		LoadingGif.setVisible(true);
	}

	/**
	 * The file finished loading.
	 */
	void endLoad()
	{
		LoadingGif.setVisible(false);
		mainPane.setDisable(false);
	}

	/**
	 * Open a window that informs the user that the connection to the server has
	 * been lost, and an option to reconnect
	 */
	public void openLostConnectionWindow() // TODO Sigal call this function when connection is lost
	{
		try
		{
			openNewPage("LostConnectionScene.fxml");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Opens new page.
	 * 
	 * @param FXMLpage new fxml page
	 * @throws IOException cannot open the file
	 */
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

	/**
	 * Loads page.
	 * 
	 * @param FXMLpage the page we want to load
	 * @throws IOException cannot loat the page
	 */
	void loadPage(String FXMLpage) throws IOException
	{
		AnchorPane pane = (AnchorPane) FXMLLoader.load((URL) this.getClass().getResource(FXMLpage));
		mainPane.getChildren().setAll(pane);
	}

	/**
	 * Sets main side button.
	 * 
	 * @param button button to set
	 */
	void setMainSideButton(JFXButton button)
	{
		Connector.sideButton.setOpacity(0.5);
		Connector.sideButton = button;
		Connector.sideButton.setOpacity(1);
		clearInfo(true);
	}

	/**
	 * Clears the data collected.
	 * 
	 * @param clearList list of object to clear
	 */
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
			mainPane.getChildren().remove(img.image);
		Connector.imageList.clear();
		ReportCityName.setVisible(false);
		ReportInfo.setVisible(false);
		FirstDate.setVisible(false);
		LastDate.setVisible(false);
		WatchButton.setVisible(false);
		PublishButton.setVisible(false);
		if (Connector.unpublished)
		{
			if (Connector.listType.equals("Map") || Connector.listType.equals("POI")
					|| Connector.listType.equals("Route"))
			{
				EditButton.setVisible(true);
				RemoveButton.setVisible(true);
				CreateButton.setVisible(true);
			}
			else
			{
				EditButton.setVisible(false);
				RemoveButton.setVisible(false);
				CreateButton.setVisible(false);
			}
			if (Connector.listType.equals("City"))
				CreateButton.setVisible(true);
		}
		else
		{
			EditButton.setVisible(false);
			RemoveButton.setVisible(false);
		}
	}

	/**
	 * Adds info to city. It add will add the maps, POIs and Routes.
	 * 
	 * @param city city to add info
	 */
	private void fillCityInfo(City city)
	{
		if ((Connector.selectedCity != null && Connector.selectedCity.getId() != city.getId())
				|| isLastCityPublish != Connector.unpublished)
		{
			Connector.searchMapResult = null;
			Connector.searchPOIResult = null;
			Connector.searchRouteResult = null;
			try
			{
				Connector.client.addStat(city.getId(), InformationSystem.Ops.Visit);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		isLastCityPublish = Connector.unpublished;
		Connector.selectedCity = city;
		InfoPane.setVisible(true);
		ResultName.setText(city.getCityName()); // set name
		ResultInfo.setText(city.getCityDescription()); // set info
		// get QUERIE
		if (Connector.unpublished)
		{
			System.out.println(city.getCopyUnpublishedVersions().size());
			Connector.cityData = city.getCopyUnpublishedVersions().get(0);
			System.out.println("search unpublished");
		}
		else
		{
			Connector.cityData = city.getCopyPublishedVersion();
			System.out.println("search published");
		}
		Text1.setText("Maps Found: " + Connector.cityData.getNumMapSights()); // #Maps for the city
		Text2.setText("POI Found: " + Connector.cityData.getNumPlaceOfInterestSights()); // #POI for the city
		Text3.setText("Routes Found: " + Connector.cityData.getNumRouteSights()); // #Routes for the city

		SideMap.setVisible(false);
		SidePOI.setVisible(false);
		SideRoutes.setVisible(false);
		ReSubscribeButton.setVisible(false);
		BuyButton.setVisible(true);
		if (Connector.user != null)
		{
			if (Connector.user instanceof Employee)
			{
				Role role = ((Employee) Connector.user).getRole();
				if (role == Role.MANAGER)
					BuyButton.setText("Change Price");
				else if (role == Role.CEO && Connector.selectedCity.isCeoNeedsToApprovePrices())
					BuyButton.setText("Checkout Price");
				else
					BuyButton.setVisible(false);
				if (Connector.unpublished && role == Role.MANAGER && city.getManagerNeedsToPublish())
				{
					PublishButton.setVisible(true);
					PublishButton.setText("Publish Version");
				}
				if (Connector.unpublished && role == Role.REGULAR)
				{
					PublishButton.setVisible(true);
					PublishButton.setText("Commit Version");
				}
				SideMap.setVisible(true);
				SidePOI.setVisible(true);
				SideRoutes.setVisible(true);
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
						SideMap.setVisible(true);
						SidePOI.setVisible(true);
						SideRoutes.setVisible(true);
						if (sub.isGoingToEnd(LocalDate.now()))
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

	/**
	 * Adds info to map. It will add the image.
	 * 
	 * @param map map to add the info
	 */
	private void fillMapInfo(Map map)
	{
		try
		{
			ResultName.setText(map.getName());// set name and type
			ResultInfo.setText(map.getInfo());// set info
			BufferedImage bufIm = Connector.client.fetchImage(Connector.mapsDir + map.getImgURL());
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

	/**
	 * Adds info to point of interest.
	 * 
	 * @param poi point of interest to add the info
	 */
	private void fillPOIInfo(PlaceOfInterest poi)
	{
		ResultName.setText(poi.getName() + ", " + poi.getType());// set name and type
		ResultInfo.setText(poi.getPlaceDescription()); // set info
		Text1.setText((poi.isAccessibilityToDisabled() ? "" : "Not ") + "Accessible to Disabled"); // Accessible
	}

	/**
	 * Adds info to route.
	 * 
	 * @param route route to add the info
	 */
	@SuppressWarnings("unchecked")
	private void fillRouteInfo(Route route)
	{
		Connector.selectedRoute = route;
		ResultName.setText(route.getName());// set name and type
		ResultInfo.setText(route.getInfo()); // set info

		ArrayList<RouteStop> list = route.getCopyRouteStops();
		StopsTable.setVisible(true);
		ObservableList<RouteStop> stops = FXCollections.observableArrayList(list);

		TableColumn<RouteStop, String> poiColumn = new TableColumn<>("POI");
		poiColumn.setMinWidth(365);
		poiColumn.setCellValueFactory(new PropertyValueFactory<>("placeName"));

		TableColumn<RouteStop, LocalTime> timeColumn = new TableColumn<>("LocalTime");
		timeColumn.setMinWidth(83);
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedTime"));

		StopsTable.setItems(stops);

		StopsTable.getColumns().clear();
		StopsTable.getColumns().addAll(poiColumn, timeColumn);

	}

	/**
	 * initialize variables
	 */
	public void initialize()
	{
		Connector.searchCityResult = null;
		
		Connector.sideButton = SideSearch;
		Connector.sideButton.setOpacity(1);

		if (Connector.user != null)
		{
			LoginButton.setText("Log Off");
			UserInfoButton.setVisible(true);
		}
		else
			LoginButton.setText("Login");

		if (Connector.user instanceof Employee) // check if employee -> can edit
		{
			EditButton.setVisible(true);
			UnpublishSearch.setVisible(true);
			SideUsers.setVisible(true);
			if (((Employee) Connector.user).getRole() != Employee.Role.REGULAR)
				SideReport.setVisible(true);
			else
				SideReport.setVisible(false);
		}
		else
		{
			SideMap.setVisible(false);
			SidePOI.setVisible(false);
			SideRoutes.setVisible(false);
			SideReport.setVisible(false);
			SideUsers.setVisible(false);
		}

		Connector.poiNameTextArea.setVisible(false);
		Connector.poiNameTextArea.setTextAlignment(TextAlignment.CENTER);
		mainPane.getChildren().add(Connector.poiNameTextArea); // TODO poi name

		if (Connector.user != null && Connector.user instanceof Customer)
		{
			LocalDate today = LocalDate.now();
			ArrayList<Subscription> subscriptList = ((Customer) Connector.user).getCopyActiveSubscription();
			for (Subscription sub : subscriptList)
			{
				if (sub.isGoingToEnd(today))
				{
					AlertButton.setVisible(true);
					break;
				}
			}
		}

		MainList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent click)
			{

				if (click.getClickCount() == 1)
				{
					int selectedIndex = MainList.getSelectionModel().getSelectedIndex();
					if (selectedIndex >= 0)
					{
						InfoPane.setVisible(true);
						if (Connector.listType.equals("City")) // City
						{
							fillCityInfo(Connector.searchCityResult.get(selectedIndex));
						}
						else if (Connector.listType.equals("POI")) // POI
						{
							Connector.selectedPOI = Connector.searchPOIResult.get(selectedIndex).getCopyPlace();
							fillPOIInfo(Connector.selectedPOI);
						}
						else if (Connector.listType.equals("Map")) // map
						{
							Connector.selectedMap = Connector.searchMapResult.get(selectedIndex).getCopyMap();
							fillMapInfo(Connector.selectedMap);
						}
						else if (Connector.listType.equals("Route")) // route
						{
							Connector.selectedRoute = Connector.searchRouteResult.get(selectedIndex).getCopyRoute();
							fillRouteInfo(Connector.selectedRoute);
						}
						else if (Connector.listType.equals("Report")) // reports
						{
							Statistic statboi = null;
							if (selectedIndex == 0) // All
							{
								try
								{
									statboi = Connector.client.getStatistics(null, FirstDate.getValue(),
											LastDate.getValue());
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
								ReportCityName.setText("All Cities");
							}
							else
							{
								try
								{
									statboi = Connector.client.getStatistics(
											Connector.allCities.get(selectedIndex - 1).b, FirstDate.getValue(),
											LastDate.getValue());
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
								ReportCityName.setText(Connector.allCities.get(selectedIndex - 1).a);

							}
							ReportInfo.setText("Number of Maps: " + statboi.getNumMaps() + "\n"
									+ "Number of One LocalTime Purchases: " + statboi.getNumOneTimePurchases() + "\n"
									+ "Number of Subscriptions: " + statboi.getNumSubscriptions() + "\n"
									+ "Number of Re-Subscriptions: " + statboi.getNumSubscriptionsRenewal() + "\n"
									+ "Number of Views: " + statboi.getNumVisited() + "\n" + "Number of Downloads: "
									+ statboi.getNumSubDownloads());
							ReportCityName.setVisible(true);
							ReportInfo.setVisible(true);
							InfoPane.setVisible(false);
						}
						else if (Connector.listType.equals("Users")) // users
						{
							Connector.selectedCustomer = Connector.customerList.get(selectedIndex);
							ResultName.setText(Connector.selectedCustomer.getUserName()); // set name and type
							ResultInfo.setText("Name: " + Connector.selectedCustomer.getFirstName() + " "
									+ Connector.selectedCustomer.getLastName() + "\n" + "Email: "
									+ Connector.selectedCustomer.getEmail() + "\n" + "Phone: "
									+ Connector.selectedCustomer.getPhoneNumber());
							ViewPurchaseHistoryButton.setVisible(true);
							EditButton.setVisible(false);
						}
					}
				}
			}
		});
	}

	/**
	 * Pops up an alert for each ending subscription.
	 * 
	 * @param event object to search
	 * @throws IOException
	 */
	@FXML
	void openAlertWindows(ActionEvent event) throws IOException
	{
		LocalDate today = LocalDate.now();
		ArrayList<Subscription> subscriptList = ((Customer) Connector.user).getCopyActiveSubscription();
		for (Subscription sub : subscriptList)
		{
			if (sub.isGoingToEnd(today))
			{
				Connector.subNameToAlert = sub.getCityName();
				openNewPage("AlertSubIsOverScene.fxml");
			}
		}
		AlertButton.setVisible(false);
	}

	/**
	 * Handle search event.
	 * 
	 * @param event object to search
	 * @throws IOException          cannot find the object
	 * @throws InterruptedException problem
	 */
	@FXML
	void search(ActionEvent event) throws IOException, InterruptedException
	{
		setMainSideButton(SideSearch);
		Connector.listType = "City";
		cityName = CityNameBox.getText();
		cityInfo = CityInfoBox.getText();
		poiName = POINameBox.getText();
		poiInfo = POIInfoBox.getText();
//		startLoad();
//		Connector.loading = true;
		Connector.searchCityResult = Connector.client.search(cityName, cityInfo, poiName, poiInfo ,UnpublishSearch.isSelected());
//		endLoad();
//		Connector.loading = false;
		if (Connector.searchCityResult != null && !Connector.searchCityResult.isEmpty())
		{

			if (UnpublishSearch.isSelected())
			{
				Connector.unpublished = true;
				System.out.println("search unpublished");
			}
			else
			{
				Connector.unpublished = false;
				System.out.println("search published");
			}

			clearInfo(true);

			MainList.getItems().addAll(Connector.getCitiesNames(Connector.searchCityResult));

			NotValid.setOpacity(0);
		}
		else
		{
			NotValid.setOpacity(1);
		}
	}

	/**
	 * Watch the report from the first date to the second date
	 * 
	 * @param event click on watch
	 */
	@FXML
	void watch(ActionEvent event)
	{
		if (FirstDate.getValue() == null || LastDate.getValue() == null
				|| 0 < FirstDate.getValue().compareTo(LastDate.getValue())) // date not valid
		{
			DateNotValid.setVisible(true);
		}
		else
		{
			ReportCityName.setText("");
			ReportInfo.setText("");
			DateNotValid.setVisible(false);
			MainList.getItems().clear();
			MainList.getItems().add("All Cities");
			for (Pair<String, Integer> pair : Connector.allCities)
				MainList.getItems().add(pair.a);
		}

	}

	/**
	 * Opens purchase history page.
	 * 
	 * @param event click on view purchase history
	 * @throws IOException cannot see history
	 */
	@FXML
	void viewPurchaseHistory(ActionEvent event) throws IOException
	{
		openNewPage("PurchaseHistoryScene.fxml");
	}

	/**
	 * Displays the map.
	 * 
	 * @param event user click on show map image
	 * @throws FileNotFoundException cannot find the image
	 */
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
			List<Location> locList = Connector.selectedMap.getCopyLocations();
			for (Location loc : locList)
			{
				POIImage poiImage = new POIImage(false, true, loc.getCopyPlaceOfInterest().getName(), loc);
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

	/**
	 * Shows the search page results.
	 * 
	 * @param event user click on show search event
	 */
	@FXML
	void showSearch(ActionEvent event)
	{
		Connector.listType = "City";
		setMainSideButton(SideSearch);
		if (Connector.searchCityResult != null && Connector.searchCityResult.size() != 0)
		{
			MainList.getItems().addAll(Connector.getCitiesNames(Connector.searchCityResult));
			if (Connector.selectedCity != null)
				fillCityInfo(Connector.selectedCity); // the index of the chosen city
		}
	}

	/**
	 * Loads login page.
	 * 
	 * @param event login page
	 * @throws IOException cannot login
	 */
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

	/**
	 * Opens edit user details scene.
	 * 
	 * @param event user want to edit his details
	 * @throws IOException cannot edit user
	 */
	@FXML
	void editUser(ActionEvent event) throws IOException
	{
		openNewPage("EditUserScene.fxml");
	}

	/**
	 * Displays a list of the maps.
	 * 
	 * @param event show map his clicked
	 * @throws IOException cannot show map
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void showMaps(ActionEvent event) throws IOException
	{
		Connector.listType = "Map";
		setMainSideButton(SideMap);
		if (Connector.searchMapResult == null)
			Connector.searchMapResult = (ArrayList<MapSight>) Connector.client.fetchSights(Connector.cityData.getId(),
					MapSight.class);
		MainList.getItems().addAll(Connector.getMapsNames(Connector.searchMapResult));
	}

	/**
	 * Displays a list of the POIs.
	 * 
	 * @param event show point of interest his clicked
	 * @throws IOException cannot show point of interest
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void showPOI(ActionEvent event) throws IOException
	{
		Connector.listType = "POI";
		setMainSideButton(SidePOI);
		if (Connector.searchPOIResult == null)
			Connector.searchPOIResult = (ArrayList<PlaceOfInterestSight>) Connector.client
					.fetchSights(Connector.cityData.getId(), PlaceOfInterestSight.class);
		MainList.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
	}

	/**
	 * Displays a list of the Routes.
	 * 
	 * @param event show route his clicked
	 * @throws IOException cannot show route
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void showRoutes(ActionEvent event) throws IOException
	{
		Connector.listType = "Route";
		setMainSideButton(SideRoutes);
		if (Connector.searchRouteResult == null)
			Connector.searchRouteResult = (ArrayList<RouteSight>) Connector.client
					.fetchSights(Connector.cityData.getId(), RouteSight.class);
		MainList.getItems().addAll(Connector.getRoutesNames(Connector.searchRouteResult));
	}

	/**
	 * Displays the reports asked for.
	 * 
	 * @param event show report his clicked
	 * @throws IOException cannot show report
	 */
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

	/**
	 * Displays a list of the users.
	 * 
	 * @param event show users his clicked
	 * @throws IOException cannot show users
	 */
	@FXML
	void showUsers(ActionEvent event)
	{
		Connector.listType = "Users";
		setMainSideButton(SideUsers);
		if (Connector.customerList == null)
		{
			try
			{
				Connector.customerList = Connector.client.customersRquest();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		for (Customer cust : Connector.customerList)
			MainList.getItems().add(cust.getUserName());
	}

	/**
	 * Handles remove scenario.
	 * 
	 * @param event remove
	 */
	@FXML
	void callRemove(ActionEvent event)
	{
		int index = MainList.getSelectionModel().getSelectedIndex();
		MainList.getItems().remove(index);
		try
		{
			if (Connector.listType.equals("City"))
			{
				Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.NumMaps, 0);
				Connector.client.deleteObject(Connector.searchCityResult.remove(index)); // TODO CHECK deletion
			}
			else if (Connector.listType.equals("Map"))
				Connector.client.deleteObject(Connector.searchMapResult.remove(index));
			else if (Connector.listType.equals("POI"))
				Connector.client.deleteObject(Connector.searchPOIResult.remove(index));
			else if (Connector.listType.equals("Route"))
				Connector.client.deleteObject(Connector.searchRouteResult.remove(index));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		clearInfo(false);
	}

	/**
	 * Handles create scenario.
	 * 
	 * @param event create is called
	 * @throws IOException cannot create
	 */
	@FXML
	void callCreate(ActionEvent event) throws IOException
	{
		Connector.isEdit = false;
		openNewPage(Connector.listType + "EditScene.fxml");
		MainList.getItems().clear();
		if (Connector.listType.equals("City"))
		{
			MainList.getItems().addAll(Connector.getCitiesNames(Connector.searchCityResult));
			Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.NumMaps, 0);
		}
		else if (Connector.listType.equals("Map"))
			MainList.getItems().addAll(Connector.getMapsNames(Connector.searchMapResult));
		else if (Connector.listType.equals("POI"))
			MainList.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
		else if (Connector.listType.equals("Route"))
			MainList.getItems().addAll(Connector.getRoutesNames(Connector.searchRouteResult));
	}

	/**
	 * Handles an edit scenario.
	 * 
	 * @param event edit is called
	 * @throws IOException cannot edit
	 */
	@FXML
	void callEdit(ActionEvent event) throws IOException
	{
		Connector.isEdit = true;
		openNewPage(Connector.listType + "EditScene.fxml");
		MainList.getItems().clear();
		if (Connector.listType.equals("City"))
			MainList.getItems().addAll(Connector.getCitiesNames(Connector.searchCityResult));
		else if (Connector.listType.equals("Map"))
		{
			MainList.getItems().addAll(Connector.getMapsNames(Connector.searchMapResult));
			fillMapInfo(Connector.selectedMap);
		}
		else if (Connector.listType.equals("POI"))
		{
			MainList.getItems().addAll(Connector.getPOIsNames(Connector.searchPOIResult));
			fillPOIInfo(Connector.selectedPOI);
		}
		else if (Connector.listType.equals("Route"))
		{
			MainList.getItems().addAll(Connector.getRoutesNames(Connector.searchRouteResult));
			fillRouteInfo(Connector.selectedRoute);
		}
	}

	/**
	 * Opens resubscribe page.
	 * 
	 * @param event need to resubscribe
	 * @throws IOException cannot resubscribe
	 */
	@FXML
	void callReSubscribe(ActionEvent event) throws IOException
	{
		openNewPage("ReSubscribeScene.fxml");
		fillCityInfo(Connector.selectedCity);
	}

	/**
	 * Publish an unpublished version.
	 * 
	 * @param event need to publish
	 * @throws IOException cannot publish
	 */
	@FXML
	void callPublish(ActionEvent event) throws IOException
	{
		// publish the unpublished version
		if (PublishButton.getText().equals("Publish Version"))
		{
			if (Connector.cityData.getNumMapSights() <= 0)
			{
				Connector.errorMsg = "In order to Publish a city version, they must have at least one map.";
				openNewPage("ErrorScene.fxml");
			}
			else
			{
				System.out.println("Published");
				Connector.client.publishVersion(Connector.selectedCity, Connector.cityData);
				Connector.selectedCity.setManagerNeedsToPublish(false);
				Connector.client.update(Connector.selectedCity);
				Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.VersionPublish);
				Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.NumMaps,
						Connector.selectedCity.getCopyPublishedVersion().getNumMapSights());
				openNewPage("InformCustomersPublishScene.fxml");
			}
		}
		else
		{
			if (Connector.cityData.getNumMapSights() <= 0)
			{
				Connector.errorMsg = "In order to Commit a city version, they must have at least one map.";
				openNewPage("ErrorScene.fxml");
			}
			else
			{
				Connector.selectedCity.setManagerNeedsToPublish(true);
				Connector.client.update(Connector.selectedCity);
			}
		}
	}

	/**
	 * Opens buy window.
	 * 
	 * @param event user want to buy
	 * @throws IOException cannot open the buy window
	 */
	@FXML
	void openBuyWindow(ActionEvent event) throws IOException
	{
		if (Connector.user == null) // check if logged in
			openNewPage("LoginErrorScene.fxml");
		else
		{
			if (BuyButton.getText().equals("Buy"))
				openNewPage("BuyScene.fxml");
			else if (BuyButton.getText().equals("Download"))
			{
				if (Connector.downloadCity()) // TODO Download the pictures
				{
					Connector.client.addStat(Connector.selectedCity.getId(), InformationSystem.Ops.SubDownload);
					openNewPage("DownloadCompleteScene.fxml");
				}
			}
			else if (BuyButton.getText().equals("Change Price"))
				openNewPage("ChangePriceScene.fxml");
			else if (BuyButton.getText().equals("Checkout Price"))
				openNewPage("CheckoutPriceScene.fxml");
			fillCityInfo(Connector.selectedCity);
		}
	}

}
