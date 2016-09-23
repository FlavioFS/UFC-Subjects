import java.util.ArrayList;
import java.rmi.RemoteException;

public class Mailbox
{
	private ArrayList<String> messages;
	
	public Mailbox ()                  throws RemoteException { this.messages = new ArrayList<String>(); }
	public ArrayList<String> msgList() throws RemoteException { return this.messages; }
	public void put (String msg)       throws RemoteException { this.messages.add(msg); }
}