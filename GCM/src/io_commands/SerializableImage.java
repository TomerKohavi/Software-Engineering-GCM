package io_commands;
//
//import javafx.scene.image.Image;
//import javafx.scene.image.PixelReader;
//import javafx.scene.image.PixelWriter;
//import javafx.scene.image.WritableImage;
//
//import java.io.Serializable;
//
//
//public class SerializableImage implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	private int width, height;
//    private int[][] data;
//
//    public SerializableImage() {}
//
//    public void setImage(Image image) {
//        width = ((int) image.getWidth());
//        height = ((int) image.getHeight());
//        data = new int[width][height];
//
//        PixelReader r = image.getPixelReader();
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                data[i][j] = r.getArgb(i, j);
//            }
//        }
//
//    }
//
//    public Image getImage() {
//        WritableImage img = new WritableImage(width, height);
//
//        PixelWriter w = img.getPixelWriter();
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                w.setArgb(i, j, data[i][j]);
//            }
//        }
//
//        return img;
//    }
//
//}

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * @author sigal
 * serializableImage by custom 
 */
class SerializableImage implements Serializable {
	private static final long serialVersionUID = 1L;
	transient BufferedImage image;

    /**
     * Writs the image as a stream.
     * @param out the strem output
     * @throws IOException can be thrown by imageIO
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (image != null)
        	ImageIO.write(image, "png", out);
    }
    
    /**
     * @param in the in output 
     * @throws IOException can be thrown by ImageIO
     * @throws ClassNotFoundException can be thrown by class
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }
}