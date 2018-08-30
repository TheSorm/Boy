package timer;

import ram.RamRegister;

public class TimerModuloRegister extends RamRegister
{
	private static final int TMA_ADRESS = 0xFF06;

	public TimerModuloRegister()
	{
		super(TMA_ADRESS);
	}

	public byte getTimerModulo()
	{
		return load(0);
	}
}
