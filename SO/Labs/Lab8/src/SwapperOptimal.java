/**
 * Creator: Flavio Freitas de Sousa
 * Contact: flaviofreitas.h@gmail.com
 * Date: 2016/Nov/25
 */

import java.util.ArrayList;
import java.util.Locale;


/**
 * LEAST-FREQUENT-LATER page swapping
 *
 */
public class SwapperOptimal extends PageSwapper{

	public SwapperOptimal(Machine mach) {
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
					// Creates a list of pages in use
					ArrayList<Integer> lastUsed = new ArrayList<Integer>();
					int pageId;
					for (int j=0; j<_mach.size(); j++) {
						pageId = _mach.getPage(j);
						
						// Do not copy free pages
						if (pageId != Machine.FREE_PAGE)
							lastUsed.add(pageId);
					}
					
					// Removes elements from the list one by one 
					// until there's only one or until the pageList finishes
					for (int j=i+1; j<pageList.length; j++) {
						
						// Removed everyone but one guy, it is the only choice 
						if (lastUsed.size() == 1)
							break;
						
						lastUsed.remove(new Integer(pageList[j]));
					}
					
					// Victim is one of the remaining "unused" pages
					int victimPage = lastUsed.get(0);
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
			String.format(Locale.ENGLISH, "          OPTIMAL          \n") +
			String.format(Locale.ENGLISH, "===========================\n") +
			String.format(Locale.ENGLISH, "          Faults: %2d       \n", faults) +
			String.format(Locale.ENGLISH, "Fault Time Ratio: %4.2f%%   \n",  faultTimeRatio)
		);
	}

}
