/**
 * FileName:     Layer.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-16 下午9:05:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-16      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.GameCommon.G2D;

import android.graphics.Canvas;

/**
 * @ClassName: Layer
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-16 下午9:05:57
 * 
 */
public abstract class Layer {

	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	boolean visible = true;

	public Layer(int width, int height) {
		setWidthImpl(width);
		setHeightImpl(height);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public void setWidthImpl(int width) {
		if (width < 0) {
			throw new IllegalArgumentException("width can't be less than 0");
		}
		this.width = width;
	}

	public void setHeightImpl(int height) {
		if (height < 0) {
			throw new IllegalArgumentException("height can't be less than 0");
		}
		this.height = height;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public final boolean isVisible() {
		return visible;
	}

	public abstract void paint(Canvas c);
}
