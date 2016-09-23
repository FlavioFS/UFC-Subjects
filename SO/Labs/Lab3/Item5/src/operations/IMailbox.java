import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMailbox extends Remote
{
	public Mailbox () throws RemoteException;
	public ArrayList<String> msgList() throws RemoteException;
	public void put (String msg)       throws RemoteException;
}