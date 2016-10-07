import java.util.ArrayList;

class SchedulerPriority extends Scheduler
{
	public SchedulerPriority (ArrayList<Process> pList)
	{
		super(pList);
	}

	// FCFS Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		int timer = 0;

		for (Process proc : this.pList) {
			TimeSlot newSlot = new TimeSlot (proc, timer, timer + proc.getBurstTime());
			tsList.add(newSlot);
			timer += proc.getBurstTime();
		}

		return tsList;
	}
}