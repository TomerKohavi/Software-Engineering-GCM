package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MapEditController {

	@FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="Name"
    private JFXTextField Name; // Value injected by FXMLLoader

    @FXML // fx:id="InfoBox"
    private TextArea InfoBox; // Value injected by FXMLLoader

    @FXML // fx:id="ApplyChanges"
    private JFXButton ApplyChanges; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private JFXButton Back; // Value injected by FXMLLoader

    @FXML // fx:id="IncorrectText"
    private Text IncorrectText; // Value injected by FXMLLoader

    @FXML // fx:id="MapImage"
    private ImageView MapImage; // Value injected by FXMLLoader
    
    @FXML // fx:id="AddImageButton"
    private JFXButton AddImageButton; // Value injected by FXMLLoader
    
    public void initialize() {
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    		MapImage.setImage();
//    	}
    	
    }
    
    @FXML
    void addImage(ActionEvent event) throws FileNotFoundException {
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Choose Image");
    	fc.getExtensionFilters().add(new ExtensionFilter("Images", "*.png", "*.jpg"));
    	File f = fc.showOpenDialog(null);
    	if (f != null)
    	{
    		FileInputStream inputstream = new FileInputStream(f.getAbsolutePath()); 
        	Image image = new Image(inputstream);
        	MapImage.setImage(image);
    	}
    	
    	
    }

    @FXML
    void apply(ActionEvent event) {
//    	Name.getText();
//		InfoBox.getText();
//		MapImage.getImage();
    	mainPane.getScene().getWindow().hide();
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
