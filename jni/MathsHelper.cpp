/*
 * MathsHelper.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#include <stdlib.h>

#include <MathsHelper.h>

//Returns a pseudo-random uniformly distributed int in the half-open range [0, n).
int randomInt(int n) {
	if (n <= 0)
		return 0;

	return (int) ((float) n * rand() / (RAND_MAX + 1.0));
}

//Returns a pseudo-random uniformly distributed double in the half-open range [0, n).
double randomDouble(double n) {
	if (n <= 0)
		return 0;

	return (float) n * rand() / (RAND_MAX + 1.0);
}

//Returns a pseudo-random uniformly distributed int in the half-open range [min, max).
int randomIntR(int min, int max) {
	return randomInt(max - min) + min;
}

//Returns a pseudo-random uniformly distributed double in the half-open range [min, max).
int randomDoubleR(double min, double max) {
	return randomDouble(max - min) + min;
}
