package ppu;

import ram.RamSpace;

public class ObjectsAttributeMap extends RamSpace
{
	private static final int START_ADRESS = 0xFE00;
	private static final int END_ADRESS = 0xFEA0;

	private int currentEntryOffset;
	private DirectMemoryAcessRegister dma;
	private LCDControlStatusRegister lcdStatus;
	private boolean isReadAllowed;
	private boolean isWriteAllowed;

	public ObjectsAttributeMap(DirectMemoryAcessRegister dma, LCDControlStatusRegister lcdStatus)
	{
		super(START_ADRESS, END_ADRESS);

		this.dma = dma;
		this.lcdStatus = lcdStatus;

		isReadAllowed = true;
		isWriteAllowed = true;

		reset();
	}

	public OAMEntry next()
	{
		currentEntryOffset += 4;
		return new OAMEntry(getValue(currentEntryOffset), getValue(currentEntryOffset + 1),
				getValue(currentEntryOffset + 2), getValue(currentEntryOffset + 3));
	}

	public boolean hasNext()
	{
		return startAdress + currentEntryOffset + 4 < endAdress;
	}

	public void reset()
	{
		currentEntryOffset = -4;
	}

	public void putFromDMA(int adress, byte input)
	{
		super.put(adress, input);
	}

	@Override
	public byte load(int adress)
	{
		if (!isReadAllowed || dma.isRunning())
		{
			return (byte) 0xFF;
		}
		return super.load(adress);
	}

	@Override
	public void put(int adress, byte input)
	{
		if (!isWriteAllowed || dma.isRunning())
		{
			return;
		}
		super.put(adress, input);
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
