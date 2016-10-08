import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerSJFP extends Scheduler
{
	public SchedulerSJFP (ArrayList<Process> pList)
	{
		super(pList);
	}

	// Shortest Job First Preemptive Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.pList, Process.ARRIVAL_TIME_COMPARATOR);
		
		// The return value
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		
		// A draft copy of pList to compute schedule, and two Queues
		ArrayList<Process> processHistory = new ArrayList<Process> (this.pList);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		
		int now = processHistory.get(0).getArrivalTime();

		while (!readyQueue.isEmpty() && !processHistory.isEmpty())
		{
			// Sends living processes to ready queue
			for (Process proc : processHistory)
			{
				if (proc.getArrivalTime() <= now)
				{
					readyQueue.add(proc);
					processHistory.remove(proc);
				}
			}
			
			// No process sent to ready queue
			// (when there is an interval such that no process arrives)
			if (readyQueue.isEmpty())
			{
				now = processHistory.get(0).getArrivalTime();
				continue;
			}

			// Sorts by burst time
			Collections.sort(readyQueue, Process.BURST_TIME_COMPARATOR);

			Process next = readyQueue.poll();	// Selects next candidate
			int duration = next.getBurstTime(); // Default duration when no interruption occurs
			
			for (Process proc : processHistory)
			{
				/* "A process arrives before the next process ends"
				 *   and
				 * "it will finish sooner"
				 */
				if ( (proc.getArrivalTime() < now + next.getBurstTime()) &&
					 (proc.getBurstTime() + proc.getArrivalTime() < next.getBurstTime() + now ) )
				{
					duration = proc.getArrivalTime() - now;
					break;
				}
			}
			
			TimeSlot newSlot = new TimeSlot (next, now, now + duration);
			next.accessCPU(duration);	// Burst time is smaller now
			tsList.add(newSlot);
			now += duration;
		}
		
		return tsList;
	}
}