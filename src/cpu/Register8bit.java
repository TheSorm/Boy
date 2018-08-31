package cpu;

/**
 * General 8 bit register of the CPU.
 */
public class Register8Bit
{
	protected static final int FULL_BYTE = 0b1111_1111;
	protected static final int EMPTY_BYTE = 0b0000_0000;
	protected static final int FULL_HIGH_NIPPLE = 0b1111_0000;
	protected static final int FULL_NIPPLE = 0b1111;
	protected static final int EMPTY_NIPPLE = 0b0000;

	protected byte signedRegisterValue;
	protected Flags flags;

	public Register8Bit(byte value, Flags flags)
	{
		this.signedRegisterValue = value;
		this.flags = flags;
	}

	public void put(byte value)
	{
		this.signedRegisterValue = value;
	}

	public byte load()
	{
		return signedRegisterValue;
	}

	public int loadUnsigned()
	{
		return Byte.toUnsignedInt(signedRegisterValue);
	}

	/**
	 * Increments the register value by 1.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Set if register value had a full lower nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void inc()
	{
		flags.setHalfCarryFlag(loadUnsigned() == FULL_NIPPLE);

		signedRegisterValue = (byte) (loadUnsigned() + 1);

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(false);
	}

	/**
	 * Decrements the register value by 1.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Set if register value had a empty lower nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void dcr()
	{
		flags.setHalfCarryFlag(loadUnsigned() == EMPTY_NIPPLE);

		signedRegisterValue = (byte) (loadUnsigned() - 1);

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(true);
	}

	/**
	 * Rotates the register value left by 1.
	 * 
	 * @CarryFlag: Set if result is greater than a full byte.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void rlc()
	{
		int result = loadUnsigned() << 1;

		flags.setCarryFlag(result > FULL_BYTE);

		result |= (result > FULL_BYTE ? 1 : 0);

		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Rotates the register value right by 1.
	 * 
	 * @CarryFlag: Set if bit 0 was set.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void rrc()
	{
		flags.setCarryFlag((signedRegisterValue & 1) == 1);

		signedRegisterValue = (byte) (loadUnsigned() >>> 1);
		signedRegisterValue |= (flags.isCarryFlag() ? (1 << 7) : 0);

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Rotates the register value left by 1 through the carry flag.
	 * 
	 * @CarryFlag: Old bit 7.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void rl()
	{
		int result = loadUnsigned() << 1;
		result |= (flags.isCarryFlag() ? 1 : 0);

		flags.setCarryFlag(result > FULL_BYTE);

		signedRegisterValue = (byte) result;
		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);

	}

	/**
	 * Rotates the register value right by 1 through the carry flag.
	 * 
	 * @CarryFlag: Old bit 0.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void rr()
	{
		boolean oldCarry = flags.isCarryFlag();
		flags.setCarryFlag((signedRegisterValue & 1) == 1);

		signedRegisterValue = (byte) (loadUnsigned() >>> 1);
		signedRegisterValue |= (oldCarry ? (1 << 7) : 0);

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Shifts the register value left arithmetically by 1. Bit 0 becomes 0.
	 * 
	 * @CarryFlag: Set if result is greater than a full byte.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void sla()
	{
		int result = loadUnsigned() << 1;
		flags.setCarryFlag(result > FULL_BYTE);

		signedRegisterValue = (byte) result;
		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Shifts the register value right arithmetically by 1. Bit 7 becomes old
	 * bit 7.
	 * 
	 * @CarryFlag: Old bit 0.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void sra()
	{
		int result = (loadUnsigned() >> 1) | (loadUnsigned() & (1 << 7));

		flags.setCarryFlag((signedRegisterValue & 1) == 1);
		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	/**
	 * Swaps the low with the high nipple.
	 * 
	 * @CarryFlag: Reset.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void swap()
	{
		int upper = loadUnsigned() & FULL_HIGH_NIPPLE;
		int lower = loadUnsigned() & FULL_NIPPLE;
		int result = (lower << 4) | (upper >> 4);
		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setCarryFlag(false);
	}

	/**
	 * Shifts the register value right logically by 1. Bit 7 becomes 0.
	 * 
	 * @CarryFlag: Old bit 0.
	 * @HalfCarryFlag: Reset.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if result is 0.
	 */
	public void srl()
	{
		int result = (loadUnsigned() >> 1);
		flags.setCarryFlag((loadUnsigned() & 1) == 1);
		signedRegisterValue = (byte) result;

		flags.setZeroFlag(loadUnsigned() == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(false);
	}

	/**
	 * Set the bit at the given position.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Not changed.
	 * @SubtractFlag: Not changed.
	 * @ZeroFlag: Not changed.
	 */
	public void set(byte position)
	{
		signedRegisterValue = (byte) (loadUnsigned() | (1 << position));
	}

	/**
	 * Reset the bit at the given position.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Not changed.
	 * @SubtractFlag: Not changed.
	 * @ZeroFlag: Not changed.
	 */
	public void res(byte position)
	{
		signedRegisterValue = (byte) (loadUnsigned() & (~(1 << position)));
	}

	/**
	 * Checks the bit at the given position.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Set.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Set if checked bit is 0.
	 */
	public void bit(byte position)
	{
		flags.setZeroFlag(((loadUnsigned() >> position) & 1) == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(true);
	}
}
