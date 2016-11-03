import java.util.concurrent.Semaphore;

public class SharedSemaphore implements ISharedResource{
	private int _data;
	
	// Thread Safe Mechanisms
	Semaphore mutexWriter = new Semaphore (1);
	
	public int read(final String id) {
		
		int rv;
		
		// Reading
		rv = _data;
		System.out.format("READER %s <<   %d\n", id, _data);
		
		return rv;
	}
	
	public void write(final String id, int data) throws InterruptedException {
		try {
			mutexWriter.acquire();
			
			// Writing
			_data = data;
			System.out.format("WRITER %s   >> %d\n", id, _data);
			
			
		} finally {
			mutexWriter.release();
		}
	}
}