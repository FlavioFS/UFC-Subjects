class Process
{
	// Attributes
	double arrivalTime;
	String id;
	double burstTime;
	int priority;

	// Constructor
	public Process (double arrivalTime, String id, double burstTime, int priority)
	{
		this.arrivalTime = arrivalTime;
		this.id          = id;
		this.burstTime   = burstTime;
		this.priority    = priority;
	}

	// Getters
	public double getArrivalTime() { return this.arrivalTime; }
	public String getID()          { return this.id;   }
	public double getBurstTime()   { return this.burstTime; }
	public int    getPriority()    { return this.priority; }

	@Override
	public String toString ()
	{
		return "{ ID: "       + getID() +
		       ", arrival: "  + String.valueOf(getArrivalTime()) +
		       ", burst: "    + String.valueOf(getBurstTime())   +
		       ", priority: " + String.valueOf(getPriority())    + " }";
	}
}