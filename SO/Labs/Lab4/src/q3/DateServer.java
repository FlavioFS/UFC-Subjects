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
				Worker worker = new Worker (sock.accept());
				worker.start();
			}
		}

		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}
}