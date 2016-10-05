import java.util.ArrayList;

class Statistics
{
	// Process list
	ArrayList<Process> pList;

	// Statistics
	double processingTime;		// b
	double
		cpuUsage,				// c
		throughput,				// d
		turnaround,				// e
		waitingTime,			// f
		answerTime,				// g
		contextSwapTime,		// h
		processCount,			// i
		processCountByQueue;	// j

	public Statistics (ArrayList<Process> pList)
	{
		this.pList = pList;
	}

	public void calcStatistics ()
	{
		// Reset statistics
		this.cpuUsage            = 0;	// c
		this.throughput          = 0;	// d 	X
		this.turnaround          = 0;	// e 	X
		this.waitingTime         = 0;	// f 	X
		this.answerTime          = 0;	// g
		this.contextSwapTime     = 0;	// h
		this.processCount        = 0;	// i 	X
		this.processCountByQueue = 0;	// j

		for (Process elem : pList)
		{
			waitingTime += processingTime;							// f
			processingTime += elem.getBurstTime();					// b
			turnaround += processingTime - elem.getArrivalTime();	// e
		}

		throughput = pList.size() / processingTime;	// d
		turnaround /= pList.size();					// e
		waitingTime /= pList.size();				// f
		processCount = pList.size();				// i

		ArrayList<Process> sortedList = new ArrayList<Process> (pList);
		Collections.sort(sortedList);
	}

	public void printStatistics ()
	{
		String text =
			"========== " + algName + " ==========\n" +
			// "CPU usage:                 " + String.valueOf(this.cpuUsage) +
			"Throughput:                " + String.valueOf(this.throughput) +
			"Turnaround:                " + String.valueOf(this.turnaround) +
			"Waiting time:              " + String.valueOf(this.waitingTime) +
			"Answer time:               " + String.valueOf(this.answerTime) +
			"Context swap time:         " + String.valueOf(this.contextSwapTime) +
			"No. of Processes:          " + String.valueOf(this.processCount) +
			"No. of Processes by Queue: " + String.valueOf(this.processCountByQueue);

		System.out.println(text);
	}
}