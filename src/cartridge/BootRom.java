package cartridge;

import ram.RamSpace;

/**
 * When the Gameboy is turned on, the bootstrap ROM is situated in a memory page
 * at positions $0-$FF. The CPU enters at $0 at startup, and the last two
 * instructions of the code writes to a special register which disables the
 * internal ROM page, thus making the lower 256 bytes of the cartridge ROM
 * readable. The last instruction is situated at position $FE and is two bytes
 * big, which means that right after that instruction has finished, the CPU
 * executes the instruction at $100, which is the entry point code on a
 * cartridge.
 * 
 * http://gbdev.gg8.se/wiki/articles/Gameboy_Bootstrap_ROM
 */
public class BootRom extends RamSpace
{
	private final static int START_ADDRESS = 0x0;
	private final static int END_ADDRESS = 0x100;

	public BootRom()
	{
		super(START_ADDRESS, END_ADDRESS);

		int[] bootRom = Rom.loadRom("resources/BootROM/GameBoyBootRom.gb");

		for (int i = 0; i < bootRom.length; i++)
		{
			put(i, (byte) bootRom[i]);
		}
	}
}
