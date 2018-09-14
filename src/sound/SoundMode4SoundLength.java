package sound;

import ram.RamRegister;

public class SoundMode4SoundLength extends RamRegister
{
	private static final int NR41_ADRESS = 0xFF20;

	private static final int UNUSED_BIT_7 = 7;
	private static final int UNUSED_BIT_6 = 6;
	private static final int SOUND_LENGTH_BIT_5 = 5;
	private static final int SOUND_LENGTH_BIT_4 = 4;
	private static final int SOUND_LENGTH_BIT_3 = 3;
	private static final int SOUND_LENGTH_BIT_2 = 2;
	private static final int SOUND_LENGTH_BIT_1 = 1;
	private static final int SOUND_LENGTH_BIT_0 = 0;

	public SoundMode4SoundLength()
	{
		super(NR41_ADRESS);
		setBit(UNUSED_BIT_7, true);
		setBit(UNUSED_BIT_6, true);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_7, true);
		setBit(UNUSED_BIT_6, true);
	}
}
