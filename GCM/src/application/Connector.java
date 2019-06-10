package application;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objectClasses.City;
import objectClasses.Customer;
import objectClasses.Map;
import objectClasses.MapSight;
import objectClasses.PlaceOfInterest;
import objectClasses.PlaceOfInterestSight;
import objectClasses.Route;
import objectClasses.RouteSight;
import objectClasses.User;
import otherClasses.Pair;
import objectClasses.Employee.Role;
import client.ChatClient;

public class Connector {
	
	public static User user;
	
	public static Stage mainStage;
	
	public static JFXButton sideButton;
	
	public static ChatClient client;

	public static final int PORT = 5555;
	public static final String LOCAL_HOST = "localhost";
	
	public static boolean isEdit = true;
	
	
	public static String listType = "City";
	
	public static boolean searchedCity = true;
	
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
	
	public static ArrayList<String> getCitiesNames(ArrayList<City> cityList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (City city : cityList)
			nameList.add(city.getCityName());
		return nameList;
	}
	
	public static ArrayList<String> getMapsNames(ArrayList<MapSight> mapList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (MapSight map : mapList)
			nameList.add(map.getCopyMap().getName());
		return nameList;
	}

	public static ArrayList<String> getPOIsNames(ArrayList<PlaceOfInterestSight> poiList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (PlaceOfInterestSight poi : poiList)
			nameList.add(poi.getCopyPlace().getName());
		return nameList;
	}
	
	public static ArrayList<String> getRoutesNames(ArrayList<RouteSight> routeList)
	{
		ArrayList<String> nameList = new ArrayList<String>();
		for (RouteSight route : routeList)
			nameList.add("route " + route.getCopyRoute().getId());
		return nameList;
	}
}
