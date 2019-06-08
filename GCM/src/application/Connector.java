package application;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;
import classes.City;
import classes.Map;
import classes.Employee.Role;
import classes.MapSight;
import classes.PlaceOfInterest;
import classes.PlaceOfInterestSight;
import classes.Route;
import classes.RouteSight;
import classes.User;
import javafx.stage.Stage;
import client.ChatClient;

public class Connector {
	
	public static User user;
	
	public static Stage mainStage;
	
	public static JFXButton sideButton;
	
	public static ChatClient client;

	public static final int PORT = 5555;
	public static final String LOCAL_HOST = "localhost";
	
	
	public static String listType = "City";
	
	public static boolean searchedCity = true;
	
	public static List<POIImage> imageList = new ArrayList<POIImage> ();
	
	public static List<POIImage> removablePOIList = new ArrayList<POIImage> ();

	public static boolean unpublished = false;
	
	public static City selctedCity;
	public static Map selctedMap;
	public static PlaceOfInterest selctedPOI;
	public static Route selctedRoute;
	
	public static ArrayList<City> searchCityResult;
	public static ArrayList<MapSight> searchMapResult;
	public static ArrayList<PlaceOfInterestSight> searchPOIResult;
	public static ArrayList<RouteSight> searchRouteResult;
	
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
