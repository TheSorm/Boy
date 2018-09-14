package sound;

import ram.RamRegister;

public class SoundMode3FrequencyLow extends RamRegister
{
	private static final int NR33_ADRESS = 0xFF1D;

	public SoundMode3FrequencyLow()
	{
		super(NR33_ADRESS);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}
}
