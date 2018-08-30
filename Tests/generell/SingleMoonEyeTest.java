package generell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import gameboy.Gameboy;
import ui.Window;

@RunWith(Parameterized.class)
public class SingleMoonEyeTest // extends NoOutputTest
{
	@Parameters(name = "{1}")
	public static List<Object> data()
	{
		List<Object> files = new ArrayList<>();

		String[] input = new String[2];
		input[0] = "resources\\TestData\\mooneye-gb_hwtests\\manual-only\\sprite_priority.gb";
		input[1] = "add_sp_e_timing.gb";
		files.add(input);

		return files;
	}

	private String fInput;

	public SingleMoonEyeTest(String input, String name)
	{
		this.fInput = input;
	}

	@Test
	public void test()
	{
		Gameboy gameboy = new Gameboy(fInput, 0);
		Window window = new Window(gameboy.getIPSMonitor(), gameboy.getPanel(), gameboy.getKeyListener());

		new Thread(window).start();
		window.setVisible(true);
		gameboy.start();

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
