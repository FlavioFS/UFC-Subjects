import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Dropbox {
	private int number;
	private boolean evenNumber = false;
	
	private boolean full = false;
	private boolean empty = true;
	
	Lock lock = new ReentrantLock();
	Condition condEven  = lock.newCondition();
	Condition condOdd   = lock.newCondition();
	Condition condNotFull = lock.newCondition();
	
	private void setEmpty (boolean value){
		this.empty = value;
		this.full = !value;
	}
	
	private boolean isEven ()
	{
		return this.evenNumber;
	}
	
	public int take(final boolean takeEven) throws InterruptedException {
		lock.lock();
		
		int rv;
		try {
			if (takeEven == true) {
				while (empty || !this.isEven()) {
					condEven.await();
				}
			}
			
			else {
				while (empty || this.isEven()) {
					condOdd.await();
				}
			}
			
			rv = this.number;
			this.setEmpty(true);
			System.out.format("%s CONSUMIDOR obtem %d.%n", takeEven ? "PAR" : "IMPAR", number);
			condNotFull.signal();
		} finally {
			lock.unlock();
		}
		
		return rv;
	}
	
	public void put(int number) throws InterruptedException {
		lock.lock();
		
		try {
			while (this.full == true)
				condNotFull.await();
			
			this.number = number;
			evenNumber = number % 2 == 0;
			
			// Signal
			if (evenNumber) condEven.signal();
			else            condOdd.signal();
			
			this.setEmpty(false);
			System.out.format("PRODUTOR gera %d.%n", number);
		} finally {
			lock.unlock();
		}
	}
}
