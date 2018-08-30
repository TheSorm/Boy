package cpu;

public class Register16bit
{
	protected static final int FULL_SHORT = 0b1111_1111_1111_1111;
	protected static final int FULL_BYTE = 0b1111_1111;
	protected static final int FULL_NIPPLE_FULL_BYTE = 0b1111_1111_1111;

	protected short registerValue;
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
		registerValue = combineTo16Bit(high, low);
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
		registerValue = value;
	}

	public short load()
	{
		return registerValue;
	}

	public byte loadLow()
	{
		return (byte) Short.toUnsignedInt(registerValue);
	}

	public byte loadHigh()
	{
		short result = (short) (registerValue >>> 8);
		return (byte) Short.toUnsignedInt(result);
	}

	public void inc()
	{
		registerValue = (short) (Short.toUnsignedInt(registerValue) + 1);
	}

	public void dcr()
	{
		registerValue = (short) (Short.toUnsignedInt(registerValue) - 1);
	}

	public void add(byte high, byte low)
	{
		this.add(combineTo16Bit(high, low));

	}

	public void add(short value)
	{
		int result = Short.toUnsignedInt(registerValue) + Short.toUnsignedInt(value);

		flags.setCarryFlag(result > FULL_SHORT);
		flags.setHalfCarryFlag((Short.toUnsignedInt(registerValue) & FULL_NIPPLE_FULL_BYTE)
				+ (Short.toUnsignedInt(value) & FULL_NIPPLE_FULL_BYTE) > FULL_NIPPLE_FULL_BYTE);

		flags.setSubtractFlag(false);

		registerValue = (short) result;
	}

	public void addSigned(byte value)
	{
		int result = Short.toUnsignedInt(registerValue) + value;
		flags.setCarryFlag((((Short.toUnsignedInt(registerValue) & 0xff) + (value & 0xff)) & 0x100) != 0);
		flags.setHalfCarryFlag((((Short.toUnsignedInt(registerValue) & 0x0f) + (value & 0x0f)) & 0x10) != 0);
		flags.setZeroFlag(false);
		flags.setSubtractFlag(false);

		registerValue = (short) result;
	}
}
