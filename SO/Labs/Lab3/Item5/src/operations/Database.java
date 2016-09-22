import java.io.IOException;
import java.util.HashMap;

public class Database
{
	private HashMap<String, Inbox> db;
	
	public Database () {
		db = new HashMap<String, Inbox>();
	}
	
	
	public Inbox getInbox (String key) { return db.get(key); }
	
	public void putMsg (String key, String msg) throws IOException {
		if (!db.containsKey(key))
		{
			db.put(key, new Inbox ());
		}
		
		db.get(key).put(msg);
	}
	
	public boolean contains (String key) { return db.containsKey(key); }
}