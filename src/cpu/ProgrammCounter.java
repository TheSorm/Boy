package cpu;

/**
 * The programm counter/pointer of the CPU.
 */
public class ProgrammCounter extends Register16bit
{

	public ProgrammCounter(byte high, byte low, Flags flags)
	{
		super(high, low, flags);
	}

	public ProgrammCounter(short value, Flags flags)
	{
		super(value, flags);
	}

	@Override
	public short load()
	{
		short loadet = super.load();
		inc();
		return loadet;
	}

	public short loadWithoutInc()
	{
		return super.load();
	}

	public void addSignedNoFlags(byte value)
	{
		int result = Short.toUnsignedInt(signedRegisterValue) + value;
		signedRegisterValue = (short) result;
	}
}
