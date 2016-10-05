class SchedulerPriority extends Scheduler
{
	public SchedulerFCFS (ArrayList<Process> pList)
	{
		super(pList);
	}

	public ArrayList<Process> schedule (ArrayList<Process> pList)
	{
		return pList;
	}
}