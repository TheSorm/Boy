package cartridge;

import ram.RamSpace;

/**
 * Contains the first 16Kb of the cartridge ROM. Can normally not be mapped
 * different (MBC1 has some ways to change ROM bank 0)
 */
public class RomBank0 extends RamSpace
{
	private final static int START_ADRESS = 0x0;
	private final static int END_ADRESS = 0x4000;

	public RomBank0(int[] data)
	{
		super(START_ADRESS, END_ADRESS);

		for (int i = 0; i < data.length; i++)
		{
			put(startAdress + i, (byte) data[i]);
		}
	}
}
