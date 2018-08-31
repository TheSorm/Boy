package cpu;

/**
 * Special register which contains the results from the recent instruction which
 * has affected flags. The lower 4 bits are allays 0.
 */
public class Flags extends Register8Bit
{
	private final static byte ZERO_FLAG_POSITION = 7;
	private final static byte SUBTRACT_FLAG_POSITION = 6;
	private final static byte HALF_CARRY_FLAG_POSITION = 5;
	private final static byte CARRY_FLAG_POSITION = 4;
	private final static byte RESET_BIT_POSITION_3 = 3;
	private final static byte RESET_BIT_POSITION_2 = 2;
	private final static byte RESET_BIT_POSITION_1 = 1;
	private final static byte RESET_BIT_POSITION_0 = 0;

	public Flags(boolean zeroFlag, boolean subtractFlag, boolean halfCarryFlag, boolean carryFlag)
	{
		super((byte) 0, null);

		setZeroFlag(zeroFlag);
		setSubtractFlag(subtractFlag);
		setHalfCarryFlag(halfCarryFlag);
		setCarryFlag(carryFlag);
		res((byte) RESET_BIT_POSITION_0);
		res((byte) RESET_BIT_POSITION_1);
		res((byte) RESET_BIT_POSITION_2);
		res((byte) RESET_BIT_POSITION_3);
	}

	/**
	 * Complement carry flag.
	 * 
	 * @CarryFlag: Complemented.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Not changed.
	 */
	public void ccf()
	{
		res(SUBTRACT_FLAG_POSITION);
		res(HALF_CARRY_FLAG_POSITION);
		invert(CARRY_FLAG_POSITION);
	}

	/**
	 * Set carry flag.
	 * 
	 * @CarryFlag: Set.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Not changed.
	 */
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
		res((byte) RESET_BIT_POSITION_0);
		res((byte) RESET_BIT_POSITION_1);
		res((byte) RESET_BIT_POSITION_2);
		res((byte) RESET_BIT_POSITION_3);
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
		signedRegisterValue = (byte) (loadUnsigned() ^ (1 << position));
	}

	private boolean isSet(byte position)
	{
		return ((loadUnsigned() >> position) & 1) == 1;
	}
}
