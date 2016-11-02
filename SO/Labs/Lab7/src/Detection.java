import java.io.IOException;

public class Detection {
	
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
		if (isDeadlocked(_sysState)) {
			System.out.print("Deadlock? Yes. ~~> ");
			_sysState.printUnfinished();
		}
		
		else
			System.out.print("Deadlock? No.");
	}
	
	
	public static boolean isDeadlocked (SystemState state)
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
		while (!lastTurn); // In last turn nothing happens
		
		return state.unfinished().isEmpty();
	}
}
