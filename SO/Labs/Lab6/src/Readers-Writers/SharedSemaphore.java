import java.util.concurrent.Semaphore;

public class SharedSemaphore implements ISharedResource{
	/* =====================================================================================
	 *   ATTRIBUTES
	 * ===================================================================================== */
	private static int MAX_READERS = 20;
	private int _data;
	
	// Thread Safe Mechanisms
	Semaphore nonMutexReader = new Semaphore(MAX_READERS);
	Semaphore mutexWriter = new Semaphore (1);
	
	
	/* =====================================================================================
	 *   CONSTRUCTOR
	 * ===================================================================================== */
	public SharedSemaphore (final int readerCount) {
		MAX_READERS = readerCount;
	}
	
	
	/* =====================================================================================
	 *   METHODS
	 * ===================================================================================== */
	public int read(final String id) throws InterruptedException {
		
		int rv;
		
		try {
			mutexWriter.acquire();
			nonMutexReader.acquire();
			mutexWriter.release();
			
			// Reading
			rv = _data;
			System.out.format("READER %s <<   %d\n", id, _data);
		} finally {
			nonMutexReader.release();
		}
		
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
