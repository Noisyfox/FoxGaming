/**
 * FileName:     Background.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-9 下午5:50:08
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-9      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import android.graphics.Bitmap;

/**
 * @ClassName: Background
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-9 下午5:50:08
 * 
 */
public class Background {

	/**
	 * 仅仅是在设置好的位置显示出来，不进行任何适应操作<br>
	 * 不支持大部分的背景操作
	 */
	public static final int ADAPTATION_PLACE = 1;
	/**
	 * 拉伸到整个屏幕
	 */
	public static final int ADAPTATION_STRETCHING = 2;
	/**
	 * 填充整个屏幕
	 */
	public static final int ADAPTATION_PADDING = 3;
	/**
	 * 自适应的背景显示，具体显示结果由高级参数决定
	 */
	public static final int ADAPTATION_SAMRT = 4;

	// 对齐方式
	public static final int ADAPTATION_OPTION_ALIGNMENT_LEFT = 1;// 左对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_RIGHT = 3;// 右对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_TOP = 4;// 顶对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_BOTTOM = 12;// 底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT = 31;// 锚点对齐

	// 绘图方式

	/**
	 * Source image
	 */
	private Bitmap sourceImage;

	private float speed_x;
	private float speed_y;

	public Background() {

	}

	public void loadFormBitmap(Bitmap bitmap) {

	}
}
