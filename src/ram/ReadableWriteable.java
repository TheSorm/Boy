package ram;

public interface ReadableWriteable
{
	public byte load(int adress);

	public void put(int adress, byte input);
}
