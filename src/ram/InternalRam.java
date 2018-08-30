package ram;

public class InternalRam extends RamSpace
{
	private final static int START_ADRESS = 0xC000;
	private final static int END_ADRESS = 0xE000;

	public InternalRam()
	{
		super(START_ADRESS, END_ADRESS);
	}

}
