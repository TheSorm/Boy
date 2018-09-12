package ppu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import connectos.LcdConnector;
import gameboy.MegiHertz;
import gameboy.TickBasedComponend;

public class LCD extends TickBasedComponend
{
	public static final int LCD_WIDTH = 160;
	public static final int LCD_HEIGHT = 144;

	private LCDControllRegister lcdControll;
	private BufferedImage lcdContent;
	private LcdConnector lcdConnector;

	public LCD(LCDControllRegister lcdControll, LcdConnector lcdConnector)
	{
		super(MegiHertz.get(4));
		this.lcdControll = lcdControll;
		this.lcdConnector = lcdConnector;
		lcdContent = new BufferedImage(LCD_WIDTH, LCD_HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		lcdConnector.setLcdOn(lcdControll.isLcdOn());

		return false;
	}

	public void setPixel(int x, int y, int color)
	{
		lcdContent.setRGB(x, y, color);
		lcdConnector.setImage(lcdContent);
	}
}
