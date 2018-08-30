package ppu;

import ram.RamRegister;

public class DirectMemoryAcessRegister extends RamRegister
{
	private static final int DMA_ADRESS = 0xFF46;

	private boolean started;
	private boolean running;

	public DirectMemoryAcessRegister()
	{
		super(DMA_ADRESS);
		started = false;
	}

	public int getStartAdress()
	{
		return (getValue() << 8);
	}

	public boolean isStartet()
	{
		if (started)
		{
			started = false;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		started = true;
	}

	public void finishedDMA()
	{
		running = false;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning()
	{
		running = true;
	}

}
