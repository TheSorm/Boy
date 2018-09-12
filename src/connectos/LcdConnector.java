package connectos;

import java.awt.image.BufferedImage;

import ppu.LCD;

public class LcdConnector
{
	private BufferedImage lcdOutput;
	private boolean isLcdOn;

	public LcdConnector()
	{
		this.lcdOutput = new BufferedImage(LCD.LCD_WIDTH, LCD.LCD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		isLcdOn = false;
	}

	public void setImage(BufferedImage lcdOutput)
	{
		this.lcdOutput = lcdOutput;
	}

	public BufferedImage getImage()
	{
		return lcdOutput;
	}

	public void setLcdOn(boolean isOn)
	{
		this.isLcdOn = isOn;
	}

	public boolean isLcdOn()
	{
		return isLcdOn;
	}
}
