HOW
TO
RUN
----------------------------------------------------------------------
1. Run the makefile:
	$ make

2. Give the following files permission to run as programs
(Right click >> "Properties" >> "Permissions" >> "Execute" checkbox):
	* test-everything.sh
	* test-graphs.sh

3. Two usages (running each of them):
	3.1. Console/txt result
		$ ./test-everything.sh
			Outputs all the possible tests (small, large and mixed processes)
			in the folder "out/".

	3.2. Graphical comparison
		$ ./test-graphs.sh
			Generates CSV files that serve as base to draw comparison
			graphs in Octave/Matlab. Outputs to folder graphs/<file>.csv,
			where the files may be 'small.csv', 'large.csv', 'mixed.csv'.

		$ octave
		> gengraphs
			Generates the graphs in folder 'graphs/'.