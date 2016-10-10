import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;

class SchedulerRankingP extends Scheduler
{
	protected Comparator<Process> CRITERION;

	public SchedulerRankingP (ArrayList<Process> processList, Comparator<Process> criterion)
	{
		super(processList);
		this.CRITERION = criterion;
	}

	// Shortest Job First Preemptive Scheduler
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

			// Sorts by burst time
			Collections.sort(readyQueue, this.CRITERION);

			Process next = readyQueue.peek();	// Selects next candidate
			int duration = next.getBurstTime(); // Default duration when no interruption occurs
			boolean interrupted = false;
			
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
					interrupted = true;
					break;
				}
			}
			
			if (!interrupted) next = readyQueue.poll(); 
			TimeSlot newSlot = new TimeSlot (next, now, now + duration, next.getBurstTime());
			next.accessCPU(duration);	// Burst time is smaller now
			tsList.add(newSlot);
			now += duration;
		}
		
		return tsList;
	}
}