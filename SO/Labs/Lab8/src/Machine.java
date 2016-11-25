/**
 * Creator: Flavio Freitas de Sousa
 * Contact: flaviofreitas.h@gmail.com
 * Date: 2016/Nov/25
 */

import java.util.ArrayList;

public class Machine {
	public static final int FREE_PAGE = -420;
	public static final int PAGE_FAULT = -8008;
	public static final int INDEX_ERROR = -9001;
	
	private int[] _frames;
	
	
	/* ===========================================================
	 *   Getters
	 * ===========================================================
	 */
	public int size () {
		return _frames.length;
	}
	
	public int getPage (final int frameIndex) {
		if (frameIndex >= 0 && frameIndex < size())
			return _frames[frameIndex];
		
		return INDEX_ERROR;
	}
	
	/* ===========================================================
	 *   Setters
	 * ===========================================================
	 */
	public void setFrame (final int i, final int pageId) {
		
		// Invalid index
		if (i < 0 || i >= size())
			return;
		
		// Repeated page
		if (pageId != FREE_PAGE)
			for (int j=0; j<size(); j++)
				if (_frames[j] == pageId)
					return;

		_frames[i] = pageId;
	}
	
	
	/* ===========================================================
	 *   Constructor
	 * ===========================================================
	 */
	public Machine (final int machineSize) {
		_frames = new int [machineSize];
		
		for (int i=0; i<size(); i++)
			freeFrame(i);
	}
	
	
	/* ===========================================================
	 *   Methods
	 * ===========================================================
	 */
	public void freeFrame (final int i) {
		setFrame(i, FREE_PAGE);
	}
	
	public void reset () {
		for (int i=0; i<size(); i++)
			freeFrame(i);
	}
	
	public int getFrameIndex (final int pageId) {
		for (int i=0; i<size(); i++) {
			if (getPage(i) == pageId)
				return i;
		}
		
		return PAGE_FAULT;
	}
	
	public int getFreeFrame () {
		for (int i=0; i<size(); i++) {
			if (getPage(i) == FREE_PAGE)
				return i;
		}
		
		return PAGE_FAULT;
	}
	
	public int[] activePages () {
		ArrayList<Integer> pageList = new ArrayList<Integer>();
		
		int newPage;
		for (int i=0; i<size(); i++) {
			newPage = getPage(i);
			
			// Discards free pages
			if (newPage != FREE_PAGE)
				pageList.add(new Integer (newPage));
		}
		
		int [] iList = new int [pageList.size()];
		
		// Casting to Integer
		for (int i=0; i<iList.length; i++)
			iList[i] = pageList.get(i).intValue();
		
		return iList;
	}
}
