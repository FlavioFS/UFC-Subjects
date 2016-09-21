#include <errno.h>
#include <signal.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <cmath>

#define SQRT5_INV 2*sqrt(5)/10
#define GOLDEN1 (1 + sqrt(5))/2
#define GOLDEN2 (1 - sqrt(5))/2

// [1] General formula
double fib (int n) {
	return SQRT5_INV * ( powf(GOLDEN1,n) - powf(GOLDEN2,n) );
}

// [2] Used by Parent and Children
void calcFibs (double *fibs, int start, int first, int size, int childCount, const char* name)
{
	int mypid = getpid();

	for (int i = start; i < size; i += 1+childCount) {
		fibs[i] = fib(i+first);
		printf("%s(%4d): fibs(%2d) == %g\n", name, mypid, i+first, fibs[i]);
	}
}

// [3] Used by Parent at the end
void displayResult (double *fibs, int first, int last, int size)
{
	printf("\n");

	printf("Fibs[%d ~ %d] = [%g", first, last, fibs[0]);
	for (int i = 1; i < size; i++)
	{
		printf(", %g", fibs[i]);
	}
	printf("]\n\n");
}

// 
void itemB (int first, int last)
{
	double *fibs;
	int childCount = 0;

	// Empty array
	int size = last - first + 1;
	fibs = new double [size];
	for (int i = 0; i < size; i++)
		fibs[i] = 0;

	// Threading
	pid_t pid = vfork();
	switch (pid)
	{
		case -1:	// Error
			printf("Fork failed!\n");
			break;
		
		case 0:		// Child
			childCount++;
			calcFibs(fibs, childCount, first, size, childCount, " Child");
			exit(0);
			break;

		default:	// Parent
			calcFibs(fibs,          0, first, size, childCount, "Parent");
			wait();
			displayResult(fibs, first, last, size);
			break;
	}
}


int main(int argc, char const *argv[])
{
	double *fibs;

	int
		first = 0,
		last = 0;

	if (argc == 3)
	{
		// Handling arguments
		first = atoi(argv[1]);
		last = atoi(argv[2]);

		if (first > last)
		{
			int temp = first;
			first = last;
			last = temp;
		}


		// Does the actual calculation
		printf("=============== MULTI  THREADED FIBONACCI (ITEM B) =============== \n");
		itemB(first, last);
	}

	exit(0);
}