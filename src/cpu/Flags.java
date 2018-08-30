package cpu;

public class Flags extends Register8bit
{
	private final static byte ZERO_FLAG_POSITION = 7;
	private final static byte SUBTRACT_FLAG_POSITION = 6;
	private final static byte HALF_CARRY_FLAG_POSITION = 5;
	private final static byte CARRY_FLAG_POSITION = 4;

	public Flags(boolean zeroFlag, boolean subtractFlag, boolean halfCarryFlag, boolean carryFlag)
	{
		super((byte) 0, null);

		setZeroFlag(zeroFlag);
		setSubtractFlag(subtractFlag);
		setHalfCarryFlag(halfCarryFlag);
		setCarryFlag(carryFlag);
	}

	public void ccf()
	{
		res(SUBTRACT_FLAG_POSITION);
		res(HALF_CARRY_FLAG_POSITION);
		invert(CARRY_FLAG_POSITION);
	}

	public void scf()
	{
		res(SUBTRACT_FLAG_POSITION);
		res(HALF_CARRY_FLAG_POSITION);
		set(CARRY_FLAG_POSITION);
	}

	@Override
	public void put(byte value)
	{
		super.put(value);
		res((byte) 0);
		res((byte) 1);
		res((byte) 2);
		res((byte) 3);
	}

	public boolean isZeroFlag()
	{
		return isSet(ZERO_FLAG_POSITION);
	}

	public void setZeroFlag(boolean zeroFlag)
	{
		if (zeroFlag)
		{
			set(ZERO_FLAG_POSITION);
		}
		else
		{
			res(ZERO_FLAG_POSITION);
		}

	}

	public boolean isSubtractFlag()
	{
		return isSet(SUBTRACT_FLAG_POSITION);
	}

	public void setSubtractFlag(boolean subtractFlag)
	{
		if (subtractFlag)
		{
			set(SUBTRACT_FLAG_POSITION);
		}
		else
		{
			res(SUBTRACT_FLAG_POSITION);
		}
	}

	public boolean isHalfCarryFlag()
	{
		return isSet(HALF_CARRY_FLAG_POSITION);
	}

	public void setHalfCarryFlag(boolean halfCarryFlag)
	{
		if (halfCarryFlag)
		{
			set(HALF_CARRY_FLAG_POSITION);
		}
		else
		{
			res(HALF_CARRY_FLAG_POSITION);
		}
	}

	public boolean isCarryFlag()
	{
		return isSet(CARRY_FLAG_POSITION);
	}

	public void setCarryFlag(boolean carryFlag)
	{
		if (carryFlag)
		{
			set(CARRY_FLAG_POSITION);
		}
		else
		{
			res(CARRY_FLAG_POSITION);
		}
	}

	private void invert(byte position)
	{
		registerValue = (byte) (Byte.toUnsignedInt(registerValue) ^ (1 << position));
	}

	private boolean isSet(byte position)
	{
		return ((Byte.toUnsignedInt(registerValue) >> position) & 1) == 1;
	}
}
