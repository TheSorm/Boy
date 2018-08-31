package cartridge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The ROM located at the cartridge. On newer cartridges it includes information
 * about the Title of the game, the logo, the cgb flag, the licensee code, sgb
 * flag, the cartridge type, the ROM size, the RAM size, a destination code, a
 * mask ROM version number, a header and a global checksum. On older cartridges
 * the license code is at a different location and the title is longer so there
 * is no cgb flag.
 */
public class Rom
{
	private static final int RAM_SIZE = 0x149;
	private static final int ROM_SIZE = 0x148;
	private static final int ROM_NAME_END = 0x142;
	private static final int ROM_NAME_START = 0x134;
	private static final int GAME_SUPPORTS_SGB_FUNCTIONS = 0x03;
	private static final int SGB_FLAG = 0x146;
	private static final int WORKS_ON_CGB_ONLY = 0xC0;
	private static final int CGB_FLAG = 0x143;
	private static final int STANDART_ROM_SIZE = 0x8000;
	private int[] rom;

	public Rom(String romPath)
	{
		this.rom = toIntegerArray(loadRom(romPath));
	}

	public Rom()
	{
		this.rom = new int[STANDART_ROM_SIZE];
	}

	public String getTitle()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = ROM_NAME_START; i <= ROM_NAME_END; i++)
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
		return rom[CGB_FLAG] == WORKS_ON_CGB_ONLY;
	}

	public boolean isSuperGameBoyGame()
	{
		return rom[SGB_FLAG] == GAME_SUPPORTS_SGB_FUNCTIONS;
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
		int romSizeCode = rom[ROM_SIZE];
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
		int ramSizeCode = rom[RAM_SIZE];

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

	/**
	 * Loads a ROM from memory located at the given path.
	 * 
	 * @param pathToRomAsString Path to the ROM.
	 * @return ROM as byte array.
	 */
	public static byte[] loadRom(String pathToRomAsString)
	{
		Path pathToRom = Paths.get(pathToRomAsString);
		try
		{
			return Files.readAllBytes(pathToRom);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Turns an byte array in to a unsigned integer array.
	 * 
	 * @param byteArray array to be transformed.
	 * @return unsigned integer array.
	 */
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
