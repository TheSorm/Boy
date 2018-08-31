package ppu;

import gameboy.MegiHertz;
import gameboy.TickBasedComponend;
import ram.Ram;

public class DMAMecanism extends TickBasedComponend
{
	private DirectMemoryAcessRegister dmaRegister;
	private ObjectsAttributeMap oam;
	private Ram ram;

	private final static int OAM_START = 0xFE00;
	private final static int OAM_END = 0xFEA0;

	private int offset;
	private Byte lastLoadet;

	public DMAMecanism(DirectMemoryAcessRegister dmaRegister, Ram ram, ObjectsAttributeMap oam)
	{
		super(MegiHertz.get(1));

		this.dmaRegister = dmaRegister;
		this.ram = ram;
		this.oam = oam;

		offset = OAM_END - OAM_START;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if (dmaRegister.isStartet())
		{
			offset = -1;
			lastLoadet = null;
		}

		if (OAM_START + offset < OAM_END)
		{
			if (lastLoadet != null)
			{
				dmaRegister.setRunning();
				oam.putFromDMA(OAM_START + offset, lastLoadet);
			}
			offset++;
			if (OAM_START + offset < OAM_END)
			{
				lastLoadet = ram.load(dmaRegister.getStartAdress() + offset);
			}
		}
		else
		{
			dmaRegister.finishedDMA();
		}

		return false;
	}
}
