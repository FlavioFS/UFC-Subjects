public class ReaderWriterSemaphoreExample {
	
	public static void main(String[] args) {
		SharedSemaphore sharedResource = new SharedSemaphore(20);
		
		// Readers
		(new Thread(new Reader("R1", sharedResource))).start();
		(new Thread(new Reader("R2", sharedResource))).start();
		(new Thread(new Reader("R3", sharedResource))).start();
		(new Thread(new Reader("R4", sharedResource))).start();
		(new Thread(new Reader("R5", sharedResource))).start();
		(new Thread(new Reader("R6", sharedResource))).start();
		
		(new Thread(new Writer("W1", sharedResource))).start();
		(new Thread(new Writer("W2", sharedResource))).start();
		(new Thread(new Writer("W3", sharedResource))).start();
	}
}