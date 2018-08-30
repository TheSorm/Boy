package cpu;

public class Accumulator extends Register8bit
{
	public Accumulator(byte value, Flags flags)
	{
		super(value, flags);
	}

	public void add(byte value)
	{
		int result = (Byte.toUnsignedInt(this.registerValue) + Byte.toUnsignedInt(value));

		flags.setCarryFlag(result > FULL_BYTE);
		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag((registerValue & FULL_NIPPLE) + (value & FULL_NIPPLE) > FULL_NIPPLE);

		registerValue = (byte) result;
		flags.setZeroFlag(registerValue == 0);
	}

	public void adC(byte value)
	{
		int result = (Byte.toUnsignedInt(this.registerValue) + Byte.toUnsignedInt(value))
				+ (flags.isCarryFlag() ? 1 : 0);

		flags.setSubtractFlag(false);
		flags.setHalfCarryFlag(
				(registerValue & FULL_NIPPLE) + (value & FULL_NIPPLE) + (flags.isCarryFlag() ? 1 : 0) > FULL_NIPPLE);
		flags.setCarryFlag(result > FULL_BYTE);

		registerValue = (byte) result;
		flags.setZeroFlag(registerValue == 0);

	}

	public void sub(byte value)
	{
		int result = (Byte.toUnsignedInt(this.registerValue) - Byte.toUnsignedInt(value));

		flags.setCarryFlag(result < EMPTY_BYTE);
		flags.setSubtractFlag(true);
		flags.setHalfCarryFlag((registerValue & FULL_NIPPLE) - (value & FULL_NIPPLE) < EMPTY_NIPPLE);

		registerValue = (byte) result;
		flags.setZeroFlag(registerValue == 0);
	}

	public void sbC(byte value)
	{
		int result = (Byte.toUnsignedInt(this.registerValue) - Byte.toUnsignedInt(value))
				- (flags.isCarryFlag() ? 1 : 0);

		flags.setSubtractFlag(true);
		flags.setHalfCarryFlag(
				(registerValue & FULL_NIPPLE) - (value & FULL_NIPPLE) - (flags.isCarryFlag() ? 1 : 0) < EMPTY_NIPPLE);
		flags.setCarryFlag(result < EMPTY_BYTE);

		registerValue = (byte) result;
		flags.setZeroFlag(registerValue == 0);
	}

	public void and(byte value)
	{
		registerValue = (byte) (Byte.toUnsignedInt(this.registerValue) & Byte.toUnsignedInt(value));
		flags.setZeroFlag(registerValue == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(true);
		flags.setSubtractFlag(false);
	}

	public void or(byte value)
	{
		registerValue = (byte) (Byte.toUnsignedInt(this.registerValue) | Byte.toUnsignedInt(value));

		flags.setZeroFlag(registerValue == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void xOr(byte value)
	{
		registerValue = (byte) (Byte.toUnsignedInt(this.registerValue) ^ Byte.toUnsignedInt(value));

		flags.setZeroFlag(registerValue == 0);
		flags.setCarryFlag(false);
		flags.setHalfCarryFlag(false);
		flags.setSubtractFlag(false);
	}

	public void cp(byte value)
	{
		byte registerSave = registerValue;
		this.sub(value);
		this.put(registerSave);
	}

	public void rlca()
	{
		rlc();
		flags.setZeroFlag(false);
	}

	public void rrca()
	{
		rrc();
		flags.setZeroFlag(false);
	}

	public void rla()
	{
		rl();
		flags.setZeroFlag(false);
	}

	public void rra()
	{
		rr();
		flags.setZeroFlag(false);
	}

	public void da()
	{

		int result = Byte.toUnsignedInt(registerValue);

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

		registerValue = (byte) result;
	}

	public void cpl()
	{
		registerValue = (byte) ~Byte.toUnsignedInt(registerValue);
		flags.setSubtractFlag(true);
		flags.setHalfCarryFlag(true);
	}
}
