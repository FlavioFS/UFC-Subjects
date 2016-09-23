import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.rmi.RemoteException;

public class Database implements IDatabase
{
	// Attributes
	private HashMap<String, Mailbox> db;
	
	// Local Methods
	public Database ()                      { db = new HashMap<String, Mailbox>(); }
	public boolean contains (String key)    { return db.containsKey(key); }
	public String solveQuery (String query)
	{
		String[] queryArgs = query.split(":");
		
		// Bad query!
		if (queryArgs.length != 3) {
			System.err.println("Invalid query!");
			return null;
		}

		String operator = queryArgs[0];
		double
			arg1 = Double.parseDouble(queryArgs[1]),
			arg2 = Double.parseDouble(queryArgs[2]);

		// Division by zero
		if (operator.equals("/") && (arg2 == 0)) {
			System.err.println("Division by zero!");
			return null;
		}

		// Good cases
		else if (operator.equals("+")) { return String.valueOf(arg1 + arg2); }
		else if (operator.equals("-")) { return String.valueOf(arg1 - arg2); }
		else if (operator.equals("x")) { return String.valueOf(arg1 * arg2); }
		else if (operator.equals("/")) { return String.valueOf(arg1 / arg2); }

		// Unsupported operator
		else
		{
			System.err.println("Invalid operator!");
			return null;
		}
	}


	// Remote Interface Methods
	public String getMailbox (String key) throws RemoteException
	{
		System.out.println("<< Request: " + key);
		System.out.println(">> Sending Mailbox: " + key);

		String rv = "";
		
		ArrayList<String> messageList = db.get(key).msgList();
		rv += key + "'s mailbox (" + messageList.size() + ")";
		for (int i = 0; i < messageList.size(); i++) {
			rv += "\n" + messageList.get(i);
		}
		
		return rv;
	}
	

	public void putMsg (String key, String msg) throws RemoteException, IOException
	{
		System.out.println("<< Request: " + key + ", " + msg);
		
		String answer = solveQuery(msg);

		// Bad query! Log it and ignore
		if (answer == null)	return;

		if (!db.containsKey(key))
		{
			db.put(key, new Mailbox ());
		}
		
		db.get(key).put(answer);

		
		System.out.println(">> Sending message to '" + key + "': " + answer + "\n");
	}
}