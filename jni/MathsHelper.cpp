/*
 * MathsHelper.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#include <stdlib.h>
#include <math.h>

#include <MathsHelper.h>

#define GaussianDistributionListLength 10000L

double GaussianDistributionList[GaussianDistributionListLength];
double GaussianDistributionMax = 0.0;

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

double randomGauss() {
	static int set = 1;
	static double gset;
	double v1, v2, r, fac;
	if (set) {
		do {
			double U1 = (double) rand() / RAND_MAX;
			double U2 = (double) rand() / RAND_MAX;
			v1 = 2.0 * U1 - 1.0;
			v2 = 2.0 * U2 - 1.0;
			r = v1 * v1 + v2 * v2;
		} while (r > 1.0);
		fac = sqrt(-2.0 * log(r) / r);
		gset = v1 * fac;
		set = 0;
		return (v2 * fac);
	} else {
		set = 1;
		return (gset);
	}
}

// 生成一个高斯分布数列
void generateGaussianDistribution() {
	for (int i = 0; i < GaussianDistributionListLength; i++) {
		GaussianDistributionList[i] = fabs(randomGauss());
		GaussianDistributionMax = fmax(GaussianDistributionMax,
				GaussianDistributionList[i]);
		GaussianDistributionMax = fmin(GaussianDistributionMax, 6.0);
	}
}

// 生成一个介于 0 和 1 之间的符合高斯分布的随机数
double randomGaussian() {

	while (GaussianDistributionMax < 0.0001) {
		generateGaussianDistribution();
	}

	int index = randomInt(GaussianDistributionListLength);

	if (GaussianDistributionMax < 0.0001) {
		return 0;
	}

	return GaussianDistributionList[index] / GaussianDistributionMax;
}

double toRadians(double angdeg) {
	return angdeg * M_PI / 180.0;
}

double toDegrees(double angrad) {
	return angrad * 180.0 / M_PI;
}

// 返回指定长度及方向的矢量线在 x 轴上的投影长度.
float lengthdir_x(float len, float dir) {
	return (float) (len * cos(toRadians(dir)));
}

// 返回指定长度及方向的矢量线在 y 轴上的投影长度.
float lengthdir_y(float len, float dir) {
	return (float) (len * sin(toRadians(dir)));
}

// 返回位置1(x1,y1)到位置2(x2,y2)的距离.
float point_distance(float x1, float y1, float x2, float y2) {
	return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
}

// 返回从位置1(x1,y1)到位置2(x2,y2)的方向角度
float point_direction(float x1, float y1, float x2, float y2) {
	return toDegrees(atan2(y1 - y2, x2 - x1));
}

// 返回从 from 角度转向 to 角度所经过的最少角度，正值逆时针旋转，负值顺时针旋转
float directionTo(float from, float to) {
	// 旋转坐标系使 from 对应的终边落在X轴正方向上
	to -= from;
	// 转化到360度以内
	to = degreeIn360(to);

	return to <= 180 ? to : 180 - to;
}

float degreeIn360(float deg) {
	if (deg >= 0) {
		float deg2 = deg - (int) deg;
		deg = ((int) deg) % 360;
		deg += deg2;
	} else {
		deg *= -1;
		float deg2 = deg - (int) deg;
		deg = ((int) deg) % 360;
		deg = 360 - deg - deg2;
	}
	return deg;
}
