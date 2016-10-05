class SchedulerRR extends Scheduler
{
	int quantum;

	public SchedulerFCFS (ArrayList<Process> pList, int quantum)
	{
		super(pList);
		this.quantum = quantum;
	}

	public ArrayList<Process> schedule (ArrayList<Process> pList)
	{
		return pList;
	}
}