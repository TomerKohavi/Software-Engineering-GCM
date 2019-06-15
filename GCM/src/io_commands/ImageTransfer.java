package io_commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Sigal transfer images from client to server and return the result to
 *         the client
 */
public class ImageTransfer extends Command
{

	/**
	 * Constructor
	 * @param readpath  the path of the image we want to transfer
	 * @param writepath the path that the image will be saved to on the receiver side
	 * @param requested load/save
	 */
	public ImageTransfer(String readpath, String writepath, boolean requested)
	{
		this.readpath = readpath;
		this.writepath = writepath;
		this.requested = requested;
		this.SrIm = new SerializableImage();
	}

	/**
	 * load the image from the server
	 */
	public void readImageFromFile()
	{
		try
		{
			File file = new File(readpath);
			SrIm.image = ImageIO.read(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * returns the actual image.
	 * @return the image from the server
	 */
	public BufferedImage getImage()
	{
		return this.SrIm.image;
	}

	/**
	 * save the image into the server
	 * @throws IOException 
	 */
	
	public void saveImage() throws IOException
	{
		ImageIO.write(SrIm.image, "png", new File(this.writepath));
	}
	
	public void delete()
	{
	}

	private SerializableImage SrIm;
	public String readpath;
	public String writepath;
	public boolean requested;
}
