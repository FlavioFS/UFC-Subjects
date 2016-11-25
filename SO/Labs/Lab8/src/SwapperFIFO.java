/**
 * Creator: Flavio Freitas de Sousa
 * Contact: flaviofreitas.h@gmail.com
 * Date: 2016/Nov/25
 */

import java.util.Locale;

/**
 * First Come, Fist Served page swapping
 *
 */
public class SwapperFIFO extends PageSwapper{

	public SwapperFIFO(Machine mach) {
		super(mach);
	}

	@Override
	public void allocate(final int[] pageList) {
		int faults = 0;
		int swapTime = 0;
		int memTime = 0;
		
		int j = 0;	// FIFO frame index
		
		for (int i=0; i<pageList.length; i++) {
			
			// Page Fault
			if (_mach.getFrameIndex(pageList[i]) == Machine.PAGE_FAULT) {
				
				// FIFO Logic
				_mach.setFrame(j, pageList[i]);
				j = (j+1) % _mach.size();
				
				// Statistics
				swapTime += PAGE_SWAP_TIME;
				faults++;
			}
			
			else {
				memTime += MEMORY_ACCESS_TIME;
			}
		}
		
		// Output
		float faultTimeRatio = 100*swapTime/(swapTime+memTime);
		System.out.println(
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "            FIFO           \n") +
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "          Faults: %2d       \n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%   \n",  faultTimeRatio)
		);
	}

}
