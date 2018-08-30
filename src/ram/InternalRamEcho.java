package ram;

public class InternalRamEcho extends RamSpace
{
	private final static int START_ADRESS = 0xE000;
	private final static int END_ADRESS = 0xFE00;

	private InternalRam internalRam;

	public InternalRamEcho(InternalRam internalRam)
	{
		super(START_ADRESS, END_ADRESS);

		this.internalRam = internalRam;
	}

	@Override
	public void put(int adress, byte input)
	{
		internalRam.put((adress - startAdress) + internalRam.startAdress, input);
	}

	@Override
	public byte load(int adress)
	{
		return internalRam.load((adress - startAdress) + internalRam.startAdress);
	}
}
