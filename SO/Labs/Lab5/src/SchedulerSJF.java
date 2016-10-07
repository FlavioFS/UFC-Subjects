import java.util.ArrayList;

class SchedulerSJF extends Scheduler
{
	public SchedulerSJF (ArrayList<Process> pList)
	{
		super(pList);
	}

	public ArrayList<TimeSlot> schedule ()
	{
		ArrayList<Process> draftList = new ArrayList<Process> (pList); // A copy to modify
		ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot> ();
		double timer = 0;


		for (int i = 0; i < draftList.size(); i++)
		{
			// TimeSlot newSlot = new TimeSlot (proc, timer, timer + proc.getBurstTime());
			// tsList.add(newSlot);
			// timer += proc.getBurstTime();
		}
		
		return tsList;
	}
}