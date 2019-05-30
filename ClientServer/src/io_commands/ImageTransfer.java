package io_commands;

import java.awt.image.BufferedImage;

public class ImageTransfer extends Command{
	
	public ImageTransfer()
	{
		im = new BufferedImage(10, 10, 10);
	}
	
	public BufferedImage im;

}
