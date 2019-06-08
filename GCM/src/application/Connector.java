package application;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;
import classes.City;
import classes.Employee.Role;
import classes.MapSight;
import classes.PlaceOfInterestSight;
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
	
	public static List<ImageView> imageList = new ArrayList<ImageView> ();
	
	public static ArrayList<City> searchCityResult;
	public static ArrayList<MapSight> searchMapResult;
	public static ArrayList<PlaceOfInterestSight> searchPOIResult;
	public static ArrayList<RouteSight> searchRouteResult;
	
}
