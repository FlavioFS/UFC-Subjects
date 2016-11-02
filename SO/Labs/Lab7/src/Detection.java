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
		if (Safety.isSafeState(_sysState))
			System.out.print("Deadlock? No.");
		
		else {
			System.out.print("Deadlock? Yes. ~~> ");
			_sysState.printUnfinished();
		}
	}
}
