import java.util.Locale;

public class Process
{	
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	private String _id;			// Process name
	private int _A, _B, _C;		// Resources in use
	private int _nA, _nB, _nC;	// Resources needed
	private int _rA, _rB, _rC;	// Resources requested
	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public Process (String id) { _id = id; }
	
	// Copy Constructor
	public Process (Process proc)
	{
		_id = proc.getID();
		setAciveResources(proc.getA(), proc.getB(), proc.getC());
		setNeededResources(proc.getNeedA(), proc.getNeedB(), proc.getNeedC());
		setRequestedResources(proc.getRequestA(), proc.getRequestB(), proc.getRequestC());
	}
	
	/* ==============================================================================
	 *  Setters
	 * ============================================================================== */
	public void setAciveResources (int A, int B, int C){
		// Active resources
		_A  = A;
		_B  = B;
		_C  = C;
	}
	
	public void setNeededResources (int nA, int nB, int nC) {
		// Max needed resources
		_nA = nA;
		_nB = nB;
		_nC = nC;
	}
	
	public void setRequestedResources (int rA, int rB, int rC) {
		// Requested resources
		_rA = rA;
		_rB = rB;
		_rC = rC;
	}
	
	
	/* ==============================================================================
	 *  Getters
	 * ============================================================================== */
	public String getID() { return _id; }
	
	public int getA()     { return _A;  }
	public int getB()     { return _B;  }
	public int getC()     { return _C;  }
	
	public int getNeedA() { return _nA; }
	public int getNeedB() { return _nB; }
	public int getNeedC() { return _nC; }
	
	public int getRequestA() { return _rA; }
	public int getRequestB() { return _rB; }
	public int getRequestC() { return _rC; }

	
	/* ==============================================================================
	 *  Methods
	 * ============================================================================== */
	public void receive (int dA, int dB, int dC)
	{
		_A += dA;
		_B += dB;
		_C += dC;
	}
	
	public void free ()
	{
		_A = 0;
		_B = 0;
		_C = 0;
	}
	
	/* ==============================================================================
	 *  OUT
	 * ============================================================================== */
	public String toStringBeautified()
	{
		return "Process\n"                   +
			   "   └─ ID      Arrival  Priority\n      " +
			   String.format(Locale.US, "%-6s  ", getID()) +
			   String.format(Locale.US, "%-2d  ", getA()) +
			   String.format(Locale.US, "%-2d  ", getB()) +
			   String.format(Locale.US, "%-2d  ", getC()) +
			   String.format(Locale.US, "%-2d  ", getNeedA()) +
			   String.format(Locale.US, "%-2d  ", getNeedB()) +
			   String.format(Locale.US, "%-2d  ", getNeedC());
	}
	
	@Override
	public String toString ()
	{
		return "Process(ID: " + getID() +
		       ", A: "  + String.valueOf(getA()) +
		       ", B: "  + String.valueOf(getB()) +
		       ", C: "  + String.valueOf(getC()) +
		       ", nA: " + String.valueOf(getNeedA()) +
		       ", nB: " + String.valueOf(getNeedB()) + 
		       ", nC: " + String.valueOf(getNeedC()) + ")";
	}
}