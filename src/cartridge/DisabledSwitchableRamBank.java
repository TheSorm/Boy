package cartridge;

import ram.RamSpace;

/**
 * Filler for the RAM space when the RAM is disabled. Writes have no effect and
 * reads are always 0xFF.
 */
public class DisabledSwitchableRamBank extends RamSpace
{
	private final static int START_ADRESS = 0xA000;
	private final static int END_ADRESS = 0xC000;

	public DisabledSwitchableRamBank()
	{
		super(START_ADRESS, END_ADRESS);
	}

	@Override
	public void put(int adress, byte input)
	{
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}
}
