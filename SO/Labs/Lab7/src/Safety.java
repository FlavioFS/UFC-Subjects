public class Safety {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			SystemState _sysState = new SystemState("../resource-list.txt");
			String msg = isSafeState(_sysState) ? "Safety: safe." : "Safety: NOT safe."; 
			System.out.println(msg);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}
	
	
	private static boolean isSafeState (SystemState state)
	{
		// TODO Coding this...
		//while ()
		
		return true;
	}
}
