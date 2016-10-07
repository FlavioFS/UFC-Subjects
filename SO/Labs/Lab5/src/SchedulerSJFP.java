import java.util.ArrayList;

class SchedulerSJFP extends Scheduler
{
	public SchedulerSJFP (ArrayList<Process> pList)
	{
		super(pList);
	}

	// FCFS Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		double timer = 0;

		for (Process proc : this.pList) {
			TimeSlot newSlot = new TimeSlot (proc, timer, timer + proc.getBurstTime());
			tsList.add(newSlot);
			timer += proc.getBurstTime();
		}

		return tsList;
	}
}