package ram;

public class UnusableMemory1 extends RamSpace
{
	private static final int START_ADRESS = 0xFF4C;
	private static final int END_ADRESS = 0xFF80;

	public UnusableMemory1()
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
