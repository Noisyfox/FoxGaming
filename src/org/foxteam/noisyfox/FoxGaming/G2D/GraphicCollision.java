/**
 * FileName:     GraphicCollision.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-27 上午11:12:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-27      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

/**
 * @ClassName: GraphicCollision
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-27 上午11:12:21
 * 
 */
public class GraphicCollision {

	public GraphicCollision() {

	}

	public final void addCircle(int x, int y, int r) {
		addCircle(x, y, r, true);
	}

	public final void addCircle(int x, int y, int r, boolean fillCircle) {

	}

	public final void addRect(int x, int y, int width, int height) {
		addRect(x, y, width, height, true);
	}

	public final void addRect(int x, int y, int width, int height,
			boolean fillRect) {

	}

	public final void addLine(int x1, int y1, int x2, int y2) {

	}

	public final void addPoint(int x, int y) {

	}

	public final void setPosition(int x, int y) {

	}

	public final boolean isCollisionWith(GraphicCollision target) {
		return false;
	}

	// 静态函数，判断多个target之间是否发生碰撞
	public final static boolean testCollision(GraphicCollision... targets) {
		for (int i = 0; i < targets.length - 1; i++) {
			for (int j = i; j < targets.length; j++) {
				if (targets[i].isCollisionWith(targets[j])) {
					return true;
				}
			}
		}
		return false;
	}
}
