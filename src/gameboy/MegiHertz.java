package gameboy;

public final class MegiHertz
{
	private static final int _1 = 1_048_576;

	private MegiHertz()
	{
	}

	public static int get(int number)
	{
		return _1 * number;
	}

}
