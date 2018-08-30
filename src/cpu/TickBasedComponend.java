package cpu;

import gameboy.MegiHertz;

public abstract class TickBasedComponend
{
	private int requiertTicks;
	private int tickCount;

	public TickBasedComponend(int hz)
	{
		this.requiertTicks = MegiHertz.get(4) / hz;
		tickCount = requiertTicks;
	}

	public boolean tick()
	{
		if (tickCount > 1)
		{
			tickCount--;
			return true;
		}
		else
		{
			tickCount = requiertTicks;
		}

		return false;
	}

	protected void resetTickCount()
	{
		tickCount = requiertTicks;
	}

}
