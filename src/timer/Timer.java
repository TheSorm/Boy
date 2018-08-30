package timer;

import cpu.InterruptFlagRegister;
import cpu.TickBasedComponend;
import gameboy.MegiHertz;

public class Timer extends TickBasedComponend
{
	TimerControlRegister timerControl;
	TimerCounterRegister timerCounter;
	TimerModuloRegister timerModulo;
	InterruptFlagRegister interruptFlag;
	DividerRegister divider;

	private short time;
	private int timerModuloDelay;
	private boolean lastValue;

	public Timer(TimerControlRegister timerControl, TimerCounterRegister timerCounter, TimerModuloRegister timerModulo,
			InterruptFlagRegister interruptFlag, DividerRegister divider)
	{
		super(MegiHertz.get(4));
		this.timerControl = timerControl;
		this.timerCounter = timerCounter;
		this.timerModulo = timerModulo;
		this.interruptFlag = interruptFlag;
		this.divider = divider;

		time = 0;
		timerModuloDelay = 0;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if (divider.isReseted())
		{
			time = 1;
		}

		divider.calculateDividerFromInternalCounter(time);

		if (lastValue && !(timerControl.isTimerStarted()
				&& ((time >>> timerControl.getActiveBitOfInternelCounter()) & 1) == 1))
		{
			timerCounter.inc();
			if (timerCounter.isZero())
			{
				interruptFlag.setTimerOverflowPending(true);
				timerModuloDelay = 5;
			}
		}
		
		if(timerModuloDelay == 1) {
			timerCounter.setCount(timerModulo.getTimerModulo());
			timerModuloDelay --;
		}else if(timerModuloDelay > 1) {
			timerModuloDelay --;
		}

		lastValue = timerControl.isTimerStarted() && ((time >>> timerControl.getActiveBitOfInternelCounter()) & 1) == 1;

		time++;

		return false;
	}

}
