package sound;

import ram.RamRegister;

public class SoundMode1FrequencyHigh extends RamRegister
{
	private static final int LOAD_MASK = 0b1011_1111;

	private static final int NR14_ADRESS = 0xFF14;

	private static final int INITIAL = 7;
	private static final int COUNTER_OR_CONSECUTIVE_SELECTION = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int UNUSED_BIT_3 = 3;
	private static final int FREQUENCY_HIGH_BIT_2 = 2;
	private static final int FREQUENCY_HIGH_BIT_1 = 1;
	private static final int FREQUENCY_HIGH_BIT_0 = 0;

	private SoundOnOrOffRegister soundOnOrOffRegister;

	public SoundMode1FrequencyHigh(SoundOnOrOffRegister soundOnOrOffRegister)
	{
		super(NR14_ADRESS);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);

		this.soundOnOrOffRegister = soundOnOrOffRegister;
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);

		if (getBit(INITIAL))
		{
			soundOnOrOffRegister.setSound1On();
		}

		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) (super.load(adress) | LOAD_MASK);
	}
}