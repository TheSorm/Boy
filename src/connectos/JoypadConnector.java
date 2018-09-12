package connectos;

public class JoypadConnector
{
	private boolean downIsPressed;
	private boolean upIsPressed;
	private boolean leftIsPressed;
	private boolean rightIsPressed;
	private boolean startIsPressed;
	private boolean selectIsPressed;
	private boolean aIsPressed;
	private boolean bIsPressed;

	public boolean isDownPressed()
	{
		return downIsPressed;
	}

	public void setDownPressed(boolean downIsPressed)
	{
		this.downIsPressed = downIsPressed;
	}

	public boolean isUpPressed()
	{
		return upIsPressed;
	}

	public void setUpPressed(boolean upIsPressed)
	{
		this.upIsPressed = upIsPressed;
	}

	public boolean isLeftPressed()
	{
		return leftIsPressed;
	}

	public void setLeftPressed(boolean leftIsPressed)
	{
		this.leftIsPressed = leftIsPressed;
	}

	public boolean isRightPressed()
	{
		return rightIsPressed;
	}

	public void setRightPressed(boolean rightIsPressed)
	{
		this.rightIsPressed = rightIsPressed;
	}

	public boolean isStartPressed()
	{
		return startIsPressed;
	}

	public void setStartPressed(boolean startIsPressed)
	{
		this.startIsPressed = startIsPressed;
	}

	public boolean isSelectPressed()
	{
		return selectIsPressed;
	}

	public void setSelectPressed(boolean selectIsPressed)
	{
		this.selectIsPressed = selectIsPressed;
	}

	public boolean isaPressed()
	{
		return aIsPressed;
	}

	public void setaPressed(boolean aIsPressed)
	{
		this.aIsPressed = aIsPressed;
	}

	public boolean isbPressed()
	{
		return bIsPressed;
	}

	public void setbPressed(boolean bIsPressed)
	{
		this.bIsPressed = bIsPressed;
	}

}
