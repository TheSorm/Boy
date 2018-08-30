package ppu;

import ram.AdressSpace;
import ram.RamSpace;

public class TileMap extends RamSpace
{
	private static final int START_ADRESS = 0x9800;
	private static final int END_ADRESS = 0xA000;

	private static final int TILE_MAP_SIZE = 32;
	private static final int TILE_SIZE = 8;
	private LCDControllRegister lcdControll;
	private ScrollXRegister scrollX;
	private ScrollYRegister scrollY;
	private int currentPositionInTileLine;
	private int currentTileLine;
	private int currentPixelLine;

	private AdressSpace backgroundTileMap;

	public TileMap(LCDControllRegister lcdControll, ScrollXRegister scrollX, ScrollYRegister scrollY)
	{
		super(START_ADRESS, END_ADRESS);

		backgroundTileMap = lcdControll.getSelectedBackgroundTileMap();
		this.lcdControll = lcdControll;
		this.scrollX = scrollX;
		this.scrollY = scrollY;
		reset();
	}

	public LineInformation next()
	{
		currentPositionInTileLine++;
		return new LineInformation((getValueFromSpace(backgroundTileMap,
				TwoDCoordinatesToOneD((scrollX.getXScroll() / TILE_SIZE + currentPositionInTileLine) % TILE_MAP_SIZE,
						scrollY.getYScroll() / TILE_SIZE + currentTileLine))),
				currentPixelLine);
	}

	private byte getValueFromSpace(AdressSpace space, int offset)
	{
		return (this.load(space.startPosition + offset));
	}

	public void nextPixelLine()
	{
		if (currentPixelLine < TILE_SIZE - 1)
		{
			currentPixelLine++;
		}
		else
		{
			currentPixelLine = 0;
			currentTileLine++;
		}
		currentPositionInTileLine = -1;
	}

	public void reset()
	{
		backgroundTileMap = lcdControll.getSelectedBackgroundTileMap();
		currentPositionInTileLine = -1;
		currentPixelLine = scrollY.getYScroll() % TILE_SIZE;
		currentTileLine = 0;
		// for (int i =
		// lcdControll.getSelectedBackgroundTileMap().startPosition; i <=
		// lcdControll
		// .getSelectedBackgroundTileMap().endPosition; i++)
		// {
		// if (i % 32 == 0)
		// {
		// System.out.println();
		// }
		//
		// System.out.print((this.load(i) > 0 ? 1 : 0) + " ");
		// }
		// System.out.println();
		// System.out.println("---------------------------------");
	}

	private int TwoDCoordinatesToOneD(int x, int y)
	{
		return x + y * TILE_MAP_SIZE;
	}

}
