package ram;

import cartridge.BootRomUnmapRegister;
import cartridge.Cartridge;
import gameboy.MegiHertz;
import gameboy.TickBasedComponend;

public class CardridgeOverBootRomMapper extends TickBasedComponend
{
	private Cartridge cardride;
	private Ram ram;
	private UnusableMemory1 unusableMemory1;
	private BootRomUnmapRegister bootRomTurnOff;
	private boolean compleatyLoadet;

	public CardridgeOverBootRomMapper(Cartridge cardride, UnusableMemory1 unusableMemory1, Ram ram,
			BootRomUnmapRegister bootRomTurnOff)
	{
		super(MegiHertz.get(4));

		this.bootRomTurnOff = bootRomTurnOff;
		this.cardride = cardride;
		this.ram = ram;
		this.unusableMemory1 = unusableMemory1;

		this.compleatyLoadet = false;
	}

	@Override
	public boolean tick()
	{
		if (super.tick() || compleatyLoadet)
		{
			return true;
		}

		if (bootRomTurnOff.isUnmapOffBootRomRequested())
		{
			this.ram.map(0x000, 0x100, cardride);
			this.ram.map(0xFF50, 0xFF51, unusableMemory1);
			compleatyLoadet = true;
		}

		return false;
	}

}
