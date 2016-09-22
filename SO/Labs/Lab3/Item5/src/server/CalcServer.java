import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CalcServer extends UnicastRemoteObject implements ICalcServer
{
	// Constructor
	public CalcServer () {}


	// Operations
	public String hiho () { return "hiho"; }

	public String sum (double arg1, double arg2) { return Double.toString(arg1 + arg2); }
	public String dif (double arg1, double arg2) { return Double.toString(arg1 - arg2); }
	public String mul (double arg1, double arg2) { return Double.toString(arg1 * arg2); }
	public String div (double arg1, double arg2)
	{
		try
		{
			return Double.toString(arg1 / arg2);
		}
		catch (Exception ex)
		{
			if (arg2 == 0)
			{
				System.err.println("Division by zero?");
				return "Error: Division by zero";
			}
			else
			{
				ex.printStackTrace();
				return "Unknown error occurred!";
			}
		}
	}


	// Main
	public static void main (String[] args)
	{
		if (System.getSecurityManager() == null) {
			System.setSecurityManager (new SecurityManager());
		}

		try
		{
			// System.setProperty("java.security.policy", "file:./access.policy");
			String name = "CalcServer";
			ICalcServer calculator = new CalcServer ();
			ICalcServer stub = (ICalcServer) UnicastRemoteObject.exportObject(calculator, 0);
			Registry registry = LocateRegistry.getRegistry(20000);
			registry.rebind(name, stub);
		}
		catch (Exception ex)
		{
			System.err.println("CalcServer exception:");
			ex.printStackTrace();
		}
	}
}