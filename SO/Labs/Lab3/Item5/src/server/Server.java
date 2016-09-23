import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.*;
import java.rmi.RemoteException;

public class Server
{
	public static void main (String[] args)
	{
		try
		{
			String name = "dbkey";

			IDatabase db = new Database ();
			IDatabase stub = (IDatabase) UnicastRemoteObject.exportObject(db, 0);

			// String name = "calckey";
			// ICalc calculator = new Calc ();
			// ICalc stub = (ICalc) UnicastRemoteObject.exportObject(calculator, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(name, stub);

			System.out.println("Server ready...");

			// new Thread ()
			// {
			// 	public void run (ICalc calculator) throws RemoteException, InterruptedException
			// 	{
			// 		while (true)
			// 		{
			// 			System.out.println(calculator.getLast());
			// 			Thread.sleep(2000);
			// 		}
			// 	}
			// }.run(calculator);

		}
		catch (Exception ex)
		{
			System.err.println("Server exception:");
			ex.printStackTrace();
		}
	}
}