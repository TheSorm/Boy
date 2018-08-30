package ppu;

import ram.RamRegister;

public class ScrollXRegister extends RamRegister
{

	private static final int SCY_ADRESS = 0xFF43;

	public ScrollXRegister()
	{
		super(SCY_ADRESS);
	}

	public int getXScroll()
	{
		return getValue();
	}
	
	@Override
	public void put(int adress, byte input)
	{
		System.out.println("SCX: " + Byte.toUnsignedInt(input));
		super.put(adress, input);
	}
}
