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

			// Next candidate (not removed from ready queue yet)
			Process next = readyQueue.peek();
			
			boolean interrupted = false;
			
			for (Process proc : pListDraft)
			{
				// A process arrives before the next candidate ends and
				// it will finish before the next candidate
				if ( (proc.getArrivalTime() < timer + next.getBurstTime()) &&
					 (proc.getBurstTime() + proc.getArrivalTime() < next.getBurstTime() + timer ) )
				{
					next = proc;
					interrupted = true;
				}
			}
			
			int duration;
			if (interrupted)
			{
				duration = next.getArrivalTime() - timer;
			}
			else
			{
				duration = next.getBurstTime();
			}
			
			next = readyQueue.poll();
			next.accessCPU(duration);	// Burst time is smaller now
			TimeSlot newSlot = new TimeSlot (next, timer, timer + duration);
			tsList.add(newSlot);
			timer += duration;
		}
		
		return tsList;
	}
}