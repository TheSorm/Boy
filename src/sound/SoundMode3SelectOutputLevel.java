package sound;

import ram.RamRegister;

public class SoundMode3SelectOutputLevel extends RamRegister
{

	private static final int NR32_ADRESS = 0xFF1C;

	private static final int UNUSED_BIT_7 = 7;
	private static final int SELECT_OUTPUT_LEVEL_BIT_1 = 6;
	private static final int SELECT_OUTPUT_LEVEL_BIT_0 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int UNUSED_BIT_3 = 3;
	private static final int UNUSED_BIT_2 = 2;
	private static final int UNUSED_BIT_1 = 1;
	private static final int UNUSED_BIT_0 = 0;

	public SoundMode3SelectOutputLevel()
	{
		super(NR32_ADRESS);
		setBit(UNUSED_BIT_7, true);
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
		setBit(UNUSED_BIT_7, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
		setBit(UNUSED_BIT_0, true);
	}
}
