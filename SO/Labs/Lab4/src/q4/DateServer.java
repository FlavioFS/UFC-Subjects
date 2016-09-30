import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateServer
{
	static int POOL_SIZE;

	public static void main(String[] args)
	{
		try
		{
			POOL_SIZE = Integer.valueOf(args[0]);
			ExecutorService thPool = Executors.newFixedThreadPool(POOL_SIZE);
			ServerSocket sock = new ServerSocket (6013);

			System.out.println("Server started! Pool Size: " + POOL_SIZE);

			while (true)
			{
				Runnable worker = new Worker (sock.accept());
				thPool.execute(worker);
			}
		}

		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}
}