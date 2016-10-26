import java.util.Locale;

public class Process
{	
	final static int RESOURCE_TYPES = 3;
	
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	private String _id;		// Process name
	private int[] _alloc;	// Resources in use
	private int[] _need;	// Resources needed
	private int[] _max;	// Resources needed
	private int[] _req;	// Resources requested
	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public Process (String id) {
		_id = id;
		_alloc = new int [RESOURCE_TYPES];
		_need  = new int [RESOURCE_TYPES];
		_max   = new int [RESOURCE_TYPES];
		_req   = new int [RESOURCE_TYPES];
	}
	
	// Copy Constructor
	public Process (Process proc)
	{
		this(proc.getID());
		
		setAlloc(proc.getAlloc(0), proc.getAlloc(1), proc.getAlloc(2));
		setNeed	( proc.getNeed(0),  proc.getNeed(1),  proc.getNeed(2));
		setMax	(  proc.getMax(0),   proc.getMax(1),   proc.getMax(2));
		setReq	(  proc.getReq(0),   proc.getReq(1),   proc.getReq(2));
	}
	
	/* ==============================================================================
	 *  Setters
	 * ============================================================================== */
	public void setAlloc (int A, int B, int C){
		// Active resources
		_alloc[0]  = A;
		_alloc[1]  = B;
		_alloc[2]  = C;
	}
	
	public void setNeed (int nA, int nB, int nC) {
		// Current needed resources
		_need[0] = nA;
		_need[1] = nB;
		_need[2] = nC;
	}
	
	public void setMax (int mA, int mB, int mC) {
		// Max needed resources
		_max[0] = mA;
		_max[1] = mB;
		_max[2] = mC;
	}
	
	public void setReq (int rA, int rB, int rC) {
		// Requested resources
		_req[0] = rA;
		_req[1] = rB;
		_req[2] = rC;
	}
	
	
	/* ==============================================================================
	 *  Getters
	 * ============================================================================== */
	public String getID() { return _id; }
	
	public int getAlloc(int i)	{ return _alloc[i];  }
	public int getNeed(int i)	{ return _need[i]; }
	public int getMax(int i)	{ return _max[i]; }
	public int getReq(int i)	{ return _req[i]; }

	
	/* ==============================================================================
	 *  Methods
	 * ============================================================================== */
	public void receive (int dA, int dB, int dC)
	{
		setAlloc(getAlloc(0)+dA, getAlloc(1)+dB, getAlloc(2)+dC);
		setNeed(getNeed(0)-dA, getNeed(1)-dB, getNeed(2)-dC);
	}
	
	public void free ()
	{
		setAlloc(0, 0, 0);
	}
	
	/* ==============================================================================
	 *  OUT
	 * ============================================================================== */
	public String toStringBeautified()
	{
		return "Process\n"                   +
			   "   └─ ID      Arrival  Priority\n      " +
			   String.format(Locale.US, "%-6s  ", getID()) +
			   String.format(Locale.US, "%-2d  ", getAlloc(0)) +
			   String.format(Locale.US, "%-2d  ", getAlloc(1)) +
			   String.format(Locale.US, "%-2d  ", getAlloc(2)) +
			   String.format(Locale.US, "%-2d  ", getNeed(0)) +
			   String.format(Locale.US, "%-2d  ", getNeed(1)) +
			   String.format(Locale.US, "%-2d  ", getNeed(2));
	}
	
	@Override
	public String toString ()
	{
		return "Process(ID: " + getID() +
		       ", A: "  + String.valueOf(getAlloc(0)) +
		       ", B: "  + String.valueOf(getAlloc(1)) +
		       ", C: "  + String.valueOf(getAlloc(2)) +
		       ", nA: " + String.valueOf(getNeed(0)) +
		       ", nB: " + String.valueOf(getNeed(1)) + 
		       ", nC: " + String.valueOf(getNeed(2)) + ")";
	}
}