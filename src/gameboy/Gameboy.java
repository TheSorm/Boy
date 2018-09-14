package gameboy;

import java.awt.event.KeyListener;

import cartridge.BootRomUnmapRegister;
import cartridge.BootRom;
import cartridge.Cartridge;
import connectos.JoypadConnector;
import connectos.LcdConnector;
import cpu.Accumulator;
import cpu.CPU;
import cpu.Flags;
import cpu.InterruptEnableRegister;
import cpu.InterruptFlagRegister;
import cpu.ProgrammCounter;
import cpu.Register16bit;
import cpu.Register8Bit;
import joypad.JoyPad;
import joypad.JoypadInformationRegister;
import ppu.BackgroundAndWindowColorPalette;
import ppu.DMAMecanism;
import ppu.DirectMemoryAcessRegister;
import ppu.LCD;
import ppu.LCDControlStatusRegister;
import ppu.LCDControllRegister;
import ppu.LCDControllYCompare;
import ppu.LCDControllYCoordinateRegister;
import ppu.ObjectColorPalette0;
import ppu.ObjectColorPalette1;
import ppu.ObjectsAttributeMap;
import ppu.PPU;
import ppu.ScrollXRegister;
import ppu.ScrollYRegister;
import ppu.TileMap;
import ppu.TilePatternTable;
import ram.CardridgeOverBootRomMapper;
import ram.HighRam;
import ram.InternalRam;
import ram.InternalRamEcho;
import ram.Ram;
import ram.UnusableMemory0;
import ram.UnusableMemory1;
import ram.VideoRam;
import ram.WavePatternRam;
import serial.SerialControllRegister;
import serial.SerialTransferDataRegister;
import sound.ChannelControlRegister;
import sound.SelectionOfSoundOutputRegister;
import sound.SoundMode1Envelope;
import sound.SoundMode1FrequencyHigh;
import sound.SoundMode1FrequencyLow;
import sound.SoundMode1SweepRegister;
import sound.SoundMode1WavePatternDutyAndSoundLength;
import sound.SoundMode2Envelope;
import sound.SoundMode2FrequencyHigh;
import sound.SoundMode2FrequencyLow;
import sound.SoundMode2WavePatternDutyAndSoundLength;
import sound.SoundMode3FrequencyHigh;
import sound.SoundMode3FrequencyLow;
import sound.SoundMode3SelectOutputLevel;
import sound.SoundMode3SoundLength;
import sound.SoundMode3SoundOnOrOff;
import sound.SoundMode4CounterOrConsecutiveSelection;
import sound.SoundMode4Envelope;
import sound.SoundMode4PolynomialCounter;
import sound.SoundMode4SoundLength;
import sound.SoundOnOrOffRegister;
import timer.DividerRegister;
import timer.Timer;
import timer.TimerControlRegister;
import timer.TimerCounterRegister;
import timer.TimerModuloRegister;
import tools.IPSMonitor;

public class Gameboy
{

	private int instructionCount;
	private long elapsedTime;

	private CPU cpu;
	private PPU ppu;
	private Timer timer;
	private Cartridge cartridge;
	private Ram ram;
	private JoyPad joypad;
	private LCD lcd;
	private DMAMecanism dma;
	private CardridgeOverBootRomMapper cardridgeOverBootRomMapper;
	private IPSMonitor ipsMonitor;

	private Accumulator accu;
	private Flags flags;
	private Register16bit sp;
	private ProgrammCounter pc;

	private Register8Bit b;
	private Register8Bit c;
	private Register8Bit d;
	private Register8Bit e;
	private Register8Bit h;
	private Register8Bit l;
	private int baseClockHz;

