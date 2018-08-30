package cpu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import cartridge.BootRomUnmapRegister;
import cartridge.BootRom;
import cartridge.Cartridge;
import cartridge.RomBank0;
import cartridge.SwitchableRamBank;
import cartridge.SwitchableRomBank;
import joypad.JoypadInformationRegister;
import ppu.BackgroundAndWindowColorPalette;
import ppu.DirectMemoryAcessRegister;
import ppu.LCDControlStatusRegister;
import ppu.LCDControllRegister;
import ppu.LCDControllYCompare;
import ppu.LCDControllYCoordinateRegister;
import ppu.ObjectColorPalette0;
import ppu.ObjectColorPalette1;
import ppu.ObjectsAttributeMap;
import ppu.ScrollXRegister;
import ppu.ScrollYRegister;
import ppu.TileMap;
import ppu.TilePatternTable;
import ram.HighRam;
import ram.InternalRam;
import ram.InternalRamEcho;
import ram.Ram;
import ram.UnusableMemory0;
import ram.UnusableMemory1;
import ram.VideoRam;
import ram.WavePatternRam;
import timer.DividerRegister;
import timer.TimerControlRegister;
import timer.TimerCounterRegister;
import timer.TimerModuloRegister;

public class CPUTest
{
	private CPU cpu;
	private Ram ram;

	private Register8bit b;
	private Register8bit c;
	private Register8bit d;
	private Register8bit e;
	private Register8bit h;
	private Register8bit l;
	private Register16bit sp;
	private ProgrammCounter pc;
	private Accumulator accu;
	private Flags flags;

	@Before
	public void initialize()
	{

		LCDControllRegister lcdControll = new LCDControllRegister();
		BootRomUnmapRegister bootRomTurnOff = new BootRomUnmapRegister();
		ScrollXRegister scrollX = new ScrollXRegister();
		ScrollYRegister scrollY = new ScrollYRegister();
		LCDControllYCoordinateRegister lcdYCoordinate = new LCDControllYCoordinateRegister();
		BackgroundAndWindowColorPalette backgroundAndWindowColorPalette = new BackgroundAndWindowColorPalette();
		InterruptEnableRegister interruptEnableRegister = new InterruptEnableRegister();
		InterruptFlagRegister interruptFlagRegister = new InterruptFlagRegister();
		LCDControllYCompare lcdControllYCompare = new LCDControllYCompare();
		LCDControlStatusRegister lcdControlStatusRegister = new LCDControlStatusRegister(lcdControll);
		TimerControlRegister timerControl = new TimerControlRegister();
		TimerCounterRegister timerCounter = new TimerCounterRegister();
		TimerModuloRegister timerModulo = new TimerModuloRegister();
		JoypadInformationRegister joypadInformation = new JoypadInformationRegister();
		DirectMemoryAcessRegister dmaRegister = new DirectMemoryAcessRegister();
		DividerRegister divider = new DividerRegister();
		ObjectColorPalette0 obj0 = new ObjectColorPalette0();
		ObjectColorPalette1 obj1 = new ObjectColorPalette1();

		BootRom bootUpRom = new BootRom();

		InternalRam internalRam = new InternalRam();
		InternalRamEcho internalRamEcho = new InternalRamEcho(internalRam);
		ObjectsAttributeMap objectAtributeMap = new ObjectsAttributeMap(dmaRegister, lcdControlStatusRegister);
		UnusableMemory0 unusableMemory = new UnusableMemory0();
		WavePatternRam wavePattern = new WavePatternRam();
		UnusableMemory1 unusableMemory1 = new UnusableMemory1();
		HighRam highRam = new HighRam();
		TileMap tileMap = new TileMap(lcdControll, scrollX, scrollY);
		TilePatternTable tilePatternTable = new TilePatternTable();
		VideoRam videoRam = new VideoRam(tileMap, tilePatternTable, lcdControlStatusRegister);

		Cartridge cartridge = Cartridge.getCartridge("");

		this.ram = new Ram(bootUpRom, cartridge, videoRam, internalRam, internalRamEcho, objectAtributeMap,
				unusableMemory, joypadInformation, divider, timerCounter, timerModulo, timerControl,
				interruptFlagRegister, wavePattern, lcdControll, lcdControlStatusRegister, scrollY, scrollX,
				lcdYCoordinate, lcdControllYCompare, dmaRegister, backgroundAndWindowColorPalette, obj0, obj1,
				unusableMemory1, bootRomTurnOff, highRam, interruptEnableRegister);

		this.flags = new Flags(false, false, false, false);

		this.b = new Register8bit((byte) 0, flags);
		this.c = new Register8bit((byte) 0, flags);
		this.d = new Register8bit((byte) 0, flags);
		this.e = new Register8bit((byte) 0, flags);
		this.h = new Register8bit((byte) 0, flags);
		this.l = new Register8bit((byte) 0, flags);
		this.accu = new Accumulator((byte) 0, flags);
		this.sp = new Register16bit((short) 0xDFFF, flags);
		this.pc = new ProgrammCounter((short) 0, flags);

		cpu = new CPU(ram, accu, pc, sp, b, c, d, e, h, l, flags, interruptEnableRegister, interruptFlagRegister);

	}

