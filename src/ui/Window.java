package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connectos.JoypadConnector;
import connectos.LcdConnector;
import ppu.LCD;
import tools.IPSMonitor;

public class Window extends JFrame implements Runnable
{
	private BoyPanel gamePane;
	private JPanel contendPane;
	private JLabel ips;
	private IPSMonitor ipsMonitor;

	public Window(IPSMonitor ipsMonitor, LcdConnector lcdConnector, JoypadConnector joypadConnector)
	{
		super("Gameboy");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		this.gamePane = new BoyPanel(lcdConnector, 3);
		this.ipsMonitor = ipsMonitor;

		contendPane = new JPanel(new BorderLayout());
		this.setContentPane(contendPane);

		ips = new JLabel("");

		contendPane.add(gamePane, BorderLayout.CENTER);
		contendPane.add(ips, BorderLayout.PAGE_START);

		this.addKeyListener(new GbKeyListener(joypadConnector));

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
