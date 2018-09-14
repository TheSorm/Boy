package sound;

import ram.RamRegister;

public class ChannelControlRegister extends RamRegister
{
	private static final int NR50_ADRESS = 0xFF24;

	private static final int CARDRIDGE_SOUND_SIGNAL_TO_SPEAKER_2 = 7;
	private static final int SPEAKER_2_OUTPUT_LEVEL_BIT_2 = 6;
	private static final int SPEAKER_2_OUTPUT_LEVEL_BIT_1 = 5;
	private static final int SPEAKER_2_OUTPUT_LEVEL_BIT_0 = 4;
	private static final int CARDRIDGE_SOUND_SIGNAL_TO_SPEAKER_1 = 3;
	private static final int SPEAKER_1_OUTPUT_LEVEL_BIT_2 = 2;
	private static final int SPEAKER_1_OUTPUT_LEVEL_BIT_1 = 1;
	private static final int SPEAKER_1_OUTPUT_LEVEL_BIT_0 = 0;

	public ChannelControlRegister()
	{
		super(NR50_ADRESS);
	}

}
