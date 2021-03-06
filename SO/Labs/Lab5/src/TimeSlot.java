public class TimeSlot
{
	private int start;
	private int end;
	private int burst;
	private Process process;

	// Constructor
	public TimeSlot (Process process, int start, int end, int burst)
	{
		this.process = process;
		this.start = start;
		this.end = end;
		this.burst = burst;
	}

	// Getters
	public int  getStart      () { return this.start;                      }
	public int  getEnd        () { return this.end;                        }
	public int  getDuration   () { return this.getEnd() - this.getStart(); }
	public int  getBurstTime  () { return this.burst;                      }
	public Process getProcess () { return this.process;                    }

	public String getID        () { return this.getProcess().getID();           }
	public int getArrivalTime  () { return this.getProcess().getArrivalTime();  }
	public float getPriority   () { return this.getProcess().getPriority();     }
}