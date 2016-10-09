import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerRR extends Scheduler
{
	int quantum;

	public SchedulerRR (ArrayList<Process> pList, int quantum)
	{
		super(pList);
		this.quantum = quantum;
	}

	// RR Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.processList, Process.ARRIVAL_TIME_COMPARATOR);
		
		// The return value
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();

		// A draft copy of processList to compute schedule, and two Queues
		ArrayList<Process> processHistory = new ArrayList<Process> (this.processList);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		
		int now = processHistory.get(0).getArrivalTime();
		
		while (!(readyQueue.isEmpty() && processHistory.isEmpty()))
		{
			// Sends living processes to ready queue
			for (int i = 0; i < processHistory.size(); i++)
			{
				Process proc = processHistory.get(i);
				if (proc.getArrivalTime() <= now)
				{
					readyQueue.add(proc);
					processHistory.remove(i);
					i--;
				}
			}
			
			// No process sent to ready queue
			// (when there is an interval such that no process arrives)
			if (readyQueue.isEmpty())
			{
				now = processHistory.get(0).getArrivalTime(); // Jumps to first process in history
				continue;
			}

			// Picks next element
			Process next = readyQueue.poll();
			
			/* Duration
			 *   1. Process not over
			 *   2. Process finishes during this quantum
			 */
			boolean notOver = next.getBurstTime() > this.quantum;
			int duration = notOver ? this.quantum : next.getBurstTime();
			
			TimeSlot newSlot = new TimeSlot (next, now, now + duration);
			next.accessCPU(duration);	// Burst time is smaller now
			if (notOver) readyQueue.add(next);
			tsList.add(newSlot);
			now += duration;
		}

		return tsList;
	}
}