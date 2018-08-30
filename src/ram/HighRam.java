package ram;

public class HighRam extends RamSpace
{
	private final static int START_ADRESS = 0xFF80;
	private final static int END_ADRESS = 0xFFFF;

	public HighRam()
	{
		super(START_ADRESS, END_ADRESS);
	}

}
