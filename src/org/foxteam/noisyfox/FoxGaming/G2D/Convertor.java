/**
 * FileName:     Convertor.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午7:58:53
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import android.graphics.Matrix;

/**
 * @ClassName:     Convertor
 * @Description:   TODO
 * @author:        Noisyfox
 * @date:          2012-8-17 下午7:58:53
 *
 */
public class Convertor {
	protected double XScale = 1;
	protected double YScale = 1;
	protected double rotation = 0;// 角度制
	protected Matrix matrix = new Matrix();
	
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
