package cartridge;

import java.util.HashMap;
import java.util.Map;

/**
 * With the memory bank controller 1 the cartridge can have up to 2MB of ROM
 * and/or 32Kb of RAM. The Chip is controlled by writing to different ROM
 * locations located between 0x0 and 0x8000.
 */
public class CartridgeWithMBC1 extends Cartridge
{
	private static final int CHOSE_ROM_OR_RAM_MODE_END = 0x8000;
	private static final int CHOSE_ROM_OR_RAM_MODE_START = 0x6000;
	private static final int CHOSE_RAM_BANK_OR_ROM_BANK_UPPER_END = 0x6000;
	private static final int CHOSE_RAM_BANK_OR_ROM_BANK_UPPER_START = 0x4000;
	private static final int CHOSE_ROM_BANK_LOWER_END = 0x4000;
	private static final int CHOSE_ROM_BANK_LOWER_START = 0x2000;
	private static final int RAM_ENABLE_END = 0x2000;
	private static final int RAM_ENABLE_START = 0x0000;

	private enum Mode
	{
		ROM_BANKING, RAM_BANKING
	};

	private Mode mode;
	private int romBankLower5Bits;
	private int romOrRam2Bit;
	private boolean ramEnabled;

	private Map<Integer, RomBank0> rom0Banks;
	private Map<Integer, SwitchableRomBank> romBanks;
	private Map<Integer, SwitchableRamBank> ramBanks;
	private DisabledSwitchableRamBank disabledRam;

	public CartridgeWithMBC1(Rom rom)
	{
		super(rom);
		System.out.println("Rom Bank : " + rom.getRomBankCount());
		System.out.println("Ram Bank : " + rom.getRamBankCount());

		rom0Banks = new HashMap<>();
		rom0Banks.put(0, rom0);
		for (int i = 0x20; i < rom.getRomBankCount(); i += 0x20)
		{
			rom0Banks.put(i, new RomBank0(rom.getRomBank(i)));
		}

		romBanks = new HashMap<>();
		romBanks.put(0, new SwitchableRomBank(rom.getRomBank(0)));
		romBanks.put(1, switchRom);
		for (int i = 2; i < rom.getRomBankCount(); i++)
		{
			romBanks.put(i, new SwitchableRomBank(rom.getRomBank(i)));
		}

		ramBanks = new HashMap<>();
		ramBanks.put(0, switchableRamBank);
		for (int i = 1; i < rom.getRamBankCount(); i++)
		{
			ramBanks.put(i, new SwitchableRamBank());
		}

		disabledRam = new DisabledSwitchableRamBank();

		mode = Mode.ROM_BANKING;
		romBankLower5Bits = 1;
		romOrRam2Bit = 0;
		enableRam(false);
	}

	@Override
	public void put(int adress, byte input)
	{
		if (adress >= RAM_ENABLE_START && adress < RAM_ENABLE_END)
		{
			enableRam((Byte.toUnsignedInt(input) & 0xF) == 0xA);
			changeRamBank();
		}
		else if (adress >= CHOSE_ROM_BANK_LOWER_START && adress < CHOSE_ROM_BANK_LOWER_END)
		{
			romBankLower5Bits = Byte.toUnsignedInt(input) & 0b1_1111;
			if (romBankLower5Bits == 0)
			{
				romBankLower5Bits++;
			}
			changeRomBank();
			changeRom0Bank();
		}
		else if (adress >= CHOSE_RAM_BANK_OR_ROM_BANK_UPPER_START && adress < CHOSE_RAM_BANK_OR_ROM_BANK_UPPER_END)
		{
			romOrRam2Bit = Byte.toUnsignedInt(input) & 0b11;
			changeRamBank();
			changeRomBank();
			changeRom0Bank();
		}
		else if (adress >= CHOSE_ROM_OR_RAM_MODE_START && adress < CHOSE_ROM_OR_RAM_MODE_END)
		{
			changeMode(Byte.toUnsignedInt(input) == 1);
			changeRamBank();
			changeRomBank();
			changeRom0Bank();
		}
		else
		{
			super.put(adress, input);
		}

	}

	private void changeMode(boolean mode)
	{
		if (mode)
		{
			this.mode = Mode.RAM_BANKING;
		}
		else
		{
			this.mode = Mode.ROM_BANKING;
		}
	}

	private void enableRam(boolean enable)
	{
		ramEnabled = enable;
		if (!enable)
		{
			map(disabledRam.getStartAdress(), disabledRam.getEndAdress(), disabledRam);
		}
	}

	private void changeRomBank()
	{
		int selectedRomBank = 0;
		switch (mode)
		{
			case RAM_BANKING:
				selectedRomBank = combine(romOrRam2Bit, romBankLower5Bits);
				break;
			case ROM_BANKING:
				selectedRomBank = combine(romOrRam2Bit, romBankLower5Bits);
				break;
		}

		selectedRomBank %= romBanks.size();

		SwitchableRomBank newBank = romBanks.get(selectedRomBank);

		map(newBank.getStartAdress(), newBank.getEndAdress(), newBank);
	}

	private void changeRom0Bank()
	{
		if (rom0Banks.size() == 1)
		{
			return;
		}

		switch (mode)
		{
			case RAM_BANKING:
				System.out.println(combine(romOrRam2Bit, 0));
				System.out
						.println("New rom0Bank: " + Integer.toHexString((combine(romOrRam2Bit, 0)) % romBanks.size()));
				RomBank0 new0Bank = rom0Banks.get((combine(romOrRam2Bit, 0)) % romBanks.size());
				map(new0Bank.getStartAdress(), new0Bank.getEndAdress(), new0Bank);
				break;
			case ROM_BANKING:
				map(rom0Banks.get(0).getStartAdress(), rom0Banks.get(0).getEndAdress(), rom0Banks.get(0));
				break;
		}
	}

	private void changeRamBank()
	{
		if (!ramEnabled)
		{
			return;
		}

		int selectedRamBank = 0;

		switch (mode)
		{
			case RAM_BANKING:
				if (ramBanks.size() == 1)
				{
					selectedRamBank = 0;
				}
				else
				{
					selectedRamBank = romOrRam2Bit % ramBanks.size();
				}
				break;
			case ROM_BANKING:
				selectedRamBank = 0;
				break;
		}

		SwitchableRamBank newBank = ramBanks.get(selectedRamBank);

		map(newBank.getStartAdress(), newBank.getEndAdress(), newBank);
	}

	private int combine(int a2BitValue, int a5BitValue)
	{
		return (a2BitValue << 5) | a5BitValue;
	}
}
