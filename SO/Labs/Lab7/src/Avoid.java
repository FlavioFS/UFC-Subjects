import java.io.IOException;


public class Avoid {
	final static int REQUEST_EXCEEDED = 0;
	final static int REQUEST_WAIT = 1;
	final static int REQUEST_OK = 2;
	final static int REQUEST_DENIED = 3;
	
	public static void main(String[] args) {
		SystemState _sysState;
		Process _proc;
		
		// Loading State
		try
		{
			_sysState = new SystemState(args[0]);
			
			// Loading Process Request
			try {
				_proc = _sysState.loadRequest(args[1]);
			} catch (IOException ex) {
				System.err.println("Error: Request file not found!");
				return;
			}
			
		} catch (IOException ex) {
			System.err.println("Error: State file not found!");
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
	
	
	private static int isAvoidable (SystemState stateArg, Process proc)
	{
		SystemState state = new SystemState(stateArg);	// Non-destructive copy
		
		if		(state.isExceededRequest(proc))		return REQUEST_EXCEEDED;
		else if	(state.isUnfeasibleRequest(proc))	return REQUEST_WAIT;
		
		state.give(proc.getID(), proc.getRequestA(), proc.getRequestB(), proc.getRequestC());
		if (Safety.isSafeState(state)) return REQUEST_OK;
		
		return REQUEST_DENIED;
	}
}
