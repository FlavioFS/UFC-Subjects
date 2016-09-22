import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalcServer extends Remote
{
	public String hiho () throws RemoteException;

	public String sum (double arg1, double arg2) throws RemoteException;
	public String dif (double arg1, double arg2) throws RemoteException;
	public String mul (double arg1, double arg2) throws RemoteException;
	public String div (double arg1, double arg2) throws RemoteException;
}