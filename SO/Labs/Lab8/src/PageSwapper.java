/**
 * Creator: Flavio Freitas de Sousa
 * Contact: flaviofreitas.h@gmail.com
 * Date: 2016/Nov/25
 */

public abstract class PageSwapper {
	protected final int MEMORY_ACCESS_TIME = 200;
	protected final int PAGE_SWAP_TIME = 2000;
	
	protected Machine _mach;
	
	
	/* ===========================================================
	 *   Setters
	 * ===========================================================
	 */
	public void setMachine (Machine mach) {
		_mach = mach;
	}
	
	
	/* ===========================================================
	 *   Constructors
	 * ===========================================================
	 */
	public PageSwapper (Machine mach) {
		setMachine(mach);
	}
	
	
	/* ===========================================================
	 *   Methods
	 * ===========================================================
	 */
	public abstract void allocate (final int[] pageList);
	
	protected boolean pageFault (final int requestedFrame) {
		return requestedFrame == Machine.PAGE_FAULT;
	}
}