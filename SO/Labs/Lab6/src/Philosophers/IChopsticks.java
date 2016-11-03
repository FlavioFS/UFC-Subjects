
public interface IChopsticks {
	public void take (final String name, final int position)  throws InterruptedException;
	public void putBack (final String name, final int position)  throws InterruptedException;
}
