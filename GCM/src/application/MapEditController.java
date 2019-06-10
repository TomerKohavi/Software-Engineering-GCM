package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.embed.swing.SwingFXUtils;
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
import objectClasses.Location;
import objectClasses.Map;
import objectClasses.MapSight;

public class MapEditController
{

	private boolean firstPOIAdded = true;

	private Image realPOI = null;

	private Map map;

	private Bounds boundsInScene;

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

	void openNewPage(String FXMLpage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLpage));
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(mainPane.getScene().getWindow());
		stage.setScene(new Scene((Parent) loader.load()));
		stage.setResizable(false);

		// showAndWait will block execution until the window closes...
		stage.showAndWait();
	}

	public void initialize() throws IOException
	{

		realPOI = new Image(new FileInputStream("Pics\\POI.png"));

		if (Connector.isEdit) // if its edit, load the data
		{
			map = Connector.selectedMap;
			Name.setText(map.getName());
			InfoBox.setText(map.getInfo());
			BufferedImage bufIm = Connector.client.getImage("Pics\\" + map.getImgURL());
			Image image = SwingFXUtils.toFXImage(bufIm, null);
			MapImage.setImage(image);
		}

		boundsInScene = MapImage.localToScene(MapImage.getBoundsInLocal());
		if (Connector.isEdit)
		{
			List<Location> locList = map.getCopyLocations();
			for (Location loc : locList)
			{
				POIImage poiImage = new POIImage(false, false);
				poiImage.image.setX(loc.getCoordinates()[0] + boundsInScene.getMinX());
				poiImage.image.setY(loc.getCoordinates()[1] + boundsInScene.getMinY());
				Connector.imageList.add(poiImage);
				mainPane.getChildren().add(poiImage.image);
			}
		}

		MapImage.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent click)
			{
				if (!firstPOIAdded)
				{
					mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
					Connector.imageList.remove(Connector.imageList.size() - 1);
				}
				firstPOIAdded = false;
				POIImage poiImage = null;
				try
				{
					poiImage = new POIImage(true, false);
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
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
	void addImage(ActionEvent event) throws FileNotFoundException
	{
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
	void apply(ActionEvent event)
	{
		try
		{
			if (Connector.isEdit)
			{
				map.setName(Name.getText());
				map.setInfo(InfoBox.getText());
//    	map.setImgURL(imgURL);
//		MapImage.getImage(); 
				Connector.client.update(map);
			}
			else
			{
				MapSight mapS = Connector.client.createMap(Connector.selectedCity.getId(), Name.getText(),
						InfoBox.getText(), null, Connector.selectedCity.getCopyUnpublishedVersions().get(0).getId());
				Connector.searchMapResult.add(mapS);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!firstPOIAdded)
		{
			mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
			Connector.imageList.remove(Connector.imageList.size() - 1);
		}
		Connector.imageList.clear();
		mainPane.getScene().getWindow().hide();
	}

	@FXML
	void addPOILoc(ActionEvent event) throws IOException
	{
		Connector.choosenPOIInLoc = null;
		openNewPage("ChoosePOIScene.fxml");
		if (Connector.choosenPOIInLoc != null)
		{ // didn't cancel
			POIImage poi = Connector.imageList.get(Connector.imageList.size() - 1);
			double[] cord = new double[2];
			cord[0] = poi.image.getX() - boundsInScene.getMinX();
			cord[0] = poi.image.getX() - boundsInScene.getMinY();
			Location newLoc = new Location(map, Connector.choosenPOIInLoc, cord);
			newLoc.saveToDatabase();
			poi.image.setImage(realPOI);
			poi.isNew = false;
			firstPOIAdded = true;
		}
	}

	@FXML
	void removeSelected(ActionEvent event)
	{
		for (POIImage img : Connector.removablePOIList)
		{
			mainPane.getChildren().remove(img.image);
			Connector.imageList.remove(img);
		}
		Connector.removablePOIList.clear();
	}

	@FXML
	void goBack(ActionEvent event)
	{
		if (!firstPOIAdded)
		{
			mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
			Connector.imageList.remove(Connector.imageList.size() - 1);
		}
		Connector.imageList.clear();
		mainPane.getScene().getWindow().hide();
	}

}
