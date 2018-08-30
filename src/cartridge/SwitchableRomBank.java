package cartridge;

import ram.RamSpace;

public class SwitchableRomBank extends RamSpace
{
	private final static int START_ADRESS = 0x4000;
	private final static int END_ADRESS = 0x8000;

	public SwitchableRomBank(int[] data)
	{
		super(START_ADRESS, END_ADRESS);

		put(data);
	}

	private void put(int[] data)
	{
		for (int i = 0; i < data.length; i++)
		{
			put(startAdress + i, (byte) data[i]);
		}
	}
}
