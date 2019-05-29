package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePageController {
	
	private String map, info;

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
    private JFXListView<String> MainList; // Value injected by FXMLLoader
    
    @FXML // fx:id="NumOfMaps"
    private Text Text1; // Value injected by FXMLLoader

    @FXML // fx:id="NumOfPOI"
    private Text Text2; // Value injected by FXMLLoader

    @FXML // fx:id="NumOfRoutes"
    private Text Text3; // Value injected by FXMLLoader
    
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

    
//    void loadPage(String FXMLpage) throws IOException {
//    	FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));  
//        Stage stage = new Stage();
//        stage.initOwner(mainPane.getScene().getWindow());
//        stage.setScene(new Scene((Parent) loader.load()));
//
//        // showAndWait will block execution until the window closes...
//        stage.showAndWait();
//    }
    
    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    void setMainSideButton(JFXButton button)
    {
    	Connector.sideButton.setOpacity(0.5);
    	Connector.sideButton = button;
    	Connector.sideButton.setOpacity(1);
    }
    
    public void initialize() {
    	
    	Connector.sideButton = SideSearch;
    	Connector.sideButton.setOpacity(1);
    }
    
    @FXML
    void searchCity(ActionEvent event) throws IOException {
    	map = NameBox.getText();
    	info = InfoBox.getText();
    	if (map.equals("city"))
    	{
    		MainList.getItems().clear();
    		MainList.getItems().addAll("map1", "map2");
    		Text1.setText("Maps Found: " + 2);
    		Text2.setText("POI Found: " + 2);
    		Text3.setText("Routes Found: " + 2);
    		NotValid.setOpacity(0);
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
    
    @FXML
    void searchPOI(ActionEvent event) throws IOException {
    	map = NameBox.getText();
    	info = InfoBox.getText();
    	if (map.equals("poi"))
    	{
    		MainList.getItems().clear();
    		MainList.getItems().addAll("map1", "map2");
    		Text1.setText("City: " + "Haifa");
    		Text2.setText("Maps Found: " + 2);
    		Text3.setText("");
    		setMainSideButton(SidePOI);
    		NotValid.setOpacity(0);
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
    
    @FXML
    void showSearch(ActionEvent event) {
    	setMainSideButton(SideSearch);

    }
    
    @FXML
    void login(ActionEvent event) throws IOException {
    	loadPage("LoginScene.fxml");
    }
    
    @FXML
    void showMaps(ActionEvent event) {
    	setMainSideButton(SideMap);
    }

    @FXML
    void showPOI(ActionEvent event) {
    	setMainSideButton(SidePOI);

    }

    @FXML
    void showRoutes(ActionEvent event) {
    	setMainSideButton(SideRoutes);

    }
}
