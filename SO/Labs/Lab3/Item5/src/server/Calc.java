import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Calc implements ICalc
{
	// Constructor
	public Calc () throws RemoteException {}


	// Operations
	public String hiho () throws RemoteException { return "hiho"; }

	public String sum (double arg1, double arg2) throws RemoteException { return Double.toString(arg1 + arg2); }
	public String dif (double arg1, double arg2) throws RemoteException { return Double.toString(arg1 - arg2); }
	public String mul (double arg1, double arg2) throws RemoteException { return Double.toString(arg1 * arg2); }
	public String div (double arg1, double arg2) throws RemoteException
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
}