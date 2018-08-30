package timer;

import ram.RamRegister;

public class DividerRegister extends RamRegister
{
	private static final int DIV_ADRESS = 0xFF04;

	private boolean reseted;

	public DividerRegister()
	{
		super(DIV_ADRESS);
		reseted = false;
	}

	public void calculateDividerFromInternalCounter(short time)
	{
		putValue((byte) (Short.toUnsignedInt(time) >>> 8));
	}

	@Override
	public void put(int adress, byte input)
	{
		reseted = true;
	}

	public boolean isReseted()
	{
		if (reseted)
		{
			reseted = false;
			return true;
		}
		else
		{
			return false;
		}
	}
}
