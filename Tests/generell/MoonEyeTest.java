package generell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import gameboy.Gameboy;

@RunWith(Parameterized.class)
public class MoonEyeTest extends NoOutputTest
{
	@Parameters(name = "{1}")
	public static List<Object> data()
	{
		List<Object> files = new ArrayList<>();

		try
		{
			Files.find(Paths.get("resources/TestData/mooneye-gb_hwtests"), Integer.MAX_VALUE,
					(filePath, fileAttr) -> filePath.getFileName().toString().endsWith(".gb")
							// && filePath.toString().contains("ppu")
							&& ((!filePath.getFileName().toString().contains("dmg0")
									&& !filePath.getFileName().toString().contains("mgb")
									&& !filePath.getFileName().toString().contains("sgb")
									&& !filePath.getFileName().toString().contains("sgb2")
									&& !filePath.getFileName().toString().contains("cgb")
									&& !filePath.getFileName().toString().contains("agb")
									&& !filePath.getFileName().toString().contains("ags")
									&& !filePath.getFileName().toString().contains("-S")
									&& !filePath.getFileName().toString().contains("-C")
									&& !filePath.getFileName().toString().contains("-A")
									&& !filePath.toString().contains("utils")
									// &&
									// !filePath.getFileName().toString().contains("unused_hwio-GS.gb")
									// // add all ram registers to pass
									// &&
									// !filePath.getFileName().toString().contains("intr_2_mode0_timing_sprites.gb")
									// // bad documented and wired sprite times
									// &&
									// !filePath.getFileName().toString().contains("tima_write_reloading.gb")
									// // bad documented
									// &&
									// !filePath.getFileName().toString().contains("tma_write_reloading.gb")
									// // bad documented
									// &&
									// !filePath.getFileName().toString().contains("multicart_rom_8Mb.gb")
									// // No Multicard support jet
									&& !filePath.getFileName().toString().contains("sprite_priority.gb"))
									|| filePath.getFileName().toString().contains("dmgABC"))
			// No Multicard support jet

			).forEach((element) -> {
				String[] input = new String[2];
				input[0] = element.toString();
				input[1] = element.getFileName().toString();
				files.add(input);
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return files;
	}

	private String fInput;

	public MoonEyeTest(String input, String name)
	{
		this.fInput = input;
	}

	@Test
	public void test()
	{
		Gameboy gameboy = new Gameboy(fInput, 0);
		while (!gameboy.getCpu().getCurrentOpCode().equals("100"))
		{
			gameboy.tick();
		}

		assertEquals("Assertion Failure at: " + Byte.toUnsignedInt(gameboy.getCpu().getAccu().load()), 0,
				Byte.toUnsignedInt(gameboy.getCpu().getAccu().load()));

		assertTrue("Test Failed!",
				Byte.toUnsignedInt(gameboy.getCpu().getB().load()) == 3
						&& Byte.toUnsignedInt(gameboy.getCpu().getC().load()) == 5
						&& Byte.toUnsignedInt(gameboy.getCpu().getD().load()) == 8
						&& Byte.toUnsignedInt(gameboy.getCpu().getE().load()) == 13
						&& Byte.toUnsignedInt(gameboy.getCpu().getH().load()) == 21
						&& Byte.toUnsignedInt(gameboy.getCpu().getL().load()) == 34);

	}

}
