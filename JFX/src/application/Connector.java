package application;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Connector {
	
	public static int usr_id = -1;
	
	public static Stage mainStage;
	
	public static JFXButton sideButton;
	
	public static String listType = "City";
	
	public static boolean searchedCity = true;
	
	public static List<POIImage> imageList = new ArrayList<POIImage> ();
	
	public static List<POIImage> removablePOIList = new ArrayList<POIImage> ();
	
	public static boolean unpublished = false;
	
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
