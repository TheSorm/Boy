package sound;

import ram.RamRegister;

public class SoundOnOrOffRegister extends RamRegister
{
	private static final int NR52_ADRESS = 0xFF26;

	private static final int SOUND_OFF_OR_ON = 7;
	private static final int UNUSED_BIT_6 = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int SOUND_4_ON = 3;
	private static final int SOUND_3_ON = 2;
	private static final int SOUND_2_ON = 1;
	private static final int SOUND_1_ON = 0;

	public SoundOnOrOffRegister()
	{
		super(NR52_ADRESS);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
	}

	public void setSound4On()
	{
		setBit(SOUND_4_ON, true);
	}

	public void setSound3On()
	{
		setBit(SOUND_3_ON, true);
	}

	public void setSound2On()
	{
		setBit(SOUND_2_ON, true);
	}

	public void setSound1On()
	{
		setBit(SOUND_1_ON, true);
	}
}
