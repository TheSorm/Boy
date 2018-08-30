package ram;

public class WavePatternRam extends RamSpace
{
	private final static int START_ADRESS = 0xFF30;
	private final static int END_ADRESS = 0xFF40;

	public WavePatternRam()
	{
		super(START_ADRESS, END_ADRESS);
	}

}
