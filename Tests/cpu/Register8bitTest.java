package cpu;

import static org.junit.Assert.*;

import org.junit.Test;

public class Register8bitTest
{

	@Test
	public void testInc()
	{
		Flags flags = new Flags(true, true, true, false);
		Register8bit reg = new Accumulator((byte) 0b1111_0000, flags);

		reg.inc();
		assertEquals((byte) 0b1111_0001, reg.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
	
	@Test
	public void testIncZero()
	{
		Flags flags = new Flags(false, true, false, false);
		Register8bit reg = new Accumulator((byte) 0b1111_1111, flags);

		reg.inc();
		assertEquals(0, reg.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
}

