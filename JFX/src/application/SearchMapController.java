package application;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SearchMapController {
	
	private String map;

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="MapName"
    private JFXTextField MapName; // Value injected by FXMLLoader

    @FXML // fx:id="MapNotValid"
    private Text MapNotValid; // Value injected by FXMLLoader
    
    @FXML
    void searchMap(ActionEvent event) {
    	map = MapName.getText();
    	if (map.equals("none"))
    	{
    		MapNotValid.setOpacity(1);
    	}
    	else
    	{
    		MapNotValid.setOpacity(0);
    		
    	}
    }
}
