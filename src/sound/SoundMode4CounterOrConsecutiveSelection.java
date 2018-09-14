package sound;

import ram.RamRegister;

public class SoundMode4CounterOrConsecutiveSelection extends RamRegister
{
	private static final int LOAD_MASK = 0b1011_1111;

	private static final int NR44_ADRESS = 0xFF23;

	private static final int INITIAL = 7;
	private static final int COUNTER_OR_CONSECUTIVE_SELECTION = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int UNUSED_BIT_3 = 3;
	private static final int UNUSED_BIT_2 = 2;
	private static final int UNUSED_BIT_1 = 1;
	private static final int UNUSED_BIT_0 = 0;

	public SoundMode4CounterOrConsecutiveSelection(SoundOnOrOffRegister soundOnOrOffRegister)
	{
		super(NR44_ADRESS);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
		setBit(UNUSED_BIT_0, true);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) (super.load(adress) | LOAD_MASK);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
		setBit(UNUSED_BIT_0, true);
	}
}
