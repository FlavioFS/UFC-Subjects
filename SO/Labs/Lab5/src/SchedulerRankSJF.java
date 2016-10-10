import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerRankSJF extends SchedulerRank
{
	public SchedulerRankSJF (ArrayList<Process> processList)
	{
		super(processList, Process.BURST_TIME_COMPARATOR);
	}
}