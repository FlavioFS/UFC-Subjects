import java.rmi.RemoteException;

public class Calc implements ICalc
{
	String lastCalc;

	// Constructor
	public Calc () throws RemoteException { this.lastCalc = ""; }

	// Operations
	public String hiho () throws RemoteException { this.lastCalc = "hiho"; return lastCalc; }

	public String sum (double arg1, double arg2) throws RemoteException { this.lastCalc = Double.toString(arg1 + arg2); return lastCalc; }
	public String dif (double arg1, double arg2) throws RemoteException { this.lastCalc = Double.toString(arg1 - arg2); return lastCalc; }
	public String mul (double arg1, double arg2) throws RemoteException { this.lastCalc = Double.toString(arg1 * arg2); return lastCalc; }
	public String div (double arg1, double arg2) throws RemoteException
	{
		try
		{
			this.lastCalc = Double.toString(arg1 / arg2);
			return lastCalc;
		}
		catch (Exception ex)
		{
			if (arg2 == 0)
			{
				System.err.println("Division by zero?");
				this.lastCalc = "Error: Division by zero";
				return lastCalc;
			}
			else
			{
				ex.printStackTrace();
				this.lastCalc = "Unknown error occurred!";
				return lastCalc;
			}
		}
	}

	public String getLast () throws RemoteException { return lastCalc; }
}