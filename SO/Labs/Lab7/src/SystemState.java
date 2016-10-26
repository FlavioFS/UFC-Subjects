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
	private int[] _avail;		// Available system resources

	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public SystemState (String filepath) throws IOException
	{
		_avail = new int [Process.RESOURCE_TYPES];
		_unfinished = new ArrayList<Process> ();
		_finished = new ArrayList<Process> ();
		this.loadState(filepath);
	}
	
	// Copy Constructor
	public SystemState (SystemState state)
	{
		_avail = new int [Process.RESOURCE_TYPES];
		_unfinished = new ArrayList<Process> ();
		_finished = new ArrayList<Process> ();
		
		for (Process proc : state.unfinished())
			_unfinished.add(new Process(proc));
		
		for (Process proc : state.finished())
			_finished.add(new Process(proc));
		
		setAvail(state.getAvail(0), state.getAvail(1), state.getAvail(2));
	}

	
	/* ==============================================================================
	 *  Getters
	 * ============================================================================== */
	public ArrayList<Process> unfinished () { return _unfinished; }
	public ArrayList<Process> finished ()   { return _finished; }
	
	public int getAvail(int i) { return _avail[i]; }
	
	/* ==============================================================================
	 *  Setters
	 * ============================================================================== */
	public void setAvail (int A, int B, int C)
	{
		_avail[0] = A;
		_avail[1] = B;
		_avail[2] = C;
	}
	
	/* ==============================================================================
	 *  Methods
	 * ============================================================================== */
	public void increase (int dA, int dB, int dC) {
		setAvail(getAvail(0)+dA, getAvail(1)+dB, getAvail(2)+dC);
	}
	
	public void decrease (int dA, int dB, int dC) {
		increase(-dA, -dB, -dC);
	}
	
	public boolean give (String processID, int dA, int dB, int dC)
	{
		for (Process proc : unfinished())
		{
			if (proc.getID().equals(processID))
			{
				proc.receive(dA, dB, dC);
				
				// Subtracts from available resources
				decrease(dA, dB, dC);
				return true;
			}
		}
		
		return false;
	}
	
	public void free (Process P)
	{
		increase(P.getAlloc(0), P.getAlloc(1), P.getAlloc(2));
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
		return 	getAvail(0) >= p.getNeed(0) &&
				getAvail(1) >= p.getNeed(1) &&
				getAvail(2) >= p.getNeed(2);
	}
	
	public boolean isExceededRequest (Process proc)
	{
		// If the Process exceeds its maximum requests
		if ( proc.getReq(0) > proc.getNeed(0) ||
			 proc.getReq(1) > proc.getNeed(1) ||
			 proc.getReq(2) > proc.getNeed(2))
			return true;
		
		return false;
	}
	
	public boolean isUnfeasibleRequest (Process proc)
	{
		// If the system cannot attend this Process now
		if ( proc.getReq(0) > proc.getNeed(0) ||
			 proc.getReq(1) > proc.getNeed(1) ||
			 proc.getReq(2) > proc.getNeed(2))
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
			while (line.equals("") || line.charAt(0) == '#')
			{
				line = br.readLine();
				continue;
			}
			
			int a, b, c;	// Reading these from files
			
			// SYSTEM RESOURCES (first line)
			splitLine = line.split(", ");
			a = Integer.parseInt(splitLine[0]);
			b = Integer.parseInt(splitLine[1]);
			c = Integer.parseInt(splitLine[2]);
			setAvail(a, b, c);
			
			// PROCESS RESOURCES (remaining lines)
			String processID;	// Process name
			line = br.readLine();
			Process proc;
			while (line != null)
			{
				if (line.equals("") || line.charAt(0) == '#')
				{
					line = br.readLine();
					continue;
				}
				
				splitLine = line.split(", ");
				
				// Parses line
				processID = splitLine[0];		// ID
				proc = new Process(processID);	// Creates new process
				
				for (int i = 0; i < 3; i ++) {
					line = br.readLine();
					splitLine = line.split(", ");
					
					// Reads 3 values
					a = Integer.parseInt(splitLine[1]);
					b = Integer.parseInt(splitLine[2]);
					c = Integer.parseInt(splitLine[3]);
					
					// Decides wether values are for Alloc, Max or Needed
					if		(splitLine[0].equals("alloc"))	proc.setAlloc(a, b, c);
					else if	(splitLine[0].equals("max"))	proc.setMax(a, b, c);
					else if	(splitLine[0].equals("need"))	proc.setNeed(a, b, c);
				}
				
				_unfinished.add(proc);	// Adds new Process to list

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
			while (line.equals("") || line.charAt(0) == '#')
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
					proc.setReq(rA, rB, rC);
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
		if (_finished.isEmpty())
		{
			System.out.print("[]");
			return;
		}
			
		System.out.print("[" + _finished.get(0).getID());
		for (int i = 1; i < _finished.size(); i++)
			System.out.print(", " + _finished.get(i).getID());
		System.out.println("]");
	}
}
