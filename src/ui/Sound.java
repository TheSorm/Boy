package ui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound implements Runnable
{

	@Override
	public void run()
	{
		while (true)
		{

			byte[] buf = new byte[1];
			;
			AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
			SourceDataLine sdl;
			try
			{
				sdl = AudioSystem.getSourceDataLine(af);
				sdl.open();
				sdl.start();
				for (int i = 0; i < 1000 * (float) 44100 / 1000; i++)
				{
					double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
					buf[0] = (byte) (Math.sin(angle) * 100);
					sdl.write(buf, 0, 1);
				}
				sdl.drain();
				sdl.stop();
			}
			catch (LineUnavailableException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
