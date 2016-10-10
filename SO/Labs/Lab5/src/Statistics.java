import java.util.ArrayList;
import java.util.Locale;

class Statistics
{
	// TimeSlot list
	ArrayList<TimeSlot> schedule;
	ArrayList<Process> processList;

	// Statistics
	int
		processingTime,			// b
		cpuUsage,				// c
		processCount,			// i
		processCountByQueue;	// j
	double
		throughput,				// d
		turnaround,				// e
		waitingTime,			// f
		answerTime,				// g
		contextSwap;		// h
	
	/** Use calcStatistics() to calculate, and toString() to display the result. */
	public Statistics (ArrayList<TimeSlot> schedule, ArrayList<Process> processList)
	{
		this.schedule = schedule;
		this.processList = processList;
	}

	/** Calculate statistics for an ArrayList<'TimeSlot'> scheduling */
	public void calcStatistics ()
	{
		// 1. Required for some of the others
		this.calcProcessCount();
		this.calcProcessingTime();
		
		// 2. Some of these require value above
		this.calcCPU();
		this.calcThroughput();
		this.calcTurnaround();
		this.calcWaitingTime();
		this.calcAnswerTime();
		this.calcContextSwap();
		
		// context swap??
		// process count by queue??
	}

	/** Display statistics */
	public String toString ()
	{
		String text =
			" Processing Time  " + String.format(Locale.US,   "%-7d", this.processingTime) + "  cycles\n" +
			"       CPU usage  " + String.format(Locale.US,   "%-7d", this.cpuUsage)       + "  cycles\n" +
			"      Throughput  " + String.format(Locale.US, "%-7.4g", this.throughput)     + "  processes/cycle\n" +
			"      Turnaround  " + String.format(Locale.US, "%-7.4g", this.turnaround)     + "  cycles\n" +
			"    Waiting time  " + String.format(Locale.US, "%-7.4g", this.waitingTime)    + "  cycles\n" +
			"     Answer time  " + String.format(Locale.US, "%-7.4g", this.answerTime)     + "  cycles\n" +
			"   Context swaps  " + String.format(Locale.US, "%-7.4g", this.contextSwap)    + "  swaps/cycle\n" +
			"      #Processes  " + String.format(Locale.US,   "%-7d", this.processCount)   + "  processes";
//			"#Processes/Queue  " + String.valueOf(this.processCountByQueue);

		return text;
	}
	
	/* ==============================================================================
	 *  Private Statistics
	 * ============================================================================== */
	private void calcProcessingTime ()
	{
		this.processingTime = this.schedule.get(this.schedule.size()-1).getEnd();
	}
	
	// ----------------------------------------
	
	private void calcCPU ()
	{
		this.cpuUsage = 0;	// Reset
		for (TimeSlot ts : this.schedule)
			this.cpuUsage += ts.getDuration();
	}
	
	// ----------------------------------------
	
	private void calcProcessCount ()
	{
		this.processCount = this.processList.size();
	}

	// ----------------------------------------
	
	private void calcThroughput ()
	{
		if (processingTime > 0)
			this.throughput = (double) this.processCount / this.processingTime;
	}

	// ----------------------------------------
	
	private void calcTurnaround ()
	{
		this.turnaround = 0; // Reset
		
		// Searches backwards for end times
		for (Process proc : processList)
		{
			for (int i = this.schedule.size()-1; i >= 0; i--)
			{
				TimeSlot ts = this.schedule.get(i);
				if ( ts.getProcess().equals(proc) )
				{
					this.turnaround += ts.getEnd() - proc.getArrivalTime();
					break;
				}
			}
		}
		
		this.turnaround /= this.processCount; // Mean value
	}

	// ----------------------------------------
	
	private void calcWaitingTime ()
	{
		this.waitingTime = 0; // Reset
		
		int end;
		TimeSlot ts;
		for (Process proc : this.processList)
		{
			// Calculates when process dies
			for (end = this.schedule.size()-1; end >= 0; end--)
			{
				ts = this.schedule.get(end); 
				if (ts.getProcess().equals(proc))
					break;
			}
			
			// Adds duration of all previous slots of different processes
			for (int i = 0; i < end; i++)
			{
				ts = this.schedule.get(i);
				
				// Ignores slot when the process did not arrive yet or when its processing 
				if (proc.getArrivalTime() > ts.getEnd() || ts.getProcess().equals(proc))
					continue;
				
				// Arrived during this Time Slot
				if (proc.getArrivalTime() > ts.getStart())
					this.waitingTime += ts.getEnd() - proc.getArrivalTime();
				
				// Arrived previously
				else
					this.waitingTime += ts.getDuration();
			}
		}
		
		this.waitingTime /= this.processCount; // Mean value
	}

	// ----------------------------------------
	
	private void calcAnswerTime ()
	{
		this.answerTime = 0; // Reset
		
		// Searches for starting times
		for (Process proc : processList)
		{
			for (int i = 0; i < this.schedule.size(); i++)
			{
				TimeSlot ts = this.schedule.get(i);
				if ( ts.getProcess().equals(proc) )
				{
					this.answerTime += ts.getStart() - proc.getArrivalTime();
					break;
				}
			}
		}
		
		this.answerTime /= this.processCount; // Mean value
	}
	
	// ----------------------------------------
	
	private void calcContextSwap ()
	{
		this.contextSwap =  (double) (this.schedule.size() - 1) / this.processingTime; // Mean value
	}
}