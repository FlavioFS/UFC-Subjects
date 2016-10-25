
public class Avoid {
	final static int REQUEST_EXCEEDED = 0;
	final static int REQUEST_WAIT = 1;
	final static int REQUEST_OK = 2;
	final static int REQUEST_DENIED = 3;
	
	public static void main(String[] args) {
		try
		{
			SystemState _sysState = new SystemState(args[0]);
			Process _proc = _sysState.loadRequest(args[1]);
			
			// Invalid Process request file
			if (_proc == null)
			{
				System.err.println("No request found");
				return;
			}
			
			// Result
			switch (isAvoidable(_sysState, _proc))
			{
			case REQUEST_EXCEEDED:
				System.out.println("EXCEEDED: request larger than needs.");
				break;
				
			case REQUEST_WAIT:
				System.out.println("WAIT: resources unavailable.");
				break;
				
			case REQUEST_OK:
				System.out.println("OK: request approved.");
				break;
				
			case REQUEST_DENIED:
				System.out.println("DENIED: dangerous state!");
				break;
			}
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}
	
	
	private static int isAvoidable (SystemState state, Process proc)
	{
		if		(state.isExceededRequest(proc))		return REQUEST_EXCEEDED;
		else if	(state.isUnfeasibleRequest(proc))	return REQUEST_WAIT;
		
		if (Safety.isSafeState(state)) return REQUEST_OK;
		
		return REQUEST_DENIED;
	}
}
