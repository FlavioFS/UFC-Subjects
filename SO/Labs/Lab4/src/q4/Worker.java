class Worker implements Runnable
{
	private String message;

	public Worker (String message)
	{
		this.message = message;
	}

	public void run ()
	{
		System.out.println("Thread {" + Thread.currentThread().getName() + "} (Start): " + this.message);
		
		// Simulates processing
		try { Thread.sleep(2000); }
		catch (InterruptedException ex) { ex.printStackTrace(); }
		
		System.out.println("Thread {" + Thread.currentThread().getName() + "} (End  ): " + this.message);
	}
}