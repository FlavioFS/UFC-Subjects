
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
		if (i >= 0 && i < size())
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
}
