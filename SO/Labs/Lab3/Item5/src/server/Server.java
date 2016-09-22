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
			String name = "calckey";
			
			ICalc calculator = new Calc ();
			ICalc stub = (ICalc) UnicastRemoteObject.exportObject(calculator, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(name, stub);

			System.out.println("Server ready...");

			// String lastCalc = calculator.getLast();
			// String now = calculator.getLast();
			// int i = 0;
			// while (true)
			// {
			// 	// now = calculator.getLast();
			// 	if ((i % 2000000000) == 0) {
			// 		System.out.println(i + calculator.getLast());
			// 		// lastCalc = now;
			// 	}
			// 	i++;
			// 	sleep()
			// }

			new Thread ()
			{
				public void run (ICalc calculator) throws RemoteException, InterruptedException
				{
					while (true)
					{
						System.out.println(calculator.getLast());
						Thread.sleep(2000);
					}
				}
			}.run(calculator);

		}
		catch (Exception ex)
		{
			System.err.println("Server exception:");
			ex.printStackTrace();
		}
	}
}