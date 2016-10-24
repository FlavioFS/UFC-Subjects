
public class Avoid {
	final static int REQUEST_EXCEEDED = 0;
	final static int REQUEST_DENIED = 1;
	final static int REQUEST_OK = 2;
	
	public static void main(String[] args) {
		try
		{
			SystemState _sysState = new SystemState(args[0]);
			
			switch (isAvoidable(_sysState))
			{
			case REQUEST_EXCEEDED:
				System.out.println("EXCEEDED: request larger than needs.");
				break;
				
			case REQUEST_DENIED:
				System.out.println("DENIED: resources unavailable.");
				break;
				
			case REQUEST_OK:
				System.out.println("OK: request approved.");
				break;
			}
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}
	
	
	private static int isAvoidable (SystemState state)
	{
		if (state.isExceededRequest()) return REQUEST_EXCEEDED;
		
		boolean lastTurn = true;
		
		// Run this until no unfinished process can be finished
		do {
			lastTurn = true;
			
			// Finishes every possible process in list
			for (int i = 0; i < state.unfinished().size(); i++) {
				if (state.isLastTurn(i)) {
					state.finish(i);
					lastTurn = false;
					--i;
				}
			}
		}
		while (!lastTurn); // In last turn nothing happens
		
		return state.unfinished().isEmpty();
	}
}
