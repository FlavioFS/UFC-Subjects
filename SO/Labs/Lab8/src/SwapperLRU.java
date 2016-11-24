import java.util.ArrayList;
import java.util.Locale;


public class SwapperLRU extends PageSwapper{

	public SwapperLRU(int[] pageList) {
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
					// Creates a list of pages in use
					ArrayList<Integer> lastUsed = new ArrayList<Integer>();
					int pageId;
					for (int j=0; j<mach.size(); j++) {
						pageId = mach.getPage(j);
						
						// Do not copy free pages
						if (pageId != Machine.FREE_PAGE)
							lastUsed.add(pageId);
					}
					
					// Removes elements from the list one by one 
					// until there's only one or until the pageList finishes
					for (int j=i; j>0; j--) {
						
						// Removed everyone but one guy, it is the only choice 
						if (lastUsed.size() == 1)
							break;
						
						lastUsed.remove(new Integer(_pageList[j]));
					}
					
					// Victim is one of the remaining "unused" pages
					int victimPage = lastUsed.get(0);
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
			String.format(Locale.ENGLISH, "            LRU            \n") +
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "          Faults: %2d\n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%\n",  faultTimeRatio)
		);
	}

}
