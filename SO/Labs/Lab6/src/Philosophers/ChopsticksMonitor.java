import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopsticksMonitor implements IChopsticks {
	private static int CHOPSTICKS_COUNT;
	
	/* =====================================================================================
	 *   ATTRIBUTES
	 * ===================================================================================== */
	private boolean[] _hashi;			// State of hashis (available or not)
	Lock lock = new ReentrantLock();
	Condition[] _hashiReturned;			// Signals when a hashi is available (true)
	ChopsticksView _chView;
	
	
	/* =====================================================================================
	 *   GETTERS & SETTERS
	 * ===================================================================================== */
	// Local Setters
	private void setHashi (boolean value, int position) { _hashi[position] = value; }
	
	// Local Getters
	private boolean isAvailable (int position) { return _hashi[position]; }
	
	
	/* =====================================================================================
	 *   CONSTRUCTOR
	 * ===================================================================================== */
	public ChopsticksMonitor (final int chopsticksCount, ChopsticksView phView) {
		CHOPSTICKS_COUNT = chopsticksCount;
		_chView = phView;
		_hashi = new boolean [CHOPSTICKS_COUNT];
		_hashiReturned = new Condition [CHOPSTICKS_COUNT];
		
		for (int i=0; i<CHOPSTICKS_COUNT; i++) {
			_hashi[i] = true;
			_hashiReturned[i] = lock.newCondition();
		}
	}
	
	
	/* =====================================================================================
	 *   METHODS
	 * ===================================================================================== */
	public void putBack (String id, final int position) throws InterruptedException {
		int
			hashi1 = position,
			hashi2 = position + 1;

		// Circular list
		if (hashi2 >= CHOPSTICKS_COUNT) hashi2 -= CHOPSTICKS_COUNT;
		
		lock.lock();
		try {
			if (!isAvailable(hashi1) && !isAvailable(hashi2)) {
				setHashi(true, hashi1);
				_chView.updateValue(hashi1, ChopsticksView.RIGHT);
				_chView.display();
				
				setHashi(true, hashi2);
				_chView.updateValue(hashi1, ChopsticksView.THINKING);
				_chView.display();
				
				_hashiReturned[hashi1].signal();
				_hashiReturned[hashi2].signal();
			}
		} finally {
			lock.unlock();
		}
		
	}
	
	public void take (String id, final int position) throws InterruptedException {
		int
			hashi1 = position,
			hashi2 = position + 1; 
		
		// Circular list
		if (hashi2 >= CHOPSTICKS_COUNT) hashi2 -= CHOPSTICKS_COUNT;
		
		_chView.updateValue(hashi1, ChopsticksView.HUNGRY);
		_chView.display();
		
		lock.lock();
		try {
			while ( !(isAvailable(hashi1) && isAvailable(hashi2)) ) {
				if (!isAvailable(hashi1))
					_hashiReturned[hashi1].await();
				
				if (!isAvailable(hashi2))
					_hashiReturned[hashi2].await();
			}
			
			// Takes hashis and eats
			setHashi(false, hashi1);
			setHashi(false, hashi2);
			_chView.updateValue(hashi1, ChopsticksView.EATING);
			_chView.display();
		} finally {
			lock.unlock();
		}
	}
}
