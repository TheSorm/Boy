package ram;

import java.util.HashMap;
import java.util.Map;

import ppu.LCDControlStatusRegister;
import ppu.TileMap;
import ppu.TilePatternTable;

public class VideoRam extends RamSpace
{
	private final static int START_ADRESS = 0x8000;
	private final static int END_ADRESS = 0xA000;

	Map<Integer, ReadableWriteable> memoryMap;
	LCDControlStatusRegister lcdStatus;
	private boolean isReadAllowed;
	private boolean isWriteAllowed;

	public VideoRam(TileMap tileMap, TilePatternTable tilePatternTable, LCDControlStatusRegister lcdStatus)
	{
		super(START_ADRESS, END_ADRESS);

		this.lcdStatus = lcdStatus;

		memoryMap = new HashMap<>();

		for (int i = tileMap.startAdress; i < tileMap.endAdress; i++)
		{
			memoryMap.put(i, tileMap);
		}

		for (int i = tilePatternTable.startAdress; i < tilePatternTable.endAdress; i++)
		{
			memoryMap.put(i, tilePatternTable);
		}

		isReadAllowed = true;
		isWriteAllowed = true;
	}

	@Override
	public byte load(int adress)
	{
		if (!isReadAllowed)
		{
			// System.out.println("Illigel READ from " +
			// Integer.toHexString(adress));
			return (byte) 0xff;
		}
		// System.out.println("Legal READ from " + Integer.toHexString(adress));
		return memoryMap.get(adress).load(adress);
	}

	@Override
	public void put(int adress, byte input)
	{
		if (!isWriteAllowed)
		{
			// System.out.println("Illigel WRITE to " +
			// Integer.toHexString(adress));
			return;
		}
		// System.out.println("Legal WRITE to " + Integer.toHexString(adress));
		memoryMap.get(adress).put(adress, (byte) input);
	}

	public boolean isReadAllowed()
	{
		return isReadAllowed;
	}

	public boolean isWriteAllowed()
	{
		return isWriteAllowed;
	}

	public void allowRead(boolean allowed)
	{
		isReadAllowed = allowed;
	}

	public void allowWrite(boolean allowed)
	{
		isWriteAllowed = allowed;
	}
}
