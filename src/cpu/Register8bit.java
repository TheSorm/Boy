package cpu;

public class Register8bit
{
	protected static final int FULL_BYTE = 0b1111_1111;
	protected static final int EMPTY_BYTE = 0b0000_0000;
	protected static final int FULL_NIPPLE = 0b1111;
	protected static final int EMPTY_NIPPLE = 0b0000;

	protected byte registerValue;
	protected Flags flags;

	public Register8bit(byte value, Flags flags)
	{
		this.registerValue = value;
		this.flags = flags;
	}

	public void put(byte value)
	{
		this.registerValue = value;
	}

	public byte load()
	{
		return registerValue;
	}

	public void inc()
	{
		flags.setHalfCarryFlag((registerValue & FULL_NIPPLE) == FULL_NIPPLE);

		registerValue = (byte) (Byte.toUnsignedInt(this.registerValue) + 1);

		flags.setZeroFlag(registerValue == 0);
		flags.setSubtractFlag(false);
	}

	public void dcr()
	{
		flags.setHalfCarryFlag((registerValue & FULL_NIPPLE) == EMPTY_NIPPLE);

		registerValue = (byte) (Byte.toUnsignedInt(this.registerValue) - 1);

		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setSubtractFlag(true);
	}

	public void rlc()
	{
		int result = Byte.toUnsignedInt(registerValue) << 1;

		flags.setCarryFlag(result > FULL_BYTE);

		result |= (result > FULL_BYTE ? 1 : 0);

		registerValue = (byte) result;
		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void rrc()
	{
		flags.setCarryFlag((registerValue & 1) != 0);

		registerValue = (byte) (Byte.toUnsignedInt(registerValue) >>> 1);
		registerValue |= (flags.isCarryFlag() ? (1 << 7) : 0);

		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void rl()
	{
		int result = Byte.toUnsignedInt(registerValue) << 1;
		result |= (flags.isCarryFlag() ? 1 : 0);

		flags.setCarryFlag(result > FULL_BYTE);

		registerValue = (byte) result;
		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);

	}

	public void rr()
	{
		boolean oldCarry = flags.isCarryFlag();
		flags.setCarryFlag((registerValue & 1) != 0);

		registerValue = (byte) (Byte.toUnsignedInt(registerValue) >>> 1);
		registerValue |= (oldCarry ? (1 << 7) : 0);

		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void sla()
	{
		int result = Byte.toUnsignedInt(registerValue) << 1;
		flags.setCarryFlag(result > FULL_BYTE);

		registerValue = (byte) result;
		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void sra()
	{
		int result = (Byte.toUnsignedInt(registerValue) >> 1) | (Byte.toUnsignedInt(registerValue) & (1 << 7));

		flags.setCarryFlag((registerValue & 1) != 0);
		registerValue = (byte) result;
		flags.setZeroFlag(result == 0);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void swap()
	{
		int upper = Byte.toUnsignedInt(registerValue) & 0b11110000;
		int lower = Byte.toUnsignedInt(registerValue) & 0b00001111;
		int result = (lower << 4) | (upper >> 4);
		registerValue = (byte) result;

		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setCarryFlag(false);
	}

	public void srl()
	{
		int result = (Byte.toUnsignedInt(registerValue) >> 1);
		flags.setCarryFlag((Byte.toUnsignedInt(registerValue) & 1) != 0);
		registerValue = (byte) result;

		flags.setZeroFlag(Byte.toUnsignedInt(registerValue) == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(false);
	}

	public void set(byte position)
	{
		registerValue = (byte) (Byte.toUnsignedInt(registerValue) | (1 << position));
	}

	public void res(byte position)
	{
		registerValue = (byte) (Byte.toUnsignedInt(registerValue) & (~(1 << position)));
	}

	public void bit(byte position)
	{
		flags.setZeroFlag(((Byte.toUnsignedInt(registerValue) >> position) & 1) == 0);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(true);
	}
}
