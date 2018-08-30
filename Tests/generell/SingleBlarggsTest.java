package generell;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import gameboy.Gameboy;

@RunWith(Parameterized.class)
public class SingleBlarggsTest
{
	@Parameters(name = "{1}")
	public static List<Object> data()
	{
		List<Object> files = new ArrayList<>();

		String[] input = new String[2];
		input[0] = "resources\\TestData\\mooneye-gb_hwtests\\acceptance\\add_sp_e_timing.gb";
		input[1] = "add_sp_e_timing.gb";
		files.add(input);

		return files;
	}

	private String fInput;

	public SingleBlarggsTest(String input, String name)
	{
		this.fInput = input;
	}

	@Test
	public void test()
	{
		Gameboy gameboy = new Gameboy(fInput, 0);
		StringBuilder result = new StringBuilder();
		while (result.indexOf("Failed") == -1 && result.indexOf("Passed") == -1)
		{
			gameboy.tick();
			if (Byte.toUnsignedInt(gameboy.getRam().load(0xFF02)) == 0x81
					&& Byte.toUnsignedInt(gameboy.getRam().load(0xFF01)) != 0)
			{
				result.append((char) Byte.toUnsignedInt(gameboy.getRam().load(0xFF01)));
				gameboy.getRam().put(0xFF01, 0);
			}
		}

		assertTrue(result.toString(), result.indexOf("Passed") != -1);
	}

}
