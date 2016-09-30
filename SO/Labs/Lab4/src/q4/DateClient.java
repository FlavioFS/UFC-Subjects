/**
 * Client program requesting current date from server.
 *
 */
 
import java.net.*;
import java.io.*;

public class DateClient
{
	public static void main(String[] args) throws IOException
	{
		Socket sock = new Socket("127.0.0.1", 6013);
		
		try {
			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			String inbox = null;
			
			while (true) 
			{
				inbox = bin.readLine();
				if (inbox != null)
				{
					System.out.println("<< " + inbox);
					break;
				}
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
