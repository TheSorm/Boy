package serial;

import ram.RamRegister;

public class SerialTransferDataRegister extends RamRegister
{
	private static final int SB_ADRESS = 0xFF01;

	public SerialTransferDataRegister()
	{
		super(SB_ADRESS);
	}

}
