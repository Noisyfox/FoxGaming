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

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

/**
 * @ClassName: Point
 * @Description: 2D基础图形类--点
 * @author: Noisyfox
 * @date: 2012-7-18 下午2:35:48
 * 
 */
public class FGPoint {

	int x = 0;
	int y = 0;

	public FGPoint() {
		this(0, 0);
	}

	public FGPoint(int x, int y) {
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
	public int squareDistance(FGPoint p2) {
		return (x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y);
	}

	public boolean equals(FGPoint p) {
		return p.x == x && p.y == y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public void draw(GL10 gl) {
		FGDraw.setColor(Color.BLACK);
		FGDraw.setAlpha(1);
		FGDraw.drawPoint(gl, x, y);
	}

}
