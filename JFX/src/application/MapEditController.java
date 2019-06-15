package application;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	private boolean firstPOIAdded = true;
	
	private Image realPOI = null;
	
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
    
    @FXML // fx:id="RemoveSelectedButton"
    private JFXButton RemoveSelectedButton; // Value injected by FXMLLoader
    
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
    
    public void initialize() throws FileNotFoundException {
    	
    	realPOI = new Image(new FileInputStream("Pics\\POI.png"));
    	
//    	if ()  // if its edit, load the data
//    	{
//    		Name.setText("");
//    		InfoBox.setText("");
//    		MapImage.setImage();
//    	}
    	
    	Bounds boundsInScene = MapImage.localToScene(MapImage.getBoundsInLocal());
    	
		List<Point> posList = new ArrayList<Point> ();
		posList.add(new Point((int) (50 + boundsInScene.getMinX()), (int) (50 + boundsInScene.getMinY())));
		posList.add(new Point((int) (100 + boundsInScene.getMinX()), (int) (100 + boundsInScene.getMinY())));
	    for (Point p : posList) {
	    	POIImage poiImage = new POIImage(false);
	    	poiImage.image.setX(p.getX());
	    	poiImage.image.setY(p.getY());
	        Connector.imageList.add(poiImage);
	        mainPane.getChildren().add(poiImage.image);
	    }
    	
    	MapImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

    	    @Override
    	    public void handle(MouseEvent click) {
    	    	if (!firstPOIAdded) {
    	    		mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
    	    		Connector.imageList.remove(Connector.imageList.size() - 1);
    	    	}
    	    	firstPOIAdded = false;
    	    	POIImage poiImage = null;
				try {
					poiImage = new POIImage(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
    	    	poiImage.image.setX((click.getX() + boundsInScene.getMinX() - 15));
    	    	poiImage.image.setY((click.getY() + boundsInScene.getMinY() - 32));
    	        Connector.imageList.add(poiImage);
        	    mainPane.getChildren().add(poiImage.image);
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
    	
    	if (!firstPOIAdded) {
    		mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
    		Connector.imageList.remove(Connector.imageList.size() - 1);
    	}
    	Connector.imageList.clear();
    	mainPane.getScene().getWindow().hide();
    }
    
    @FXML
    void addPOILoc(ActionEvent event) throws IOException {
    	openNewPage("ChoosePOIScene.fxml");
    	if (true) { // didn't cancel
    		Connector.imageList.get(Connector.imageList.size() - 1).image.setImage(realPOI);
    		Connector.imageList.get(Connector.imageList.size() - 1).isNew = false;
    		firstPOIAdded = true;
    	}
    }
    
    @FXML
    void removeSelected(ActionEvent event) {
    	for (POIImage img : Connector.removablePOIList) {
	    	mainPane.getChildren().remove(img.image);
	    	Connector.imageList.remove(img);
    	}
	    Connector.removablePOIList.clear();
    }

    @FXML
    void goBack(ActionEvent event) {
    	if (!firstPOIAdded) {
    		mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
    		Connector.imageList.remove(Connector.imageList.size() - 1);
    	}
    	Connector.imageList.clear();
    	mainPane.getScene().getWindow().hide();
    }

	
}
