package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objectClasses.City;
import objectClasses.CityDataVersion;
import objectClasses.Customer;
import objectClasses.Employee;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.Subscription;
import objectClasses.User;
import otherClasses.Pair;
import client.ChatClient;
import controller.Downloader;

/**
 * @author tomer connect the UI to the client
 */
public class Connector
{

	public static User user;

	public static Stage mainStage;
	
	public static AnchorPane mainPaneForConnectionLost;
	
	public static Semaphore semaphoreForLostConnection = new Semaphore(0);

	public static JFXButton sideButton;

	public static ChatClient client;

	public static final int PORT = 5556;
	public static final String LOCAL_HOST = "localhost";
	public static final double DEFULT_ONE_TIME_PRICE = 10;
	public static final double DEFULT_SUB_MONTH_PRICE = 40;

	public static boolean isEdit = true;

	public static String listType = "City";

	public static List<POIImage> imageList = new ArrayList<POIImage>();

	public static List<POIImage> removablePOIList = new ArrayList<POIImage>();

	public static boolean unpublished = false;

	public static String errorMsg;

	public static String subNameToAlert;

	public static City selectedCity;
	public static Map selectedMap;
	public static PlaceOfInterest selectedPOI;
	public static Route selectedRoute;
	public static Customer selectedCustomer;
	public static PlaceOfInterest choosenPOIInLoc;

	public static ArrayList<City> searchCityResult;
	public static ArrayList<MapSight> searchMapResult;
	public static ArrayList<PlaceOfInterestSight> searchPOIResult;
	public static ArrayList<RouteSight> searchRouteResult;
	public static ArrayList<Pair<String, Integer>> allCities;
	public static ArrayList<Customer> customerList;

	public static CityDataVersion cityData = null;

	public static boolean loading = false;

	public Connector()
	{
		Task<Void> task = new Task<Void>() {
		    @Override
		    public Void call() throws Exception {
		    	semaphoreForLostConnection.acquire();
		    	System.out.println("SEM REL");
		        return null;
		    }
		};
		task.setOnSucceeded(e -> {
		    try
			{
				openConnectionLostPage();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		new Thread(task).start();
	}
	
//	public void initialize()
//	{
//		Task<Void> task = new Task<Void>() {
//		    @Override
//		    public Void call() throws Exception {
//		    	semaphoreForLostConnection.acquire();
//		        return null;
//		    }
//		};
//		task.setOnSucceeded(e -> {
//		    openConnectionLostPage();
//		});
//		new Thread(task).start();
//	}
	
	public static Text poiNameTextArea = new Text("");

	/**
	 * A function that gets a list of cities and displays the cities' name. If the
	 * user is an employee then it would mark the cities waiting for approval.
	 * 
	 * @param cityList list of city to we want to get
	 * @return list of names of the cities.
	 */
	public static ArrayList<String> getCitiesNames(ArrayList<City> cityList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (City city : cityList)
		{
			if (Connector.user != null && Connector.user instanceof Employee
					&& ((Employee) Connector.user).getRole() == Employee.Role.CEO && city.getManagerNeedsToPublish())
				nameList.add("★" + city.getCityName());
			else
				nameList.add(city.getCityName());
		}
		return nameList;
	}

	/**
	 * A function that gets a list of maps and displays the maps' name.
	 * 
	 * @param mapList list of map to we want to get
	 * @return list of names of the maps.
	 */
	public static ArrayList<String> getMapsNames(ArrayList<MapSight> mapList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (MapSight map : mapList)
			nameList.add(map.getCopyMap().getName());
		return nameList;
	}

	/**
	 * A function that gets a list of POIs and displays the POIs' name.
	 * 
	 * @param poiList list of point of interest to we want to get
	 * @return list of names of the points of interest.
	 */
	public static ArrayList<String> getPOIsNames(ArrayList<PlaceOfInterestSight> poiList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (PlaceOfInterestSight poi : poiList)
			nameList.add(poi.getCopyPlace().getName());
		return nameList;
	}

	/**
	 * A function that gets a list of routes and displays the routes' name.
	 * 
	 * @param routeList list of route to we want to get
	 * @return list of names of the routes.
	 */
	public static ArrayList<String> getRoutesNames(ArrayList<RouteSight> routeList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (RouteSight route : routeList)
		{
			if (route.getCopyRoute().getIsFavorite())
				nameList.add("★" + "Route " + route.getCopyRoute().getId());
			else
				nameList.add("Route " + route.getCopyRoute().getId());
		}
		return nameList;
	}

	/**
	 * Downloads the city details to the user's computer.
	 * 
	 * @return success or failure
	 * @throws IOException from client
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public static boolean downloadCity() throws IOException
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose Download Location");
		File defaultDirectory = new File("c:/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(null);
		if (selectedDirectory != null)
		{
			Downloader.downloadPOIs(
					(ArrayList<PlaceOfInterestSight>) Connector.client.fetchSights(
							Connector.selectedCity.getCopyPublishedVersion().getId(), PlaceOfInterestSight.class),
					selectedDirectory.getPath() + "\\" + Connector.selectedCity.getCityName() + " "
							+ Connector.selectedCity.getCopyPublishedVersion().getVersionName() + ".txt");
			if (searchMapResult == null)
				Connector.searchMapResult = (ArrayList<MapSight>) Connector.client
						.fetchSights(Connector.cityData.getId(), MapSight.class);
			for (MapSight mapS : searchMapResult)
			{
				Map map = mapS.getCopyMap();
				BufferedImage bufIm = Connector.client.fetchImage("Pics\\" + map.getImgURL());
				ImageIO.write(bufIm, "png", new File(selectedDirectory.getPath() + "\\" + map.getName() + ".png"));
			}
			return true;
		}
		return false;

	}

	/**
	 * Get the customers that subscribes to the city
	 * 
	 * @return list of customers
	 */
	public static ArrayList<Customer> getCustomersSubscibeToCity()
	{
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
		ArrayList<Customer> ans = new ArrayList<Customer>(customerList);
		for (Customer c : customerList)
		{
			if (c.getActiveSubscriptionsByCity(selectedCity.getId()).size() > 0)
				ans.add(c);
		}
		return ans;
	}
	
	/**
	 * Opens new page.
	 * @param FXMLpage new fxml page
	 * @throws IOException cannot open the file
	 */
	void openConnectionLostPage() throws IOException
	{
		System.out.println("HOLAAAAAAAAAAAaaa");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LostConnectionScene.fxml")); // TODO check if works
		Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Connection Error");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}
}
