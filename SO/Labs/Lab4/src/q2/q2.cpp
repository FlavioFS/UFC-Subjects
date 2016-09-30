#include <pthread.h>
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#define SQRT5_INV 2*sqrt(5)/10
#define GOLDEN1 (1 + sqrt(5))/2
#define GOLDEN2 (1 - sqrt(5))/2
#define THREAD_COUNT 4

double *fibs;
int
	first = 0,
	last = 0,
	size = 0;

double fib (int n);
void calcFibs ();
void displayResult (pthread_t *tid);
void *runner(void *param); // Thread run() function


/* ========================================================================================
 *   MAIN - Verifies the arguments and triggers calculation when valid.
 * ======================================================================================== */
int main(int argc, char const *argv[])
{
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

		printf("=============== PTHREADS FIBONACCI =============== \n");
		calcFibs();
	}

	return 1;
}


/* ========================================================================================
 *   Calculates fibonacci(n), given an integer n.
 * ======================================================================================== */
double fib (int n)
{
	return SQRT5_INV * ( powf(GOLDEN1,n) - powf(GOLDEN2,n) );
}


/* ========================================================================================
 *   Creates <THREAD_COUNT> threads and assigns computations to them.
 * ======================================================================================== */
void calcFibs ()
{
	pthread_t tid[THREAD_COUNT];
	pthread_attr_t attr[THREAD_COUNT];

	int offset = 0;
	for (offset = 0; offset < THREAD_COUNT; offset++)
	{
		pthread_attr_init(&attr[offset]);
		pthread_create(&tid[offset], &attr[offset], runner, (void *)&offset);
	}

	// Empty array
	size = last - first + 1;
	fibs = new double [size];
	for (int i = 0; i < size; i++)
		fibs[i] = 0;

	displayResult(tid);

	for (int i = 0; i < THREAD_COUNT; i++)
	{
		pthread_join(tid[i], NULL);
	}
}


/* ========================================================================================
 *   Displays the result of every thread.
 * ======================================================================================== */
void displayResult (pthread_t *tid)
{
	// Result by thread
	for (int i = 0; i < THREAD_COUNT; i++)
	{
		printf("Thread(%ld) ~~~~~~~\n", tid[i]);
		for (int j = i; j < size; j+=THREAD_COUNT)
		{
			printf("F(%2d): %g\n", j + first, fibs[j]);
		}
		printf("\n");
	}

	// Combined result
	printf("--------------- Combined Result --------------- \n");
	printf("Fibs[%d ~ %d]", first, last);
	for (int i = 0; i < size; i++)
	{
		printf("(%ld) F(%2d): %g\n", tid[i%4], i + first, fibs[i]);
	}
	printf("\n");
}


/* ========================================================================================
 *   What each thread runs
 * ======================================================================================== */
void *runner(void *param)
{
	int *offset = (int *) param;

	// Calculate
	for (int i = *offset; i <= size; i+=THREAD_COUNT) {
		fibs[i] = fib(i+first);
	}

	pthread_exit(0);
}