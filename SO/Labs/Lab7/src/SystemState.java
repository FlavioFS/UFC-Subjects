import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SystemState {
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	private ArrayList<Process> _processList;
	private int _A, _B, _C;		// Total system resources

	
	/* ==============================================================================
	 *  Constructors
	 * ============================================================================== */
	public SystemState (String filepath) throws IOException
	{
		_processList = new ArrayList<Process> ();
		this.loadCSV(filepath);
	}

	
	/* ==============================================================================
	 *  Getters
	 * ============================================================================== */
	public ArrayList<Process> processList () { return _processList; }
	
	public int getA() { return _A; }
	public int getB() { return _B; }
	public int getC() { return _C; }

	
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
				_processList.add(new Process (processID, A, B, C, nA, nB, nC));

				line = br.readLine();
			};
		}
		finally { br.close(); }

	}
}
