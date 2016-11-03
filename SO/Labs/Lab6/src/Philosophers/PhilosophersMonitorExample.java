
public class PhilosophersMonitorExample {
	
	public static void main(String[] args) {
		final int PHILOSOPHERS_COUNT = 5;
		final String[] names = {"P0", "P1", "P2", "P3", "P4"};
		final boolean isJumper = (args.length == 0);
		
		ChopsticksView _chView = new ChopsticksView(PHILOSOPHERS_COUNT, names, isJumper);
		IChopsticks _chopsticks = new ChopsticksMonitor(PHILOSOPHERS_COUNT, _chView);
		
		_chView.display();
		// Readers
		for (int i=0; i<PHILOSOPHERS_COUNT; i++) {
			(new Thread(new Philosopher(names[i], i, _chopsticks))).start();
		}
	}
	
}
