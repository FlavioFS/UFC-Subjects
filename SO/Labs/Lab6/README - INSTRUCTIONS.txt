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

~~> Providing arguments to the program will de-activate the "jumper"
	feature (which is a good thing!), but it is not possible in Eclipse
	due to a bug!