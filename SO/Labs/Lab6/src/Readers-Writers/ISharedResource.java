
public interface ISharedResource {
	public int read (final String id) throws InterruptedException;
	public void write (final String id, final int data) throws InterruptedException;
}
