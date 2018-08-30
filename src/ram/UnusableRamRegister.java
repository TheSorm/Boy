package ram;

public class UnusableRamRegister extends RamRegister
{

	public UnusableRamRegister(int adress)
	{
		super(adress);
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
