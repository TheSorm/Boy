package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import connectos.JoypadConnector;

public class GbKeyListener implements KeyListener
{
	private JoypadConnector joypadConnector;

	public GbKeyListener(JoypadConnector joypadConnector)
	{
		this.joypadConnector = joypadConnector;
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_DOWN:
				joypadConnector.setDownPressed(true);
				break;
			case KeyEvent.VK_UP:
				joypadConnector.setUpPressed(true);
				break;
			case KeyEvent.VK_LEFT:
				joypadConnector.setLeftPressed(true);
				break;
			case KeyEvent.VK_RIGHT:
				joypadConnector.setRightPressed(true);
				break;
			case KeyEvent.VK_SPACE:
				joypadConnector.setStartPressed(true);
				break;
			case KeyEvent.VK_ENTER:
				joypadConnector.setSelectPressed(true);
				break;
			case KeyEvent.VK_B:
				joypadConnector.setbPressed(true);
				break;
			case KeyEvent.VK_A:
				joypadConnector.setaPressed(true);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_DOWN:
				joypadConnector.setDownPressed(false);
				break;
			case KeyEvent.VK_UP:
				joypadConnector.setUpPressed(false);
				break;
			case KeyEvent.VK_LEFT:
				joypadConnector.setLeftPressed(false);
				break;
			case KeyEvent.VK_RIGHT:
				joypadConnector.setRightPressed(false);
				break;
			case KeyEvent.VK_SPACE:
				joypadConnector.setStartPressed(false);
				break;
			case KeyEvent.VK_ENTER:
				joypadConnector.setSelectPressed(false);
				break;
			case KeyEvent.VK_B:
				joypadConnector.setbPressed(false);
				break;
			case KeyEvent.VK_A:
				joypadConnector.setaPressed(false);
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

}
