package general;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import gameboy.Gameboy;

@RunWith(Parameterized.class)
public class BlarggsRomsTest extends NoOutputTest
{
	@Parameters( name = "{1}" )
	public static List<Object> data()
	{
		List<Object> files = new ArrayList<>();
		
		File folder = new File("resources/TestData/blarggs");
		File[] listOfFolders = folder.listFiles();
				
		for (int i = 0; i < listOfFolders.length; i++)
		{
			File[] listOfFiles = new File(listOfFolders[i].getPath() + "/individual").listFiles();
			for (int j = 0; j < listOfFiles.length; j++)
			{
				String[] input = new String[2];
				input[0] = listOfFiles[j].getPath();
				input[1] = listOfFiles[j].getName();
				files.add(input);
			}
		}
		return files;
	}

	private String fInput;

	public BlarggsRomsTest(String input, String name)
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
		
		assertTrue(result.toString() + "\n" + fInput, result.indexOf("Passed") != -1);
	}

}
