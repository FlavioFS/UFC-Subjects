import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

class SchedulerSJF extends SchedulerRanking
{
	public SchedulerSJF (ArrayList<Process> processList)
	{
		super(processList, Process.BURST_TIME_COMPARATOR);
	}
}