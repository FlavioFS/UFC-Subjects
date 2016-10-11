HOW
TO
RUN
----------------------------------------------------------------------
1. Run the makefile:
	$ make

2. Give the following files permission to run as a program
(Right click >> "Properties" >> "Permissions" >> "Execute" checkbox):
	* test-everything.sh
	* test-debug.sh

3. The result of running them (in this order):
	$ ./test-everything.sh
		Outputs all the possible tests (small, large and mixed processes)
		in the folder "out/".

	$ ./test-graphs.sh
		Generates CSV files that serve as base to draw comparison
		graphs in Octave/Matlab. Outputs to folder graphs/<file>.csv,
		where the files may be 'small.csv', 'large.csv', 'mixed.csv'.

4. Run gengraphs.m in octave to produce a comparison graph:
	$ octave
	$ gengraphs.mixed