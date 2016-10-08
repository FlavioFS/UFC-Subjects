import java.util.ArrayList;

class SchedulerRR extends Scheduler
{
	int quantum;

	public SchedulerRR (ArrayList<Process> pList, int quantum)
	{
		super(pList);
		this.quantum = quantum;
	}

	// FCFS Scheduler
	public ArrayList<TimeSlot> schedule ()
	{
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		int timer = 0;

		for (Process proc : this.processList) {
			TimeSlot newSlot = new TimeSlot (proc, timer, timer + proc.getBurstTime());
			tsList.add(newSlot);
			timer += proc.getBurstTime();
		}

		return tsList;
	}
}