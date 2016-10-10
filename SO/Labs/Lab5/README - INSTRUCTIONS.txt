HOW
TO
RUN
----------------------------------------------------------------------
1. Run the makefile:
	$ make

2. Give permission to the test files to run as programs
(Right click >> "Properties" >> "Permissions" >> "Execute" checkbox):
	* Statistics mode only:
		test-slides-statistics.sh
		test-comparison-statistics.sh
	
	* List mode only:
		test-slides-list.sh
		test-comparison-list.sh
	
	* Mixed mode:
		test-slides-all.sh
		test-comparison-all.sh

3. Run some tests:
	* The samples taught in the class slides (ufc_ck069_cap_5_Escalonamento2.pdf)
		$ ./test-slides-statistics.sh
		$ ./test-slides-list.sh
		$ ./test-slides-all.sh

	* A comparison (same test for everyone)
		$ ./test-comparison-statistics.sh
		$ ./test-comparison-list.sh
		$ ./test-comparison-all.sh