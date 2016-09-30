import java.net.*;
import java.io.*;

class Worker extends Thread
{
	private Socket client;

	public Worker (Socket client) { this.client = client; }

	public void run ()
	{
		System.out.println("Thread {" + Thread.currentThread().getName() + "} (Start)");
		
		// Simulates processing
		try { Thread.sleep(1000); }
		catch (InterruptedException ex) { ex.printStackTrace(); }

		// Output
		try
		{
			PrintWriter pout = new PrintWriter (client.getOutputStream(), true);
			String msg = new java.util.Date().toString();
			System.out.println("Thread {" + Thread.currentThread().getName() + "} >> " + msg);
			pout.println(msg);
			client.close();
		}
		catch (IOException ex) { ex.printStackTrace(); }
		

		System.out.println("Thread {" + Thread.currentThread().getName() + "} (End  )\n");
	}
}