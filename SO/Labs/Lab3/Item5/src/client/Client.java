import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.lang.*;
import java.rmi.RemoteException;

public class Client
{
	public static void main (String[] args)
	{
		try
		{
			String name = "dbkey";

			Registry registry = LocateRegistry.getRegistry(1099);
			IDatabase stub = (IDatabase) registry.lookup(name);

			String key, msg;
			switch (args.length)
			{
				case 1:	// Request Inbox
					key = args[0];
					System.out.println("\n" + stub.getMailbox(key));
					break;

				case 4:	// Send Message
					key = args[0];
					msg = args[1] + ":" + args[2] + ":" + args[3]; // operator:arg1:arg2
					stub.putMsg(key, msg);
					System.out.println(">> Message sent to '" + key + "': " + msg);
					break;
			}
		}
		catch (Exception ex)
		{
			System.err.println("Client exception:");
			ex.printStackTrace();
		}
	}
}