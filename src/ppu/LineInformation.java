package ppu;

public class LineInformation
{
	public final byte tileNumber;
	public final int currentPixelLine;

	public LineInformation(byte tileNumber, int currentPixelLine)
	{
		this.tileNumber = tileNumber;
		this.currentPixelLine = currentPixelLine;
	}
}