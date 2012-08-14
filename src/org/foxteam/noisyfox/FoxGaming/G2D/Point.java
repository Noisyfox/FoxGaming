/**
 * FileName:     Point.java
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
 * @ClassName: Point
 * @Description: 2D基础图形类--点
 * @author: Noisyfox
 * @date: 2012-7-18 下午2:35:48
 * 
 */
public class Point {

	int x = 0;
	int y = 0;
	protected static Paint paint = new Paint();

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		setPosition(x, y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// 得到两点距离的平方
	public int squareDistance(Point p2) {
		return (x - p2.getX()) * (x - p2.getX()) + (y - p2.getY())
				* (y - p2.getY());
	}

	public boolean equals(Point p) {
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
