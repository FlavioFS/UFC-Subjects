import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client
{
	public static void main (String[] args)
	{
		try
		{
			String name = "calckey";

			Registry registry = LocateRegistry.getRegistry(1099);
			ICalc stub = (ICalc) registry.lookup(name);

			System.out.println(stub.hiho());
		}
		catch (Exception ex)
		{
			System.err.println("Client exception:");
			ex.printStackTrace();
		}
	}
}