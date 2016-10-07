import java.util.ArrayList;
import java.util.Collections;

class SchedulerFCFS extends Scheduler
{
	public SchedulerFCFS (ArrayList<Process> pList)
	{
		super(pList);
	}

	// FCFS Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.pList, Process.ARRIVAL_TIME_COMPARATOR);

		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		double timer = this.pList.get(0).getArrivalTime();

		for (Process proc : this.pList) {
			TimeSlot newSlot = new TimeSlot (proc, timer, timer + proc.getBurstTime());
			tsList.add(newSlot);
			timer += proc.getBurstTime() + proc.getWaitingTime();
		}

		return tsList;
	}
}