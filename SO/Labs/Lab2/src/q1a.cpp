#include <errno.h>
#include <signal.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <cmath>

#define SQRT5_INV 2*sqrt(5)/10
#define GOLDEN1 (1 + sqrt(5))/2
#define GOLDEN2 (1 - sqrt(5))/2

double fib (int n)
{
	return SQRT5_INV * ( powf(GOLDEN1,n) - powf(GOLDEN2,n) );
}

void itemA (int first, int last)
{
	double *fibs;

	// Empty array
	int size = last - first + 1;
	fibs = new double [size];
	for (int i = 0; i < size; i++)
		fibs[i] = 0;

	// Calculate
	for (int i = 0; i <= size; i++) {
		fibs[i] = fib(i+first);
	}

	// Display
	printf("Fibs[%d ~ %d] = [%g", first, last, fibs[0]);
	for (int i = 1; i < size; i++)
	{
		printf(", %g", fibs[i]);
	}
	printf("]\n\n");
}


int main(int argc, char const *argv[])
{
	double *fibs;

	int
		first = 0,
		last = 0;

	if (argc == 3)
	{
		first = atoi(argv[1]);
		last = atoi(argv[2]);

		if (first > last)
		{
			int temp = first;
			first = last;
			last = temp;
		}

		printf("=============== SINGLE THREADED FIBONACCI (ITEM A) =============== \n");
		itemA(first, last);
	}

	exit(0);
}