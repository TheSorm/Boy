package ppu;

import ram.AdressSpace;
import ram.RamRegister;

public class LCDControllRegister extends RamRegister
{

	private static final int LCDC_ADRESS = 0xFF40;

	private static final int LCD_CONTROLL_OPERATION_POSITION = 7;
	private static final int WINDOW_TILE_MAP_DISPLAY_SELECT_POSITION = 6;
	private static final int WINDOW_DISPLAY_POSITION = 5;
	private static final int BACKGROUND_AND_WINDOW_TILE_DATA_SELECT_POSITION = 4;
	private static final int BACKGROUND_TILEMAP_DISPLAY_SELECT_POSITION = 3;
	private static final int OBJECT_SIZE_POSITION = 2;
	private static final int OBJECT_DISPLAY_POSITION = 1;
	private static final int BACKGROUND_AND_WINDOW_DISPLAY_POSITION = 0;

	private static final AdressSpace ZERO_WINDOW_TILE_MAP = new AdressSpace(0x9800, 0x9BFF);
	private static final AdressSpace ONE_WINDOW_TILE_MAP = new AdressSpace(0x9C00, 0x9FFF);

	private static final AdressSpace ZERO_BACKGROUND_TILE_MAP = new AdressSpace(0x9800, 0x9BFF);
	private static final AdressSpace ONE_BACKGROUND_TILE_MAP = new AdressSpace(0x9C00, 0x9FFF);

	public enum objectSize
	{
		EIGHT_BY_EIGHT, EIGHT_BY_SIXTEEN
	};

	public LCDControllRegister()
	{
		super(LCDC_ADRESS);
	}

	public boolean isLcdOn()
	{
		return getBit(LCD_CONTROLL_OPERATION_POSITION);
	}

	public AdressSpace getSelectedWindowTileMap()
	{
		if (getBit(WINDOW_TILE_MAP_DISPLAY_SELECT_POSITION))
		{
			return ONE_WINDOW_TILE_MAP;
		}
		else
		{
			return ZERO_WINDOW_TILE_MAP;
		}
	}

	public boolean isWindowDisplayed()
	{
		return getBit(WINDOW_DISPLAY_POSITION);
	}

	public boolean getSelectedWindowAndBackgroundTileData()
	{
		return (getBit(BACKGROUND_AND_WINDOW_TILE_DATA_SELECT_POSITION));
	}

	public boolean isSelectedWindowAndBackgroundTileDataAdressedSigned()
	{
		if (getBit(BACKGROUND_AND_WINDOW_TILE_DATA_SELECT_POSITION))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public AdressSpace getSelectedBackgroundTileMap()
	{
		if (getBit(BACKGROUND_TILEMAP_DISPLAY_SELECT_POSITION))
		{
			return ONE_BACKGROUND_TILE_MAP;
		}
		else
		{
			return ZERO_BACKGROUND_TILE_MAP;
		}
	}

	public objectSize getObjectSize()
	{
		if (getBit(OBJECT_SIZE_POSITION))
		{
			return objectSize.EIGHT_BY_SIXTEEN;
		}
		else
		{
			return objectSize.EIGHT_BY_EIGHT;
		}
	}

	public boolean areObjectsDisplayed()
	{
		return getBit(OBJECT_DISPLAY_POSITION);
	}

	public boolean areBackgroundAndWindowDisplayed()
	{
		return getBit(BACKGROUND_AND_WINDOW_DISPLAY_POSITION);
	}
}
