package ui;

import gameboy.Gameboy;
import gameboy.MegiHertz;

public class Main
{

	public static void main(String[] args)
	{
		Gameboy gb = new Gameboy("resources/Roms/SuperMarioLand.gb", MegiHertz.get(4));
		Window window = new Window(gb.getIPSMonitor(), gb.getPanel(), gb.getKeyListener());
		Sound sound = new Sound();

		//new Thread(sound).start();
		new Thread(window).start();
		window.setVisible(true);
		gb.start();
	}
}
