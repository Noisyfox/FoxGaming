/*
 * MathsHelper.h
 *
 *  Created on: 2013-1-16
 *      Author: Noisyfox
 */

#ifndef MATHSHELPER_H_
#define MATHSHELPER_H_
#ifdef __cplusplus
extern "C" {
#endif

//Returns a pseudo-random uniformly distributed int in the half-open range [0, n).
int randomInt(int n);

//Returns a pseudo-random uniformly distributed double in the half-open range [0, n).
double randomDouble(double n);

//Returns a pseudo-random uniformly distributed int in the half-open range [min, max).
int randomIntR(int min, int max);

//Returns a pseudo-random uniformly distributed double in the half-open range [min, max).
int randomDoubleR(double min, double max);

#ifdef __cplusplus
}
#endif
#endif /* MATHSHELPER_H_ */
