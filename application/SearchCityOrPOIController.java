package application;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SearchCityOrPOIController {
	
	private String map;

	 @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="MapNotValid"
    private Text NotValid; // Value injected by FXMLLoader

    @FXML // fx:id="SearchBox"
    private JFXTextField SearchBox; // Value injected by FXMLLoader

    @FXML
    void search(ActionEvent event) {
    	map = SearchBox.getText();
    	if (map.equals("none"))
    	{
    		NotValid.setOpacity(1);
    	}
    	else
    	{
    		NotValid.setOpacity(0);
    		
    	}
    }
}
