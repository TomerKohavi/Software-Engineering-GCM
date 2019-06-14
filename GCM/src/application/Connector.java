package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
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
import objectClasses.User;
import otherClasses.Pair;
import client.ChatClient;
import controller.Downloader;

/**
 * @author tomer
 * connect the UI to the client
 */
public class Connector {
	
	public static User user;
	
	public static Stage mainStage;
	
	public static JFXButton sideButton;
	
	public static ChatClient client;

	public static final int PORT = 5555;
	public static final String LOCAL_HOST = "localhost";
	
	public static boolean isEdit = true;
	
	
	public static String listType = "City";
	
	public static List<POIImage> imageList = new ArrayList<POIImage> ();
	
	public static List<POIImage> removablePOIList = new ArrayList<POIImage> ();

	public static boolean unpublished = false;
	
	public static String errorMsg;
	
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
	
	public static Text poiNameTextArea = new Text("");
	
	/**
	 * @param cityList list of city to we want to get
	 * @return list of names of the cities.
	 */
	public static ArrayList<String> getCitiesNames(ArrayList<City> cityList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (City city : cityList)
		{
			if (Connector.user != null && Connector.user instanceof Employee && ((Employee) Connector.user).getRole() == Employee.Role.CEO && city.getManagerNeedsToPublish())
				nameList.add("★" + city.getCityName());
			else
				nameList.add(city.getCityName());
		}
		return nameList;
	}
	
	/**
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
	 * @param mapList list of point of interest to we want to get
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
	 * @param mapList list of route to we want to get
	 * @return list of names of the routes.
	 */
	public static ArrayList<String> getRoutesNames(ArrayList<RouteSight> routeList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (RouteSight route : routeList)
		{
			if (route.getIsFavorite())
				nameList.add("★" + "Route " + route.getCopyRoute().getId());
			else
				nameList.add("Route " + route.getCopyRoute().getId());
		}
		return nameList;
	}
	
	public static void downloadCity()
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose Download Location");
		File defaultDirectory = new File("c:/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(null);
		Downloader.downloadPOIs(Connector.selectedCity.getCopyPublishedVersion(),
				selectedDirectory.getPath() + "\\" + Connector.selectedCity.getCityName() + " "
						+ Connector.selectedCity.getCopyPublishedVersion().getVersionName() + ".txt");
	}
}
