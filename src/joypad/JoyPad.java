package joypad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import cpu.InterruptFlagRegister;
import gameboy.TickBasedComponend;

public class JoyPad extends TickBasedComponend implements KeyListener
{
	private JoypadInformationRegister joypadInformation;
	private InterruptFlagRegister interruptFlag;

	private boolean downIsPressed;
	private boolean upIsPressed;
	private boolean leftIsPressed;
	private boolean rightIsPressed;
	private boolean startIsPressed;
	private boolean selectIsPressed;
	private boolean aIsPressed;
	private boolean bIsPressed;

	public JoyPad(JoypadInformationRegister joypadInformation, InterruptFlagRegister interruptFlag)
	{
		super(4194304);
		this.joypadInformation = joypadInformation;
		this.interruptFlag = interruptFlag;
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_DOWN:
				downIsPressed = true;
				break;
			case KeyEvent.VK_UP:
				upIsPressed = true;
				break;
			case KeyEvent.VK_LEFT:
				leftIsPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightIsPressed = true;
				break;
			case KeyEvent.VK_SPACE:
				startIsPressed = true;
				break;
			case KeyEvent.VK_ENTER:
				selectIsPressed = true;
				break;
			case KeyEvent.VK_B:
				bIsPressed = true;
				break;
			case KeyEvent.VK_A:
				aIsPressed = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_DOWN:
				downIsPressed = false;
				break;
			case KeyEvent.VK_UP:
				upIsPressed = false;
				break;
			case KeyEvent.VK_LEFT:
				leftIsPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightIsPressed = false;
				break;
			case KeyEvent.VK_SPACE:
				startIsPressed = false;
				break;
			case KeyEvent.VK_ENTER:
				selectIsPressed = false;
				break;
			case KeyEvent.VK_B:
				bIsPressed = false;
				break;
			case KeyEvent.VK_A:
				aIsPressed = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		boolean isAnyButtonPressed = joypadInformation.isButtonPressed();

		if (downIsPressed)
		{
			joypadInformation.setInputDown(true);
		}

		if (upIsPressed)
		{
			joypadInformation.setInputUp(true);
		}

		if (leftIsPressed)
		{
			joypadInformation.setInputLeft(true);
		}

		if (rightIsPressed)
		{
			joypadInformation.setInputRight(true);
		}

		if (startIsPressed)
		{
			joypadInformation.setInputStart(true);
		}

		if (selectIsPressed)
		{
			joypadInformation.setInputSelect(true);
		}

		if (aIsPressed)
		{
			joypadInformation.setInputA(true);
		}

		if (bIsPressed)
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
