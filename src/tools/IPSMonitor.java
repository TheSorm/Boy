package tools;

import gameboy.MegiHertz;
import gameboy.TickBasedComponend;

public class IPSMonitor extends TickBasedComponend
{
	private long count;
	private long time;
	private long instructionsPerSecond;

	public IPSMonitor()
	{
		super(MegiHertz.get(4));
		count = 0;
		time = 0;
		instructionsPerSecond = 0;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if ((System.nanoTime() - time) > 1000000000)
		{
			instructionsPerSecond = count;
			count = 0;
			time = System.nanoTime();
		}
		count++;

		return false;
	}

	public long getInstructionsPerSecond()
	{
		return instructionsPerSecond;
	}

}
