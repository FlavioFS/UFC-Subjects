import java.util.Random;

public class Writer implements Runnable {
	private ISharedResource _sharedResource;
	private String _name;
	
	public Writer(final String name, ISharedResource sharedResource) {
		_sharedResource = sharedResource;
		_name = name;
	}
	
	public void run() {
		Random random = new Random();
		
		// Starting Delay
		try { Thread.sleep(random.nextInt(2000)); }
		catch (InterruptedException e1) { e1.printStackTrace(); }
		
		while (true) {
			int number = random.nextInt(10);
			
			try {
				_sharedResource.write(_name, number);
				Thread.sleep(random.nextInt(3000));
			} catch (InterruptedException e) { }
		}
	}
}