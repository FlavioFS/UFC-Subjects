/**
 * Client program requesting current date from server.
 *
 */

// package lab4;
 
import java.net.*;
import java.io.*;

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

			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
			
			switch (args.length)
			{
			case 1:	// Retrieve inbox
				pout.println(args[0]);							// Request inbox
				int lines = Integer.parseInt(bin.readLine());	// Inbox size
				
				if (lines == 0)
				{
					System.out.println("<< " + args[0] + "'s inbox is empty!");
				}
				else
				{
					// Header
					System.out.println("<< " + args[0] + "'s Inbox (" + lines + ")");
					
					// Inbox
					for (int i = 0; i < lines; i++)
						System.out.println(bin.readLine());
					
					System.out.println();
				}
				
				
				break;
				
			case 4:	// Send Message
				// Key Operator double1 double2
				String query = args[0] + ":" + args[1] + ":" + args[2] + ":" + args[3];
				
				pout.println(query);
				System.out.println(">> " + query);
				break;
				
			default:
				System.out.print("Client side default case! -> " + args.length + " arguments:\n[" + args[0]);
				
				for(int i = 1; i < args.length; i++)
				{
					System.out.print(", " + args[i]);
				}
				System.out.println("]");
				
				break;
			}
			
			sock.close();
			pout.close();
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
                finally {
                    sock.close();
                }
	}
}
