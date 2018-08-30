package ram;

public abstract class RamRegister implements ReadableWriteable
{
	private byte registerValue;
	protected int adress;

	public RamRegister(int adress)
	{
		this.adress = adress;
	}

	protected boolean getBit(int position)
	{
		return ((registerValue >> position) & 1) == 1;
	}

	protected void setBit(int position, boolean value)
	{
		if (value)
		{
			registerValue = (byte) (registerValue | (1 << position));
		}
		else
		{
			registerValue = (byte) (registerValue & (~(1 << position)));
		}
	}

	protected int getValue()
	{
		return Byte.toUnsignedInt(registerValue);
	}

	protected void putValue(byte value)
	{
		registerValue = value;
	}

	@Override
	public byte load(int adress)
	{
		return registerValue;
	}

	@Override
	public void put(int adress, byte input)
	{
		registerValue = input;
	}

}
