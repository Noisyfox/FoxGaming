/**
 * FileName:     MathsHelper.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-13 下午2:49:29
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-13      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.Random;

import org.foxteam.noisyfox.FoxGaming.G2D.FGCircle;
import org.foxteam.noisyfox.FoxGaming.G2D.FGPoint;
import org.foxteam.noisyfox.FoxGaming.G2D.FGPolygon;

import android.graphics.Rect;

/**
 * @ClassName: MathsHelper
 * @Description: 静态数学计算函数
 * @author: Noisyfox
 * @date: 2012-8-13 下午2:49:29
 * 
 */
public final class FGMathsHelper {

	private static double[] GaussianDistributionList;
	private static double GaussianDistributionMax = 0.0;
	private static int GaussianDistributionListLength = 100000;
	private static Random random = new Random();

	// 生成一个高斯分布数列
	public final static void generateGaussianDistribution() {
		GaussianDistributionList = new double[GaussianDistributionListLength];

		for (int i = 0; i < GaussianDistributionListLength; i++) {
			GaussianDistributionList[i] = Math.abs(random.nextGaussian());
			GaussianDistributionMax = Math.max(GaussianDistributionMax,
					GaussianDistributionList[i]);
			GaussianDistributionMax = Math.min(GaussianDistributionMax, 6.0);
		}
	}

	// 生成一个介于 0 和 1 之间的符合高斯分布的随机数
	public final static double randomGaussian() {

		int index = random.nextInt(GaussianDistributionListLength);

		if (GaussianDistributionMax < 0.0001) {
			return 0;
		}

		return GaussianDistributionList[index] / GaussianDistributionMax;
	}

	// 判断一个点是否在一个圆内
	public final static boolean pointInCircle(FGPoint p, FGCircle c) {
		int d2 = p.squareDistance(c);
		if (d2 <= (c.getR() * c.getR())) {
			return true;
		}
		return false;
	}

	// 判断一个点是否在一个夹角内
	public final static boolean pointInAngle(FGPoint point, FGPoint vertex,
			FGPoint p1, FGPoint p2) {
		int x = point.getX() - vertex.getX();
		int y = point.getY() - vertex.getY();
		// int p1X = p1.getX() - vertex.getX();
		// int p1Y = p1.getY() - vertex.getY();
		// int p2X = p2.getX() - vertex.getX();
		// int p2Y = p2.getY() - vertex.getY();

		int v1 = x * (p1.getY() - vertex.getY()) - y
				* (p1.getX() - vertex.getX());
		int v2 = x * (p2.getY() - vertex.getY()) - y
				* (p2.getX() - vertex.getX());

		return v1 * v2 <= 0;
	}

	// 判断两条线段是否相交
	public final static boolean lineVSline(FGPoint l1P1, FGPoint l1P2,
			FGPoint l2P1, FGPoint l2P2) {
		return pointInAngle(l1P2, l1P1, l2P1, l2P2)
				&& pointInAngle(l2P2, l2P1, l1P1, l1P2);
	}

	// 判断点是否在多边形内
	public final static boolean pointInPolygon(FGPoint p, FGPolygon pol) {
		if (!pol.isLine()) {
			int nVertex = pol.getVertexNumber();
			boolean collision = true;
			for (int i = 0; i < nVertex; i++) {
				if (!pointInAngle(p, pol.getVertex(i), pol.getVertex(i - 1),
						pol.getVertex(i + 1))) {
					collision = false;
					break;
				}
			}
			if (collision) {
				return true;
			}
		}
		return false;
	}

	// 向量数量积
	public final static int dotProduct(FGPoint vertex, FGPoint p1, FGPoint p2) {
		// int x1, y1;
		// int x2, y2;

		// x1 = p1.getX() - vertex.getX();
		// y1 = p1.getY() - vertex.getY();
		// x2 = p2.getX() - vertex.getX();
		// y2 = p2.getY() - vertex.getY();

		return (p1.getX() - vertex.getX()) * (p2.getX() - vertex.getX())
				+ (p1.getY() - vertex.getY()) * (p2.getY() - vertex.getY());
	}

