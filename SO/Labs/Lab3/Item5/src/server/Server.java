import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
	public static void main (String[] args)
	{
		try
		{
			String name = "calckey";
			
			ICalc calculator = new Calc ();
			ICalc stub = (ICalc) UnicastRemoteObject.exportObject(calculator, 0);

			// System.setProperty("java.rmi.server.hostname", "localhost");
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(name, stub);

			System.out.println("Server ready...");
		}
		catch (Exception ex)
		{
			System.err.println("Server exception:");
			ex.printStackTrace();
		}
	}
}