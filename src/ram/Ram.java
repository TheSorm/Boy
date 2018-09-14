package ram;

import java.util.HashMap;
import java.util.Map;

import cartridge.BootRomUnmapRegister;
import cartridge.BootRom;
import cartridge.Cartridge;
import cartridge.RomBank0;
import cartridge.SwitchableRomBank;
import cpu.InterruptEnableRegister;
import cpu.InterruptFlagRegister;
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
import timer.TimerControlRegister;
import timer.TimerCounterRegister;
import timer.TimerModuloRegister;

public class Ram
{
	Map<Integer, ReadableWriteable> memoryMap;

	public Ram(BootRom bootUpRom, Cartridge cartridge, VideoRam videoRam, InternalRam internalRam,
			InternalRamEcho internalRamEcho, ObjectsAttributeMap objectAtributeMap, UnusableMemory0 unusableMemory,
			JoypadInformationRegister joypadInformationRegister, DividerRegister dividerRegister,
			TimerCounterRegister timerCounter, TimerModuloRegister timerModulo, TimerControlRegister timerControl,
			InterruptFlagRegister interruptFlag, WavePatternRam wavePattern, LCDControllRegister lcdControll,
			LCDControlStatusRegister lcdControllStatus, ScrollYRegister scrollY, ScrollXRegister scrollX,
			LCDControllYCoordinateRegister lcdControllYCoordinate, LCDControllYCompare lcdControllYCompare,
			DirectMemoryAcessRegister directMemoryAcess,
			BackgroundAndWindowColorPalette backgroundAndWindowColorPalette, ObjectColorPalette0 objectColorPalette0,
			ObjectColorPalette1 objectColorPalette1, UnusableMemory1 unusableMemory1,
			BootRomUnmapRegister bootRomTurnOff, HighRam highRam, InterruptEnableRegister interruptEnable,
			SoundMode1Envelope soundMode1Envelope, SoundMode1FrequencyHigh soundMode1FrequencyHigh,
			SoundMode1FrequencyLow soundMode1FrequencyLow, SoundMode1SweepRegister soundMode1SweepRegister,
			SoundMode1WavePatternDutyAndSoundLength soundMode1WavePatternDutyAndSoundLength,
			SoundMode2Envelope soundMode2Envelope, SoundMode2FrequencyHigh soundMode2FrequencyHigh,
			SoundMode2FrequencyLow soundMode2FrequencyLow,
			SoundMode2WavePatternDutyAndSoundLength soundMode2WavePatternDutyAndSoundLength,
			SoundMode3FrequencyHigh soundMode3FrequencyHigh, SoundMode3FrequencyLow soundMode3FrequencyLow,
			SoundMode3SelectOutputLevel soundMode3SelectOutputLevel, SoundMode3SoundLength soundMode3SoundLength,
			SoundMode3SoundOnOrOff soundMode3SoundOnOrOff,
			SoundMode4CounterOrConsecutiveSelection soundMode4CounterOrConsecutiveSelection,
			SoundMode4Envelope soundMode4Envelope, SoundMode4PolynomialCounter soundMode4PolynomialCounter,
			SoundMode4SoundLength soundMode4SoundLength, SelectionOfSoundOutputRegister selectionOfSoundOutputRegister,
			ChannelControlRegister channelControlRegister, SoundOnOrOffRegister soundOnOrOffRegister,
			SerialTransferDataRegister serialTransferDataRegister, SerialControllRegister serialControllRegister)
	{
		memoryMap = new HashMap<>();

		for (int i = bootUpRom.startAdress; i < bootUpRom.endAdress; i++)
		{
			memoryMap.put(i, bootUpRom);
		}

		for (int i = bootUpRom.endAdress; i < cartridge.getRomEndAddress(); i++)
		{
			memoryMap.put(i, cartridge);
		}

		for (int i = videoRam.startAdress; i < videoRam.endAdress; i++)
		{
			memoryMap.put(i, videoRam);
		}

		for (int i = cartridge.getRamStartAddress(); i < cartridge.getRamEndAddress(); i++)
		{
			memoryMap.put(i, cartridge);
		}

		for (int i = internalRam.startAdress; i < internalRam.endAdress; i++)
		{
			memoryMap.put(i, internalRam);
		}

		for (int i = internalRamEcho.startAdress; i < internalRamEcho.endAdress; i++)
		{
			memoryMap.put(i, internalRamEcho);
		}

		for (int i = objectAtributeMap.startAdress; i < objectAtributeMap.endAdress; i++)
		{
			memoryMap.put(i, objectAtributeMap);
		}

		for (int i = unusableMemory.startAdress; i < unusableMemory.endAdress; i++)
		{
			memoryMap.put(i, unusableMemory);
		}

		memoryMap.put(joypadInformationRegister.adress, joypadInformationRegister);
		memoryMap.put(serialTransferDataRegister.adress, serialTransferDataRegister);
		memoryMap.put(serialControllRegister.adress, serialControllRegister);
		memoryMap.put(0xFF03, new UnusableRamRegister(0xFF03));
		memoryMap.put(dividerRegister.adress, dividerRegister);
		memoryMap.put(timerCounter.adress, timerCounter);
		memoryMap.put(timerModulo.adress, timerModulo);
		memoryMap.put(timerControl.adress, timerControl);

		memoryMap.put(0xFF08, new UnusableRamRegister(0xFF08));
		memoryMap.put(0xFF09, new UnusableRamRegister(0xFF09));
		memoryMap.put(0xFF0A, new UnusableRamRegister(0xFF0a));
		memoryMap.put(0xFF0B, new UnusableRamRegister(0xFF0b));
		memoryMap.put(0xFF0C, new UnusableRamRegister(0xFF0c));
		memoryMap.put(0xFF0D, new UnusableRamRegister(0xFF0d));
		memoryMap.put(0xFF0E, new UnusableRamRegister(0xFF0e));
		memoryMap.put(interruptFlag.adress, interruptFlag);

		memoryMap.put(soundMode1SweepRegister.adress, soundMode1SweepRegister);
		memoryMap.put(soundMode1WavePatternDutyAndSoundLength.adress, soundMode1WavePatternDutyAndSoundLength);
		memoryMap.put(soundMode1Envelope.adress, soundMode1Envelope);
		memoryMap.put(soundMode1FrequencyLow.adress, soundMode1FrequencyLow);
		memoryMap.put(soundMode1FrequencyHigh.adress, soundMode1FrequencyHigh);
		memoryMap.put(0xFF15, new UnusableRamRegister(0xFF15));
		memoryMap.put(soundMode2WavePatternDutyAndSoundLength.adress, soundMode2WavePatternDutyAndSoundLength);
		memoryMap.put(soundMode2Envelope.adress, soundMode2Envelope);
		memoryMap.put(soundMode2FrequencyLow.adress, soundMode2FrequencyLow);
		memoryMap.put(soundMode2FrequencyHigh.adress, soundMode2FrequencyHigh);
		memoryMap.put(soundMode3SoundOnOrOff.adress, soundMode3SoundOnOrOff);
		memoryMap.put(soundMode3SoundLength.adress, soundMode3SoundLength);
		memoryMap.put(soundMode3SelectOutputLevel.adress, soundMode3SelectOutputLevel);
		memoryMap.put(soundMode3FrequencyLow.adress, soundMode3FrequencyLow);
		memoryMap.put(soundMode3FrequencyHigh.adress, soundMode3FrequencyHigh);
		memoryMap.put(0xFF1F, new UnusableRamRegister(0xFF1F));
		memoryMap.put(soundMode4SoundLength.adress, soundMode4SoundLength);
		memoryMap.put(soundMode4Envelope.adress, soundMode4Envelope);
		memoryMap.put(soundMode4PolynomialCounter.adress, soundMode4PolynomialCounter);
		memoryMap.put(soundMode4CounterOrConsecutiveSelection.adress, soundMode4CounterOrConsecutiveSelection);
		memoryMap.put(channelControlRegister.adress, channelControlRegister);
		memoryMap.put(selectionOfSoundOutputRegister.adress, selectionOfSoundOutputRegister);
		memoryMap.put(soundOnOrOffRegister.adress, soundOnOrOffRegister);
		memoryMap.put(0xFF27, new UnusableRamRegister(0xFF27));
		memoryMap.put(0xFF28, new UnusableRamRegister(0xFF28));
		memoryMap.put(0xFF29, new UnusableRamRegister(0xFF29));
		memoryMap.put(0xFF2A, new UnusableRamRegister(0xFF2A));
		memoryMap.put(0xFF2B, new UnusableRamRegister(0xFF2B));
		memoryMap.put(0xFF2C, new UnusableRamRegister(0xFF2C));
		memoryMap.put(0xFF2D, new UnusableRamRegister(0xFF2D));
		memoryMap.put(0xFF2E, new UnusableRamRegister(0xFF2E));
		memoryMap.put(0xFF2F, new UnusableRamRegister(0xFF2F));

		for (int i = wavePattern.startAdress; i < wavePattern.endAdress; i++)
		{
			memoryMap.put(i, wavePattern);
		}

		memoryMap.put(lcdControll.adress, lcdControll);
		memoryMap.put(lcdControllStatus.adress, lcdControllStatus);
		memoryMap.put(scrollY.adress, scrollY);
		memoryMap.put(scrollX.adress, scrollX);
		memoryMap.put(lcdControllYCoordinate.adress, lcdControllYCoordinate);
		memoryMap.put(lcdControllYCompare.adress, lcdControllYCompare);
		memoryMap.put(directMemoryAcess.adress, directMemoryAcess);
		memoryMap.put(backgroundAndWindowColorPalette.adress, backgroundAndWindowColorPalette);
		memoryMap.put(objectColorPalette0.adress, objectColorPalette0);
		memoryMap.put(objectColorPalette1.adress, objectColorPalette1);
		memoryMap.put(0xFF4A, new RamRegisterPlaceHolder(0xFF4A));
		memoryMap.put(0xFF4B, new RamRegisterPlaceHolder(0xFF4B));

		for (int i = unusableMemory1.startAdress; i < unusableMemory1.endAdress; i++)
		{
			memoryMap.put(i, unusableMemory1);
		}

		memoryMap.put(bootRomTurnOff.adress, bootRomTurnOff);

		for (int i = highRam.startAdress; i < highRam.endAdress; i++)
		{
			memoryMap.put(i, highRam);
		}

		memoryMap.put(interruptEnable.adress, interruptEnable);
	}

	public byte load(byte high, byte low)
	{
		return memoryMap.get(calculateAdress(high, low)).load(calculateAdress(high, low));
	}

	public byte load(short adress)
	{
		return memoryMap.get(Short.toUnsignedInt(adress)).load(Short.toUnsignedInt(adress));
	}

	public byte load(int adress)
	{
		return memoryMap.get(adress).load(adress);
	}

	public void put(byte high, byte low, byte value)
	{
		memoryMap.get(calculateAdress(high, low)).put(calculateAdress(high, low), value);
	}

	public void put(int adress, int input)
	{
		memoryMap.get(adress).put(adress, (byte) input);
	}

	private int calculateAdress(byte high, byte low)
	{
		int adress = Byte.toUnsignedInt(high);
		adress <<= 8;
		adress |= Byte.toUnsignedInt(low);
		return adress;
	}

	public void map(int startAdress, int endAdress, ReadableWriteable value)
	{
		for (int i = startAdress; i < endAdress; i++)
		{
			memoryMap.put(i, value);
		}
	}
}
