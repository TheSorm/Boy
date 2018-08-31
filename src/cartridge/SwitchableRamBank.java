package cartridge;

import ram.RamSpace;

/**
 * External RAM from cartridge, it can be battery buffered and has normally a
 * size of 8Kb.
 */
public class SwitchableRamBank extends RamSpace
{
	private final static int START_ADRESS = 0xA000;
	private final static int END_ADRESS = 0xC000;

	public SwitchableRamBank()
	{
		super(START_ADRESS, END_ADRESS);
	}
}
