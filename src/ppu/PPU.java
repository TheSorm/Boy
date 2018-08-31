package ppu;

import cpu.InterruptFlagRegister;
import gameboy.MegiHertz;
import gameboy.TickBasedComponend;
import ppu.Fetcher.Mode;
import ppu.LCDControlStatusRegister.LCDStatus;
import ppu.OAMSearcher.OAMState;
import ram.VideoRam;

public class PPU extends TickBasedComponend
{
	private static final int OAM_SEARCH_TICKS = 80;
	private static final int PIXEL_TRANSFER_TICKS = 172;
	private static final int H_BLANCK_TICKS = 204;
	private static final int SCREEN_LINES = 144;
	private static final int V_BLANCK_LINES = 10;
	private static final int VERY_FIRST_LINE_H_BLANK_START_TICKS = 128;

	private Fetcher fetcher;
	private PixelFifo fifo;
	private OAMSearcher oamSearcher;
	private LCDControllYCoordinateRegister lcdYCoordinate;
	private InterruptFlagRegister interruptFlagRegister;
	private LCDControlStatusRegister lcdControlStatusRegister;
	private LCDControllYCompare lcdControllYCompare;
	private ObjectsAttributeMap oam;
	private VideoRam vRam;

	private LCDControllRegister lcdControll;
	private int tickCount;
	private int pixelTransferTickCount;
	private boolean veryFirstLine;

	private LCDControlStatusRegister.LCDStatus internelMode;

	public PPU(TilePatternTable tilePatternTable, ScrollXRegister scrollX, ScrollYRegister scrollY,
			LCDControllRegister lcdControll, LCDControllYCompare lcdControllYCompare,
			LCDControlStatusRegister lcdControlStatusRegister, LCDControllYCoordinateRegister lcdYCoordinate,
			BackgroundAndWindowColorPalette backgroundAndWindowColorPalette, ObjectColorPalette0 obj0,
			ObjectColorPalette1 obj1, InterruptFlagRegister interruptFlagRegister, TileMap tileMap,
			ObjectsAttributeMap oaMap, VideoRam videoRam)
	{
		super(MegiHertz.get(4));
		this.fifo = new PixelFifo(scrollX, scrollY, backgroundAndWindowColorPalette, obj0, obj1);
		this.oamSearcher = new OAMSearcher(lcdControll, lcdYCoordinate, oaMap);
		this.lcdYCoordinate = lcdYCoordinate;
		this.fetcher = new Fetcher(tilePatternTable, fifo, lcdControll, oamSearcher, lcdYCoordinate, tileMap);
		this.interruptFlagRegister = interruptFlagRegister;
		this.lcdControlStatusRegister = lcdControlStatusRegister;
		this.lcdControllYCompare = lcdControllYCompare;
		this.lcdControll = lcdControll;
		this.oam = oaMap;
		this.vRam = videoRam;

		this.lcdControlStatusRegister.setModeHBlank();
		this.internelMode = LCDStatus.H_BLANCK;
		tickCount = VERY_FIRST_LINE_H_BLANK_START_TICKS;
		pixelTransferTickCount = PIXEL_TRANSFER_TICKS;
		this.lcdYCoordinate.writeLcdYCoordinate(0);
		veryFirstLine = true;
	}

	private int count = 0;

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		if (!lcdControll.isLcdOn())
		{
			this.lcdControlStatusRegister.setModeHBlank();
			this.internelMode = LCDStatus.H_BLANCK;
			tickCount = VERY_FIRST_LINE_H_BLANK_START_TICKS;
			pixelTransferTickCount = PIXEL_TRANSFER_TICKS;
			this.lcdYCoordinate.writeLcdYCoordinate(0);
			veryFirstLine = true;
			oamSearcher.reset();
			fetcher.reset();
			fifo.reset();
			count = 0;
			return true;
		}
		else
		{
			lcdControlStatusRegister.setLYCEqualsLY(lcdControllYCompare.getY() == lcdYCoordinate.loadLcdYCoordinate());

			if (lcdControlStatusRegister.isLYCEqualsLYInterruptEnabled() && lcdControlStatusRegister.isLYCEqualsLY())
			{
				interruptFlagRegister.setLCDCPending(true);
			}
		}

		count++;
		drawScreen();

		if (lcdControlStatusRegister.isLYCEqualsLY())
		{
			lcdControlStatusRegister.setLYCEqualsLY(lcdControllYCompare.getY() == lcdYCoordinate.loadLcdYCoordinate());

			if (lcdControlStatusRegister.isLYCEqualsLYInterruptEnabled() && lcdControlStatusRegister.isLYCEqualsLY())
			{
				interruptFlagRegister.setLCDCPending(true);
			}
		}
		if (count == 79)
		{
			System.out.println("79 " + internelMode);
			System.out.println(
					"LY: " + lcdYCoordinate.loadLcdYCoordinate() + ", Mode: " + lcdControlStatusRegister.getStatus()
							+ ", OAM read: " + oam.isReadAllowed() + ", OAM write: " + oam.isWriteAllowed()
							+ ", VRam read: " + vRam.isReadAllowed() + ", VRam write: " + vRam.isWriteAllowed());
		}

