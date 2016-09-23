import java.util.HashMap;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.IOException;

public interface IDatabase extends Remote
{
	public String getMailbox (String key)       throws RemoteException;
	public void putMsg (String key, String msg) throws RemoteException, IOException;
}