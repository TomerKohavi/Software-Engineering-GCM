package application;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SearchController {
	
	private String map;

	 @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="MapNotValid"
    private Text NotValid; // Value injected by FXMLLoader

    @FXML // fx:id="SearchBox"
    private JFXTextField SearchBox; // Value injected by FXMLLoader
    
    @FXML // fx:id="Info"
    private JFXListView<String> Info; // Value injected by FXMLLoader
    
    @FXML // fx:id="NumOfMaps"
    private Text Text1; // Value injected by FXMLLoader

    @FXML // fx:id="NumOfPOI"
    private Text Text2; // Value injected by FXMLLoader

    @FXML // fx:id="NumOfRoutes"
    private Text Text3; // Value injected by FXMLLoader
    
    void loadPage(String FXMLpage) throws IOException {
        AnchorPane pane = (AnchorPane)FXMLLoader.load((URL)this.getClass().getResource(FXMLpage));
        mainPane.getChildren().setAll(pane);
    }
    
    @FXML
    void search(ActionEvent event) throws IOException {
    	map = SearchBox.getText();
    	if (map.equals("city"))
    	{
    		Info.getItems().clear();
    		Info.getItems().addAll("map1", "map2");
    		Text1.setText("Maps Found: " + 2);
    		Text2.setText("POI Found: " + 2);
    		Text3.setText("Routes Found: " + 2);
    	}
    	else if (map.equals("poi"))
    	{
    		Info.getItems().clear();
    		Info.getItems().addAll("map1", "map2");
    		Text1.setText("City: " + "Haifa");
    		Text2.setText("Maps Found: " + 2);
    		Text3.setText("");
    	}
    	else
    	{
    		NotValid.setOpacity(1);
    	}
    }
}
