/**
 * Time-of-day server listening to port 6013.
 *
 */
 
// package lab4;
 
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class DateServer 
{	
	static Database db = new Database ();
	
	static void sendHistory (String key, PrintWriter pout)
	{
		if (!db.contains(key))
		{
			pout.println("0");
			return;
		}
		
		Inbox box = db.getInbox(key);
		ArrayList<String> msgList = box.msgList();
		
		// Display: <size> \n message1 \n message2 ...
		String response = msgList.size() + "\n";
		for (String msg : msgList) { response += (msg + "\n"); }
		
		pout.print(response);
	}
	
	public static void main(String[] args) throws IOException {
		
		Socket client = null;
		final ServerSocket sock = new ServerSocket(6013);
	
		InputStream in = null;
		BufferedReader bin = null;

		try {
			// now listen for connections
			while (true) {
				client = sock.accept();
				
				System.out.println("<~~~~~~~~ New query arriving ~~~~~~~~\nserver = " + sock + "\nclient = " + client);

				// we have a connection
				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

				// Read inputs
				in = client.getInputStream();
				bin = new BufferedReader(new InputStreamReader(in));

				// Handles query
				String query = bin.readLine();
				if (query != null)
				{
					System.out.println("<< " + query);
					String[] queryArgs = query.split(":");
					
					// Display split query args
					System.out.print("Args: [" + queryArgs[0]);
					for (int i = 1; i < queryArgs.length; i++) {
						System.out.print(", " + queryArgs[i]);
					}
					System.out.println("]");
					
					switch (queryArgs.length) {
					case 1:	// Request message history
						String key = queryArgs[0];
						sendHistory(key, pout);
						System.out.println(">> Sending " + key + "'s inbox");
						break;
					
					case 4: // New query
						try {
							String target = queryArgs[0];
							String opCode = queryArgs[1];
							
							double arg1 = Double.parseDouble(queryArgs[2]);
							double arg2 = Double.parseDouble(queryArgs[3]);
							
							String result;
							if (opCode.equals("+")) {
								result = Double.toString(arg1 + arg2);
								System.out.println(arg1 + " + " + arg2 + " = " + result + "\n>> " + result);
								db.putMsg(target, result);
							}
							else if (opCode.equals("-")) {
								result = Double.toString(arg1 - arg2);
								System.out.println(arg1 + " - " + arg2 + " = " + result + "\n>> " + result);
								db.putMsg(target, result);
							}
							else if (opCode.equals("x")) {
								result = Double.toString(arg1 * arg2);
								System.out.println(arg1 + " x " + arg2 + " = " + result + "\n>> " + result);
								db.putMsg(target, result);
							}
							else if (opCode.equals("/")) {
								if (arg2 == 0) {
									System.out.println(">> Invalid arguments: Division by zero.");
									pout.println("Error: Division by zero.");
								}
								else 
								{
									result = Double.toString(arg1 / arg2);
									System.out.println(arg1 + " / " + arg2 + " = " + result + "\n>> " + result);
									db.putMsg(target, result);
								}
							}

						} catch (Exception ex)
						{
							System.out.println(">> Invalid arguments.\n");
							ex.printStackTrace();
							pout.println("Invalid arguments.");
						}
						break;

					default:
						System.out.println(">> Wrong usage. Try <operator>:<argument1>:<argument2> ~> operator: + - . /");
						pout.println("Wrong usage. Try <operator>:<argument1>:<argument2> ~> operator: + - . /");
						break;
					}

					System.out.println();
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
