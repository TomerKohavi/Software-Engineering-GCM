package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * @author tomer
 * treat the image in the point of interest
 */
public class POIImage {
	
	public ImageView image;
	
	private Image realImg, addedImage, removeImg;
	public boolean isNew;
	private boolean isRemovable, dontChange;
	private String poiName;
	
	/**
	 * Constructor.
	 * @param _isNew if the point of interest is new
	 * @param _dontChange if we want to change to point of interest
	 * @param _poiName the name of the point of interest
	 * @throws FileNotFoundException cannot find the image or the point of interest
	 */
	public POIImage(boolean _isNew, boolean _dontChange, String _poiName) throws FileNotFoundException {
		
		isNew = _isNew;
		isRemovable = false;
		dontChange = _dontChange;
		poiName = _poiName;
		
		realImg = new Image(new FileInputStream("Pics\\POI.png"));
		addedImage = new Image(new FileInputStream("Pics\\Add_POI.png"));
    	removeImg = new Image(new FileInputStream("Pics\\Remove_POI.png"));
    	
    	if (isNew)
    		image = new ImageView(addedImage);
    	else
    		image = new ImageView(realImg);
		
		image.setOnMouseClicked(new EventHandler<MouseEvent>() {

    	    @Override
    	    public void handle(MouseEvent click) {
    	    	if (dontChange)
    	    	{
    	    		if (Connector.poiNameTextArea.getText().equals(""))
    	    		{
    	    				Connector.poiNameTextArea.setText(poiName);
    	    				System.out.println(image.getLayoutX() + "" + image.getLayoutY());
    	    				System.out.println(image.getX() + "" + image.getY());
    	    				Connector.poiNameTextArea.setX(image.getX() - 15);
    	    				Connector.poiNameTextArea.setY(image.getY());
    	    				Connector.poiNameTextArea.setVisible(true);
    	    		}
    	    		else
    	    		{
    	    			Connector.poiNameTextArea.setText("");
    	    			Connector.poiNameTextArea.setVisible(false);
    	    		}
    	    	}
    	    	if (!Connector.unpublished || isNew || Connector.isEdit)
    	    		return;
    	    	isRemovable = !isRemovable;
    	    	if (isRemovable) {
    	    		image.setImage(removeImg);
    	    		addToRemovalList();
    	    	}
    	    	else {
    	    		image.setImage(realImg);
    	    		removeFromRemovalList();
    	    	}
        	}
    	});
	}
	
	/**
	 * add to list of things to remove
	 */
	private void addToRemovalList() {
		Connector.removablePOIList.add(this);
	}
	
	/**
	 * remove all things from the remove list
	 */
	private void removeFromRemovalList() {
		Connector.removablePOIList.remove(this);
	}
	
	/**
	 * set to new name
	 * @param str new name
	 */
	public void setName(String str)
	{
		poiName = str;
	}
	
}
