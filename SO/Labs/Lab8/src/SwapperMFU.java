import java.util.Locale;

/**
 * LEAST-FREQUENTLY-USED page swapping
 *
 */
public class SwapperMFU extends PageSwapper{

	public SwapperMFU(int[] pageList) {
		super(pageList);
	}

	@Override
	public void allocate(Machine mach) {
		int faults = 0;
		int swapTime = 0;
		int memTime = 0;
		
		int requestedPage = Machine.FREE_PAGE;
		int requestedFrame = Machine.PAGE_FAULT;
		
		for (int i=0; i<_pageList.length; i++) {
			
			requestedPage = _pageList[i];
			requestedFrame = mach.getFrameIndex(requestedPage);
			
			// Page Fault
			if (pageFault(requestedFrame)) {
				
				// Searches for free page
				requestedFrame = mach.getFreeFrame();
				
				// Got it
				if (!pageFault(requestedFrame))
					mach.setFrame(requestedFrame, requestedPage);
				
				// No free page => Get a victim
				else {
					// Gets the list of active pages
					int[] activePages = mach.activePages();
					
					// Creates a frequency list
					int[] frequencyList = new int [activePages.length];
					for (int j=0; j <frequencyList.length; j++)
						frequencyList[j] = 0;
						
					// Through all active pages...
					for (int j=0; j<activePages.length; j++) {
						
						// ...counts its frequency
						for (int k=0; k<=i; k++)
							if (_pageList[k] == activePages[j])
								frequencyList[j]++;
					}
					
					
					// Victim is the least frequent one (before this moment)
					int maximum = 0;
					for (int j=1; j <frequencyList.length; j++)
						if (frequencyList[j] > frequencyList[maximum])
							maximum = j;
						
					int victimPage = activePages[maximum];
					mach.setFrame(mach.getFrameIndex(victimPage), requestedPage);
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
			String.format(Locale.ENGLISH, "            MFU            \n") +
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "          Faults: %2d\n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%\n",  faultTimeRatio)
		);
	}

}
