/**
 * FileName:     Circle.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午2:37:19
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
 * @ClassName: Circle
 * @Description: 2D基础图形类--圆
 * @author: Noisyfox
 * @date: 2012-7-18 下午2:37:19
 * 
 */
public class Circle extends Point {
	int r;

	boolean fill = false;

	public Circle(int x, int y, int r, boolean fill) {
		super(x, y);
		if (r <= 0) {
			throw new IllegalArgumentException();
		}
		this.r = r;

		this.fill = fill;
	}

	public int getR() {
		return r;
	}

	public final boolean filled() {
		return fill;
	}

	@Override
	public void draw(Canvas c) {

		paint.reset();
		paint.setAlpha(170);

		if (!fill) {
			paint.setStyle(Paint.Style.STROKE);
		}

		c.drawCircle(x, y, r, paint);
	}

}