	private void tick1Mhz()
	{
		cpu.tick();
		cpu.tick();
		cpu.tick();
		cpu.tick();
	}

	@Test
	public void testPutImmediate8InRegister()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x06);
		ram.put((byte) 0, (byte) 1, (byte) 0b10010101);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b10010101, Byte.toUnsignedLong(b.load()));
		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutRegisterInRegister()
	{
		e.put((byte) 6);
		ram.put((byte) 0, (byte) 0, (byte) 0x4b);

		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(e.load()), Byte.toUnsignedLong(c.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedByRegisterInRegister()
	{
		h.put((byte) 32);
		l.put((byte) 111);
		ram.put((byte) 0, (byte) 0, (byte) 0x46);
		ram.put((byte) 32, (byte) 111, (byte) 0b01000110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01000110, Byte.toUnsignedLong(b.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutRegisterInRamAdressedByRegister()
	{
		e.put((byte) 6);
		h.put((byte) 32);
		l.put((byte) 111);

		ram.put((byte) 0, (byte) 0, (byte) 0x73);

		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(e.load()), Byte.toUnsignedLong(ram.load((byte) 32, (byte) 111)));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedByImmediate16InRegister()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0xfa);
		ram.put((byte) 0, (byte) 1, (byte) 111);
		ram.put((byte) 0, (byte) 2, (byte) 32);

		ram.put((byte) 32, (byte) 111, (byte) 0b01010110);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01010110, Byte.toUnsignedLong(accu.load()));
		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutRegisterInRamAdressedByImmediate16()
	{

		accu.put((byte) 232);
		ram.put((byte) 0, (byte) 0, (byte) 0xea);
		ram.put((byte) 0, (byte) 1, (byte) 15);
		ram.put((byte) 0, (byte) 2, (byte) 34);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(accu.load()), Byte.toUnsignedLong(ram.load((byte) 34, (byte) 15)));
		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutAccuInRamAdressedInUpperSpacePlusRegisterC()
	{

		accu.put((byte) 123);
		c.put((byte) 34);
		ram.put((byte) 0, (byte) 0, (byte) 0xe2);

		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(accu.load()), Byte.toUnsignedLong(ram.load((byte) 255, (byte) 34)));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedInUpperSpacePlusRegisterCinAccu()
	{

		c.put((byte) 122);
		ram.put((byte) 0, (byte) 0, (byte) 0xf2);
		ram.put((byte) 255, (byte) 122, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01100110, Byte.toUnsignedLong(accu.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedByHLinAccuAndDcrHL()
	{

		h.put((byte) 255);
		l.put((byte) 122);

		ram.put((byte) 0, (byte) 0, (byte) 0x3a);
		ram.put((byte) 255, (byte) 122, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01100110, Byte.toUnsignedLong(accu.load()));
		assertEquals(121, Byte.toUnsignedLong(l.load()));
		assertEquals(255, Byte.toUnsignedLong(h.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutAccuInRamAdressedByHLAndDcrHL()
	{

		h.put((byte) 123);
		l.put((byte) 0);
		accu.put((byte) 10);

		ram.put((byte) 0, (byte) 0, (byte) 0x32);
		ram.put((byte) 255, (byte) 122, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(accu.load()), Byte.toUnsignedLong(ram.load((byte) 123, (byte) 0)));
		assertEquals(255, Byte.toUnsignedLong(l.load()));
		assertEquals(122, Byte.toUnsignedLong(h.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedByHLinAccuAndIncHL()
	{

		h.put((byte) 255);
		l.put((byte) 255);

		ram.put((byte) 0, (byte) 0, (byte) 0x2a);
		ram.put((byte) 255, (byte) 255, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01100110, Byte.toUnsignedLong(accu.load()));
		assertEquals(0, Byte.toUnsignedLong(l.load()));
		assertEquals(0, Byte.toUnsignedLong(h.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutAccuInRamAdressedByHLAndIncHL()
	{

		h.put((byte) 123);
		l.put((byte) 0);
		accu.put((byte) 10);

		ram.put((byte) 0, (byte) 0, (byte) 0x22);
		ram.put((byte) 255, (byte) 122, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(accu.load()), Byte.toUnsignedLong(ram.load((byte) 123, (byte) 0)));
		assertEquals(1, Byte.toUnsignedLong(l.load()));
		assertEquals(123, Byte.toUnsignedLong(h.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutAccuInRamAdressedInUpperSpacePlusImmediate()
	{

		accu.put((byte) 123);

		ram.put((byte) 0, (byte) 0, (byte) 0xe0);
		ram.put((byte) 0, (byte) 1, (byte) 122);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(accu.load()), Byte.toUnsignedLong(ram.load((byte) 255, (byte) 122)));
		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadRamAdressedInUpperSpacePlusImmediateInAccu()
	{

		ram.put((byte) 0, (byte) 0, (byte) 0xf0);
		ram.put((byte) 0, (byte) 1, (byte) 122);
		ram.put((byte) 255, (byte) 122, (byte) 0b01100110);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01100110, Byte.toUnsignedLong(accu.load()));
		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadImmediate16InRegisterPair()
	{

		ram.put((byte) 0, (byte) 0, (byte) 0x11);
		ram.put((byte) 0, (byte) 1, (byte) 0b01100110);
		ram.put((byte) 0, (byte) 2, (byte) 0b01101110);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0b01101110, Byte.toUnsignedLong(d.load()));
		assertEquals(0b01100110, Byte.toUnsignedLong(e.load()));
		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadImmediate16InSP()
	{

		ram.put((byte) 0, (byte) 0, (byte) 0x31);
		ram.put((byte) 0, (byte) 1, (byte) 0b01100110);
		ram.put((byte) 0, (byte) 2, (byte) 0b01101110);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0b0110111001100110, Short.toUnsignedInt(sp.load()));

	}

	@Test
	public void testLoadHLinSP()
	{
		h.put((byte) 0b10010110);
		l.put((byte) 0b10010110);
		ram.put((byte) 0, (byte) 0, (byte) 0xF9);

		tick1Mhz();
		tick1Mhz();

		assertEquals(0b1001011010010110, Short.toUnsignedLong(sp.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadSPPlusImmediate8InHL()
	{

		h.put((byte) 12);
		l.put((byte) 20);
		sp.put((short) 20);
		ram.put((byte) 0, (byte) 0, (byte) 0xF8);
		ram.put((byte) 0, (byte) 1, (byte) -10);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals((byte) 10, Byte.toUnsignedLong(l.load()));
		assertEquals((byte) 0, Byte.toUnsignedLong(h.load()));
		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPutSPInRamAdressedByImmediate16()
	{

		sp.put((short) 0b1001011010010110);
		ram.put((byte) 0, (byte) 0, (byte) 0x08);
		ram.put((byte) 0, (byte) 1, (byte) 15);
		ram.put((byte) 0, (byte) 2, (byte) 34);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(Byte.toUnsignedLong(sp.loadLow()), Byte.toUnsignedLong(ram.load((byte) 34, (byte) 15)));
		assertEquals(Byte.toUnsignedLong(sp.loadHigh()), Byte.toUnsignedLong(ram.load((byte) 34, (byte) 16)));
		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPushRegisterPairOntoStack()
	{

		sp.put((short) 0xdfff);
		d.put((byte) 122);
		e.put((byte) 69);
		ram.put((byte) 0, (byte) 0, (byte) 0xD5);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0xdfff - 2, Short.toUnsignedLong(sp.load()));
		assertEquals(Byte.toUnsignedLong(e.load()), Byte.toUnsignedLong(ram.load(sp.loadHigh(), sp.loadLow())));
		assertEquals(Byte.toUnsignedLong(d.load()),
				Byte.toUnsignedLong(ram.load(sp.loadHigh(), (byte) (sp.loadLow() + 1))));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testPopRegisterPairOffStack()
	{
		sp.put((short) 0xdffd);
		ram.put(0xdffd, (byte) 45);
		ram.put(0xdffe, (byte) 254);
		ram.put((byte) 0, (byte) 0, (byte) 0xE1);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0xdfff, Short.toUnsignedLong(sp.load()));
		assertEquals(254, Byte.toUnsignedLong(h.load()));
		assertEquals(45, Byte.toUnsignedLong(l.load()));
		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testNOP()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x00);

		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testHALT()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x76);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testSTOP()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x10);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testJump()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0xc3);
		ram.put((byte) 0, (byte) 1, (byte) 0x04);
		ram.put((byte) 0, (byte) 2, (byte) 0xff);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0xff04, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testconditionalJumpTrue()
	{
		flags.setZeroFlag(true);
		ram.put((byte) 0, (byte) 0, (byte) 0xCA);
		ram.put((byte) 0, (byte) 1, (byte) 0x99);
		ram.put((byte) 0, (byte) 2, (byte) 0x00);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0x0099, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testconditionalJumpFalse()
	{
		flags.setZeroFlag(true);
		ram.put((byte) 0, (byte) 0, (byte) 0xC2);
		ram.put((byte) 0, (byte) 1, (byte) 0x99);
		ram.put((byte) 0, (byte) 2, (byte) 0x00);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testJumpToHL()
	{
		h.put((byte) 0xfe);
		l.put((byte) 0x04);
		ram.put((byte) 0, (byte) 0, (byte) 0xE9);

		tick1Mhz();

		assertEquals(0xfe04, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testAddJumpPositive()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x18);
		ram.put((byte) 0, (byte) 1, (byte) 24);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(26, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testAddJumpNegative()
	{
		pc.put((byte) 0, (byte) 44);
		ram.put((byte) 0, (byte) 45, (byte) 0x18);
		ram.put((byte) 0, (byte) 46, (byte) -12);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(35, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testConditionalAddJumpTrue()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x20);
		ram.put((byte) 0, (byte) 1, (byte) 122);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(124, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testConditionalAddJumpFalse()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0x38);
		ram.put((byte) 0, (byte) 1, (byte) 78);

		tick1Mhz();
		tick1Mhz();

		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testCall()
	{
		sp.put((short) 0xdffd);
		ram.put((byte) 0, (byte) 0, (byte) 0xCD);
		ram.put((byte) 0, (byte) 1, (byte) 0x11);
		ram.put((byte) 0, (byte) 2, (byte) 0xfe);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0xfe11, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0, Byte.toUnsignedInt(ram.load(0xdffc)));
		assertEquals(3, Byte.toUnsignedInt(ram.load(0xdffb)));
		assertEquals(0xdffb, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testConditionalCallTrue()
	{
		sp.put((short) 0xdffd);
		ram.put((byte) 0, (byte) 0, (byte) 0xD4);
		ram.put((byte) 0, (byte) 1, (byte) 0x11);
		ram.put((byte) 0, (byte) 2, (byte) 0x11);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0x1111, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0, Byte.toUnsignedInt(ram.load(0xdffc)));
		assertEquals(3, Byte.toUnsignedInt(ram.load(0xdffb)));
		assertEquals(0xdffb, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testConditionalCallFalse()
	{
		sp.put((short) 0xdffd);
		ram.put((byte) 0, (byte) 0, (byte) 0xCC);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(3, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testRestart()
	{
		sp.put((short) 0xdffd);
		ram.put((byte) 0, (byte) 0, (byte) 0xDF);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0x18, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0, Byte.toUnsignedInt(ram.load(0xdffc)));
		assertEquals(1, Byte.toUnsignedInt(ram.load(0xdffb)));
		assertEquals(0xdffb, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testReturn()
	{
		sp.put((short) 0xdffb);
		ram.put(0xdffc, (byte) 0x11);
		ram.put(0xdffb, (byte) 0x85);
		ram.put((byte) 0, (byte) 0, (byte) 0xC9);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0x1185, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0xdffd, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testConditionalReturnTrue()
	{
		sp.put((short) 0xdffb);
		ram.put(0xdffc, (byte) 0x11);
		ram.put(0xdffb, (byte) 0x85);
		ram.put((byte) 0, (byte) 0, (byte) 0xD0);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(0x1185, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0xdffd, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testConditionalReturnfalse()
	{
		ram.put((byte) 0, (byte) 0, (byte) 0xC8);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
	}

	@Test
	public void testLoadimediate8toRamAdressedByHL()
	{
		h.put((byte) 0x22);
		l.put((byte) 0x22);
		ram.put((byte) 0, (byte) 0, (byte) 0x36);
		ram.put((byte) 0, (byte) 1, (byte) 0x24);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0x24, Byte.toUnsignedInt(ram.load(h.load(), l.load())));
	}

	@Test
	public void testIncRegisterPair()
	{
		b.put((byte) 111);
		c.put((byte) 255);
		ram.put((byte) 0, (byte) 0, (byte) 0x03);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0, Byte.toUnsignedInt(c.load()));
		assertEquals(112, Byte.toUnsignedInt(b.load()));
	}

	@Test
	public void testIncSP()
	{
		sp.put((short) 0xff11);
		ram.put((byte) 0, (byte) 0, (byte) 0x33);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0xff12, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testDcrRegisterPair()
	{
		b.put((byte) 111);
		c.put((byte) 255);
		ram.put((byte) 0, (byte) 0, (byte) 0x0b);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(254, Byte.toUnsignedInt(c.load()));
		assertEquals(111, Byte.toUnsignedInt(b.load()));
	}

	@Test
	public void testDcrSP()
	{
		sp.put((short) 0x0);
		ram.put((byte) 0, (byte) 0, (byte) 0x3b);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0xffff, Short.toUnsignedInt(sp.load()));
	}

	@Test
	public void testIncRamAdressedByHL()
	{
		h.put((byte) 0x23);
		l.put((byte) 0x22);
		ram.put((byte) 0, (byte) 0, (byte) 0x34);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(1, Byte.toUnsignedInt(ram.load(h.load(), l.load())));
	}

	@Test
	public void testDcrRamAdressedByHL()
	{
		h.put((byte) 0x23);
		l.put((byte) 0x22);
		ram.put((byte) 0, (byte) 0, (byte) 0x35);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(255, Byte.toUnsignedInt(ram.load(h.load(), l.load())));
	}

	@Test
	public void testAddRegisterPairToHL()
	{
		d.put((byte) 111);
		e.put((byte) 255);
		ram.put((byte) 0, (byte) 0, (byte) 0x19);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(111, Byte.toUnsignedInt(h.load()));
		assertEquals(255, Byte.toUnsignedInt(l.load()));
	}

	@Test
	public void testAddSPtoHL()
	{
		sp.put((short) 0xff00);
		h.put((byte) 0);
		l.put((byte) 0xf1);
		ram.put((byte) 0, (byte) 0, (byte) 0x39);

		tick1Mhz();
		tick1Mhz();

		assertEquals(1, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(0xff, Byte.toUnsignedInt(h.load()));
		assertEquals(0xf1, Byte.toUnsignedInt(l.load()));
	}

	@Test
	public void testAddSignedImediate8toSP()
	{
		sp.put((short) 424);
		ram.put((byte) 0, (byte) 0, (byte) 0xe8);
		ram.put((byte) 0, (byte) 1, (byte) -10);

		tick1Mhz();
		tick1Mhz();
		tick1Mhz();
		tick1Mhz();

		assertEquals(2, Short.toUnsignedInt(pc.loadWithoutInc()));
		assertEquals(414, Short.toUnsignedInt(sp.load()));
	}
}
