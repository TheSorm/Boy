package joypad;

import ram.RamRegister;

public class JoypadInformationRegister extends RamRegister
{

	private static final int P1_ADRESS = 0xFF00;

	private static final int ARE_BUTTON_KEYS_SELECTED_POSITION = 5;
	private static final int ARE_DIRECTION_KEYS_SELECTED_POSITION = 4;
	private static final int IS_DOWN_OR_START_PRESSED_POSITION = 3;
	private static final int IS_UP_OR_SELECT_PRESSED_POSITION = 2;
	private static final int IS_LEFT_OR_B_PRESSED_POSITION = 1;
	private static final int IS_RIGHT_OR_A_PRESSED_POSITION = 0;

	public JoypadInformationRegister()
	{
		super(P1_ADRESS);

		setBit(0, true);
		setBit(1, true);
		setBit(2, true);
		setBit(3, true);
		setBit(6, true);
		setBit(7, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);

		setBit(0, true);
		setBit(1, true);
		setBit(2, true);
		setBit(3, true);
		setBit(6, true);
		setBit(7, true);
	}

	public boolean isButtonPressed()
	{
		return !(getBit(IS_DOWN_OR_START_PRESSED_POSITION) && getBit(IS_UP_OR_SELECT_PRESSED_POSITION)
				&& getBit(IS_LEFT_OR_B_PRESSED_POSITION) && getBit(IS_RIGHT_OR_A_PRESSED_POSITION));
	}

	public void setInputDown(boolean value)
	{
		if (!getBit(ARE_DIRECTION_KEYS_SELECTED_POSITION))
		{
			setBit(IS_DOWN_OR_START_PRESSED_POSITION, !value);
		}
	}

	public void setInputUp(boolean value)
	{
		if (!getBit(ARE_DIRECTION_KEYS_SELECTED_POSITION))
		{
			setBit(IS_UP_OR_SELECT_PRESSED_POSITION, !value);
		}
	}

	public void setInputLeft(boolean value)
	{
		if (!getBit(ARE_DIRECTION_KEYS_SELECTED_POSITION))
		{
			setBit(IS_LEFT_OR_B_PRESSED_POSITION, !value);
		}
	}

	public void setInputRight(boolean value)
	{
		if (!getBit(ARE_DIRECTION_KEYS_SELECTED_POSITION))
		{
			setBit(IS_RIGHT_OR_A_PRESSED_POSITION, !value);
		}
	}

	public void setInputStart(boolean value)
	{
		if (!getBit(ARE_BUTTON_KEYS_SELECTED_POSITION))
		{
			setBit(IS_DOWN_OR_START_PRESSED_POSITION, !value);
		}
	}

	public void setInputSelect(boolean value)
	{
		if (!getBit(ARE_BUTTON_KEYS_SELECTED_POSITION))
		{
			setBit(IS_UP_OR_SELECT_PRESSED_POSITION, !value);
		}
	}

	public void setInputB(boolean value)
	{
		if (!getBit(ARE_BUTTON_KEYS_SELECTED_POSITION))
		{
			setBit(IS_LEFT_OR_B_PRESSED_POSITION, !value);
		}
	}

	public void setInputA(boolean value)
	{
		if (!getBit(ARE_BUTTON_KEYS_SELECTED_POSITION))
		{
			setBit(IS_RIGHT_OR_A_PRESSED_POSITION, !value);
		}
	}

}
