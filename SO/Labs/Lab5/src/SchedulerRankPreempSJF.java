import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerRankPreempSJF extends SchedulerRankPreemp
{
	public SchedulerRankPreempSJF (ArrayList<Process> processList)
	{
		super(processList, Process.BURST_TIME_COMPARATOR);
	}
}