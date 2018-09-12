package ppu;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import gameboy.MegiHertz;
import gameboy.TickBasedComponend;
import ppu.PixelInformation.Source;

public class PixelFifo extends TickBasedComponend
{
	private static final int MINIMUM_FIFO_SIZE = 8;

	private LinkedList<PixelInformation> fifo;
	private ScrollXRegister scrollX;
	private BackgroundAndWindowColorPalette backgroundAndWindowColorPalette;
	private ObjectColorPalette0 obj0;
	private ObjectColorPalette1 obj1;
	private LCD lcd;

	private int lcdXDrawPosition;
	private int lcdYDrawPosition;
	private int xOffset;
	private boolean isLineFinished;

	public PixelFifo(ScrollXRegister scrollX, ScrollYRegister scrollY,
			BackgroundAndWindowColorPalette backgroundAndWindowColorPalette, ObjectColorPalette0 obj0,
			ObjectColorPalette1 obj1, LCD lcd)
	{
		super(MegiHertz.get(4));
		this.scrollX = scrollX;
		this.backgroundAndWindowColorPalette = backgroundAndWindowColorPalette;
		this.obj0 = obj0;
		this.obj1 = obj1;
		this.lcd = lcd;

		lcdXDrawPosition = 0;
		lcdYDrawPosition = 0;
		xOffset = this.scrollX.getXScroll() % 8;

		fifo = new LinkedList<>();
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if (xOffset == -1)
		{
			xOffset = this.scrollX.getXScroll() % 8;
		}

		if (fifo.size() > MINIMUM_FIFO_SIZE)
		{
			if (xOffset > 0)
			{
				fifo.poll();
				xOffset--;
				return true;
			}

			PixelInformation infos = fifo.poll();

			switch (infos.source)
			{
				case BACKGROUND_AND_WINDOW:
					lcd.setPixel(lcdXDrawPosition, lcdYDrawPosition,
							backgroundAndWindowColorPalette.getColorFor(infos.color));
					break;
				case OBJECT_0:
					lcd.setPixel(lcdXDrawPosition, lcdYDrawPosition, obj0.getColorFor(infos.color));
					break;
				case OBJECT_1:
					lcd.setPixel(lcdXDrawPosition, lcdYDrawPosition, obj1.getColorFor(infos.color));
					break;
			}

			lcdXDrawPosition++;
		}

		if (lcdXDrawPosition == LCD.LCD_WIDTH)
		{
			isLineFinished = true;
			lcdXDrawPosition = 0;
			lcdYDrawPosition++;
			xOffset = -1;

			if (lcdYDrawPosition == LCD.LCD_HEIGHT)
			{
				lcdYDrawPosition = 0;
			}
			return true;
		}

		return false;
	}

	public void add(PixelInformation num)
	{
		fifo.add(num);
	}

	public void addObjectOver(PixelInformation num, int position, boolean priority)
	{
		PixelInformation pixel = fifo.get(position);

		if ((pixel.source == Source.BACKGROUND_AND_WINDOW && num.color != 0)
				&& ((priority && pixel.color == 0) || !priority))
		{
			fifo.set(position, num);
		}
	}

	public int getXPosition()
	{
		return lcdXDrawPosition;
	}

	public boolean isFull()
	{
		return fifo.size() > 8;
	}

	public boolean isEmpty()
	{
		return fifo.isEmpty();
	}

	public void reset()
	{
		fifo.clear();
		lcdXDrawPosition = 0;
		lcdYDrawPosition = 0;
	}

	public boolean isLineFinished()
	{
		return isLineFinished;
	}

	public void nextLine()
	{
		fifo.clear();
		this.isLineFinished = false;
	}
}
