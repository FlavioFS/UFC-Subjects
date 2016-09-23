import java.util.ArrayList;

public class Mailbox
{
	// Attributes
	private ArrayList<String> messages;
	
	// Constructor
	public Mailbox () { this.messages = new ArrayList<String>(); }

	// Methods
	int size () { return messages.size(); }

	// Remote Interface Methods
	public ArrayList<String> msgList() { return this.messages; }
	public void put (String msg)       { this.messages.add(msg); }
}