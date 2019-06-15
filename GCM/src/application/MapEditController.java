package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

/**
 * @author tomer treat edit map controller and connect to the client
 */
public class MapEditController
{

	private boolean firstPOIAdded = true;

	private Image realPOI = null;

	private Map map;

	private Bounds boundsInScene;

	private String readpath; // TODO Kohavi make sure one cant save map w/out image

	private List<Location> locList;

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

	private ArrayList<Location> toDelete;

	private boolean changedImage;

	/**
	 * open new fxml page
	 * 
	 * @param FXMLpage the page we want to open
	 * @throws IOException cannot open the page
	 */
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

	/**
	 * initialize variables
	 * 
	 * @throws IOException problem with the variables load
	 */
	public void initialize() throws IOException
	{
		changedImage = false;
		toDelete = new ArrayList<Location>();

		realPOI = new Image(new FileInputStream("Pics\\POI.png"));

		if (Connector.isEdit) // if its edit, load the data
		{
			map = Connector.selectedMap;
			Name.setText(map.getName());
			InfoBox.setText(map.getInfo());
			BufferedImage bufIm = Connector.client.fetchImage("Pics\\" + map.getImgURL());
			Image image = SwingFXUtils.toFXImage(bufIm, null);
			MapImage.setImage(image);
		}

		boundsInScene = MapImage.localToScene(MapImage.getBoundsInLocal());
		if (Connector.isEdit)
		{
			locList = map.getCopyLocations();
			for (Location loc : locList)
			{
				POIImage poiImage = new POIImage(false, false, loc.getCopyPlaceOfInterest().getName(), loc);
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
					poiImage = new POIImage(true, false, "", null);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				poiImage.image.setX((click.getX() + boundsInScene.getMinX() - 15));
				poiImage.image.setY((click.getY() + boundsInScene.getMinY() - 32));
				Connector.imageList.add(poiImage);
				mainPane.getChildren().add(poiImage.image);
			}
		});

	}

	/**
	 * Handle add map request.
	 * 
	 * @param event user ask for add new image
	 * @throws FileNotFoundException cannot add the image
	 */
	@FXML
	void addImage(ActionEvent event) throws FileNotFoundException
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose Image");
		fc.getExtensionFilters().add(new ExtensionFilter("Images", "*.png", "*.jpg"));
		File f = fc.showOpenDialog(null);
		if (f != null)
		{
			changedImage = true; // TODO check if i handled it correctly
			readpath = f.getAbsolutePath();
			FileInputStream inputstream = new FileInputStream(readpath);
			Image image = new Image(inputstream);
			MapImage.setImage(image);
		}
	}

	static String generateRandomString(int length)
	{
		char[] arr = new char[length];
		Random r = new Random();
		for (int i = 0; i < length; i++)
		{
			boolean caps = r.nextBoolean();
			arr[i] = (char) (r.nextInt(26) + (caps ? 'A' : 'a'));
		}
		return String.valueOf(arr);
	}

	// TODO Kohavi can press add POI without pressing on the map
	
	/**
	 * Apply the changes tracked.
	 * 
	 * @param event user click on edit map
	 * @throws IOException 
	 */
	@FXML
	void apply(ActionEvent event) throws IOException
	{
		if (!Connector.isEdit && !changedImage)
		{
			Connector.errorMsg = "New map doesn't have picture.";
			openNewPage("ErrorScene.fxml");
			return;
		}
		try
		{
			if (Connector.isEdit)
			{
				map.setName(Name.getText());
				map.setInfo(InfoBox.getText());
				if (changedImage)
				{
					String generatedPath = Connector.selectedCity.getCityName() + " " + generateRandomString(15) + ".png";
					map.setImgURL(generatedPath);
					Connector.client.sendImage(readpath, "Pics\\" + generatedPath);
				}
			}
			else
			{
				String generatedPath = "Pics\\" + Connector.selectedCity.getCityName() + generateRandomString(15) + ".png";
			
				MapSight mapS = Connector.client.createMap(Connector.selectedCity.getId(), Name.getText(),
						InfoBox.getText(), generatedPath, Connector.selectedCity.getCopyUnpublishedVersions().get(0).getId());
				Connector.searchMapResult.add(mapS);
				map = mapS.getCopyMap();

				map.setImgURL(generatedPath);
				Connector.client.sendImage(readpath, "Pics\\" + generatedPath);

			}
			for (Location loc : toDelete)
				Connector.client.deleteObject(loc);
			ArrayList<Location> toCreate = new ArrayList<Location>();
			for (POIImage poi : Connector.imageList)
			{
				poi.getLoc()._setMapId(map.getId());
				toCreate.add(poi.getLoc());
			}
			ArrayList<Integer> idList = Connector.client.createLocations(toCreate);
			for (int i = 0; i < idList.size(); i++)
				toCreate.get(i)._setId(idList.get(i));
			map._setLocationsList(new ArrayList<Location>());
			Connector.client.update(map);
			map._setLocationsList(toCreate);

			// TODO sigal create removal list for route edit
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

	/**
	 * Handle add POI location event. We will try to define a new location on the
	 * location the user clicked on.
	 * 
	 * @param event user click on add new point of interest to the map
	 * @throws IOException cannot add the new point of interest to the map
	 */
	@FXML
	void addPOILoc(ActionEvent event) throws IOException
	{
		Connector.choosenPOIInLoc = null;
		openNewPage("ChoosePOIScene.fxml");
		if (Connector.choosenPOIInLoc != null)// didn't cancel
		{
			POIImage poi = Connector.imageList.get(Connector.imageList.size() - 1);
			poi.setName(Connector.choosenPOIInLoc.getName());
			double[] cord = new double[2];
			cord[0] = poi.image.getX() - boundsInScene.getMinX();
			cord[1] = poi.image.getY() - boundsInScene.getMinY();
			poi.image.setImage(realPOI);
			poi.isNew = false;
			poi.setLoc(Location._createLocalLocation(Connector.choosenPOIInLoc, cord));
			firstPOIAdded = true;
			Connector.imageList.add(poi);
		}
	}

	/**
	 * remove some parts in the map
	 * 
	 * @param event user click on remove
	 */
	@FXML
	void removeSelected(ActionEvent event)
	{
		for (POIImage img : Connector.removablePOIList)
		{
			mainPane.getChildren().remove(img.image);
			try
			{
				if (img.getLoc().getId() != -1)
				{
					System.out.println(img.getLoc().getId());
					toDelete.add(img.getLoc());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Connector.imageList.remove(img);
		}
		Connector.removablePOIList.clear();
	}

	/**
	 * go to the previous page
	 * 
	 * @param event user click go previous page
	 */
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
