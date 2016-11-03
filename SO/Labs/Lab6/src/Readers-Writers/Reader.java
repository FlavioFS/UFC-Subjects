import java.util.Random;

public class Reader implements Runnable {
	private final ISharedResource _sharedResource;
	private String _name;
	
	public Reader(final String name, ISharedResource sharedResource) {
		_sharedResource = sharedResource;
		_name = name;
	}

	public void run() {
		Random random = new Random();
		
		// Starting Delay
		try { Thread.sleep(random.nextInt(2000)); }
		catch (InterruptedException e1) { e1.printStackTrace(); }
		
		while (true) {
			try {
				_sharedResource.read(_name);
				Thread.sleep(random.nextInt(6000));
			}
			catch (InterruptedException e1) { e1.printStackTrace(); }
		}
	}
}