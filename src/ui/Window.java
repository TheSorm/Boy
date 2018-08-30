package ui;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameboy.Gameboy;
import ppu.LCD.Panel;
import tools.IPSMonitor;

public class Window extends JFrame implements Runnable
{
	private Panel gamePane;
	private JPanel contendPane;
	private JLabel ips;
	private IPSMonitor ipsMonitor;

	public Window(IPSMonitor ipsMonitor, Panel panel, KeyListener listener)
	{
		super("Gameboy");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.gamePane = panel;
		this.ipsMonitor = ipsMonitor;

		contendPane = new JPanel(new BorderLayout());
		this.setContentPane(contendPane);

		ips = new JLabel();

		contendPane.add(gamePane, BorderLayout.CENTER);
		contendPane.add(ips, BorderLayout.PAGE_START);

		this.addKeyListener(listener);

		pack();
	}

	@Override
	public void run()
	{
		while (true)
		{
			gamePane.repaint();
			ips.setText(Long.toString(ipsMonitor.getInstructionsPerSecond()));
		}

	}
}
