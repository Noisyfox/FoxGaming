/**
 * FileName:     Views.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-31 下午2:42:35
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-31      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.graphics.RectF;

/**
 * @ClassName: Views
 * @Description: 视角的操作
 * @author: Noisyfox
 * @date: 2012-7-31 下午2:42:35
 * 
 */
public final class FGViews {

	protected RectF targetView = new RectF();
	protected RectF sourceView = new RectF();
	protected float sourceAngle = 0.0f;

	public FGViews() {
	}

	public void setPositionFromStage(float x, float y) {
		sourceView.offsetTo(x, y);
	}

	public float getXFromStage() {
		return sourceView.left;
	}

	public float getYFromStage() {
		return sourceView.top;
	}

	public void setSizeFromStage(float width, float height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException();
		}

		sourceView.set(sourceView.left, sourceView.top,
				sourceView.left + width, sourceView.top + height);
	}

	public float getHeightFromStage() {
		return sourceView.height();
	}

	public float getWidthFromStage() {
		return sourceView.width();
	}

	public void setAngleFromStage(float angle) {
		sourceAngle = angle;
	}

	public float getAngleFromScreen() {
		return sourceAngle;
	}

	public void setPositionFromScreen(float x, float y) {
		targetView.offsetTo(x, y);
	}

	public float getXFromScreen() {
		return targetView.left;
	}

	public float getYFromScreen() {
		return targetView.top;
	}

	public void setSizeFromScreen(float width, float height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException();
		}

		targetView.set(targetView.left, targetView.top,
				targetView.left + width, targetView.top + height);
	}

	public float getHeightFromScreen() {
		return targetView.height();
	}

	public float getWidthFromScreen() {
		return targetView.width();
	}

	public int coordinateScreen2Stage_X(int x, int y) {
		float dx = (float) x - targetView.centerX();
		float dy = (float) y - targetView.centerY();
		dx *= sourceView.width() / targetView.width();
		dy *= sourceView.height() / targetView.height();
		float x0 = dx * (float) Math.cos(Math.toRadians(sourceAngle)) - dy
				* (float) Math.sin(Math.toRadians(sourceAngle));
		x0 += sourceView.centerX();
		return (int) x0;
	}

	public int coordinateScreen2Stage_Y(int x, int y) {
		float dx = (float) x - targetView.centerX();
		float dy = (float) y - targetView.centerY();
		dx *= sourceView.width() / targetView.width();
		dy *= sourceView.height() / targetView.height();
		float y0 = dy * (float) Math.cos(Math.toRadians(sourceAngle)) + dx
				* (float) Math.sin(Math.toRadians(sourceAngle));
		y0 += sourceView.centerY();
		return (int) y0;
	}

	public int coordinateStage2Screen_X(int x, int y) {
		float dx = (float) x - sourceView.centerX();
		float dy = (float) y - sourceView.centerY();
		dx *= targetView.width() / sourceView.width();
		dy *= targetView.height() / sourceView.height();
		float x0 = dx * (float) Math.cos(Math.toRadians(-sourceAngle)) - dy
				* (float) Math.sin(Math.toRadians(-sourceAngle));
		x0 += targetView.centerX();
		return (int) x0;
	}

	public int coordinateStage2Screen_Y(int x, int y) {
		float dx = (float) x - sourceView.centerX();
		float dy = (float) y - sourceView.centerY();
		dx *= targetView.width() / sourceView.width();
		dy *= targetView.height() / sourceView.height();
		float y0 = dy * (float) Math.cos(Math.toRadians(-sourceAngle)) + dx
				* (float) Math.sin(Math.toRadians(-sourceAngle));
		y0 += targetView.centerY();
		return (int) y0;
	}

}
