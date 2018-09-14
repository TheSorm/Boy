package cpu;

import ram.RamRegister;

/**
 * Ram register which shows, which interrupts are currently pending.
 */
public class InterruptFlagRegister extends RamRegister
{
	private static final int IF_ADRESS = 0xFF0F;

	private static final int UNUSED_BIT_7 = 7;
	private static final int UNUSED_BIT_6 = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int BUTTON_PRESSED_POSITION = 4;
	private static final int SERIAL_IO_TRANSFER_COMPLETE_POSITION = 3;
	private static final int TIMER_OVERFLOW_POSITION = 2;
	private static final int LCDC_POSITION = 1;
	private static final int V_BLANK_POSITION = 0;

	public static final byte BUTTON_PRESSED_JUMP_ADRESS = 0x60;
	public static final byte SERIAL_IO_TRANSFER_COMPLETE_JUMP_ADRESS = 0x58;
	public static final byte TIMER_OVERFLOW_JUMP_ADRESS = 0x50;
	public static final byte LCDC_JUMP_ADRESS = 0x48;
	public static final byte V_BLANK_JUMP_ADRESS = 0x40;

	public InterruptFlagRegister()
	{
		super(IF_ADRESS);
		setBit(UNUSED_BIT_7, true);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
	}

	public boolean getButtonPressedPending()
	{
		return getBit(BUTTON_PRESSED_POSITION);
	}

	public void setButtonPressedPending(boolean value)
	{
		setBit(BUTTON_PRESSED_POSITION, value);
	}

	public boolean getSerialIOTransferCompletePending()
	{
		return getBit(SERIAL_IO_TRANSFER_COMPLETE_POSITION);
	}

	public void setSerialIOTransferCompletePending(boolean value)
	{
		setBit(SERIAL_IO_TRANSFER_COMPLETE_POSITION, value);
	}

	public boolean getTimerOverflowPending()
	{
		return getBit(TIMER_OVERFLOW_POSITION);
	}

	public void setTimerOverflowPending(boolean value)
	{
		setBit(TIMER_OVERFLOW_POSITION, value);
	}

	public boolean getLCDCPending()
	{
		return getBit(LCDC_POSITION);
	}

	public void setLCDCPending(boolean value)
	{
		setBit(LCDC_POSITION, value);
	}

	public boolean getVBlankPending()
	{
		return getBit(V_BLANK_POSITION);
	}

	public void setVBlankPending(boolean value)
	{
		setBit(V_BLANK_POSITION, value);
	}

	public byte getCurrentInterruptJumpAdress(InterruptEnableRegister interruptEnable)
	{
		if (getVBlankPending() && interruptEnable.isVBlankEnabled())
		{
			setVBlankPending(false);
			return V_BLANK_JUMP_ADRESS;
		}
		else if (getLCDCPending() && interruptEnable.isLCDCEnabled())
		{
			setLCDCPending(false);
			return LCDC_JUMP_ADRESS;
		}
		else if (getTimerOverflowPending() && interruptEnable.isTimerOverflowEnabled())
		{
			setTimerOverflowPending(false);
			return TIMER_OVERFLOW_JUMP_ADRESS;
		}
		else if (getSerialIOTransferCompletePending() && interruptEnable.isSerialIOTransferCompleteEnabled())
		{
			setSerialIOTransferCompletePending(false);
			return SERIAL_IO_TRANSFER_COMPLETE_JUMP_ADRESS;
		}
		else if (getButtonPressedPending() && interruptEnable.isButtonPressedEnabled())
		{
			setButtonPressedPending(false);
			return BUTTON_PRESSED_JUMP_ADRESS;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, (byte) (Byte.toUnsignedInt(input) | 0b1110_0000));
	}

	@Override
	protected void putValue(byte value)
	{
		super.putValue((byte) (Byte.toUnsignedInt(value) | 0b1110_0000));
	}

}
