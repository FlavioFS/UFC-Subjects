import java.util.Locale;

public class Process
{	
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	private String _id;			// Process name
	private int _A, _B, _C;		// Resources in use
	private int _nA, _nB, _nC;	// Resources needed
	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public Process (String id, int A, int B, int C, int nA, int nB, int nC)
	{
		_id = id;
		_A  = A;
		_B  = B;
		_C  = C;
		_nA = A;
		_nB = B;
		_nC = C;
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