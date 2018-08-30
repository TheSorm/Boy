package timer;

import ram.RamRegister;

public class TimerCounterRegister extends RamRegister
{
	private static final int TIMA_ADRESS = 0xFF05;

	public TimerCounterRegister()
	{
		super(TIMA_ADRESS);
	}

	public boolean isZero()
	{
		return getValue() == 0;
	}

	public void inc()
	{
		putValue((byte) (getValue() + 1));
	}

	public void setCount(byte count)
	{
		putValue(count);
	}
}
