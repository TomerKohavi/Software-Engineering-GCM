package io_commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

public class ImageTransfer extends Command{
	
	public ImageTransfer(String pathname, boolean requested)
	{
		this.pathname = pathname;
		this.requested = requested;
		this.SrIm = new SerializableImage();
	}
	
	public void loadImage()
	{
		try {
			File file = new File(pathname);
			SrIm.image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage()
	{
		return this.SrIm.image;
	}
	
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
}
