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
			Scanner outbox = new Scanner (System.in);
			String query;
			String inbox;
			while (true)
			{
				if ( (inbox = bin.readLine()) != null) {
					System.out.println(" << " + inbox);
				}

				System.out.print(" >> ");
				// query = input.next();
				pout.println(outbox.next());
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
