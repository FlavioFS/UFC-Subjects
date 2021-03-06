import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SharedMonitor implements ISharedResource {
	private int _data;
	
	private boolean _writing = false;
	private boolean _reading = true;
	
	// Thread Safe Mechanisms
	Lock lock = new ReentrantLock();
	Condition condNotWriting  = lock.newCondition();
	Condition condNotReading  = lock.newCondition();
	
	// Local Setters
	private void setWriting (boolean value) { _writing = value; }
	private void setReading (boolean value) { _reading = value; }
	
	// Local Getters
	private boolean isWriting () { return _writing; }
	private boolean isReading () { return _reading; }
	
	
	public int read(String id) throws InterruptedException {
		lock.lock();
		
		int rv;
		try {
			while (isWriting()) {
				condNotWriting.await();
			}
			
			// Reading
			this.setReading(true);
			rv = _data;
			System.out.format("READER %s <<   %d\n", id, _data);
			
			// Signal
			this.setReading(false);
			condNotReading.signal();
			
		} finally {
			lock.unlock();
		}
		
		return rv;
	}
	
	public void write(String id, int data) throws InterruptedException {
		lock.lock();
		
		try {
			while (isReading() || isWriting()) {
				if (isReading())
					condNotReading.await();
				
				if (isWriting())
					condNotWriting.await();
			}
			
			// Writing
			setWriting(true);
			_data = data;
			System.out.format("WRITER %s   >> %d\n", id, _data);
			
			// Signal
			setWriting(false);
			condNotWriting.signal();
			
		} finally {
			lock.unlock();
		}
	}
}
