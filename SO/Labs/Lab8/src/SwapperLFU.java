import java.util.Locale;

/**
 * LEAST-FREQUENTLY-USED page swapping
 *
 */
public class SwapperLFU extends PageSwapper{

	public SwapperLFU(Machine mach) {
		super(mach);
	}

	@Override
	public void allocate(final int[] pageList) {
		int faults = 0;
		int swapTime = 0;
		int memTime = 0;
		
		int requestedPage = Machine.FREE_PAGE;
		int requestedFrame = Machine.PAGE_FAULT;
		
		for (int i=0; i<pageList.length; i++) {
			
			requestedPage = pageList[i];
			requestedFrame = _mach.getFrameIndex(requestedPage);
			
			// Page Fault
			if (pageFault(requestedFrame)) {
				
				// Searches for free page
				requestedFrame = _mach.getFreeFrame();
				
				// Got it
				if (!pageFault(requestedFrame))
					_mach.setFrame(requestedFrame, requestedPage);
				
				// No free page => Get a victim
				else {
					// Gets the list of active pages
					int[] activePages = _mach.activePages();
					
					// Creates a frequency list
					int[] frequencyList = new int [activePages.length];
					for (int j=0; j <frequencyList.length; j++)
						frequencyList[j] = 0;
						
					// Through all active pages...
					for (int j=0; j<activePages.length; j++) {
						
						// ...counts its frequency
						for (int k=0; k<=i; k++)
							if (pageList[k] == activePages[j])
								frequencyList[j]++;
					}
					
					
					// Victim is the least frequent one (before this moment)
					int minimum = 0;
					for (int j=1; j <frequencyList.length; j++)
						if (frequencyList[j] < frequencyList[minimum])
							minimum = j;
						
					int victimPage = activePages[minimum];
					_mach.setFrame(_mach.getFrameIndex(victimPage), requestedPage);
				}
				
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
			String.format(Locale.ENGLISH, "            LFU            \n") +
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "          Faults: %2d       \n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%   \n",  faultTimeRatio)
		);
	}

}
