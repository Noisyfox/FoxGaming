/**
 * FileName:     SpriteConvertor.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-27 上午10:43:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-27      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import android.graphics.Matrix;

/**
 * @ClassName: SpriteConvertor
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-27 上午10:43:57
 * 
 */
public class SpriteConvertor {
	private double XScale = 1;
	private double YScale = 1;
	private double rotation = 0;// 角度制
	private double alpha = 1;
	private Matrix matrix = new Matrix();
	
	public SpriteConvertor() {

	}

	public final void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public final double getRotation() {
		return rotation;
	}

	public final void setScale(double XScale, double YScale) {
		this.XScale = XScale;
		this.YScale = YScale;
	}

	public final double getXScale() {
		return XScale;
	}

	public final double getYScale() {
		return YScale;
	}

	public final void setAlpha(double alpha) {
		if (alpha > 1) {
			alpha = 1;
		} else if (alpha < 0) {
			alpha = 0;
		}
		this.alpha = alpha;
	}

	public final double getAlpha() {
		return alpha;
	}

	public final Matrix getConvertMatrix() {
		return getConvertMatrix(0, 0);
	}

	public final Matrix getConvertMatrix(int offsetX, int offsetY) {
		matrix.reset();
		matrix.postScale((float) XScale, (float) YScale, offsetX, offsetY);
		matrix.postRotate((float) rotation, offsetX, offsetY);
		return matrix;
	}
}