HOW
TO
RUN
----------------------------------------------------------------------

/* ===================================================================
 *   Shell Instructions
 * =================================================================== */
1. Run the makefile:
	$ make

2. Give the following files permission to run as programs
(Right click >> "Properties" >> "Permissions" >> "Execute" checkbox):
	* 1.a.producers-consumers.sh
	* 2.a.readers-writers-semaphore.sh
	* 2.b.readers-writers-monitor.sh
	* 3.a.philosophers-semaphore.sh 	(recommended!)
	* 3.b.philosophers-monitor.sh

3. Run it
	$ ./test-everything.sh



/* ===================================================================
 *   Eclipse Instructions for the Philosophers Example
 * =================================================================== */
 1. Copy this: -DrunInEclipse=true

 2. In Eclipse, open "Run Configurations".

 3. Go to the Arguments tab, in the "VM arguments" section and paste it
 	(it is the "VM Arguments", not the "Program Arguments").

 4. Run some example (PhilosophersMonitorExample.java,
 	PhilosophersSemaphoreExample.java)