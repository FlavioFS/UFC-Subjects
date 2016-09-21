import java.io.IOException;
import java.util.ArrayList;

public class Inbox
{
	private ArrayList<String> messages;
	
	public Inbox () throws IOException
	{
		this.messages = new ArrayList<String>();
	}
	
	public ArrayList<String> msgList() { return this.messages; }
	public void put (String msg) { this.messages.add(msg); }
}