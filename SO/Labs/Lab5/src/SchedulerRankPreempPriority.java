import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerRankPreempPriority extends SchedulerRankPreemp
{
	public SchedulerRankPreempPriority (ArrayList<Process> processList)
	{
		super(processList, Process.PRIORITY_BURST_COMPARATOR);
	}
}