package ppu;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import cpu.TickBasedComponend;
import gameboy.MegiHertz;
import ppu.LCDControllRegister.objectSize;

public class OAMSearcher extends TickBasedComponend
{
	private static final int MAXIMUM_OBJECTS_PER_LINE = 10;

	public enum OAMState
	{
		OAM_FETCH, OAM_DESITE, FINISHED
	}

	private OAMState state;

	private OAMEntry currentEntry;
	private ObjectsAttributeMap oaMap;
	private List<OAMEntry> objectsOfLine;
	private LCDControllRegister lcdControll;
	private LCDControllYCoordinateRegister lcdYCoordinate;

	public OAMSearcher(LCDControllRegister lcdControll, LCDControllYCoordinateRegister lcdYCoordinate,
			ObjectsAttributeMap oaMap)
	{
		super(MegiHertz.get(4));
		this.state = OAMState.OAM_FETCH;
		this.objectsOfLine = new ArrayList<OAMEntry>();
		this.lcdControll = lcdControll;
		this.lcdYCoordinate = lcdYCoordinate;

		this.oaMap = oaMap;
	}

	@Override
	public boolean tick()
	{
		if (super.tick())
		{
			return true;
		}

		switch (state)
		{
			case OAM_FETCH:
				fetch();
				state = OAMState.OAM_DESITE;
				break;
			case OAM_DESITE:
				search();
				state = OAMState.OAM_FETCH;
				break;
			default:
				break;
		}

		if (!oaMap.hasNext() && state == OAMState.OAM_FETCH)
		{
			state = OAMState.FINISHED;
		}

		return false;
	}

	private void fetch()
	{
		currentEntry = oaMap.next();
	}

	private void search()
	{
		if (lcdYCoordinate.loadLcdYCoordinate() >= currentEntry.positionYStart
				&& lcdYCoordinate.loadLcdYCoordinate() < currentEntry.positionYStart
						+ (lcdControll.getObjectSize() == objectSize.EIGHT_BY_EIGHT ? 8 : 16)
				&& objectsOfLine.size() < MAXIMUM_OBJECTS_PER_LINE)
		{
			objectsOfLine.add(currentEntry);
		}
	}

	public OAMState getState()
	{
		return state;
	}

	public void setState(OAMState state)
	{
		this.state = state;
	}

	public void reset()
	{
		oaMap.reset();
		objectsOfLine.clear();
		this.state = OAMState.OAM_FETCH;
	}

	public int size()
	{
		return objectsOfLine.size();
	}

	public OAMEntry getFirstObject()
	{
		objectsOfLine.sort((OAMEntry obj1, OAMEntry obj2) -> {
			return Integer.compare(obj1.positionXEnd, obj2.positionXEnd);
		});
		return objectsOfLine.get(0);
	}

	public void removeFirstObject()
	{
		objectsOfLine.remove(getFirstObject());
	}

	public boolean isEmpty()
	{
		return objectsOfLine.isEmpty();
	}
}
