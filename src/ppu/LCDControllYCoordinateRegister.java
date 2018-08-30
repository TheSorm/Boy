package ppu;

import ram.RamRegister;

public class LCDControllYCoordinateRegister extends RamRegister
{

	private static final int LY_ADRESS = 0xFF44;

	public LCDControllYCoordinateRegister()
	{
		super(LY_ADRESS);
	}

	public void writeLcdYCoordinate(int coordinate)
	{
		putValue((byte) coordinate);
	}

	public int loadLcdYCoordinate()
	{
		return getValue();
	}
	

}
