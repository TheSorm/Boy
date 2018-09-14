package sound;

import ram.RamRegister;

public class SoundMode3SoundOnOrOff extends RamRegister
{

	private static final int NR30_ADRESS = 0xFF1A;

	private static final int SOUND_OFF_OR_ON = 7;
	private static final int UNUSED_BIT_6 = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int UNUSED_BIT_3 = 3;
	private static final int UNUSED_BIT_2 = 2;
	private static final int UNUSED_BIT_1 = 1;
	private static final int UNUSED_BIT_0 = 0;

	public SoundMode3SoundOnOrOff()
	{
		super(NR30_ADRESS);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
		setBit(UNUSED_BIT_0, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
		setBit(UNUSED_BIT_0, true);
	}
}
