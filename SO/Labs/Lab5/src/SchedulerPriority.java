import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerPriority extends SchedulerRanking
{
	public SchedulerPriority (ArrayList<Process> processList)
	{
		super(processList, Process.PRIORITY_BURST_COMPARATOR);
	}
}