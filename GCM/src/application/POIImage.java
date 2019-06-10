package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class POIImage {

//	public POI poi;
	
	public ImageView image;
	
	private Image realImg, addedImage, removeImg;
	public boolean isNew;
	private boolean isRemovable, dontChange;
	
	public POIImage(boolean _isNew, boolean _dontChange) throws FileNotFoundException {
		
		isNew = _isNew;
		isRemovable = false;
		dontChange = _dontChange;
		
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
    	    	if (!Connector.unpublished || isNew || dontChange)
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
	
	private void addToRemovalList() {
		Connector.removablePOIList.add(this);
	}
	
	private void removeFromRemovalList() {
		Connector.removablePOIList.remove(this);
	}
	
}
