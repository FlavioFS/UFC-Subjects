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
		this.loadCSV(filepath);
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
	public boolean give (Process P, int dA, int dB, int dC)
	{
		// Not enough resources
		if (_A < dA || _B < dB || _C < dC)
			return false;
		
		// Resources available
		P.receive(dA, dB, dC);
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
	}
	
	public void finish (int index)
	{
		Process _P = _unfinished.remove(index);
		_finished.add(_P);
		this.free(_P);
	}
	
	public boolean isDone (int index)
	{
		Process p = _unfinished.get(index);
		return 	_A >= p.getNeedA() &&
				_B >= p.getNeedB() &&
				_C >= p.getNeedC();
	}
	
	/* ==============================================================================
	 *  IN
	 * ============================================================================== */
	public void loadCSV (String filepath) throws IOException
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
				
				A = Integer.parseInt(splitLine[1]);
				B = Integer.parseInt(splitLine[2]);
				C = Integer.parseInt(splitLine[3]);
				
				nA = Integer.parseInt(splitLine[4]);
				nB = Integer.parseInt(splitLine[5]);
				nC = Integer.parseInt(splitLine[6]);
				
				
				// Adds new Process to list
				_unfinished.add(new Process (processID, A, B, C, nA, nB, nC));

				line = br.readLine();
			};
		}
		finally { br.close(); }

	}
}
