package sound;

import ram.RamRegister;

public class SoundMode2FrequencyLow extends RamRegister
{
	private static final int NR23_ADRESS = 0xFF18;

	public SoundMode2FrequencyLow()
	{
		super(NR23_ADRESS);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}

}
