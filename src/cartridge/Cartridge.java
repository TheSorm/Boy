package cartridge;

import java.util.HashMap;
import java.util.Map;

import cpu.TickBasedComponend;
import gameboy.MegiHertz;
import ram.ReadableWriteable;

public class Cartridge implements ReadableWriteable
{
	Map<Integer, ReadableWriteable> memoryMap;

	protected RomBank0 rom0;
	protected SwitchableRomBank switchRom;
	protected SwitchableRamBank switchableRamBank;

	protected Cartridge(Rom rom)
	{
		this.memoryMap = new HashMap<>();

		this.rom0 = new RomBank0(rom.getRomBank(0));
		this.switchRom = new SwitchableRomBank(rom.getRomBank(1));
		this.switchableRamBank = new SwitchableRamBank();

		map(rom0.getStartAdress(), rom0.getEndAdress(), rom0);
		map(switchRom.getStartAdress(), switchRom.getEndAdress(), switchRom);
		map(switchableRamBank.getStartAdress(), switchableRamBank.getEndAdress(), switchableRamBank);

	}

	@Override
	public byte load(int address)
	{
		return memoryMap.get(address).load(address);
	}

	@Override
	public void put(int address, byte input)
	{
		if (address >= this.getRamStartAddress() && address < this.getRamEndAddress())
		{
			memoryMap.get(address).put(address, (byte) input);
		}
	}

	public int getRomEndAddress()
	{
		return this.switchRom.getEndAdress();
	}

	public int getRamStartAddress()
	{
		return this.switchableRamBank.getStartAdress();
	}

	public int getRamEndAddress()
	{
		return this.switchableRamBank.getEndAdress();
	}

	public void map(int startAddress, int endAddress, ReadableWriteable value)
	{
		for (int i = startAddress; i < endAddress; i++)
		{
			memoryMap.put(i, value);
		}
	}

	public static Cartridge getCartridge(String pathToRom)
	{
		if (pathToRom.length() == 0)
		{
			return new Cartridge(new Rom());
		}

		Rom rom = new Rom(pathToRom);

		if (rom.hasNoMBC())
		{
			return new Cartridge(rom);
		}
		else if (rom.hasMBC1())
		{
			return new CartridgeWithMBC1(rom);
		}

		return null;
	}
}
