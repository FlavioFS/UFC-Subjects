import java.util.Comparator;

class Process implements Comparable<Process>
{
	/* ==============================================================================
	 *  Comparators
	 * ============================================================================== */
	public static final Comparator<Process> ARRIVAL_TIME_COMPARATOR = new Comparator <Process> ()
	{
		@Override
		public int compare (Process p1, Process p2)
		{
			if (p1.getArrivalTime() == p2.getArrivalTime()) return 0;
			return p1.getArrivalTime() > p2.getArrivalTime() ? 1 : -1;
		}
	};

	public static final Comparator<Process> BURST_TIME_COMPARATOR = new Comparator <Process> ()
	{
		@Override
		public int compare (Process p1, Process p2)
		{
			if (p1.getBurstTime() == p2.getBurstTime()) return 0;
			return p1.getBurstTime() > p2.getBurstTime() ? 1 : -1;
		}
	};

	public static final Comparator<Process> PRIORITY_COMPARATOR = new Comparator <Process> ()
	{
		@Override
		public int compare (Process p1, Process p2)
		{
			if (p1.getPriority() == p2.getPriority()) return 0;
			return p1.getPriority() > p2.getPriority() ? 1 : -1;
		}
	};
	
	/* ==============================================================================
	 *  Attributes
	 * ============================================================================== */
	// Attributes
	private String id;
	private int arrivalTime;
	private int burstTime;
	private int priority;

	// Constructors
	public Process (String id, int arrivalTime, int burstTime, int priority)
	{
		this.id          = id;
		this.arrivalTime = arrivalTime;
		this.burstTime   = burstTime;
		this.priority    = priority;
	}

	// Getters
	public String getID()        { return this.id;           }
	public int getArrivalTime()  { return this.arrivalTime;  }
	public int getBurstTime()    { return this.burstTime;    }
	public int getPriority()     { return this.priority;     }
	
	public void accessCPU (int dt)
	{
		this.burstTime -= dt;
	}

	@Override
	public String toString ()
	{
		return "{ ID: "            + getID() +
		       ", arrival: "       + String.valueOf(getArrivalTime())  +
		       ", burst: "         + String.valueOf(getBurstTime())    +
		       ", priority: "      + String.valueOf(getPriority())     + " }";
	}

	@Override
	public int compareTo (Process process)
	{
		if (this.getArrivalTime() == process.getArrivalTime())
			return 0;
		
		return this.getArrivalTime() > process.getArrivalTime() ? 1 : -1;
	}
}