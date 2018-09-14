package sound;

import ram.RamRegister;

public class SoundMode3SoundLength extends RamRegister
{
	private static final int NR31_ADRESS = 0xFF1B;

	public SoundMode3SoundLength()
	{
		super(NR31_ADRESS);
	}

	@Override
	public byte load(int adress)
	{
		return (byte) 0xFF;
	}

}
