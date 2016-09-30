import java.net.*;
import java.io.*;

public class DateServer
{
	public static void main(String[] args)
	{
		try
		{
			ServerSocket sock = new ServerSocket (6013);

			while (true)
			{
				Socket client = sock.accept();

				new Thread ()
				{
					public void run (Socket client) throws IOException
					{
						System.out.println("Thread {" + Thread.currentThread().getName() + "} (Start)");

						PrintWriter pout = new PrintWriter (client.getOutputStream(), true);
						String msg = new java.util.Date().toString();

						// Simulates processing
						try { Thread.sleep(1000); }
						catch (InterruptedException ex) { ex.printStackTrace(); }

						System.out.println("Thread {" + Thread.currentThread().getName() + "} >> " + msg);
						pout.println(msg);
						client.close();

						System.out.println("Thread {" + Thread.currentThread().getName() + "} (End  )\n");
					}
				}.run(client);
			}
		}

		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}
}