package cartridge;

import ram.RamSpace;

/**
 * The boot ROM is the first code that run after start of the gameboy. It is
 * mapped to RAM from 0x0 to 0x100 and disables itself after it is finished. It
 * setup the stack pointer to $FFFE, compares the logo from boot ROM with the
 * logo in the cartridge, copies the logo to video RAM and scroll it in and
 * turns on the lcd. The first instruction of the user program is located at
 * 0x100.
 */
public class BootRom extends RamSpace
{
	private static final String BOOT_ROM_PATH = "resources/BootROM/GameBoyBootRom.gb";
	private final static int START_ADDRESS = 0x0;
	private final static int END_ADDRESS = 0x100;

	public BootRom()
	{
		super(START_ADDRESS, END_ADDRESS);

		byte[] bootRom = Rom.loadRom(BOOT_ROM_PATH);

		for (int i = 0; i < bootRom.length; i++)
		{
			put(i, bootRom[i]);
		}
	}
}
