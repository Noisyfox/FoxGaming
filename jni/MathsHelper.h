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

double randomGauss();

// 生成一个高斯分布数列
void generateGaussianDistribution();

// 生成一个介于 0 和 1 之间的符合高斯分布的随机数
double randomGaussian();

double toRadians(double angdeg);

double toDegrees(double angrad);

// 返回指定长度及方向的矢量线在 x 轴上的投影长度.
float lengthdir_x(float len, float dir);

// 返回指定长度及方向的矢量线在 y 轴上的投影长度.
float lengthdir_y(float len, float dir);

// 返回位置1(x1,y1)到位置2(x2,y2)的距离.
float point_distance(float x1, float y1, float x2, float y2);

// 返回从位置1(x1,y1)到位置2(x2,y2)的方向角度
float point_direction(float x1, float y1, float x2, float y2);

// 返回从 from 角度转向 to 角度所经过的最少角度，正值逆时针旋转，负值顺时针旋转
float directionTo(float from, float to);

float degreeIn360(float deg);

#ifdef __cplusplus
}
#endif
#endif /* MATHSHELPER_H_ */
