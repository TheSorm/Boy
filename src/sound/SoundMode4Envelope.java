package sound;

import ram.RamRegister;

public class SoundMode4Envelope extends RamRegister
{
	private static final int NR42_ADRESS = 0xFF21;

	private static final int INITIAL_VOLUME_BIT_3 = 7;
	private static final int INITIAL_VOLUME_BIT_2 = 6;
	private static final int INITIAL_VOLUME_BIT_1 = 5;
	private static final int INITIAL_VOLUME_BIT_0 = 4;
	private static final int ENVELOPE_UP_OR_DOWN = 3;
	private static final int NUMBER_OF_ENVELOPE_SWEEP_BIT_2 = 2;
	private static final int NUMBER_OF_ENVELOPE_SWEEP_BIT_1 = 1;
	private static final int NUMBER_OF_ENVELOPE_SWEEP_BIT_0 = 0;

	public SoundMode4Envelope()
	{
		super(NR42_ADRESS);
	}

}
