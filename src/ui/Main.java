package ui;

import connectos.JoypadConnector;
import connectos.LcdConnector;
import gameboy.Gameboy;
import gameboy.MegiHertz;
import joypad.JoyPad;
import ppu.LCD;
import tools.IPSMonitor;

public class Main
{

	public static void main(String[] args)
	{
		LcdConnector lcdConnector = new LcdConnector();
		JoypadConnector joypadConnector = new JoypadConnector();
		IPSMonitor ipsMonitor = new IPSMonitor();
		Gameboy gb = new Gameboy("resources/Roms/SuperMarioLand.gb", MegiHertz.get(4), ipsMonitor, lcdConnector,
				joypadConnector);
		Window window = new Window(ipsMonitor, lcdConnector, joypadConnector);
		Sound sound = new Sound();

		// new Thread(sound).start();
		new Thread(window).start();
		window.setVisible(true);
		gb.start();
	}
}
