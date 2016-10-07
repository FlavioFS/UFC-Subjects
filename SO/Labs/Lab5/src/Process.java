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
	private double arrivalTime;
	private double burstTime;
	private int priority;
	private int waitingTime;

	// Constructors
	public Process (String id, double arrivalTime, double burstTime, int priority, int waitingTime)
	{
		this.id          = id;
		this.arrivalTime = arrivalTime;
		this.burstTime   = burstTime;
		this.priority    = priority;
		this.waitingTime = waitingTime;
	}
	
	public Process (String id, double arrivalTime, double burstTime, int priority)
	{
		this(id, arrivalTime, burstTime, priority, 0);
	}

	// Getters
	public String getID()          { return this.id;          }
	public double getArrivalTime() { return this.arrivalTime; }
	public double getBurstTime()   { return this.burstTime;   }
	public int    getPriority()    { return this.priority;    }
	public int    getWaitingTime() { return this.waitingTime; }

	@Override
	public String toString ()
	{
		return "{ ID: "       + getID() +
		       ", arrival: "  + String.valueOf(getArrivalTime()) +
		       ", burst: "    + String.valueOf(getBurstTime())   +
		       ", priority: " + String.valueOf(getPriority())    + " }";
	}

	@Override
	public int compareTo (Process process)
	{
		if (this.getArrivalTime() == process.getArrivalTime())
			return 0;
		
		return this.getArrivalTime() > process.getArrivalTime() ? 1 : -1;
	}
}