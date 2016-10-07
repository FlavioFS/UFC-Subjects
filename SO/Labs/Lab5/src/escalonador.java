import java.io.*;
import java.util.ArrayList;
import java.util.AbstractQueue;
import java.util.Collections;

public class escalonador
{
	// Algorithm enum
	private static final int
		FCFS      = 0,
		SJF       = 1,
		SJFP      = 2,
		PRIORITY  = 3,
		PRIORITYP = 4,
		RR        = 5;

	// Output enum
	private static final int
		STATISTICS = 1,
		LIST       = 2;

	static Scheduler scheduler;
	static ArrayList<TimeSlot> result;

	/* =====================================
	 *  Shared args
	 * ===================================== */
	static String filepath;
	static int alg;		// Algorithm enum
	static int outType; // Output enum


	/* =====================================
	 *  Output
	 * ===================================== */
	static double processingTime; // a - this one is a shared output

	// Statistics
	static String algName;
	static double
		cpuUsage,				// b
		throughput,				// c
		turnaround,				// d
		waitingTime,			// e
		answerTime,				// f
		contextSwapTime,		// g
		processCount,			// h
		processCountByQueue;	// j

	// List
	static ArrayList<Process> processList;
	static ArrayList<Integer> schedule;


	/* =====================================
	 *  Exclusive args
	 * ===================================== */
	// RR
	static int quantum;



	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */



	/* ==============================================================================
	 *  Main
	 * ============================================================================== */
	public static void main(String[] args) throws IOException
	{
		if (!processArgs(args)) return;
		loadCSV(filepath);
		pickScheduler();

		result = scheduler.schedule();

		output(outType);
	}

	/* ==============================================================================
	 *  Management
	 * ============================================================================== */
	static void pickScheduler ()
	{
		switch (alg)
		{
			case FCFS:
				scheduler = new SchedulerFCFS(processList);
				break;
			
			case SJF:
				scheduler = new SchedulerSJF(processList);
				break;
			
			case SJFP:
				scheduler = new SchedulerSJFP(processList);
				break;
			
			case PRIORITY:
				scheduler = new SchedulerPriority(processList);
				break;
			
			case PRIORITYP:
				scheduler = new SchedulerPriorityP(processList);
				break;
			
			case RR:
				scheduler = new SchedulerRR(processList, quantum);
				break;

			default:
				scheduler = new SchedulerFCFS(processList);
				break;
		}
	}


	/* ==============================================================================
	 *  Inputs
	 * ============================================================================== */
	static boolean processArgs (String[] args)
	{
		if (args.length < 3)
		{
			System.out.println("Usage: escalonador <input_file.csv> <output_mode> <algorithm> <specific_arguments>");
			return false; 
		}

		// CSV file
		filepath = args[0];

		// Output type: Statistics or List
		switch (Integer.parseInt(args[1]))
		{
			case STATISTICS:
				outType = STATISTICS;
				break;

			case LIST:
				outType = LIST;
				break;
		}

		// Selects algorithm and processes special args
		if      (args[2].equals("FCFS"))		alg = FCFS;
		else if (args[2].equals("SJF"))			alg = SJF;
		else if (args[2].equals("SJFP"))		alg = SJFP;
		else if (args[2].equals("PRIORITY"))	alg = PRIORITY;
		else if (args[2].equals("PRIORITYP"))	alg = PRIORITYP;

		else if (args[2].equals("RR"))
		{
			if (args.length < 4)
			{
				System.out.println("Usage: escalonador <input_file.csv> <output_mode> RR <quantum>");
				return false; 
			}

			alg = RR;
			quantum = Integer.parseInt(args[3]);
		}

		return true;
	}

	// ----------------------------------------

	static void loadCSV (String filepath) throws IOException
	{
		BufferedReader br = new BufferedReader (new FileReader(filepath));
		processList = new ArrayList<Process> ();

		try
		{
			String line = br.readLine();
			String[] splitLine;

			double arrivalTime;
			String processID;
			double burstTime;
			int    priority;

			while (line != null)
			{
				splitLine = line.split(", ");

				arrivalTime = Double.parseDouble(splitLine[0]);
				processID   = splitLine[1];
				burstTime   = Double.parseDouble(splitLine[2]);
				priority    = Integer.parseInt(splitLine[3]);

				processList.add(new Process (processID, arrivalTime, burstTime, priority));

				line = br.readLine();
			};
		}

		finally
		{
			br.close();
		}


	}

	/* ==============================================================================
	 *  Output
	 * ============================================================================== */
	static void output (int type)
	{
		switch (type)
		{
			case STATISTICS:
				Statistics stats = new Statistics (result);
				stats.calcStatistics();
				System.out.println(stats);
				break;

			case LIST:
				break;
		}
	}

	// ----------------------------------------

	static void printProcessList ()
	{
		for (Process elem : processList)
			System.out.println(elem);
	}


	/* ==============================================================================
	 *  Algorithms
	 * ============================================================================== */
	// static void fcfs (int mode)
	// {
	// 	algName = "FCFS";				// a

	// 	if (mode == STATISTICS)
	// 	{
	// 		Statistics stats = new Statistics (processList);
	// 		stats.calcStatistics();
	// 		stats.printStatistics();
	// 	}

	// 	else if (mode == LIST)
	// 	{
	// 		// Display schedule
	// 		for (int i = 0; i < schedule.size(); i++)
	// 			System.out.println("{ id: " + processList.get(i).getID() + ", processing time: " + processingTime + " }");	
	// 	}
	// }
}