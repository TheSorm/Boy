package cpu;

public class Register16bit
{
	protected static final int FULL_SHORT = 0b1111_1111_1111_1111;
	protected static final int FULL_BYTE = 0b1111_1111;
	protected static final int FULL_NIPPLE_FULL_BYTE = 0b1111_1111_1111;

	protected short signedRegisterValue;
	protected Flags flags;

	public Register16bit(short value, Flags flags)
	{
		this.flags = flags;
		put(value);
	}

	public Register16bit(byte high, byte low, Flags flags)
	{
		this.flags = flags;
		put(high, low);
	}

	public void put(byte high, byte low)
	{
		signedRegisterValue = combineTo16Bit(high, low);
	}

	private short combineTo16Bit(byte high, byte low)
	{
		short result = 0;
		result |= Byte.toUnsignedInt(high);
		result <<= 8;
		result |= Byte.toUnsignedInt(low);

		return result;
	}

	public void put(short value)
	{
		signedRegisterValue = value;
	}

	public short load()
	{
		return signedRegisterValue;
	}

	public int loadUnsigned()
	{
		return Short.toUnsignedInt(signedRegisterValue);
	}

	public byte loadLow()
	{
		return (byte) loadUnsigned();
	}

	public byte loadHigh()
	{
		return (byte) (loadUnsigned() >>> 8);
	}

	/**
	 * Increments the register value by 1.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Not changed.
	 * @SubtractFlag: Not changed.
	 * @ZeroFlag: Not changed.
	 */
	public void inc()
	{
		signedRegisterValue = (short) (Short.toUnsignedInt(signedRegisterValue) + 1);
	}

	/**
	 * Decrements the register value by 1.
	 * 
	 * @CarryFlag: Not changed.
	 * @HalfCarryFlag: Not changed.
	 * @SubtractFlag: Not changed.
	 * @ZeroFlag: Not changed.
	 */
	public void dcr()
	{
		signedRegisterValue = (short) (Short.toUnsignedInt(signedRegisterValue) - 1);
	}

	public void add(byte high, byte low)
	{
		this.add(combineTo16Bit(high, low));
	}

	/**
	 * Adds the given value to the register value.
	 * 
	 * @CarryFlag: Set if result is greater than a full short.
	 * @HalfCarryFlag: Set if both lower bytes and upper lower nipple added
	 *                 together are greater than a full lower bytes and upper
	 *                 lower nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Not changed.
	 */
	public void add(short value)
	{
		int result = Short.toUnsignedInt(signedRegisterValue) + Short.toUnsignedInt(value);

		flags.setCarryFlag(result > FULL_SHORT);
		flags.setHalfCarryFlag((Short.toUnsignedInt(signedRegisterValue) & FULL_NIPPLE_FULL_BYTE)
				+ (Short.toUnsignedInt(value) & FULL_NIPPLE_FULL_BYTE) > FULL_NIPPLE_FULL_BYTE);

		flags.setSubtractFlag(false);

		signedRegisterValue = (short) result;
	}

	/**
	 * Adds the given value to the register value.
	 * 
	 * @CarryFlag: Set if lower bytes added together are greater than a full
	 *             byte.
	 * @HalfCarryFlag: Set if both lower nipple added together are greater than
	 *                 a full nipple.
	 * @SubtractFlag: Reset.
	 * @ZeroFlag: Reset.
	 */
	public void addSigned(byte value)
	{
		int result = Short.toUnsignedInt(signedRegisterValue) + value;
		flags.setCarryFlag((((Short.toUnsignedInt(signedRegisterValue) & 0xff) + (value & 0xff)) & 0x100) != 0);
		flags.setHalfCarryFlag((((Short.toUnsignedInt(signedRegisterValue) & 0x0f) + (value & 0x0f)) & 0x10) != 0);
		flags.setZeroFlag(false);
		flags.setSubtractFlag(false);

		signedRegisterValue = (short) result;
	}
}
