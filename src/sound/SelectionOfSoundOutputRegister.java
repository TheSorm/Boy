package sound;

import ram.RamRegister;

public class SelectionOfSoundOutputRegister extends RamRegister
{
	private static final int NR51_ADRESS = 0xFF25;

	private static final int OUTPUT_SOUND_4_TO_SPEAKER_2 = 7;
	private static final int OUTPUT_SOUND_3_TO_SPEAKER_2 = 6;
	private static final int OUTPUT_SOUND_2_TO_SPEAKER_2 = 5;
	private static final int OUTPUT_SOUND_1_TO_SPEAKER_2 = 4;
	private static final int OUTPUT_SOUND_4_TO_SPEAKER_1 = 3;
	private static final int OUTPUT_SOUND_3_TO_SPEAKER_1 = 2;
	private static final int OUTPUT_SOUND_2_TO_SPEAKER_1 = 1;
	private static final int OUTPUT_SOUND_1_TO_SPEAKER_1 = 0;

	public SelectionOfSoundOutputRegister()
	{
		super(NR51_ADRESS);
	}

}
