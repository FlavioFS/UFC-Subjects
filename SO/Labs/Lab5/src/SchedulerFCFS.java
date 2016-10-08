import java.util.ArrayList;
import java.util.Collections;

class SchedulerFCFS extends Scheduler
{
	public SchedulerFCFS (ArrayList<Process> processList)
	{
		super(processList);
	}

	// First Come First Served Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		// Sorts by arrival time
		Collections.sort(this.processList, Process.ARRIVAL_TIME_COMPARATOR);

		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		int now = this.processList.get(0).getArrivalTime();

		for (Process proc : this.processList) {
			TimeSlot newSlot = new TimeSlot (proc, now, now + proc.getBurstTime());
			tsList.add(newSlot);
			now += proc.getBurstTime();
		}

		return tsList;
	}
}