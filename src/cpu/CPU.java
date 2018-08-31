package cpu;

import gameboy.MegiHertz;
import gameboy.TickBasedComponend;
import ram.Ram;

/**
 * The 8-bit CPU.
 */
public class CPU extends TickBasedComponend
{

	public enum State
	{
		NORMAL, HALT, STOP
	}

	private State state;
	private Ram ram;
	private Accumulator accu;
	private Flags flags;
	private Register16bit sp;
	private ProgrammCounter pc;
	private String currentOpCode;

	private boolean interruptMasterEnable;
	private boolean shouldInterruptMasterBeEnabled;
	private boolean changeInterruptMasterEnable;
	private InterruptEnableRegister interruptEnable;
	private InterruptFlagRegister interruptFlag;

	private Register8Bit b;
	private Register8Bit c;
	private Register8Bit d;
	private Register8Bit e;
	private Register8Bit h;
	private Register8Bit l;

	private InstructrionSet instructionSet;

	public CPU(Ram ram, Accumulator accu, ProgrammCounter pc, Register16bit sp, Register8Bit b, Register8Bit c,
			Register8Bit d, Register8Bit e, Register8Bit h, Register8Bit l, Flags flags,
			InterruptEnableRegister interruptEnable, InterruptFlagRegister interruptFlag)
	{
		super(MegiHertz.get(1));

		this.ram = ram;
		this.flags = flags;
		this.accu = accu;
		this.pc = pc;
		this.sp = sp;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.h = h;
		this.l = l;

		this.interruptFlag = interruptFlag;
		this.interruptEnable = interruptEnable;
		interruptMasterEnable = false;

		state = State.NORMAL;
		currentOpCode = "000";

		instructionSet = new InstructrionSet(this, ram, accu, pc, sp, b, c, d, e, h, l, flags);
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if (!instructionSet.isOperationQueueEmpty())
		{
			instructionSet.pollFromOperationQueue().invokeOperation();
			return true;
		}

		if (interruptMasterEnable || state == State.HALT)
		{
			if (interruptFlag.getVBlankPending() && interruptEnable.isVBlankEnabled())
			{
				if (state == State.NORMAL || interruptMasterEnable)
				{
					instructionSet.restartAt(pc, sp, ram, interruptFlag, interruptEnable);
					interruptMasterEnable = false;
					state = State.NORMAL;
					return true;
				}
				state = State.NORMAL;
			}
			else if (interruptFlag.getLCDCPending() && interruptEnable.isLCDCEnabled())
			{
				if (state == State.NORMAL || interruptMasterEnable)
				{
					instructionSet.restartAt(pc, sp, ram, interruptFlag, interruptEnable);
					interruptMasterEnable = false;
					state = State.NORMAL;
					return true;
				}

				state = State.NORMAL;
			}
			else if (interruptFlag.getTimerOverflowPending() && interruptEnable.isTimerOverflowEnabled())
			{
				if (state == State.NORMAL || interruptMasterEnable)
				{
					instructionSet.restartAt(pc, sp, ram, interruptFlag, interruptEnable);
					interruptMasterEnable = false;
					state = State.NORMAL;
					return true;
				}

				state = State.NORMAL;
			}
			else if (interruptFlag.getSerialIOTransferCompletePending()
					&& interruptEnable.isSerialIOTransferCompleteEnabled())
			{
				if (state == State.NORMAL || interruptMasterEnable)
				{
					instructionSet.restartAt(pc, sp, ram, interruptFlag, interruptEnable);
					interruptMasterEnable = false;
					state = State.NORMAL;
					return true;
				}

				state = State.NORMAL;
			}
			else if (interruptFlag.getButtonPressedPending() && interruptEnable.isButtonPressedEnabled())
			{
				if (state == State.NORMAL || interruptMasterEnable)
				{
					instructionSet.restartAt(pc, sp, ram, interruptFlag, interruptEnable);
					interruptMasterEnable = false;
					state = State.NORMAL;
					return true;
				}

				state = State.NORMAL;
			}
		}

		switch (state)
		{
			case HALT:
				return true;
			case STOP:
				return true;
			case NORMAL:
				break;
		}

		if (changeInterruptMasterEnable)
		{
			interruptMasterEnable = shouldInterruptMasterBeEnabled;
			changeInterruptMasterEnable = false;
		}

		// System.out.print(Integer.toHexString(Short.toUnsignedInt(pc.loadWithoutInc())));
		currentOpCode = loadOctalOpCode(pc, ram);

		// System.out.println(": " + currentOpCode);
		instructionSet.get(currentOpCode).invoke();

		return false;
	}

	public void setInterruptMasterEnableDelayed(boolean value)
	{
		shouldInterruptMasterBeEnabled = value;
		changeInterruptMasterEnable = true;
	}

	public void setInterruptMasterEnableImediate(boolean value)
	{
		interruptMasterEnable = value;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * Loads the current opcode from ram and parse it to a 3 digit octal string.
	 * 
	 * @param pc The current program counter.
	 * @param ram The ram the opcode should be loaded from.
	 * @return The opcode as a 3 digit octal string.
	 */
	public String loadOctalOpCode(ProgrammCounter pc, Ram ram)
	{
		StringBuilder octalNumber = new StringBuilder(Integer.toOctalString(Byte.toUnsignedInt(ram.load(pc.load()))));
		octalNumber.reverse();
		while (octalNumber.length() < 3)
		{
			octalNumber.append("0");
		}
		return octalNumber.reverse().toString();
	}

	public String getCurrentOpCode()
	{
		return currentOpCode;
	}

	public Accumulator getAccu()
	{
		return accu;
	}

	public Register8Bit getB()
	{
		return b;
	}

	public Register8Bit getC()
	{
		return c;
	}

	public Register8Bit getD()
	{
		return d;
	}

	public Register8Bit getE()
	{
		return e;
	}

	public Register8Bit getH()
	{
		return h;
	}

	public Register8Bit getL()
	{
		return l;
	}
}
