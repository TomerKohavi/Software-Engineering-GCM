package application;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomePageController {
	
	private String name, info;
	
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
    
    @FXML // fx:id="StopsTable"
    private TableView<String> StopsTable; // Value injected by FXMLLoader
    
    @FXML // fx:id="ShowMapButton"
    private JFXButton ShowMapButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="addPOILocButton"
    private JFXButton AddPOILocButton; // Value injected by FXMLLoader
    
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

    
    
    
    void startLoad() throws FileNotFoundException {
    	mainPane.setDisable(true);
    	Random r = new Random();
		Image image = new Image(new FileInputStream("Pics\\Gif_" + (r.nextInt(4) + 1) + ".gif"));
		LoadingGif.setImage(image);
    	LoadingGif.setVisible(true);
    }
    
    void endLoad() {
    	LoadingGif.setVisible(false);
    	mainPane.setDisable(true);
    }
    
    void openNewPage(String FXMLpage) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainPane.getScene().getWindow());
        stage.setScene(new Scene((Parent) loader.load()));
        stage.setResizable(false);

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
 	    clearInfo(true);
    }
    
    void clearInfo(boolean clearList) {
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
	    AddPOILocButton.setVisible(false);
		StopsTable.setVisible(false);
		BuyButton.setVisible(false);
		ReSubscribeButton.setVisible(false);
	    for (ImageView img : Connector.imageList) {
	    	mainPane.getChildren().remove(img);
	    }
	    Connector.imageList.clear();
	    ReportCityName.setVisible(false);
	    ReportInfo.setVisible(false);
	    FirstDate.setVisible(false);
	    LastDate.setVisible(false);
	    WatchButton.setVisible(false);
    	if (Connector.unpublished) {
			EditButton.setVisible(true);
			RemoveButton.setVisible(true);
			if (Connector.listType.equals("Map") || Connector.listType.equals("POI") || Connector.listType.equals("Route"))
		    	CreateButton.setVisible(true);
		    else
		    	CreateButton.setVisible(false);
    	}
    	else {
    		EditButton.setVisible(false);
    		RemoveButton.setVisible(false);
    	}
    }
    
    private void fillCityInfo(String currentItemSelected) {
    	InfoPane.setVisible(true);
    	ResultName.setText(currentItemSelected); // set name
    	ResultInfo.setText("Info"); // set info
		Text1.setText("Maps Found: " + 2); // #Maps for the city 
		Text2.setText("POI Found: " + 2); // #POI for the city
	    Text3.setText("Routes Found: " + 2); // #Routes for the city
	    BuyButton.setVisible(true);
//	    if (user is director of content department)
//		    BuyButton.setText("Change Price");
//	    else if (user is regulsar employee or CEO)
//	 	    BuyButton.setVisible(false);
//	    else if (Connector.user_id != -1  && subscribed to the map) {// check if the user is subscribed to the map
//	    	BuyButton.setText("Download");
//	    	if (three days left for subscription)
//	    		ReSubscribeButton.setVisible(true);
//    	}
//	    else
//		    BuyButton.setText("Buy");
	    ShowMapButton.setVisible(false);
	    SideMap.setDisable(false);
	    SidePOI.setDisable(false);
	    SideRoutes.setDisable(false);
    }
    
    public void initialize() {
    	
    	Connector.sideButton = SideSearch;
    	Connector.sideButton.setOpacity(1);
    
    	if (Connector.usr_id != -1)  {
    		LoginButton.setText("Log Off");
    		UserInfoButton.setVisible(true);
    	}
    	else
    		LoginButton.setText("Login");
    	
//    	if (employee) { // check if employee -> can edit
//    		UnpublishSearch.SetVisible(true);
//    	}
    	
    	SideReport.setDisable(false);
	    SideUsers.setDisable(false);
    	
    	MainList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    	    @Override
    	    public void handle(MouseEvent click) {

    	        if (click.getClickCount() == 2) {
    	           int selectedIndex = MainList.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                       City city = Connector.searchCityResult.get(selectedIndex);
    	        	   InfoPane.setVisible(true);
    	        	   if (Connector.listType.equals("City")) { // City 
    	        		   fillCityInfo(currentItemSelected);
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
    	        	   else if (Connector.listType.equals("Report")) { // users
    	        		   ReportCityName.setVisible(true);
    	        		   ReportInfo.setVisible(true);
    	        		   InfoPane.setVisible(false);
    	        		   ReportCityName.setText(currentItemSelected); // set name and type
    	        		   ReportInfo.setText("Number of Maps: " + 6 + "\n" +
    	        				   "Number of One Time Purchases: " + 6 + "\n" +
    	        				   "Number of Subscriptions: " + 6 + "\n" +
    	        				   "Number of Re-Subscriptions: " + 6 + "\n" +
    	        				   "Number of Views: " + 6 + "\n" +
    	        				   "Number of Downloads: " + 6);
    	        	   }
    	        	   else if (Connector.listType.equals("Users")) { // users
    	        		   ResultName.setText(currentItemSelected); // set name and type
    	        		   ResultInfo.setText("Name: " + "first" + " " + "last" + "\n" +
    	        				   "Email: " + "coreset@sigal.is.gay" + "\n" +
    	        				   "Phone: " + "0544444444");
    	        		   ViewPurchaseHistoryButton.setVisible(true);
    	        		   EditButton.setDisable(false);
    	        		   RemoveButton.setDisable(false);
    	        	   }
    	           }
    	        }
    	    }
    	});
    	
    	MapImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

    	    @Override
    	    public void handle(MouseEvent click) {
    	    	if (!Connector.unpublished)
    	    		return;
    	    	if (AddPOILocButton.isVisible()) {
    	    		mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
    	    	}
        		AddPOILocButton.setVisible(true);
        		Image image = null;
				try {
					image = new Image(new FileInputStream("Pics\\Add_POI.png"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	    ImageView img = new ImageView(image);
    	    	Bounds boundsInScene = MapImage.localToScene(MapImage.getBoundsInLocal());
    	    	img.setX(click.getX() + boundsInScene.getMinX() - 15);
    	    	img.setY(click.getY() + boundsInScene.getMinY() - 32);
    	        Connector.imageList.add(img);
        	    mainPane.getChildren().add(img);
        	}
    	});
    	
    }
    
    @FXML
    void search(ActionEvent event) throws IOException {
    	setMainSideButton(SideSearch);
    	Connector.searchedCity = true;
    	Connector.listType = "City";
    	name = CityNameBox.getText();
    	info = CityInfoBox.getText();
    	name = POINameBox.getText();
    	info = POIInfoBox.getText();
    	if (name.equals("city"))
    	{
    		if (UnpublishSearch.isSelected()) {// search unpublished
    			Connector.unpublished = true;
    			System.out.println();
    		}
    		else {// search published
    			Connector.unpublished = false;
    			System.out.println();
    		}
    		
    		clearInfo(true);

    		MainList.getItems().addAll("city1", "city2");
    		NotValid.setOpacity(0);
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
    
    @FXML
    void watch(ActionEvent event) {
    	if (FirstDate.getValue() == null || LastDate.getValue() == null) { // date not valid
    		DateNotValid.setVisible(true);
    	}
    	else {
    		MainList.getItems().addAll("city1", "city2");
    	}
    		
    }
    
    @FXML
    void viewPurchaseHistory(ActionEvent event) throws IOException {
    	openNewPage("PurchaseHistoryScene.fxml");
    }
    
    @FXML
    void showMapImage(ActionEvent event) throws FileNotFoundException {
    	show_map = !show_map;
    	if (show_map) {
    		InfoPane.setVisible(false);
    		MapImage.setVisible(true);
    		ShowMapButton.setText("Hide Map");
    		Image image = new Image(new FileInputStream("Pics\\POI.png"));
    		List<Point> posList = new ArrayList<Point> ();
    		posList.add(new Point(1211 - 334, 578 - 130));
    		posList.add(new Point(1211 - 334 + 50, 578 - 130 + 50));
    	    for (Point p : posList) {
    	    	ImageView img = new ImageView(image);
    	    	img.setX(p.getX());
    	    	img.setY(p.getY());
    	        Connector.imageList.add(img);
    	    }
    	    mainPane.getChildren().addAll(Connector.imageList);
    	}
    	else {
    		InfoPane.setVisible(true);
    		MapImage.setVisible(false);
    		AddPOILocButton.setVisible(false);
    		ShowMapButton.setText("Show Map");
    	    for (ImageView img : Connector.imageList)
    	    	mainPane.getChildren().remove(img);
    	    Connector.imageList.clear();
    	}
    }
    
    @FXML
    void showSearch(ActionEvent event) {
    	Connector.listType = "City";
    	setMainSideButton(SideSearch);
		MainList.getItems().addAll("city1", "city2");
		fillCityInfo("city1");
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
    void editUser(ActionEvent event) throws IOException {
    		openNewPage("EditUserScene.fxml");
    }
    
    @FXML
    void showMaps(ActionEvent event) throws FileNotFoundException {
    	Connector.listType = "Map";
    	setMainSideButton(SideMap);
		MainList.getItems().addAll("map1", "map2");
    }

    @FXML
    void showPOI(ActionEvent event) {
    	Connector.listType = "POI";
    	setMainSideButton(SidePOI);
		MainList.getItems().addAll("POI1", "POI2");
    }

    @FXML
    void showRoutes(ActionEvent event) {
    	Connector.listType = "Route";
    	setMainSideButton(SideRoutes);
		MainList.getItems().addAll("route1", "route2");
    }

    @FXML
    void showReport(ActionEvent event) {
    	Connector.listType = "Report";
    	setMainSideButton(SideReport);
    	FirstDate.setVisible(true);
    	LastDate.setVisible(true);
    	WatchButton.setVisible(true);
    }

    @FXML
    void showUsers(ActionEvent event) {
    	Connector.listType = "Users";
    	setMainSideButton(SideUsers);
		MainList.getItems().addAll("user1", "user2");
    }
    
    @FXML
    void callEdit(ActionEvent event) throws IOException {
    	openNewPage(Connector.listType + "EditScene.fxml");
    }
    
    @FXML
    void callRemove(ActionEvent event) {
    	int index = MainList.getSelectionModel().getSelectedIndex();
    	MainList.getItems().remove(index);
    	// call server to remove that thing -> can check which type it is by looking at the value of Connector.listType
    	clearInfo(false);
    }
    
    @FXML
    void callCreate(ActionEvent event) throws IOException {
    	openNewPage(Connector.listType + "EditScene.fxml");
    }
    
    @FXML
    void addPOILoc(ActionEvent event) throws IOException {
    	openNewPage("ChoosePOIScene.fxml");
    }
    
    @FXML
    void callReSubscribe(ActionEvent event) throws IOException {
    	openNewPage("ReSubscribeScene.fxml");
    }
 
    @FXML
    void openBuyWindodw(ActionEvent event) throws IOException {
    	if (Connector.usr_id == -1) // check if logged in
    		openNewPage("LoginErrorScene.fxml");
    	else {
    		if (BuyButton.getText().equals("Buy"))
    			openNewPage("BuyScene.fxml");
    		else if (BuyButton.getText().equals("Download"))
    			// download
    			System.out.println();
    		else if (BuyButton.getText().equals("Change Price"))
    			openNewPage("ChangePriceScene.fxml");
    	}
    }
    
}
