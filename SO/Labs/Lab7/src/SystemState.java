import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SystemState {
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	private ArrayList<Process> _unfinished;
	private ArrayList<Process> _finished;
	private int _A, _B, _C;		// Available system resources

	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public SystemState (String filepath) throws IOException
	{
		_unfinished = new ArrayList<Process> ();
		_finished = new ArrayList<Process> ();
		this.loadState(filepath);
	}

	
	/* ==============================================================================
	 *  Getters
	 * ============================================================================== */
	public ArrayList<Process> unfinished () { return _unfinished; }
	public ArrayList<Process> finished ()   { return _finished; }
	
	public int getA() { return _A; }
	public int getB() { return _B; }
	public int getC() { return _C; }
	
	
	/* ==============================================================================
	 *  Methods
	 * ============================================================================== */
	public boolean give (int dA, int dB, int dC)
	{
		// Resources available
		_A -= dA;
		_B -= dB;
		_C -= dC;
		return true;
	}
	
	public void free (Process P)
	{
		_A += P.getA();
		_B += P.getB();
		_C += P.getC();
		P.free();
	}
	
	public void finish (int index)
	{
		Process _P = _unfinished.remove(index);
		_finished.add(_P);
		this.free(_P);
	}
	
	public boolean isLastTurn (int index)
	{
		Process p = _unfinished.get(index);
		return 	_A >= p.getNeedA() &&
				_B >= p.getNeedB() &&
				_C >= p.getNeedC();
	}
	
	public boolean isExceededRequest (Process proc)
	{
		// If the Process exceeds its maximum requests
		if ( proc.getRequestA() > proc.getNeedA() ||
			 proc.getRequestB() > proc.getNeedB() ||
			 proc.getRequestC() > proc.getNeedC())
			return true;
		
		return false;
	}
	
	public boolean isUnfeasibleRequest (Process proc)
	{
		// If the system cannot attend this Process now
		if ( proc.getRequestA() > proc.getNeedA() ||
			 proc.getRequestB() > proc.getNeedB() ||
			 proc.getRequestC() > proc.getNeedC())
			return true;
		
		return false;
	}
	
	/* ==============================================================================
	 *  IN
	 * ============================================================================== */
	public void loadState (String filepath) throws IOException
	{
		BufferedReader br = new BufferedReader (new FileReader(filepath));

		try
		{
			String line = br.readLine();
			String[] splitLine;
			
			// Skips first comments
			while (line == null || line.charAt(0) == '#')
			{
				line = br.readLine();
				continue;
			}
			
			// SYSTEM RESOURCES (first line)
			splitLine = line.split(", ");
			_A = Integer.parseInt(splitLine[0]);
			_B = Integer.parseInt(splitLine[1]);
			_C = Integer.parseInt(splitLine[2]);
			
			// PROCESS RESOURCES (remaining lines)
			String processID;	// Process name
			int A, B, C;		// Resources in use
			int nA, nB, nC;		// Resources needed
			line = br.readLine();
			Process proc;
			while (line != null)
			{
				if (line.charAt(0) == '#')
				{
					line = br.readLine();					
					continue;
				}
				
				splitLine = line.split(", ");
				
				// Parses line
				processID   = splitLine[0];
				
				// Holding
				A = Integer.parseInt(splitLine[1]);
				B = Integer.parseInt(splitLine[2]);
				C = Integer.parseInt(splitLine[3]);
				
				// Needed
				nA = Integer.parseInt(splitLine[4]);
				nB = Integer.parseInt(splitLine[5]);
				nC = Integer.parseInt(splitLine[6]);
				
				
				// Adds new Process to list
				proc = new Process (processID);
				proc.setAciveResources(A, B, C);
				proc.setNeededResources(nA, nB, nC);
				_unfinished.add(proc);

				line = br.readLine();
			};
		}
		finally { br.close(); }
	}
	
	public Process loadRequest (String filepath) throws IOException
	{
		BufferedReader br = new BufferedReader (new FileReader(filepath));
		Process rv = null;
		
		try
		{
			String line = br.readLine();
			String[] splitLine = line.split(", ");
			
			// Skips first comments
			while (line == null || line.charAt(0) == '#')
			{
				line = br.readLine();
				continue;
			}
			
			// PROCESS RESOURCES (remaining lines)
			int rA, rB, rC;		// Resources in use
			splitLine = line.split(", ");
			
			// Parses line
			String processID = splitLine[0];
			
			// Requested resources
			rA = Integer.parseInt(splitLine[1]);
			rB = Integer.parseInt(splitLine[2]);
			rC = Integer.parseInt(splitLine[3]);
			
			// Sets request to Process
			for (Process proc : _unfinished)
			{
				if (proc.getID().equals(processID)) {
					proc.setRequestedResources(rA, rB, rC);
					rv = proc;
					break;
				}
			}
		}
		finally { br.close(); }
		
		return rv;
	}
	
	/* ==============================================================================
	 *  OUT
	 * ============================================================================== */
	public void printFinished()
	{
		System.out.print("[" + _finished.get(0).getID());
		for (int i = 1; i < _finished.size(); i++)
			System.out.print(", " + _finished.get(i).getID());
		System.out.println("]");
	}
}
