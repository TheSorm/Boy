package sound;

import ram.RamRegister;

public class SoundMode4PolynomialCounter extends RamRegister
{
	private static final int NR43_ADRESS = 0xFF22;

	private static final int SELECTION_OF_SHIFT_CLOCK_FREQUENCY_BIT_3 = 7;
	private static final int SELECTION_OF_SHIFT_CLOCK_FREQUENCY_BIT_2 = 6;
	private static final int SELECTION_OF_SHIFT_CLOCK_FREQUENCY_BIT_1 = 5;
	private static final int SELECTION_OF_SHIFT_CLOCK_FREQUENCY_BIT_0 = 4;
	private static final int SELECTION_OFPOLYNOMIAL_COUNTER_STEP = 3;
	private static final int SELECTION_OF_DIVIDING_RATIO_OF_FREQUENCIES_BIT_2 = 2;
	private static final int SELECTION_OF_DIVIDING_RATIO_OF_FREQUENCIES_BIT_1 = 1;
	private static final int SELECTION_OF_DIVIDING_RATIO_OF_FREQUENCIES_BIT_0 = 0;

	public SoundMode4PolynomialCounter()
	{
		super(NR43_ADRESS);
	}

}
