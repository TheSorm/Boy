package ppu;

import ram.RamRegister;

public class LCDControllYCompare extends RamRegister
{
	private static final int LYC_ADRESS = 0xFF45;

	public LCDControllYCompare()
	{
		super(LYC_ADRESS);
	}

	public int getY()
	{
		return getValue();
	}
}
