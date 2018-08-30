package cpu;

import ram.RamRegister;

/**
 * Gives Information about enabled interrupts
 * 
 * @author Torge
 *
 */
public class InterruptEnableRegister extends RamRegister
{

	private static final int IE_ADRESS = 0xFFFF;

	private static final int BUTTON_PRESSED_POSITION = 4;
	private static final int SERIAL_IO_TRANSFER_COMPLETE_POSITION = 3;
	private static final int TIMER_OVERFLOW_POSITION = 2;
	private static final int LCDC_POSITION = 1;
	private static final int V_BLANK_POSITION = 0;

	public InterruptEnableRegister()
	{
		super(IE_ADRESS);
	}

	public boolean isButtonPressedEnabled()
	{
		return getBit(BUTTON_PRESSED_POSITION);
	}

	public void setButtonPressedEnabled(boolean value)
	{
		setBit(BUTTON_PRESSED_POSITION, value);
	}

	public boolean isSerialIOTransferCompleteEnabled()
	{
		return getBit(SERIAL_IO_TRANSFER_COMPLETE_POSITION);
	}

	public void setSerialIOTransferCompleteEnabled(boolean value)
	{
		setBit(SERIAL_IO_TRANSFER_COMPLETE_POSITION, value);
	}

	public boolean isTimerOverflowEnabled()
	{
		return getBit(TIMER_OVERFLOW_POSITION);
	}

	public void setTimerOverflowEnabled(boolean value)
	{
		setBit(TIMER_OVERFLOW_POSITION, value);
	}

	public boolean isLCDCEnabled()
	{
		return getBit(LCDC_POSITION);
	}

	public void setLCDCEnabled(boolean value)
	{
		setBit(LCDC_POSITION, value);
	}

	public boolean isVBlankEnabled()
	{
		return getBit(V_BLANK_POSITION);
	}

	public void setVBlankEnabled(boolean value)
	{
		setBit(V_BLANK_POSITION, value);
	}
}
