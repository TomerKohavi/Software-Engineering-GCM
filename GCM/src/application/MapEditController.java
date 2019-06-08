package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
     
    @FXML // fx:id="addPOILocButton"
    private JFXButton AddPOILocButton; // Value injected by FXMLLoader
    
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
    
    public void initialize() {
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    		MapImage.setImage();
//    	}
    	
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
    void addPOILoc(ActionEvent event) throws IOException {
    	openNewPage("ChoosePOIScene.fxml");
    }

    @FXML
    void goBack(ActionEvent event) {
    	mainPane.getScene().getWindow().hide();
    }

	
}
