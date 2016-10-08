import java.util.ArrayList;

public class Scheduler
{
	ArrayList<Process> processList;

	public Scheduler (ArrayList<Process> processList)
	{
		this.processList = new ArrayList<Process> (processList);
	}

	// Abstract scheduling algorithm
	public ArrayList<TimeSlot> schedule ()
	{
		return new ArrayList<TimeSlot> ();
	}
}