package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import connectos.LcdConnector;
import ppu.LCD;

public class BoyPanel extends JPanel
{
	private LcdConnector lcdConnector;
	private int scale;

	public BoyPanel(LcdConnector lcdConnector, int scale)
	{
		this.lcdConnector = lcdConnector;
		this.setPreferredSize(new Dimension(LCD.LCD_WIDTH * scale, LCD.LCD_HEIGHT * scale));
		this.scale = scale;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(scale, scale);

		if (lcdConnector.isLcdOn())
		{
			g2D.drawImage(lcdConnector.getImage(), 0, 0, this);
		}
		else
		{
			g2D.setColor(new Color(170, 170, 170));
			g2D.fillRect(0, 0, LCD.LCD_WIDTH, LCD.LCD_HEIGHT);
		}
	}
}
