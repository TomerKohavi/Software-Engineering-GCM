package io_commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author sigal
 * transfer images from client to server and return the result to the client
 */
public class ImageTransfer extends Command{
	
	/**
	 * @param pathname the path of the image we want to transfer
	 * @param requested load/save
	 */
	public ImageTransfer(String pathname, boolean requested)
	{
		this.pathname = pathname;
		this.requested = requested;
		this.SrIm = new SerializableImage();
	}
	
	/**
	 * load the image from the server 
	 */
	public void readImageFromFile()
	{
		try {
			File file = new File(pathname);
			SrIm.image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the image from the server
	 */
	public BufferedImage getImage()
	{
		return this.SrIm.image;
	}
	
	/**
	 * save the image into the server
	 * @param pathname the path of the image we want to save
	 */
	public void saveImage(String pathname)
	{
		try {
			ImageIO.write(SrIm.image, "png", new File(pathname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete()
	{
		
	}

	private SerializableImage SrIm;
	private String pathname;
	public boolean requested;
	
	public static void main(String[] args)
	{
		File file = new File("Pics\\haifa.png");
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
	}
}
