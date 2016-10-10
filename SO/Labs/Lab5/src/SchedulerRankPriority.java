import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerRankPriority extends SchedulerRank
{
	public SchedulerRankPriority (ArrayList<Process> processList)
	{
		super(processList, Process.PRIORITY_BURST_COMPARATOR);
	}
}