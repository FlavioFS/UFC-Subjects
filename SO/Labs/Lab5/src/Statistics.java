import java.util.ArrayList;

class Statistics
{
	// TimeSlot list
	ArrayList<TimeSlot> schedule;
	ArrayList<Process> processList;

	// Statistics
	int processingTime;		// b
	double
		cpuUsage,				// c
		throughput,				// d
		turnaround,				// e
		waitingTime,			// f
		answerTime,				// g
		contextSwapTime,		// h
		processCount,			// i
		processCountByQueue;	// j

	public Statistics (ArrayList<TimeSlot> schedule, ArrayList<Process> processList)
	{
		this.schedule = schedule;
		this.processList = processList;
	}

	public void calcStatistics ()
	{
		this.calcProcessCount(); // Required for some of the others
		
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
			"CPU usage:                 " + String.valueOf(this.cpuUsage) +
			"Throughput:                " + String.valueOf(this.throughput) +
			"Turnaround:                " + String.valueOf(this.turnaround) +
			"Waiting time:              " + String.valueOf(this.waitingTime) +
			"Answer time:               " + String.valueOf(this.answerTime) +
//			"Context swap time:         " + String.valueOf(this.contextSwapTime) +
			"No. of Processes:          " + String.valueOf(this.processCount);
//			"No. of Processes by Queue: " + String.valueOf(this.processCountByQueue);

		return text;
	}
	
	/* ==============================================================================
	 *  Private Statistics
	 * ============================================================================== */
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
		this.throughput = this.processCount / this.schedule.get(this.schedule.size()-1).getEnd();
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
		
		for (TimeSlot ts : this.schedule)
		{
			for (Process proc : this.processList)
			{
				if (
					!ts.getProcess().equals(proc) &&
					proc.getArrivalTime() <= ts.getStart()
				)
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