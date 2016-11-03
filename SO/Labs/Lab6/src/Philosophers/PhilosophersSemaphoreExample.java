
public class PhilosophersSemaphoreExample {
	
	public static void main(String[] args) {
		final int PHILOSOPHERS_COUNT = 5;
		final String[] names = {"P0", "P1", "P2", "P3", "P4"};
		
		ChopsticksView _chView = new ChopsticksView(PHILOSOPHERS_COUNT, names);
		IChopsticks _chopsticks = new ChopsticksSemaphore(PHILOSOPHERS_COUNT, _chView);
		
		_chView.display();
		// Readers
		for (int i=0; i<PHILOSOPHERS_COUNT; i++) {
			(new Thread(new Philosopher(names[i], i, _chopsticks))).start();			
		}
	}
	
}
