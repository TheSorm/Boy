package serial;

import ram.RamRegister;

public class SerialControllRegister extends RamRegister
{
	private static final int SC_ADRESS = 0xFF02;

	private static final int TRANSFER_START_FLAG = 7;
	private static final int UNUSED_BIT_6 = 6;
	private static final int UNUSED_BIT_5 = 5;
	private static final int UNUSED_BIT_4 = 4;
	private static final int UNUSED_BIT_3 = 3;
	private static final int UNUSED_BIT_2 = 2;
	private static final int UNUSED_BIT_1 = 1;
	private static final int SHIFT_CLOCK = 0;

	public SerialControllRegister()
	{
		super(SC_ADRESS);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
	}

	@Override
	public void put(int adress, byte input)
	{
		super.put(adress, input);
		setBit(UNUSED_BIT_6, true);
		setBit(UNUSED_BIT_5, true);
		setBit(UNUSED_BIT_4, true);
		setBit(UNUSED_BIT_3, true);
		setBit(UNUSED_BIT_2, true);
		setBit(UNUSED_BIT_1, true);
	}

}
