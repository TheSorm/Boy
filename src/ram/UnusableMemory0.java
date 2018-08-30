package ram;

public class UnusableMemory0 extends RamSpace
{
	private static final int START_ADRESS = 0xFEA0;
	private static final int END_ADRESS = 0xFF00;

	public UnusableMemory0()
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
		return (byte) 0xff;
	}
}
