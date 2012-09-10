/**
 * FileName:     PointF.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午2:35:48
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @ClassName: PointF
 * @Description: 2D基础图形类--点
 * @author: Noisyfox
 * @date: 2012-7-18 下午2:35:48
 * 
 */
public class FGPointF {

	float x = 0;
	float y = 0;
	protected static Paint paint = new Paint();

	public FGPointF() {
		this(0, 0);
	}

	public FGPointF(float x, float y) {
		setPosition(x, y);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void move(float dx, float dy) {
		x += dx;
		y += dy;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// 得到两点距离的平方
	public float squareDistance(FGPointF p2) {
		return (x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y);
	}

	public float distance(FGPointF p2) {
		return (float) Math.sqrt((double) (x - p2.x) * (double) (x - p2.x)
				+ (double) (y - p2.y) * (double) (y - p2.y));
	}

	public boolean equals(FGPointF p) {
		return p.x == x && p.y == y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public void draw(Canvas c) {
		paint.reset();
		c.drawPoint(x, y, paint);
	}

}
