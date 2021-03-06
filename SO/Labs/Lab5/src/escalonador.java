import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

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
		LIST       = 2,
		DEBUG      = 3;

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
				algName = "FCFS";
				scheduler = new SchedulerFCFS(processList);
				break;
			
			case RR:
				algName = "RR";
				scheduler = new SchedulerRR(processList, quantum);
				break;

			case SJF:
				algName = "SJF";
				scheduler = new SchedulerRankSJF(processList);
				break;
			
			case SJFP:
				algName = "SJFP";
				scheduler = new SchedulerRankPreempSJF(processList);
				break;
			
			case PRIORITY:
				algName = "PRIORITY";
				scheduler = new SchedulerRankPriority(processList);
				break;
			
			case PRIORITYP:
				algName = "PRIORITYP";
				scheduler = new SchedulerRankPreempPriority(processList);
				break;

			default:
				algName = "FCFS";
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

			case DEBUG:
				outType = DEBUG;
				break;
		}

		// Selects algorithm and processes special args
		if (args[2].equals("FCFS"))      alg = FCFS;
		else if (args[2].equals("SJF"))  alg = SJF;
		else if (args[2].equals("SJFP")) alg = SJFP;
		else if (args[2].equals("PRIORITY") || args[2].equals("Priority"))   alg = PRIORITY;
		else if (args[2].equals("PRIORITYP") || args[2].equals("PriorityP")) alg = PRIORITYP;
		else if (args[2].equals("RR"))
		{
			if (args.length < 4)
			{
				System.out.println("Usage: escalonador <input_file.csv> <output_mode> RR <quantum>\n");
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

			int arrivalTime;
			String processID;
			int burstTime;
			int priority;

			while (line != null)
			{
				if (line.charAt(0) == '#')
				{
					line = br.readLine();					
					continue;
				}
				
				splitLine = line.split(", ");

				arrivalTime = Integer.parseInt(splitLine[0]);
				processID   = splitLine[1];
				burstTime   = Integer.parseInt(splitLine[2]);
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
		Statistics stats;
		switch (type)
		{
			case STATISTICS:
				stats = new Statistics (result, processList);
				stats.calcStatistics();
				System.out.println
				(
					"╔════════════════════╦════════════════════╗\n" +
					String.format(Locale.US, "║ %-18s ║ STATISTICS         ║\n", algName) +
					"╚════════════════════╩════════════════════╝"
				);
				System.out.println(stats + "\n");
				break;

			case LIST:
				System.out.println
				(
					"╔════════════════╦════════════════╗\n" +
					String.format(Locale.US, "║ %-14s ║ LIST           ║\n", algName) +
					"╚════════════════╩════════════════╝"
				);
				for (int i = 0; i < result.size(); i++)
				{
					TimeSlot ts = result.get(i);
					System.out.println
					(
						"┌────────── Time Slot " + String.valueOf(i) + " ───────────\n" +
						"├─ Burst   Start    End   Duration\n"        +
						String.format(Locale.US, "│  %-6d  ", ts.getBurstTime()) +
						String.format(Locale.US, "%-7d  ", ts.getStart())     +
						String.format(Locale.US, "%-4d  ", ts.getEnd())       +
						String.format(Locale.US, "%-8d", ts.getDuration())    + "\n└─ " +
						ts.getProcess().timeSlotString()              + "\n"
					);
				}
				break;

			case DEBUG:
				stats = new Statistics (result, processList);
				stats.calcStatistics();
				System.out.println(stats.csvOutput());
				break;
		}
	}

	// ----------------------------------------

	static void printProcessList ()
	{
		for (Process elem : processList)
			System.out.println(elem);
	}
}