import java.util.ArrayList;

public class Scheduler
{
	ArrayList<Process> pList;

	public Scheduler (ArrayList<Process> pList)
	{
		this.pList = new ArrayList<Process> (pList);
	}

	// The first process always starts at time zero
	// public void trim ()
	// {
	// 	double start = this.pList.get(0).getArrivalTime();

	// 	for (Process elem : this.pList)
	// 		start = elem.getArrivalTime() < start ? elem.getArrivalTime() : start;

	// 	for (Process elem : this.pList)
	// 		elem.setArrivalTime(elem.getArrivalTime() - start);
	// }

	// Abstract scheduling algorithm
	public ArrayList<TimeSlot> schedule ()
	{
		return new ArrayList<TimeSlot> ();
	}
}