	// 点到直线距离平方
	public final static int squareDistanceFromPointToLine(FGPoint point,
			FGPoint p1, FGPoint p2) {
		int dp = dotProduct(p1, point, p2);
		int dp2 = dp * dp;
		int p1p22 = p1.squareDistance(p2);
		int projectionSquareLength = dp2 / p1p22;
		return point.squareDistance(p1) - projectionSquareLength;
	}

	// 判断圆与直线（线段）有无交点
	public final static boolean circleVSline(FGCircle c, FGPoint p1,
			FGPoint p2, boolean segment) {
		if (segment) {// 线段
			boolean hasIn = false;
			boolean hasOut = false;
			if (pointInCircle(p1, c)) {
				hasIn = true;
			} else {
				hasOut = true;
			}
			if (pointInCircle(p2, c)) {
				hasIn = true;
			} else {
				hasOut = true;
			}
			// 先判断两个端点是不是都在圆内
			if (hasIn && !hasOut) {
				// 都在圆内，判断圆是否实心
				return c.filled();
			}
			// 判断圆心是不是在线段两侧
			if (dotProduct(p1, c, p2) * dotProduct(p2, c, p1) <= 0) {
				// 在线段两侧，则只需要验证两个端点即可
				return !(hasOut && !hasIn);
			}
		}
		// 直线或者圆心在线段上方，只需判断圆心到直线距离即可
		int squareDistance = squareDistanceFromPointToLine(c, p1, p2);
		return squareDistance <= c.getR() * c.getR();
	}

	// 判断两个 Rect 是否相交
	public final static boolean rectVSrect(Rect rect1, Rect rect2) {
		int x1 = rect1.left;
		int y1 = rect1.top;
		int w1 = rect1.width();
		int h1 = rect1.height();
		int x2 = rect2.left;
		int y2 = rect2.top;
		int w2 = rect2.width();
		int h2 = rect2.height();

		if (x1 > x2 && x1 > x2 + w2) {
			return false;
		} else if (x1 < x2 && x1 + w1 < x2) {
			return false;
		} else if (y1 > y2 && y1 > y2 + h2) {
			return false;
		} else if (y1 < y2 && y1 + h1 < y2) {
			return false;
		}
		return true;
	}

	// 返回指定长度及方向的矢量线在 x 轴上的投影长度.
	public final static float lengthdir_x(float len, float dir) {
		return (float) (len * Math.cos(Math.toRadians(dir)));
	}

	// 返回指定长度及方向的矢量线在 y 轴上的投影长度.
	public final static float lengthdir_y(float len, float dir) {
		return (float) (len * Math.sin(Math.toRadians(dir)));
	}

	// 获取一个 >= min && <= max 的随机整数
	public final static int random(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException("MAX can't be smaller than MIN!");
		}

		return random.nextInt(max - min + 1) + min;
	}

	// 获取一个 >= min && < max 的随机浮点数
	public final static double random(double min, double max) {
		if (min > max) {
			throw new IllegalArgumentException("MAX can't be smaller than MIN!");
		}

		return random.nextDouble() * (max - min) + min;
	}

	// 返回位置1(x1,y1)到位置2(x2,y2)的距离.
	public final static float point_distance(float x1, float y1, float x2,
			float y2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	// 返回从位置1(x1,y1)到位置2(x2,y2)的方向角度
	public final static float point_direction(float x1, float y1, float x2,
			float y2) {
		return (float) Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
	}

	// 返回从 from 角度转向 to 角度所经过的最少角度，正值逆时针旋转，负值顺时针旋转
	public final static float directionTo(float from, float to) {
		// 旋转坐标系使 from 对应的终边落在X轴正方向上
		to -= from;
		// 转化到360度以内
		to = degreeIn360(to);

		return to <= 180 ? to : 180 - to;
	}

	public final static float degreeIn360(float deg) {
		if (deg >= 0) {
			deg %= 360;
		} else {
			deg *= -1;
			deg %= 360;
			deg = 360 - deg;
		}
		return deg;
	}

}
