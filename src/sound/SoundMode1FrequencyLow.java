package sound;

import ram.RamRegister;

public class SoundMode1FrequencyLow extends RamRegister
{
	private static final int NR13_ADRESS = 0xFF13;

	public SoundMode1FrequencyLow()
	{
		super(NR13_ADRESS);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}

}