		return false;
	}

	public void drawScreen()
	{

		switch (internelMode)
		{
			case OAM_SEARCH:
				if (!this.lcdControlStatusRegister.isModeOAM() && lcdControlStatusRegister.isOAMInterruptEnabled())
				{
					interruptFlagRegister.setLCDCPending(true);
				}
				this.lcdControlStatusRegister.setModeOAM();
				oamSearcher.tick();
				oam.allowRead(false);
				oam.allowWrite(false);
				vRam.allowRead(true);
				vRam.allowWrite(true);
				if (oamSearcher.getState() == OAMState.FINISHED)
				{
					internelMode = LCDStatus.PIXEL_TRANSFER;
					System.out.println(internelMode);
					oam.allowRead(false);
					oam.allowWrite(true);
					vRam.allowRead(false);
					vRam.allowRead(false);
				}
				break;
			case PIXEL_TRANSFER:
				this.lcdControlStatusRegister.setModePixelTransfer();
				pixelTransferTickCount++;
				oam.allowRead(false);
				oam.allowWrite(false);
				vRam.allowRead(false);
				vRam.allowWrite(false);
				if (!oamSearcher.isEmpty() && !fifo.isEmpty()
						&& oamSearcher.getFirstObject().positionXStart <= fifo.getXPosition())
				{
					fetcher.setMode(Mode.FETCH_OBJECT);
					fetcher.tick();
				}
				else
				{
					fifo.tick();
					fetcher.tick();
				}

				if (fifo.isLineFinished())
				{
					internelMode = LCDStatus.H_BLANCK;
					System.out.println(internelMode);
				}

				break;
			case H_BLANCK:
				if (!this.lcdControlStatusRegister.isModeHBlank()
						&& lcdControlStatusRegister.isHBlankInterruptEnabled())
				{
					interruptFlagRegister.setLCDCPending(true);
					System.out.println("Interrupt");
				}
				this.lcdControlStatusRegister.setModeHBlank();
				tickCount++;
				oam.allowRead(true);
				oam.allowWrite(true);
				vRam.allowRead(true);
				vRam.allowWrite(true);
				if (tickCount == H_BLANCK_TICKS - (pixelTransferTickCount - PIXEL_TRANSFER_TICKS))
				{
					if (veryFirstLine)
					{
						internelMode = LCDStatus.PIXEL_TRANSFER;
						System.out.println(internelMode);
						tickCount = 0;
						pixelTransferTickCount = 0;
						veryFirstLine = false;
						return;
					}
					oam.allowRead(false);
					oam.allowWrite(true);
					vRam.allowRead(true);
					vRam.allowWrite(true);
					internelMode = LCDStatus.OAM_SEARCH;
					System.out.println(internelMode);

					oamSearcher.reset();
					tickCount = 0;
					pixelTransferTickCount = 0;
					fetcher.nextLine();
					fifo.nextLine();
					lcdYCoordinate.writeLcdYCoordinate(lcdYCoordinate.loadLcdYCoordinate() + 1);
				}

				if (lcdYCoordinate.loadLcdYCoordinate() == SCREEN_LINES)
				{
					oam.allowRead(true);
					oam.allowWrite(true);
					vRam.allowRead(true);
					vRam.allowWrite(true);
					internelMode = LCDStatus.V_BLANK;
				}
				break;
			case V_BLANK:
				if (!this.lcdControlStatusRegister.isModeVBlank())
				{
					lcdControlStatusRegister.setModeVBlank();
					interruptFlagRegister.setVBlankPending(true);
					if (lcdControlStatusRegister.isOAMInterruptEnabled())
					{
						interruptFlagRegister.setLCDCPending(true);
					}
					if (lcdControlStatusRegister.isVBlankInterruptEnabled())
					{
						interruptFlagRegister.setLCDCPending(true);
					}
				}
				tickCount++;
				oam.allowRead(true);
				oam.allowWrite(true);
				vRam.allowRead(true);
				vRam.allowWrite(true);
				if (tickCount == OAM_SEARCH_TICKS + PIXEL_TRANSFER_TICKS + H_BLANCK_TICKS)
				{
					lcdYCoordinate.writeLcdYCoordinate(lcdYCoordinate.loadLcdYCoordinate() + 1);
					tickCount = 0;
				}

				if (lcdYCoordinate.loadLcdYCoordinate() - SCREEN_LINES == V_BLANCK_LINES)
				{
					internelMode = LCDStatus.OAM_SEARCH;
					System.out.println(internelMode);
					lcdYCoordinate.writeLcdYCoordinate(0);
					fetcher.reset();
					fifo.reset();
				}
				break;
		}
	}

	public PixelFifo getPixelFifo()
	{
		return fifo;
	}

}
