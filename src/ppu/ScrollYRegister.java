package ppu;

import ram.RamRegister;

public class ScrollYRegister extends RamRegister
{

	private static final int SCY_ADRESS = 0xFF42;

	public ScrollYRegister()
	{
		super(SCY_ADRESS);
	}

	public int getYScroll()
	{
		return getValue();
	}
}
