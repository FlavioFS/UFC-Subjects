import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerSJF extends Scheduler
{
	public SchedulerSJF (ArrayList<Process> pList)
	{
		super(pList);
	}

	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.pList, Process.ARRIVAL_TIME_COMPARATOR);
		
		// The return value
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		
		// A copies to modify and compute the correct order
		ArrayList<Process> pListDraft = new ArrayList<Process> (this.pList);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		double timer = 0;

		
		while (!pListDraft.isEmpty())
		{
			// Sends to ready queue
			for (Process proc : pListDraft)
			{
				if (proc.getArrivalTime() <= timer)
				{
					readyQueue.add(proc);
					pListDraft.remove(proc);
				}
			}
			
			Collections.sort(readyQueue, Process.BURST_TIME_COMPARATOR);
			
			Process next = readyQueue.poll();
			TimeSlot newSlot = new TimeSlot (next, timer, timer + next.getBurstTime());
			tsList.add(newSlot);
			timer += next.getBurstTime() + next.getWaitingTime();
			
			
		}
		
		
		
		return tsList;
	}
}