/**
 * Time-of-day server listening to port 6013.
 *
 */
 
import java.net.*;
import java.io.*;

public class DateServer
{
	public static void main(String[] args) throws IOException {
		Socket client = null;
		ServerSocket sock = null;

		InputStream in = null;
		BufferedReader bin = null;

		try {
			sock = new ServerSocket(6013);
			// now listen for connections
			while (true) {
				client = sock.accept();
				System.out.println("server = " + sock);
				System.out.println("client = " + client);

				// we have a connection
				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
				// write the Date to the socket
				pout.println(new java.util.Date().toString());

				// Read inputs
				in = client.getInputStream();
				bin  = new BufferedReader(new InputStreamReader(in));

				String query;
				String[] queryArgs;
				double arg1, arg2;
				while (client != null)
				{
					query = bin.readLine();
					if (query != null)
					{
						System.out.println(" << " + query);
						queryArgs = query.split(":");

						for (int i = 0; i < queryArgs.length; i++) {
							System.out.println(queryArgs[i]);
						}

						if (queryArgs.length == 3) {
							try {
								System.out.println("Success!");

								arg1 = Double.parseDouble(queryArgs[1]);
								arg2 = Double.parseDouble(queryArgs[2]);

								if (queryArgs[0].equals("+")) {
									System.out.println(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
									pout.println(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
								}
								else if (queryArgs[0].equals("-")) {
									System.out.println(arg1 + " - " + arg2 + " = " + (arg1 - arg2));
									pout.println(arg1 + " - " + arg2 + " = " + (arg1 - arg2));
								}
								else if (queryArgs[0].equals("*")) {
									System.out.println(arg1 + " * " + arg2 + " = " + (arg1 * arg2));
									pout.println(arg1 + " * " + arg2 + " = " + (arg1 * arg2));	
								}
								else if (queryArgs[0].equals("/")) {
									if (arg2 == 0) {
										System.out.println(" >> Invalid arguments: Division by zero.");
										pout.println("Invalid arguments: Division by zero.");
									}
									else 
									{
										System.out.println(arg1 + " / " + arg2 + " = " + (arg1 / arg2));
										pout.println(arg1 + " / " + arg2 + " = " + (arg1 / arg2));
									}
								}

							} catch (Exception ex)
							{
								System.out.println(" >> Invalid arguments.");
								pout.println("Invalid arguments.");
							}
						}
						else
						{
							System.out.println(" >> Wrong usage. Try <operator>:<argument1>:<argument2> ~> operator: + - * /");
							pout.println("Wrong usage. Try <operator>:<argument1>:<argument2> ~> operator: + - * /");
						}
					}
				}

				pout.close();
				client.close();
			}
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
		finally {
			if (sock != null)
				sock.close();
			if (client != null)
				client.close();
		}
	}
}
