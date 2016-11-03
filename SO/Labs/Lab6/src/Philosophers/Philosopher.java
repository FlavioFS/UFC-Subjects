import java.util.Random;

public class Philosopher extends Thread {
	
	/* =====================================================================================
	 *   ATTRIBUTES
	 * ===================================================================================== */
	private final String _name;
	private final int _position;
	private IChopsticks _chopsticks;
//	private ChopsticksView _phView;
	
	
	/* =====================================================================================
	 *   CONSTRUCTOR
	 * ===================================================================================== */
	public Philosopher (final String name, final int position, IChopsticks chopsticks) {
		_name = name;
		_position = position;
		_chopsticks = chopsticks;
//		_phView = phView;
	}
	
	
	/* =====================================================================================
	 *   GETTERS & SETTERS
	 * ===================================================================================== */
	public final String name  () { return _name; }
	public final int position () { return _position; }
	
	/* =====================================================================================
	 *   METHODS
	 * ===================================================================================== */
	public void run () {
		Random random = new Random();
		
		// Starting Delay
		try { Thread.sleep(random.nextInt(6000)); }
		catch (InterruptedException e1) { e1.printStackTrace(); }
		
		while (true) {
			
			try {
//				_phView.updateValue(_position, ChopsticksView.HUNGRY);
//				_phView.display();
				_chopsticks.take(_name, _position);				// Take hashis
				
//				_phView.updateValue(_position, ChopsticksView.EATING);
//				_phView.display();
				Thread.sleep(random.nextInt(9000));				// Eats...
				
				_chopsticks.putBack(_name, _position);			// Return hashis
				
//				_phView.updateValue(_position, ChopsticksView.THINKING);
//				_phView.display();
				Thread.sleep(random.nextInt(9000));				// Thinks...
				
			} catch (InterruptedException e) { }
		}
	}
}
