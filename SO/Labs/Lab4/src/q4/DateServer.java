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
						PrintWriter pout = new PrintWriter (client.getOutputStream(), true);
						String msg = new java.util.Date().toString();
						System.out.println(">> " + msg);
						pout.println(msg);
						client.close();
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