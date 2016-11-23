import java.util.Locale;


public class SwapperFIFO extends PageSwapper{

	public SwapperFIFO(int[] pageList) {
		super(pageList);
	}

	@Override
	public void allocate(Machine mach) {
		int faults = 0;
		int swapTime = 0;
		int memTime = 0;
		
		int j = 0;	// FIFO frame index
		
		for (int i=0; i<_pageList.length; i++) {
			
			// Page Fault
			if (mach.getFrameIndex(_pageList[i]) == Machine.PAGE_FAULT) {
				
				// FIFO Logic
				mach.setFrame(j, _pageList[i]);
				j = (j+1) % mach.size();
				
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
			String.format(Locale.ENGLISH, "          Faults: %2d\n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%\n",  faultTimeRatio)
		);
	}

}
