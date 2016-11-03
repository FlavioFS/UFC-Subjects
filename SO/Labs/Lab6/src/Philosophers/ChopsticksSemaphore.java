import java.util.concurrent.Semaphore;

public class ChopsticksSemaphore implements IChopsticks {
	private static int CHOPSTICKS_COUNT;
	
	/* =====================================================================================
	 *   ATTRIBUTES
	 * ===================================================================================== */
	private boolean[] _hashi;	// State of hashis (available or not)
	Semaphore[] _mutexHashi;	// One Semaphore for each hashi
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
	public ChopsticksSemaphore (final int chopsticksCount, ChopsticksView chView) {
		CHOPSTICKS_COUNT = chopsticksCount;
		_chView = chView;
		_hashi = new boolean [CHOPSTICKS_COUNT];
		_mutexHashi = new Semaphore [CHOPSTICKS_COUNT];
		
		for (int i=0; i<CHOPSTICKS_COUNT; i++) {
			_hashi[i] = true;
			_mutexHashi[i] = new Semaphore(1);
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
		
		// Returns hashis
		if (!isAvailable(hashi1) && !isAvailable(hashi2)) {
			setHashi(true, hashi1);
			_chView.updateValue(hashi1, ChopsticksView.RIGHT);
			_chView.display();
			
			setHashi(true, hashi2);
			_chView.updateValue(hashi1, ChopsticksView.THINKING);
			_chView.display();
			
			_mutexHashi[hashi1].release();
			_mutexHashi[hashi2].release();
		}
	}
	
	public void take (String id, final int position) throws InterruptedException {
		_chView.updateValue(position, ChopsticksView.HUNGRY);
		_chView.display();
		
		int
			hashi1 = position,
			hashi2 = position + 1; 
		
		// Circular list
		if (hashi2 >= CHOPSTICKS_COUNT) hashi2 -= CHOPSTICKS_COUNT;
		
		// Attempts to take hashis
		_mutexHashi[hashi1].acquire();
		_chView.updateValue(hashi1, ChopsticksView.LEFT);
		_chView.display();
		
		_mutexHashi[hashi2].acquire();
		_chView.updateValue(hashi1, ChopsticksView.EATING);
		_chView.display();
		
		// Eating
		setHashi(false, hashi1);
		setHashi(false, hashi2);
	}
}
