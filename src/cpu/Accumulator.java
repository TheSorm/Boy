package cpu;

/**
 * The accumulator from the CPU.
 */
public class Accumulator extends Register8Bit
{
	public Accumulator(byte value, Flags flags)
	{
		super(value, flags);
	}

	/**
	 * Adds the given value to the register value.
	 * 
	 * @CarryFlag: Set if result is greater than a full byte.
	 * @HalfCarryFlag: Set if both lower ripples added together are greater than
	 *                 a full nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void add(byte value)
	{
		int result = (loadUnsigned() + Byte.toUnsignedInt(value));

		flags.setCarryFlag(result > FULL_BYTE);
		flags.setHalfCarryFlag((signedRegisterValue & FULL_NIPPLE) + (value & FULL_NIPPLE) > FULL_NIPPLE);

		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(false);
	}

	/**
	 * Adds the given value and the carry flag to the register value.
	 * 
	 * @CarryFlag: Set if result is greater than a full byte.
	 * @HalfCarryFlag: Set if both lower ripples added together plus the carry
	 *                 flag are greater than a full nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void adC(byte value)
	{
		int result = (loadUnsigned() + Byte.toUnsignedInt(value)) + (flags.isCarryFlag() ? 1 : 0);

		flags.setHalfCarryFlag((signedRegisterValue & FULL_NIPPLE) + (value & FULL_NIPPLE)
				+ (flags.isCarryFlag() ? 1 : 0) > FULL_NIPPLE);
		flags.setCarryFlag(result > FULL_BYTE);

		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(false);
	}

	/**
	 * Subtracts the given value from register value.
	 * 
	 * @CarryFlag: Set if result is smaller than a empty byte.
	 * @HalfCarryFlag: Set if both lower ripples subtracted from each other are
	 *                 smaller than a empty nipple.
	 * @SubtractFlag: Set.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void sub(byte value)
	{
		int result = (loadUnsigned() - Byte.toUnsignedInt(value));

		flags.setCarryFlag(result < EMPTY_BYTE);
		flags.setHalfCarryFlag((signedRegisterValue & FULL_NIPPLE) - (value & FULL_NIPPLE) < EMPTY_NIPPLE);

		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(true);
	}

	/**
	 * Subtracts the given value and the carry flag from register value.
	 * 
	 * @CarryFlag: Set if result is smaller than a empty byte.
	 * @HalfCarryFlag: Set if both lower ripples subtracted from each other
	 *                 minus the carry flag are smaller than a empty nipple.
	 * @SubtractFlag: Set.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void sbC(byte value)
	{
		int result = (loadUnsigned() - Byte.toUnsignedInt(value)) - (flags.isCarryFlag() ? 1 : 0);

		flags.setHalfCarryFlag((signedRegisterValue & FULL_NIPPLE) - (value & FULL_NIPPLE)
				- (flags.isCarryFlag() ? 1 : 0) < EMPTY_NIPPLE);
		flags.setCarryFlag(result < EMPTY_BYTE);

		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(true);
	}

	/**
	 * Logical AND between register value and given value.
	 * 
	 * @CarryFlag: Reset.
	 * @HalfCarryFlag: Set
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void and(byte value)
	{
		signedRegisterValue = (byte) (loadUnsigned() & Byte.toUnsignedInt(value));
		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(true);
		flags.setSubtractFlag(false);
	}

	/**
	 * Logical OR between register value and given value.
	 * 
	 * @CarryFlag: Reset.
	 * @HalfCarryFlag: Reset
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void or(byte value)
	{
		signedRegisterValue = (byte) (loadUnsigned() | Byte.toUnsignedInt(value));

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Logical XOR between register value and given value.
	 * 
	 * @CarryFlag: Reset.
	 * @HalfCarryFlag: Reset
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void xOr(byte value)
	{
		signedRegisterValue = (byte) (loadUnsigned() ^ Byte.toUnsignedInt(value));

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Compares the given value and the register value. Subtracts both values
	 * but restore register value after that.
	 * 
	 * @CarryFlag: Set if result is smaller than a empty byte.
	 * @HalfCarryFlag: Set if both lower ripples subtracted from each other are
	 *                 smaller than a empty nipple.
	 * @SubtractFlag: Set.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void cp(byte value)
	{
		byte registerSave = signedRegisterValue;
		this.sub(value);
		this.put(registerSave);
	}

	/**
	 * Rotates the register value left by 1.
	 * 
	 * @CarryFlag: Set if result is greater than a full byte.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Reset.
	 */
	public void rlca()
	{
		rlc();
		flags.setZeroFlag(false);
	}

	/**
	 * Rotates the register value right by 1.
	 * 
	 * @CarryFlag: Set if bit 0 was set.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Reset.
	 */
	public void rrca()
	{
		rrc();
		flags.setZeroFlag(false);
	}

	/**
	 * Rotates the register value left by 1 through the carry flag.
	 * 
	 * @CarryFlag: Old bit 7.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Reset.
	 */
	public void rla()
	{
		rl();
		flags.setZeroFlag(false);
	}

	/**
	 * Rotates the register value right by 1 through the carry flag.
	 * 
	 * @CarryFlag: Old bit 0.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Reset.
	 */
	public void rra()
	{
		rr();
		flags.setZeroFlag(false);
	}

	/**
	 * Decimal adjust the register value based on the flags.
	 * 
	 * @CarryFlag: Set if result is one greater than a full byte.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Not changed.
	 * @ZeroFlag: Set if result is 0, else Reset.
	 */
	public void da()
	{

		int result = loadUnsigned();

		if (!flags.isSubtractFlag())
		{
			if (flags.isHalfCarryFlag() || (result & 0xF) > 9)
				result += 0x06;

			if (flags.isCarryFlag() || result > 0x9F)
				result += 0x60;
		}
		else
		{
			if (flags.isHalfCarryFlag())
				result = (result - 6) & 0xFF;

			if (flags.isCarryFlag())
				result -= 0x60;
		}

		flags.setZeroFlag(false);
		flags.setHalfCarryFlag(false);

		if ((result & 0x100) == 0x100)
			flags.setCarryFlag(true);

		result &= 0xFF;

		if (result == 0)
			flags.setZeroFlag(true);

		signedRegisterValue = (byte) result;
	}

	/**
	 * Builds the complement of the register value.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Set.
	 * @SubtractFlag: Set.
	 * @ZeroFlag: Not changed.
	 */
	public void cpl()
	{
		signedRegisterValue = (byte) ~loadUnsigned();
		flags.setSubtractFlag(true);
		flags.setHalfCarryFlag(true);
	}
}
