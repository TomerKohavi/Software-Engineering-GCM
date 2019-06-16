package io_commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * transfer images from client to server and return the result to the client
 * 
 * @author Sigal
 */
public class ImageTransfer extends Command
{

	/**
	 * Constructor
	 * 
	 * @param readpath  the path of the image we want to transfer
	 * @param writepath the path that the image will be saved to on the receiver
	 *                  side
	 * @param requested load/save
	 */
	public ImageTransfer(String readpath, String writepath, boolean requested, String toDelete)
	{
		this.readpath = readpath;
		this.writepath = writepath;
		this.requested = requested;
		this.toDelete = toDelete;
		this.SrIm = new SerializableImage();
	}

	/**
	 * load the image from the server
	 * @throws IOException 
	 */
	public void readImageFromFile() throws IOException
	{
		try
		{
			File file = new File(readpath);
			SrIm.image = ImageIO.read(file);
		}
		catch (IOException e)
		{
			SrIm.image = ImageIO.read(new File("Pics\\No_Image"));
		}
	}

	/**
	 * returns the actual image.
	 * 
	 * @return the image from the server
	 */
	public BufferedImage getImage()
	{
		return this.SrIm.image;
	}

	/**
	 * save the image into the server
	 * 
	 * @throws IOException if write doesn't work
	 */

	public void saveImage() throws IOException
	{
		if (writepath != null)
			ImageIO.write(SrIm.image, "png", new File(this.writepath));
		if (toDelete != null)
			(new File(this.toDelete)).delete();
	}

	/**
	 * delete the request
	 */
	public void delete()
	{
	}

	private SerializableImage SrIm;
	public String readpath, writepath, toDelete;
	public boolean requested;
}
