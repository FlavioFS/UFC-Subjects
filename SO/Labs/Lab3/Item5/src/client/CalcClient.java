import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalcClient
{
	public static void main (String[] args)
	{
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager ());
		}

		try
		{
			String name = "Calculator";
			Registry registry = LocateRegistry.getRegistry(args[0]);
			ICalcServer calculator = (ICalcServer) registry.lookup(name);

			System.out.println(calculator.hiho());
		}
		catch (Exception ex)
		{
			System.err.println("CalcClient exception:");
			ex.printStackTrace();
		}
	}
}