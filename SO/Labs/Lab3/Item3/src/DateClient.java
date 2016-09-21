/**
 * Client program requesting current date from server.
 *
 */
 
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class DateClient
{
	public static void main(String[] args) throws IOException {
		InputStream in = null;
		BufferedReader bin = null;
		Socket sock = null;

		try {
			sock = new Socket("127.0.0.1",6013);
			in = sock.getInputStream();
			bin = new BufferedReader(new InputStreamReader(in));

			// Send query
			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);

			switch (args.length)
			{
				case 1:
					System.out.println(">> " + args[0]);
					pout.println(args[0]);

					String inbox = null;
					while (true) 
					{
						inbox = bin.readLine();
						if (inbox != null) {
							System.out.println("<< " + inbox + "\n");
							break;
						}
					}		
					break;
			}
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
        finally {
            sock.close();
        }
	}
}
