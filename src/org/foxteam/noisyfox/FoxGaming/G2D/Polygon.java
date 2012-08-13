/**
 * FileName:     Polygon.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午2:39:19
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
import android.graphics.Path;

/**
 * @ClassName: Polygon
 * @Description: 2D基础图形类--多边形
 * @author: Noisyfox
 * @date: 2012-7-18 下午2:39:19
 * 
 */
public class Polygon {
	Point[] vertex;
	int num_edges = 0;
	int num_vertexs = 0;
	boolean isLine = false;
	boolean fill = false;
	Paint paint = new Paint();

	public Polygon(Point[] vertex, boolean fill) {
		if (vertex.length <= 1) {
			throw new IllegalArgumentException();
		}

		// 判断有无点重合
		for (int i = 0; i < vertex.length - 1; i++) {
			for (int j = i + 1; j < vertex.length; j++) {
				if (vertex[i].equals(vertex[j])) {
					throw new IllegalArgumentException();
				}
			}
		}

		this.vertex = vertex;
		num_vertexs = vertex.length;
		if (num_vertexs == 2) {
			num_edges = 1;
			isLine = true;
		} else {
			num_edges = num_vertexs;
		}
		this.fill = fill;
	}

	public final Point getVertex(int vertexIndex) {
		while (vertexIndex < 0)
			vertexIndex += num_vertexs;
		while (vertexIndex >= num_vertexs)
			vertexIndex -= num_vertexs;
		return vertex[vertexIndex];
	}

	public final int getEdgeNumber() {
		return num_edges;
	}

	public final int getVertexNumber() {
		return num_vertexs;
	}

	public final boolean filled() {
		return fill;
	}

	public final boolean isLine() {
		return isLine;
	}

	public void draw(Canvas c) {

		paint.reset();
		if (!fill) {
			paint.setStyle(Paint.Style.STROKE);
		}

		Path path1 = new Path();
		for (int i = 0; i < vertex.length; i++) {
			if (i == 0) {
				path1.moveTo(vertex[i].x, vertex[i].y);
			} else {
				path1.lineTo(vertex[i].x, vertex[i].y);
			}
		}
		path1.close();
		c.drawPath(path1, paint);
	}

}
