package ppu;

public class PixelInformation
{
	public enum Source
	{
		BACKGROUND_AND_WINDOW, OBJECT_0, OBJECT_1
	};

	public final Source source;
	public final int color;

	public PixelInformation(Source source, int color)
	{
		this.source = source;
		this.color = color;
	}
}
