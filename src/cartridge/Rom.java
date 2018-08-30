package cartridge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Rom
{
	private int[] rom;

	public Rom(String romPath)
	{
		this.rom = loadRom(romPath);
	}

	public Rom()
	{
		this.rom = new int[0x8000];
	}

	public String getTitle()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0x134; i <= 0x142; i++)
		{
			if ((char) rom[i] != 0)
			{
				builder.append((char) rom[i]);
			}
		}
		return builder.toString();
	}

	public boolean isGameBoyColorGame()
	{
		return rom[0x143] == 0x80;
	}

	public boolean isSuperGameBoyGame()
	{
		return rom[0x146] == 0x03;
	}

	public boolean hasNoMBC()
	{
		return rom[0x147] == 0x0 || rom[0x147] == 0x8 || rom[0x147] == 0x9;
	}

	public boolean hasMBC1()
	{
		return rom[0x147] == 0x1 || rom[0x147] == 0x2 || rom[0x147] == 0x3;
	}

	public int getRomBankCount()
	{
		int romSizeCode = rom[0x148];
		if (romSizeCode == 0x52)
		{
			return 72;
		}
		else if (romSizeCode == 0x53)
		{
			return 80;
		}
		else if (romSizeCode == 0x54)
		{
			return 96;
		}
		else
		{
			return (32 << romSizeCode) / 16;
		}
	}

	public int getRamBankCount()
	{
		int ramSizeCode = rom[0x149];

		switch (ramSizeCode)
		{
			case 0x0:
				return 0;
			case 0x1:
				return 1;
			case 0x2:
				return 1;
			case 0x3:
				return 4;
			case 0x4:
				return 16;
			case 0x5:
				return 5;
			default:
				return 0;
		}
	}

	public int[] getRomBank(int number)
	{
		if (0x4000 * (number + 1) <= rom.length)
		{
			return Arrays.copyOfRange(rom, 0x4000 * number, 0x4000 * (number + 1));
		}
		else
		{
			return new int[0x4000];
		}
	}

	public static int[] loadRom(String pathToRomAsString)
	{
		Path pathToRom = Paths.get(pathToRomAsString);
		try
		{
			return toIntegerArray(Files.readAllBytes(pathToRom));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	private static int[] toIntegerArray(byte[] byteArray)
	{
		int[] integerArray = new int[byteArray.length];
		for (int i = 0; i < byteArray.length; i++)
		{
			integerArray[i] = Byte.toUnsignedInt(byteArray[i]);
		}
		return integerArray;
	}
}
