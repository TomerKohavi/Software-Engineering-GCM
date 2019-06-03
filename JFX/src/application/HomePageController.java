package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePageController {
	
	private String name, info;
	
	static private boolean show_map = false;

	 @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="MapNotValid"
    private Text NotValid; // Value injected by FXMLLoader

    @FXML // fx:id="NameBox"
    private JFXTextField NameBox; // Value injected by FXMLLoader

    @FXML // fx:id="InfoBox"
    private JFXTextField InfoBox; // Value injected by FXMLLoader

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

    
    void openNewPage(String FXMLpage) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));  
        Stage stage = new Stage();
        stage.initOwner(mainPane.getScene().getWindow());
        stage.setScene(new Scene((Parent) loader.load()));

        // showAndWait will block execution until the window closes...
        stage.showAndWait();
    }
    
    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    void setMainSideButton(JFXButton button)
    {
    	Connector.sideButton.setOpacity(0.5);
    	Connector.sideButton = button;
    	Connector.sideButton.setOpacity(1);
 	    clearInfo();
    }
    
    void clearInfo() {
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
    }
    
    public void initialize() {
    	
    	Connector.sideButton = SideSearch;
    	Connector.sideButton.setOpacity(1);
    
    	if (Connector.usr_id != -1)
    		LoginButton.setText("Log Off");
    	else
    		LoginButton.setText("Login");
    	
//    	if (employee) { // check if employee -> can edit
//    		EditButton.setVisible(true);
//    	}
    	
    	MainList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    	    @Override
    	    public void handle(MouseEvent click) {

    	        if (click.getClickCount() == 2) {
    	           String currentItemSelected = MainList.getSelectionModel()
    	                                                    .getSelectedItem();
    	           if (currentItemSelected != null) {
    	        	   InfoPane.setVisible(true);
    	        	   if (Connector.listType.equals("search")) { // search tab 
    	        		   if (Connector.searchedCity) { // City
    	        			   ResultName.setText(currentItemSelected); // set name
        	        		   ResultInfo.setText("Info"); // set info
        	        		   Text1.setText("Maps Found: " + 2); // #Maps for the city 
        	        		   Text2.setText("POI Found: " + 2); // #POI for the city
        	        		   Text3.setText("Routes Found: " + 2); // #Routes for the city
        	        		   BuyButton.setVisible(true);
        	        		   SideMap.setDisable(false);
        	        		   SidePOI.setDisable(false);
        	        		   SideRoutes.setDisable(false);
        	        		   ShowMapButton.setVisible(false);
    	        		   }
    	        		   else { // POI Search
    	        			   ResultName.setText(currentItemSelected + " - " + "Hotel"); // set name and type
        	        		   ResultInfo.setText("Info"); // set info
        	        		   Text1.setText("City: " + "Haifa"); // name of the city 
        	        		   Text2.setText("Maps Found: " + 2); // #Maps for the city 
        	        		   Text3.setText("Accessible to Disabled"); // Accessible or not
        	        		   BuyButton.setVisible(false);
        	        		   SideMap.setDisable(false);
        	        		   SidePOI.setDisable(true);
        	        		   SideRoutes.setDisable(true);
    	        		   }
    	        	   }
    	        	   else if (Connector.listType.equals("POI")) { // POI
    	        		   ResultName.setText(currentItemSelected + " - " + "Hotel"); // set name and type
    	        		   ResultInfo.setText("Info"); // set info
    	        		   Text1.setText("Accessible to Disabled"); // Accessible or not
    	        	   }
    	        	   else if (Connector.listType.equals("Map")) { // map
    	        		   ResultName.setText(currentItemSelected); // set name
    	        		   ResultInfo.setText("Info"); // set info
    	        		   ShowMapButton.setVisible(true);
    	        	   }
    	        	   else if (Connector.listType.equals("Route")) { // route
    	        		   ResultName.setText(currentItemSelected); // set name and type
    	        		   ResultInfo.setText("Info"); // set info
    	        		   Text1.setText("Accessible to Disabled"); // Accessible or not
    	        		   StopsTable.setVisible(true);
    	        		   ObservableList<String> stops = FXCollections.observableArrayList();
    	        		   StopsTable.setItems(stops);
    	        	   }
    	           }
    	        }
    	    }
    	});
    	
    }
    
    @FXML
    void searchCity(ActionEvent event) throws IOException {
    	setMainSideButton(SideSearch);
    	Connector.searchedCity = true;
    	Connector.listType = "search";
    	name = NameBox.getText();
    	info = InfoBox.getText();
    	if (name.equals("city"))
    	{
    		MainList.getItems().clear();
    		MainList.getItems().addAll("city1", "city2");
    		NotValid.setOpacity(0);
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
    
    @FXML
    void searchPOI(ActionEvent event) throws IOException {
    	setMainSideButton(SideSearch);
    	Connector.searchedCity = false;
    	Connector.listType = "search";
    	name = NameBox.getText();
    	info = InfoBox.getText();
    	if (name.equals("poi"))
    	{
    		MainList.getItems().clear();
    		MainList.getItems().addAll("poi1", "poi2");
    		NotValid.setOpacity(0);
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
    
    @FXML
    void showMapImage(ActionEvent event) {
    	show_map = !show_map;
    	if (show_map) {
    		InfoPane.setVisible(false);
    		MapImage.setVisible(true);
    		ShowMapButton.setText("Hide Map");
    	}
    	else {
    		InfoPane.setVisible(true);
    		MapImage.setVisible(false);
    		ShowMapButton.setText("Show Map");
    	}
    }
    
    @FXML
    void showSearch(ActionEvent event) {
    	Connector.listType = "search";
    	setMainSideButton(SideSearch);
    	MainList.getItems().clear();
		MainList.getItems().addAll("city1", "poi1");
    }
    
    @FXML
    void login(ActionEvent event) throws IOException {
    	if (Connector.usr_id == -1)
    		loadPage("LoginScene.fxml");
    	else {
    		Connector.usr_id = -1;
    		// send that the user has logged off to the server
    		loadPage("HomePageScene.fxml");
    	}
    		
    }
    
    @FXML
    void showMaps(ActionEvent event) {
    	Connector.listType = "Map";
    	setMainSideButton(SideMap);
    	MainList.getItems().clear();
		MainList.getItems().addAll("map1", "map2");
    }

    @FXML
    void showPOI(ActionEvent event) {
    	Connector.listType = "POI";
    	setMainSideButton(SidePOI);
    	MainList.getItems().clear();
		MainList.getItems().addAll("POI1", "POI2");
    }

    @FXML
    void showRoutes(ActionEvent event) {
    	Connector.listType = "Route";
    	setMainSideButton(SideRoutes);
    	MainList.getItems().clear();
		MainList.getItems().addAll("route1", "route2");
    }
    
    @FXML
    void callEdit(ActionEvent event) throws IOException {
    	String prefix = Connector.listType;
    	if (prefix.equals("search"))
    		prefix = Connector.searchedCity ? "City" : "POI";
    	openNewPage(prefix + "EditScene.fxml");
    }
 

    @FXML
    void openBuyWindodw(ActionEvent event) {

    }
    
}
