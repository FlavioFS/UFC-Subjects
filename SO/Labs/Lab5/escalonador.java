import java.io.*;
import java.util.ArrayList;
import java.util.AbstractQueue;

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
		processArgs(args);
		loadCSV(filepath);
		// runAlgorithm();
		// output(outType);
		printProcessList();
	}

	/* ==============================================================================
	 *  Management
	 * ============================================================================== */
	static void runAlgorithm (int mode)
	{
		switch (alg)
		{
			case FCFS:
				fcfs(mode);
				break;
			
			case SJF:
				sjf(mode);
				break;
			
			case SJFP:
				sjfp(mode);
				break;
			
			case PRIORITY:
				priority(mode);
				break;
			
			case PRIORITYP:
				priorityP(mode);
				break;
			
			case RR:
				rr(mode);
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
		if (args[2].equals("FCFS"))
		{
			alg = FCFS;
		}
		else if (args[2].equals("SJF"))
		{
			alg = SJF;
		}
		else if (args[2].equals("SJFP"))
		{
			alg = SJFP;
		}
		else if (args[2].equals("PRIORITY"))
		{
			alg = PRIORITY;
		}
		else if (args[2].equals("PRIORITYP"))
		{
			alg = PRIORITYP;
		}
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

				processList.add(new Process (arrivalTime, processID, burstTime, priority));

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
				break;

			case LIST:
				break;

			default:
				break;
		}
	}

	// ----------------------------------------

	static void printProcessList ()
	{
		for (Process elem : processList)
			System.out.println(elem);
	}

	// ----------------------------------------

	static void printStatistics ()
	{
		String text =
			"========== " + algName + " ==========\n" +
			"CPU usage:                 " + String.valueOf(cpuUsage) +
			"Throughput:                " + String.valueOf(throughput) +
			"Turnaround:                " + String.valueOf(turnaround) +
			"Waiting time:              " + String.valueOf(waitingTime) +
			"Answer time:               " + String.valueOf(answerTime) +
			"Context swap time:         " + String.valueOf(contextSwapTime) +
			"No. of Processes:          " + String.valueOf(processCount) +
			"No. of Processes by Queue: " + String.valueOf(processCountByQueue);

		System.out.println(text);
	}

	/* ==============================================================================
	 *  Algorithms
	 * ============================================================================== */
	static void fcfs (int mode)
	{
		processingTime = 0;
		algName = "FCFS";

		if (mode == STATISTICS)
		{
			// Reset statistics
			cpuUsage            = 0,
			throughput          = 0,
			turnaround          = 0,
			waitingTime         = 0,
			answerTime          = 0,
			contextSwapTime     = 0,
			processCount        = 0,
			processCountByQueue = 0;



			printStatistics();
		}

		else if (mode == LIST)
		{
			// Display schedule
			for (Integer index : schedule)
				System.out.println("{ id: " + processList[elem].getID() + ", processing time: " + processingTime + " }");	
		}
	}

	// ----------------------------------------

	static void sjf (int mode)
	{

	}

	// ----------------------------------------

	static void sjfp (int mode)
	{

	}

	// ----------------------------------------

	static void priority (int mode)
	{

	}

	// ----------------------------------------

	static void priorityP (int mode)
	{

	}

	// ----------------------------------------

	static void rr (int mode)
	{

	}
}