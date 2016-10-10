import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerPriorityP extends SchedulerRankingP
{
	public SchedulerPriorityP (ArrayList<Process> processList)
	{
		super(processList, Process.PRIORITY_BURST_COMPARATOR);
	}
}