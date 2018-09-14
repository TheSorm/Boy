package timer;

import ram.RamRegister;

public class TimerControlRegister extends RamRegister
{
	private static final int TAC_ADRESS = 0xFF07;

	private static final int CONSTANTLY_SETTED_7_POSITION = 7;
	private static final int CONSTANTLY_SETTED_6_POSITION = 6;
	private static final int CONSTANTLY_SETTED_5_POSITION = 5;
	private static final int CONSTANTLY_SETTED_4_POSITION = 4;
	private static final int CONSTANTLY_SETTED_3_POSITION = 3;
	private static final int TIMER_STOP_POSITION = 2;
	private static final int INPUT_CLOCK_1_POSITION = 1;
	private static final int INPUT_CLOCK_0_POSITION = 0;

	private static final int CLOCK_4096_HZ = 4096;
	private static final int CLOCK_65536_HZ = 65536;
	private static final int CLOCK_16384_HZ = 16384;
	private static final int CLOCK_262144_HZ = 262144;

	private static final int CLOCK_4096_BIT = 9;
	private static final int CLOCK_65536_BIT = 5;
	private static final int CLOCK_16384_BIT = 7;
	private static final int CLOCK_262144_BIT = 3;

	public TimerControlRegister()
	{
		super(TAC_ADRESS);
		setBit(CONSTANTLY_SETTED_3_POSITION, true);
		setBit(CONSTANTLY_SETTED_4_POSITION, true);
		setBit(CONSTANTLY_SETTED_5_POSITION, true);
		setBit(CONSTANTLY_SETTED_6_POSITION, true);
		setBit(CONSTANTLY_SETTED_7_POSITION, true);
	}

	public boolean isTimerStarted()
	{
		return getBit(TIMER_STOP_POSITION);
	}

	public int getInputClockInHz()
	{
		if (isInputClock262144Hz())
		{
			return CLOCK_262144_HZ;
		}
		else if (isInputClock16384Hz())
		{
			return CLOCK_16384_HZ;
		}
		else if (isInputClock65536Hz())
		{
			return CLOCK_65536_HZ;
		}
		else
		{
			return CLOCK_4096_HZ;
		}
	}

	public int getActiveBitOfInternelCounter()
	{
		if (isInputClock262144Hz())
		{
			return CLOCK_262144_BIT;
		}
		else if (isInputClock16384Hz())
		{
			return CLOCK_16384_BIT;
		}
		else if (isInputClock65536Hz())
		{
			return CLOCK_65536_BIT;
		}
		else
		{
			return CLOCK_4096_BIT;
		}
	}

	public boolean isInputClock4096Hz()
	{
		return !getBit(INPUT_CLOCK_1_POSITION) && !getBit(INPUT_CLOCK_0_POSITION);
	}

	public boolean isInputClock262144Hz()
	{
		return !getBit(INPUT_CLOCK_1_POSITION) && getBit(INPUT_CLOCK_0_POSITION);
	}

	public boolean isInputClock65536Hz()
	{
		return getBit(INPUT_CLOCK_1_POSITION) && !getBit(INPUT_CLOCK_0_POSITION);
	}

	public boolean isInputClock16384Hz()
	{
		return getBit(INPUT_CLOCK_1_POSITION) && getBit(INPUT_CLOCK_0_POSITION);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(CONSTANTLY_SETTED_3_POSITION, true);
		setBit(CONSTANTLY_SETTED_4_POSITION, true);
		setBit(CONSTANTLY_SETTED_5_POSITION, true);
		setBit(CONSTANTLY_SETTED_6_POSITION, true);
		setBit(CONSTANTLY_SETTED_7_POSITION, true);
	}
}
