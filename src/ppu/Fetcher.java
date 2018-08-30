
package ppu;

import cpu.TickBasedComponend;
import gameboy.MegiHertz;
import ppu.Fetcher.State;
import ppu.PixelInformation.Source;
import ram.AdressSpace;

public class Fetcher extends TickBasedComponend
{
	private TilePatternTable tilePatternTable;
	private PixelFifo fifo;
	private LCDControllRegister lcdControll;
	private TileMap tileMap;
	private OAMSearcher oamSearcher;
	private LCDControllYCoordinateRegister lcdYCoordinate;

	private byte tileNumber = 0;
	private int backgroundFirstPart = 0;
	private int backgroundSecondPart = 0;
	private LineInformation tileInfos = new LineInformation((byte) 0, 0);

	private byte objectTileNumber = 0;
	private int objectFirstPart = 0;
	private int objectSecondPart = 0;

	public enum State
	{
		FETCH_TILE_NUMBER, LOAD_FIRST_PART, LOAD_SECOND_PART, IDEEL, PUSH
	};

	public enum Mode
	{
		FETCH_BACKGROUND, FETCH_OBJECT
	};

	private Mode mode;
	private State backgroundState;
	private State objectState;

	public Fetcher(TilePatternTable tilePatternTable, PixelFifo fifo, LCDControllRegister lcdControll,
			OAMSearcher oamSearcher, LCDControllYCoordinateRegister lcdYCoordinate, TileMap tileMap)
	{
		super(MegiHertz.get(2));
		this.tilePatternTable = tilePatternTable;
		this.fifo = fifo;
		this.lcdControll = lcdControll;
		this.tileMap = tileMap;
		this.oamSearcher = oamSearcher;
		this.lcdYCoordinate = lcdYCoordinate;

		this.mode = Mode.FETCH_BACKGROUND;
		this.backgroundState = State.FETCH_TILE_NUMBER;
		this.objectState = State.FETCH_TILE_NUMBER;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		switch (mode)
		{
			case FETCH_BACKGROUND:
				fetchTiles();
				break;
			case FETCH_OBJECT:
				fetchObjects();
				break;
		}

		return false;
	}

	private void fetchTiles()
	{
		switch (backgroundState)
		{
			case FETCH_TILE_NUMBER:
				tileInfos = tileMap.next();
				tileNumber = tileInfos.tileNumber;
				backgroundState = State.LOAD_FIRST_PART;
				break;
			case LOAD_FIRST_PART:
				backgroundFirstPart = tilePatternTable.loadTilePatternPart(
						lcdControll.getSelectedWindowAndBackgroundTileData(), tileNumber, tileInfos.currentPixelLine,
						0);
				backgroundState = State.LOAD_SECOND_PART;
				break;
			case LOAD_SECOND_PART:
				backgroundSecondPart = tilePatternTable.loadTilePatternPart(
						lcdControll.getSelectedWindowAndBackgroundTileData(), tileNumber, tileInfos.currentPixelLine,
						1);
				if (fifo.isFull())
				{
					backgroundState = State.IDEEL;
				}
				else
				{
					backgroundState = State.PUSH;
					fetchTiles();
				}
				break;
			case IDEEL:
				backgroundState = State.PUSH;
				fetchTiles();
				break;
			case PUSH:
				for (int k = 7; k >= 0; k--)
				{
					fifo.add(new PixelInformation(Source.BACKGROUND_AND_WINDOW,
							combineBits(getBit(backgroundFirstPart, k), getBit(backgroundSecondPart, k))));
				}
				backgroundState = State.FETCH_TILE_NUMBER;
				return;
		}
	}

	private void fetchObjects()
	{
		switch (objectState)
		{
			case FETCH_TILE_NUMBER:
				objectTileNumber = oamSearcher.getFirstObject().tileNumber;
				objectState = State.LOAD_FIRST_PART;
				break;
			case LOAD_FIRST_PART:
				objectFirstPart = tilePatternTable.loadTilePatternPart(true, objectTileNumber,
						(lcdYCoordinate.loadLcdYCoordinate() - oamSearcher.getFirstObject().positionYStart), 0);
				objectState = State.LOAD_SECOND_PART;
				break;
			case LOAD_SECOND_PART:
				objectSecondPart = tilePatternTable.loadTilePatternPart(true, objectTileNumber,
						(lcdYCoordinate.loadLcdYCoordinate() - oamSearcher.getFirstObject().positionYStart), 1);
				if (oamSearcher.getFirstObject().flipX)
				{
					for (int k = 7; k >= 0; k--)
					{
						fifo.addObjectOver(
								new PixelInformation(
										oamSearcher.getFirstObject().palette ? Source.OBJECT_1 : Source.OBJECT_0,
										combineBits(getBit(objectFirstPart, k), getBit(objectSecondPart, k))),
								k, oamSearcher.getFirstObject().preority);
					}
				}
				else
				{
					for (int k = 7; k >= 0; k--)
					{
						fifo.addObjectOver(
								new PixelInformation(
										oamSearcher.getFirstObject().palette ? Source.OBJECT_1 : Source.OBJECT_0,
										combineBits(getBit(objectFirstPart, k), getBit(objectSecondPart, k))),
								7 - k, oamSearcher.getFirstObject().preority);
					}
				}
				oamSearcher.removeFirstObject();
				objectState = State.FETCH_TILE_NUMBER;
				mode = Mode.FETCH_BACKGROUND;
				break;
		}
	}

	public void nextLine()
	{
		tileMap.nextPixelLine();
		setState(State.FETCH_TILE_NUMBER);
		super.resetTickCount();
	}

	public void reset()
	{
		tileMap.reset();
		setState(State.FETCH_TILE_NUMBER);
		super.resetTickCount();
	}

	private int getBit(int number, int position)
	{
		return (number >> position) & 1;
	}

	private int combineBits(int first, int second)
	{
		int result = first;
		result <<= 1;
		result |= second;
		return result;
	}

	public void setState(State state)
	{
		this.backgroundState = state;
	}

	public void setMode(Mode mode)
	{
		this.mode = mode;
	}
}
