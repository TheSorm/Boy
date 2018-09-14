package sound;

import ram.RamRegister;

public class SoundMode1SweepRegister extends RamRegister
{

	private static final int NR10_ADRESS = 0xFF10;

	private static final int UNUSED_BIT_7 = 7;
	private static final int SWEEP_TIME_BIT_2 = 6;
	private static final int SWEEP_TIME_BIT_1 = 5;
	private static final int SWEEP_TIME_BIT_0 = 4;
	private static final int SWEEP_INCREASES_OR_DECREASES = 3;
	private static final int NUMBER_OF_SEEP_SHIFT_BIT_2 = 2;
	private static final int NUMBER_OF_SEEP_SHIFT_BIT_1 = 1;
	private static final int NUMBER_OF_SEEP_SHIFT_BIT_0 = 0;

	public SoundMode1SweepRegister()
	{
		super(NR10_ADRESS);
		setBit(UNUSED_BIT_7, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_7, true);
	}

}
