package application;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;
import classes.Employee.Role;
import javafx.stage.Stage;
import client.ChatClient;

public class Connector {
	
	public static int usr_id = -1;
	public static Role usr_role;
	
	public static Stage mainStage;
	
	public static JFXButton sideButton;
	
	public static ChatClient client;

	public static final int PORT = 5555;
	public static final String LOCAL_HOST = "localhost";
	
	
	public static String listType = "City";
	
	public static boolean searchedCity = true;
	
	public static List<ImageView> imageList = new ArrayList<ImageView> ();
	
//	static void loadPage(AnchorPane pane, String FXMLpage) throws IOException
//	{
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample2Scene.fxml"));  
//        Stage stage = new Stage();
//        stage.initOwner(pane.getScene().getWindow());
//        stage.setScene(new Scene((Parent) loader.load()));
//
//        // showAndWait will block execution until the window closes...
//        stage.showAndWait();
//	}
	
}
