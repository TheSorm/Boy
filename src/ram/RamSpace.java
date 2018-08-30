package ram;

public abstract class RamSpace implements ReadableWriteable
{
	private byte[] ramSpace;
	protected int startAdress;
	protected int endAdress;

	public RamSpace(int startAdress, int endAdress)
	{
		this.ramSpace = new byte[endAdress - startAdress];
		this.startAdress = startAdress;
		this.endAdress = endAdress;
	}

	protected byte getValue(int offset)
	{
		return ramSpace[offset];
	}

	@Override
	public byte load(int adress)
	{
		return ramSpace[adress - startAdress];
	}

	@Override
	public void put(int adress, byte input)
	{
		ramSpace[adress - startAdress] = (byte) input;
	}

	public int getStartAdress()
	{
		return startAdress;
	}

	public int getEndAdress()
	{
		return endAdress;
	}
}
