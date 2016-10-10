import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerSJFP extends SchedulerRankingP
{
	public SchedulerSJFP (ArrayList<Process> processList)
	{
		super(processList, Process.BURST_TIME_COMPARATOR);
	}
}