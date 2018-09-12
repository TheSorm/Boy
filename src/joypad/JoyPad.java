package joypad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import connectos.JoypadConnector;
import cpu.InterruptFlagRegister;
import gameboy.MegiHertz;
import gameboy.TickBasedComponend;

public class JoyPad extends TickBasedComponend
{
	private JoypadInformationRegister joypadInformation;
	private InterruptFlagRegister interruptFlag;
	private JoypadConnector joypadConnector;

	public JoyPad(JoypadInformationRegister joypadInformation, InterruptFlagRegister interruptFlag,
			JoypadConnector joypadConnector)
	{
		super(MegiHertz.get(4));
		this.joypadInformation = joypadInformation;
		this.interruptFlag = interruptFlag;
		this.joypadConnector = joypadConnector;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		boolean isAnyButtonPressed = joypadInformation.isButtonPressed();

		if (joypadConnector.isDownPressed())
		{
			joypadInformation.setInputDown(true);
		}

		if (joypadConnector.isUpPressed())
		{
			joypadInformation.setInputUp(true);
		}

		if (joypadConnector.isLeftPressed())
		{
			joypadInformation.setInputLeft(true);
		}

		if (joypadConnector.isRightPressed())
		{
			joypadInformation.setInputRight(true);
		}

		if (joypadConnector.isStartPressed())
		{
			joypadInformation.setInputStart(true);
		}

		if (joypadConnector.isSelectPressed())
		{
			joypadInformation.setInputSelect(true);
		}

		if (joypadConnector.isaPressed())
		{
			joypadInformation.setInputA(true);
		}

		if (joypadConnector.isbPressed())
		{
			joypadInformation.setInputB(true);
		}

		if (!isAnyButtonPressed && joypadInformation.isButtonPressed())
		{
			interruptFlag.setButtonPressedPending(true);
		}

		return false;
	}

}
