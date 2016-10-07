import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerSJF extends Scheduler
{
	public SchedulerSJF (ArrayList<Process> pList)
	{
		super(pList);
	}
	
	// Shortest Job First Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.pList, Process.ARRIVAL_TIME_COMPARATOR);
		
		// The return value
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		
		// A draft copy of pList to compute schedule, and two Queues
		ArrayList<Process> pListDraft = new ArrayList<Process> (this.pList);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		
		int timer = pListDraft.get(0).getArrivalTime();
		boolean nobodyIsReady = true;

		while (!readyQueue.isEmpty() && !pListDraft.isEmpty())
		{
			// Sends living processes to ready queue
			for (Process proc : pListDraft)
			{
				if (proc.getArrivalTime() <= timer)
				{
					readyQueue.add(proc);
					pListDraft.remove(proc);
					nobodyIsReady = false;
				}
			}
			
			// No process sent to ready queue
			if (nobodyIsReady)
			{
				timer = pListDraft.get(0).getArrivalTime();
				nobodyIsReady = true;
				continue;
			}

			// Sorts by burst time
			Collections.sort(readyQueue, Process.BURST_TIME_COMPARATOR);

			// Defines next element
			Process next = readyQueue.poll();
			TimeSlot newSlot = new TimeSlot (next, timer, timer + next.getBurstTime());
			tsList.add(newSlot);
			timer += next.getBurstTime();
		}
		
		return tsList;
	}
}