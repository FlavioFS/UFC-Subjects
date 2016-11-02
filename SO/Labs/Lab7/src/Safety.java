import java.io.IOException;

public class Safety {
	public static void main(String[] args) {
		SystemState _sysState;
		
		// Loading State
		try {
			_sysState = new SystemState(args[0]);
		} catch (IOException ex) {
			System.err.println("Error: State file not found!");
			return;
		}
		
		// Verifying
		if (isSafeState(_sysState)) {
			System.out.print("Safety: safe. ~~> ");
			_sysState.printFinished();
		}
		
		else
			System.out.print("Safety: NOT safe.");
	}
	
	
	public static boolean isSafeState (SystemState state)
	{
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
		while (!lastTurn); // In last turn no changes occur
		
		return state.unfinished().isEmpty();
	}
}
