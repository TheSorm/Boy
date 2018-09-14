package ppu;

import ram.RamRegister;

public class LCDControlStatusRegister extends RamRegister
{

	private static final int STAT_ADRESS = 0xFF41;

	private static final int UNUSED_BIT_7_POSITION = 7;
	private static final int LYC_LY_COINCIDENCE_INTERRUPT_ENABLED_POSITION = 6;
	private static final int OAM_INTERRUPT_ENABLED_POSITION = 5;
	private static final int V_BLANK_INTERRUPT_ENABLED_POSITION = 4;
	private static final int H_BLANK_INTERRUPT_ENABLED_POSITION = 3;
	private static final int LYC_LY_COINCIDENCE_FLAG = 2;
	private static final int MODE_FLAG_1 = 1;
	private static final int MODE_FLAG_0 = 0;

	public enum LCDStatus
	{
		OAM_SEARCH, PIXEL_TRANSFER, H_BLANCK, V_BLANK
	};

	private LCDControllRegister lcdControll;

	public LCDControlStatusRegister(LCDControllRegister lcdControll)
	{
		super(STAT_ADRESS);
		this.lcdControll = lcdControll;
		
		setBit(UNUSED_BIT_7_POSITION, true);
	}

	public boolean isLYCEqualsLYInterruptEnabled()
	{
		return getBit(LYC_LY_COINCIDENCE_INTERRUPT_ENABLED_POSITION);
	}

	public void setLYCEqualsLYInterrupt(boolean value)
	{
		setBit(LYC_LY_COINCIDENCE_INTERRUPT_ENABLED_POSITION, value);
	}

	public boolean isOAMInterruptEnabled()
	{
		return getBit(OAM_INTERRUPT_ENABLED_POSITION);
	}

	public void setOAMInterrupt(boolean value)
	{
		setBit(OAM_INTERRUPT_ENABLED_POSITION, value);
	}

	public boolean isVBlankInterruptEnabled()
	{
		return getBit(V_BLANK_INTERRUPT_ENABLED_POSITION);
	}

	public void setVBlankInterrupt(boolean value)
	{
		setBit(V_BLANK_INTERRUPT_ENABLED_POSITION, value);
	}

	public boolean isHBlankInterruptEnabled()
	{
		return getBit(H_BLANK_INTERRUPT_ENABLED_POSITION);
	}

	public void setHBlankInterrupt(boolean value)
	{
		setBit(H_BLANK_INTERRUPT_ENABLED_POSITION, value);
	}

	public boolean isLYCEqualsLY()
	{
		return getBit(LYC_LY_COINCIDENCE_FLAG);
	}

	public void setLYCEqualsLY(boolean value)
	{
		setBit(LYC_LY_COINCIDENCE_FLAG, value);
	}

	public boolean isModeHBlank()
	{
		if (!lcdControll.isLcdOn())
		{
			return true;
		}
		return !getBit(MODE_FLAG_1) && !getBit(MODE_FLAG_0);
	}

	public boolean isModeVBlank()
	{
		return !getBit(MODE_FLAG_1) && getBit(MODE_FLAG_0);
	}

	public boolean isModeOAM()
	{
		if (!lcdControll.isLcdOn())
		{
			return false;
		}
		return getBit(MODE_FLAG_1) && !getBit(MODE_FLAG_0);
	}

	public boolean isModePixelTransfer()
	{
		return getBit(MODE_FLAG_1) && getBit(MODE_FLAG_0);
	}

	public void setModeHBlank()
	{
		setBit(MODE_FLAG_1, false);
		setBit(MODE_FLAG_0, false);
	}

	public void setModeVBlank()
	{
		setBit(MODE_FLAG_1, false);
		setBit(MODE_FLAG_0, true);
	}

	public void setModeOAM()
	{
		setBit(MODE_FLAG_1, true);
		setBit(MODE_FLAG_0, false);
	}

	public void setModePixelTransfer()
	{
		setBit(MODE_FLAG_1, true);
		setBit(MODE_FLAG_0, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, (byte) ((Byte.toUnsignedInt(input) | 0b0000_0111) & (getValue() | 0b1111_1000)) );
		setBit(UNUSED_BIT_7_POSITION, true);
	}

	@Override
	protected void putValue(byte value)
	{
		super.putValue(value);
		setBit(UNUSED_BIT_7_POSITION, true);
	}

	@Override
	public byte load(int adress)
	{
		if (!lcdControll.isLcdOn())
		{
			return (byte) (Byte.toUnsignedInt(super.load(adress)) & 0b1111_1100);
		}
		return super.load(adress);
	}

	public LCDStatus getStatus()
	{
		if (isModeHBlank())
		{
			return LCDStatus.H_BLANCK;
		}
		else if (isModeOAM())
		{
			return LCDStatus.OAM_SEARCH;
		}
		else if (isModePixelTransfer())
		{
			return LCDStatus.PIXEL_TRANSFER;
		}
		else
		{
			return LCDStatus.V_BLANK;
		}
	}
}
