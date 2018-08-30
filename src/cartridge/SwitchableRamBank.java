package cartridge;

import ram.RamSpace;

public class SwitchableRamBank extends RamSpace
{
	private final static int START_ADRESS = 0xA000;
	private final static int END_ADRESS = 0xC000;

	public SwitchableRamBank()
	{
		super(START_ADRESS, END_ADRESS);
	}
}
