package ppu;

public class OAMEntry
{
	private static final int PRIORITY_POSITION = 7;
	private static final int FLIP_Y_POSITION = 6;
	private static final int FLIP_X_POSITION = 5;
	private static final int PALETTE_NUMBER_POSITION = 4;

	public final int positionYEnd;
	public final int positionYStart;
	public final int positionXEnd;
	public final int positionXStart;
	public final byte tileNumber;
	public final boolean preority;
	public final boolean flipY;
	public final boolean flipX;
	public final boolean palette;

	public OAMEntry(byte positionY, byte positionX, byte tileNumber, byte tileFlags)
	{
		this.positionYEnd = Byte.toUnsignedInt(positionY);
		this.positionYStart = positionYEnd - 16;
		this.positionXEnd = Byte.toUnsignedInt(positionX);
		this.positionXStart = positionXEnd - 8;
		this.tileNumber = tileNumber;

		preority = (Byte.toUnsignedInt(tileFlags) & (1 << PRIORITY_POSITION)) != 0;
		flipY = (Byte.toUnsignedInt(tileFlags) & (1 << FLIP_Y_POSITION)) != 0;
		flipX = (Byte.toUnsignedInt(tileFlags) & (1 << FLIP_X_POSITION)) != 0;
		palette = (Byte.toUnsignedInt(tileFlags) & (1 << PALETTE_NUMBER_POSITION)) != 0;
	}

}
