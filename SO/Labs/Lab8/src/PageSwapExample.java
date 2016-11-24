
public class PageSwapExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int MACHINE_SIZE = 3;
		Machine mach = new Machine(MACHINE_SIZE);
		
		final int[] pageList = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};
		
		SwapperOptimal optimal = new SwapperOptimal (pageList);
		optimal.allocate(mach);
		
		mach.reset();
		SwapperFIFO fifo = new SwapperFIFO (pageList);
		fifo.allocate(mach);
		
		mach.reset();
		SwapperLRU lru = new SwapperLRU (pageList);
		lru.allocate(mach);
//		
//		mach.reset();
//		SwapperLFU lfu = new SwapperLFU (pageList);
//		lfu.allocate(mach);
//		
//		mach.reset();
//		SwapperMFU mfu = new SwapperMFU (pageList);
//		mfu.allocate(mach);
	}

}