	public Gameboy(String RomPath, int baseClockHz, IPSMonitor ipsMonitor, LcdConnector lcdConnector,
			JoypadConnector joypadConnector)
	{
		this.baseClockHz = baseClockHz;

		this.flags = new Flags(false, false, false, false);
		this.pc = new ProgrammCounter((short) 0, flags);
		this.accu = new Accumulator((byte) 0, flags);
		this.sp = new Register16bit((short) 0x0000, flags);
		this.b = new Register8Bit((byte) 0, flags);
		this.c = new Register8Bit((byte) 0, flags);
		this.d = new Register8Bit((byte) 0, flags);
		this.e = new Register8Bit((byte) 0, flags);
		this.h = new Register8Bit((byte) 0, flags);
		this.l = new Register8Bit((byte) 0, flags);

		SerialControllRegister serialControllRegister = new SerialControllRegister();
		SerialTransferDataRegister serialTransferDataRegister = new SerialTransferDataRegister();

		SoundOnOrOffRegister soundOnOrOffRegister = new SoundOnOrOffRegister();
		SoundMode1Envelope soundMode1Envelope = new SoundMode1Envelope();
		SoundMode1FrequencyHigh soundMode1FrequencyHigh = new SoundMode1FrequencyHigh(soundOnOrOffRegister);
		SoundMode1FrequencyLow soundMode1FrequencyLow = new SoundMode1FrequencyLow();
		SoundMode1SweepRegister soundMode1SweepRegister = new SoundMode1SweepRegister();
		SoundMode1WavePatternDutyAndSoundLength soundMode1WavePatternDutyAndSoundLength = new SoundMode1WavePatternDutyAndSoundLength();
		SoundMode2Envelope soundMode2Envelope = new SoundMode2Envelope();
		SoundMode2FrequencyHigh soundMode2FrequencyHigh = new SoundMode2FrequencyHigh(soundOnOrOffRegister);
		SoundMode2FrequencyLow soundMode2FrequencyLow = new SoundMode2FrequencyLow();
		SoundMode2WavePatternDutyAndSoundLength soundMode2WavePatternDutyAndSoundLength = new SoundMode2WavePatternDutyAndSoundLength();
		SoundMode3FrequencyHigh soundMode3FrequencyHigh = new SoundMode3FrequencyHigh(soundOnOrOffRegister);
		SoundMode3FrequencyLow soundMode3FrequencyLow = new SoundMode3FrequencyLow();
		SoundMode3SelectOutputLevel soundMode3SelectOutputLevel = new SoundMode3SelectOutputLevel();
		SoundMode3SoundLength soundMode3SoundLength = new SoundMode3SoundLength();
		SoundMode3SoundOnOrOff soundMode3SoundOnOrOff = new SoundMode3SoundOnOrOff();
		SoundMode4CounterOrConsecutiveSelection soundMode4CounterOrConsecutiveSelection = new SoundMode4CounterOrConsecutiveSelection(soundOnOrOffRegister);
		SoundMode4Envelope soundMode4Envelope = new SoundMode4Envelope();
		SoundMode4PolynomialCounter soundMode4PolynomialCounter = new SoundMode4PolynomialCounter();
		SoundMode4SoundLength soundMode4SoundLength = new SoundMode4SoundLength();
		SelectionOfSoundOutputRegister selectionOfSoundOutputRegister = new SelectionOfSoundOutputRegister();
		ChannelControlRegister channelControlRegister = new ChannelControlRegister();

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

		this.cartridge = Cartridge.getCartridge(RomPath);

		this.ram = new Ram(bootUpRom, this.cartridge, videoRam, internalRam, internalRamEcho, objectAtributeMap,
				unusableMemory, joypadInformation, divider, timerCounter, timerModulo, timerControl,
				interruptFlagRegister, wavePattern, lcdControll, lcdControlStatusRegister, scrollY, scrollX,
				lcdYCoordinate, lcdControllYCompare, dmaRegister, backgroundAndWindowColorPalette, obj0, obj1,
				unusableMemory1, bootRomTurnOff, highRam, interruptEnableRegister, soundMode1Envelope,
				soundMode1FrequencyHigh, soundMode1FrequencyLow, soundMode1SweepRegister,
				soundMode1WavePatternDutyAndSoundLength, soundMode2Envelope, soundMode2FrequencyHigh,
				soundMode2FrequencyLow, soundMode2WavePatternDutyAndSoundLength, soundMode3FrequencyHigh,
				soundMode3FrequencyLow, soundMode3SelectOutputLevel, soundMode3SoundLength, soundMode3SoundOnOrOff,
				soundMode4CounterOrConsecutiveSelection, soundMode4Envelope, soundMode4PolynomialCounter,
				soundMode4SoundLength, selectionOfSoundOutputRegister, channelControlRegister, soundOnOrOffRegister,
				serialTransferDataRegister, serialControllRegister);

		this.cpu = new CPU(ram, accu, pc, sp, b, c, d, e, h, l, flags, interruptEnableRegister, interruptFlagRegister);

		this.lcd = new LCD(lcdControll, lcdConnector);

		this.ppu = new PPU(tilePatternTable, scrollX, scrollY, lcdControll, lcdControllYCompare,
				lcdControlStatusRegister, lcdYCoordinate, backgroundAndWindowColorPalette, obj0, obj1,
				interruptFlagRegister, tileMap, objectAtributeMap, videoRam, lcd);

		this.timer = new Timer(timerControl, timerCounter, timerModulo, interruptFlagRegister, divider);

		this.joypad = new JoyPad(joypadInformation, interruptFlagRegister, joypadConnector);

		this.dma = new DMAMecanism(dmaRegister, ram, objectAtributeMap);

		this.cardridgeOverBootRomMapper = new CardridgeOverBootRomMapper(cartridge, unusableMemory1, ram,
				bootRomTurnOff);

		this.ipsMonitor = ipsMonitor;

		instructionCount = 0;
		elapsedTime = System.nanoTime();
	}

	public void start()
	{
		while (true)
		{
			tick();
		}
	}

	public void tick()
	{
		ppu.tick();
		cardridgeOverBootRomMapper.tick();
		dma.tick();
		timer.tick();
		cpu.tick();
		joypad.tick();
		lcd.tick();

		ipsMonitor.tick();

		if (instructionCount == baseClockHz / 16 && baseClockHz != 0)
		{
			while ((System.nanoTime() - elapsedTime) < 1000000000 / 16)
				;
			instructionCount = 0;
			elapsedTime = System.nanoTime();
		}
		instructionCount++;
	}

	public Ram getRam()
	{
		return ram;
	}

	public CPU getCpu()
	{
		return cpu;
	}
}
