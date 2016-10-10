import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;

class SchedulerRank extends Scheduler
{
	protected Comparator<Process> CRITERION;

	public SchedulerRank (ArrayList<Process> processList, Comparator<Process> criterion)
	{
		super(processList);
		this.CRITERION = criterion;
	}
	
	// Shortest Job First Scheduler
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

			// Defines next element
			Process next = readyQueue.poll();
			TimeSlot newSlot = new TimeSlot (next, now, now + next.getBurstTime(), next.getBurstTime());
			tsList.add(newSlot);
			now += next.getBurstTime();
		}
		
		return tsList;
	}
}