package sound;

import ram.RamRegister;

public class SoundMode1WavePatternDutyAndSoundLength extends RamRegister
{
	private static final int LOAD_MASK = 0b0011_1111;

	private static final int NR11_ADRESS = 0xFF11;

	private static final int WAVE_PATTERN_DUTY_BIT_1 = 7;
	private static final int WAVE_PATTERN_DUTY_BIT_0 = 6;
	private static final int SOUND_LENGTH_BIT_5 = 5;
	private static final int SOUND_LENGTH_BIT_4 = 4;
	private static final int SOUND_LENGTH_BIT_3 = 3;
	private static final int SOUND_LENGTH_BIT_2 = 2;
	private static final int SOUND_LENGTH_BIT_1 = 1;
	private static final int SOUND_LENGTH_BIT_0 = 0;

	public SoundMode1WavePatternDutyAndSoundLength()
	{
		super(NR11_ADRESS);
	}
	
	@Override
	public byte load(int adress)
	{
		return (byte) (super.load(adress) | LOAD_MASK);
	}

}
