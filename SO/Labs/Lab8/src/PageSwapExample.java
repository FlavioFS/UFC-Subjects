/**
 * Creator: Flavio Freitas de Sousa
 * Contact: flaviofreitas.h@gmail.com
 * Date: 2016/Nov/25
 */

import java.util.Locale;


public class PageSwapExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int MACHINE_SIZE = 3;
		Machine mach = new Machine(MACHINE_SIZE);
		
//		final int[] pageList = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};
		final int[] pageList = {1, 2, 3, 4, 2, 1, 5, 6, 2, 1, 2, 3, 7, 6, 3, 2, 1, 2, 3, 6};
		
		System.out.print(
				String.format(Locale.ENGLISH, "===========================\n") +
				String.format(Locale.ENGLISH, "       INPUT SEQUENCE      \n") +
				String.format(Locale.ENGLISH, "===========================\n"));
		System.out.print(  String.format(Locale.US, "[%d", pageList[0]) );
		for(int i=1; i<pageList.length; i++)
			System.out.print( String.format(Locale.US, ", %d", pageList[i]) );
		System.out.println("]\n\n>~~~~~~~~~~~~~~~~~~~~~~~~~<\n");
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		SwapperOptimal optimal = new SwapperOptimal (mach);
		SwapperFIFO fifo       = new SwapperFIFO (mach);
		SwapperLRU lru         = new SwapperLRU (mach);
		SwapperLFU lfu         = new SwapperLFU (mach);
		SwapperMFU mfu         = new SwapperMFU (mach);
		
		mach.reset();
		optimal.allocate(pageList);
		
		mach.reset();
		fifo.allocate(pageList);
		
		mach.reset();
		lru.allocate(pageList);
		
		mach.reset();
		lfu.allocate(pageList);
		
		mach.reset();
		mfu.allocate(pageList);
	}

}
