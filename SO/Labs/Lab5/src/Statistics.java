import java.util.ArrayList;

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
		contextSwapTime;		// h

	public Statistics (ArrayList<TimeSlot> schedule, ArrayList<Process> processList)
	{
		this.schedule = schedule;
		this.processList = processList;
	}

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
		
		// context swap??
		// process count bu queue??
	}

	public String toString ()
	{
		String text =
			"Processing Time:           " + String.valueOf(this.processingTime) + "\n" +
			"CPU usage:                 " + String.valueOf(this.cpuUsage) + "\n" +
			"Throughput:                " + String.valueOf(this.throughput) + "\n" +
			"Turnaround:                " + String.valueOf(this.turnaround) + "\n" +
			"Waiting time:              " + String.valueOf(this.waitingTime) + "\n" +
			"Answer time:               " + String.valueOf(this.answerTime) + "\n" +
//			"Context swap time:         " + String.valueOf(this.contextSwapTime) +
			"No. of Processes:          " + String.valueOf(this.processCount);
//			"No. of Processes by Queue: " + String.valueOf(this.processCountByQueue);

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
					this.turnaround += ts.getEnd();
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
				if (!ts.equals(proc))
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
					this.answerTime += ts.getStart();
					break;
				}
			}
		}
		
		this.answerTime /= this.processCount; // Mean value
	}
}