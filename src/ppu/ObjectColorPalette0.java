package ppu;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import ram.RamRegister;

public class ObjectColorPalette0 extends RamRegister
{
	private static final int OBP0_ADRESS = 0xFF48;

	private Map<Integer, Integer> colorMap;

	public ObjectColorPalette0()
	{
		super(OBP0_ADRESS);

		colorMap = new HashMap<>();
		colorMap.put(0b00, new Color(155, 188, 15).getRGB());
		colorMap.put(0b01, new Color(139, 172, 15).getRGB());
		colorMap.put(0b10, new Color(48, 98, 48).getRGB());
		colorMap.put(0b11, new Color(15, 56, 15).getRGB());
	}

	public int getColorFor(int colorCode)
	{
		return colorMap.get(combineBits(getBit(colorCode * 2 + 1), getBit(colorCode * 2)));
	}

	private int combineBits(boolean b, boolean c)
	{
		int result = b ? 1 : 0;
		result <<= 1;
		result |= c ? 1 : 0;
		return result;
	}
}
