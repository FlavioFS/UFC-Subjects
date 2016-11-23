
public abstract class PageSwapper {
	protected final int MEMORY_ACCESS_TIME = 200;
	protected final int PAGE_SWAP_TIME = 2000;
	
	protected int[] _pageList;
	
	
	/* ===========================================================
	 *   Setters
	 * ===========================================================
	 */
	public void setPageList (final int[] pageList) {
		_pageList = pageList;
	}
	
	
	/* ===========================================================
	 *   Constructor
	 * ===========================================================
	 */
	public PageSwapper (final int[] pageList) {
		setPageList(pageList);
	}
	
	
	/* ===========================================================
	 *   Methods
	 * ===========================================================
	 */
	public abstract void allocate (Machine mach);
	
	protected boolean pageFault (final int requestedFrame) {
		return requestedFrame == Machine.PAGE_FAULT;
	}
}