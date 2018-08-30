package cartridge;

import ram.RamRegister;

/**
 * Ram register which the boot ROM writes to, to unmap the boot ROM from ram.
 */
public class BootRomUnmapRegister extends RamRegister
{
	private static final int TURN_OFF_ADDRESS = 0xFF50;

	private static final int TURNED_OFF_POSITION_POSITION = 0;

	public BootRomUnmapRegister()
	{
		super(TURN_OFF_ADDRESS);
	}

	public boolean isUnmapOffBootRomRequested()
	{
		return getBit(TURNED_OFF_POSITION_POSITION);
	}

}
