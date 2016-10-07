public class TimeSlot
{
	private int start;
	private int end;
	private Process process;

	// Constructor
	public TimeSlot (Process process, int start, int end)
	{
		this.process = process;
		this.start = start;
		this.end = end;
	}

	// Getters
	public int  getStart      () { return this.start;   }
	public int  getEnd        () { return this.end;     }
	public Process getProcess () { return this.process; }

	public String getID        () { return this.getProcess().getID();           }
	public int getArrivalTime  () { return this.getProcess().getArrivalTime();  }
	public int getBurstTime    () { return this.getProcess().getBurstTime();    }
	public int getPriority     () { return this.getProcess().getPriority();     }
}