package cpu;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccumulatorTest
{

	@Test
	public void testAddWithCarry()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b1111_1111, flags);

		accu.add((byte) 0b0000_0001);
		assertEquals(0, accu.load());
		assertTrue(flags.isZeroFlag());
		assertTrue(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testAddWithoutCarry()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b1111_0001, flags);

		accu.add((byte) 0b0000_0011);
		assertEquals((byte) 0b1111_0100, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testAdCCarryTrue()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0001, flags);

		accu.adC((byte) 0b0000_0011);
		assertEquals((byte) 0b1111_0101, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
	
	@Test
	public void testAdCCarryFalse()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b1111_0001, flags);

		accu.adC((byte) 0b0000_1111);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertTrue(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testSubWithBorrow()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b0000_0000, flags);

		accu.sub((byte) 0b0000_0001);
		assertEquals((byte) 0b1111_1111, accu.load());
		assertFalse(flags.isZeroFlag());
		assertTrue(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}

	@Test
	public void testSubWithoutBorrow()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b1111_0001, flags);

		accu.sub((byte) 0b1111_0001);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}

	@Test
	public void testSbCCarryTrue()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.sbC((byte) 0b1111_0010);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}
	
	@Test
	public void testSbCCarryFalse()
	{
		Flags flags = new Flags(false, false, false, false);
		Accumulator accu = new Accumulator((byte) 0b1111_1110, flags);

		accu.sbC((byte) 0b1111_1111);
		assertEquals((byte) 0b1111_1111, accu.load());
		assertFalse(flags.isZeroFlag());
		assertTrue(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}

	@Test
	public void testAnd()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.and((byte) 0b1001_1100);
		assertEquals((byte) 0b1001_0000, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
	
	@Test
	public void testAndZero()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1010_1010, flags);

		accu.and((byte) 0b0101_0101);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testOr()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.or((byte) 0b1001_1100);
		assertEquals((byte) 0b1111_1111, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
	
	@Test
	public void testOrZero()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b0000_0000, flags);

		accu.or((byte) 0b0000_0000);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testXor()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.xOr((byte) 0b1001_1100);
		assertEquals((byte) 0b0110_1111, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}
	
	@Test
	public void testXorZero()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0000, flags);

		accu.xOr((byte) 0b1111_0000);
		assertEquals((byte) 0b0000_0000, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertFalse(flags.isSubtractFlag());
	}

	@Test
	public void testCpFalse()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.cp((byte) 0b1001_1100);
		assertEquals((byte) 0b1111_0011, accu.load());
		assertFalse(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertTrue(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}
	
	@Test
	public void testCpTrue()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.cp((byte) 0b1111_0011);
		assertEquals((byte) 0b1111_0011, accu.load());
		assertTrue(flags.isZeroFlag());
		assertFalse(flags.isCarryFlag());
		assertFalse(flags.isHalfCarryFlag());
		assertTrue(flags.isSubtractFlag());
	}
	
	@Test
	public void testCpl()
	{
		Flags flags = new Flags(false, false, false, true);
		Accumulator accu = new Accumulator((byte) 0b1111_0011, flags);

		accu.cpl();
		assertEquals((byte) 0b0000_1100, accu.load());
		assertTrue(flags.isSubtractFlag());
		assertTrue(flags.isHalfCarryFlag());
	}
}
