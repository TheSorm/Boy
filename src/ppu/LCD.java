package ppu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import cpu.TickBasedComponend;
import gameboy.MegiHertz;

public class LCD extends TickBasedComponend
{
	public static final int LCD_WIDTH = 160;
	public static final int LCD_HEIGHT = 144;

	private PixelFifo fifo;
	private LCDControllRegister lcdControll;
	private Panel panel;
	private double scale;
	private boolean isOn;

	public LCD(PixelFifo fifo, LCDControllRegister lcdControll)
	{
		super(MegiHertz.get(4));
		this.fifo = fifo;
		this.lcdControll = lcdControll;

		panel = new Panel();
		isOn = lcdControll.isLcdOn();
		scale = 3.0;
		panel.setPreferredSize(new Dimension((int) (LCD_WIDTH * scale), (int) (LCD_HEIGHT * scale)));
	}

	public class Panel extends JPanel
	{

		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2D = (Graphics2D) g;
			g2D.scale(scale, scale);
			if (true)
			{
				g2D.drawImage(fifo.getImage(), 0, 0, this);
			}
			else
			{
				g2D.setColor(new Color(170, 170, 170));
				g2D.fillRect(0, 0, LCD_WIDTH, LCD_HEIGHT);
			}
		}
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		isOn = lcdControll.isLcdOn();

		return false;
	}

	public Panel getPanel()
	{
		return panel;
	}
}
