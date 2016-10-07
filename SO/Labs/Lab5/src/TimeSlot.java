public class TimeSlot
{
	private double start;
	private double end;
	private Process process;

	// Constructor
	public TimeSlot (Process process, double start, double end)
	{
		this.process = process;
		this.start = start;
		this.end = end;
	}

	// Getters
	public double  getStart   () { return this.start;   }
	public double  getEnd     () { return this.end;     }
	public Process getProcess () { return this.process; }

	public String getID          () { return this.getProcess().getID();          }
	public double getArrivalTime () { return this.getProcess().getArrivalTime(); }
	public double getBurstTime   () { return this.getProcess().getBurstTime();   }
	public int    getPriority    () { return this.getProcess().getPriority();    }
	public int    getWaitingTime () { return this.getProcess().getWaitingTime(); }
}