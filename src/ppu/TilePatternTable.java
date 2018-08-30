package ppu;

import ram.AdressSpace;
import ram.RamSpace;

public class TilePatternTable extends RamSpace
{
	private static final int START_ADRESS = 0x8000;
	private static final int END_ADRESS = 0x9800;

	private static final int BYTES_PER_TILE_DATA = 16;
	private static final int SIGNED_OFFSET = 0x1000;

	public static final AdressSpace ZERO_BACKGROUND_AND_WINDOW_TILE_DATA = new AdressSpace(0x8800, 0x97FF);
	public static final AdressSpace ONE_BACKGROUND_AND_WINDOW_TILE_DATA = new AdressSpace(0x8000, 0x8FFF);

	public TilePatternTable()
	{
		super(START_ADRESS, END_ADRESS);
	}

	public int loadTilePatternPart(boolean is1TileData, byte tileNumber, int line, int part)
	{
		if (is1TileData)
		{
			return loadTilePatternPartFrom1BackgroundAndWindowTileData(tileNumber, line, part);
		}
		else
		{
			return loadTilePatternPartFrom0BackgroundAndWindowTileData(tileNumber, line, part);
		}
	}

	private int loadTilePatternPartFrom1BackgroundAndWindowTileData(byte tileNumber, int line, int part)
	{
		return Byte.toUnsignedInt(getValue(Byte.toUnsignedInt(tileNumber) * BYTES_PER_TILE_DATA + line * 2 + part));
	}

	private int loadTilePatternPartFrom0BackgroundAndWindowTileData(byte signedTileNumber, int line, int part)
	{
		return Byte.toUnsignedInt(getValue((signedTileNumber * BYTES_PER_TILE_DATA) + SIGNED_OFFSET + line * 2 + part));
	}
}
