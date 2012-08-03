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
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @ClassName: Background
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-9 下午5:50:08
 * 
 */
public class Background {

	/**
	 * 仅仅是在设置好的位置显示出来，不进行任何适应操作，强制为锚点对齐方式<br>
	 * 不支持大部分的背景操作
	 */
	public static final int ADAPTATION_PLACE = 1;
	/**
	 * 拉伸到整个屏幕，不支持背景运动
	 */
	public static final int ADAPTATION_STRETCHING = 2;
	/**
	 * 填充整个屏幕，强制为锚点对齐方式，不支持背景运动
	 */
	public static final int ADAPTATION_PADDING = 3;
	/**
	 * 自适应的背景显示，具体显示结果由高级参数决定
	 */
	public static final int ADAPTATION_SMART = 4;

	// 对齐方式
	public static final int ADAPTATION_OPTION_ALIGNMENT_LEFT = 1;// 左对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_RIGHT = 2;// 右对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_TOP = 3;// 顶对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_BOTTOM = 4;// 底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_LEFT_TOP = 5;// 左顶对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_LEFT_BOTTOM = 6;// 左底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_RIGHT_TOP = 7;// 右顶对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_RIGHT_BOTTOM = 8;// 右底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL = 9;// 水平居中，等同于同时左右对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL = 10;// 竖直居中，等同于同时顶底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_TOP = 11;// 水平居中，顶部对齐，等同于同时左右顶对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM = 12;// 水平居中，底部对齐，等同于同时左右底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL_LEFT = 13;// 竖直居中，左侧对齐，等同于同时左顶底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL_RIGHT = 14;// 竖直居中，右侧对齐，等同于同时右顶底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_CENTER = 15;// 居中，等同于同时左右顶底对齐
	public static final int ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT = 16;// 锚点对齐，等于左右顶底都不对齐

	// 绘图方式
	public static final int ADAPTATION_OPTION_DRAW_SINGLE = 1;// 单纯绘制一次背景
	public static final int ADAPTATION_OPTION_DRAW_REPEATING = 2;// 无缝循环拼接

	// 拉伸方式
	public static final int ADAPTATION_OPTION_SCALE_NONE = 1;// 禁止拉伸
	public static final int ADAPTATION_OPTION_SCALE_NORMAL = 2;// 拉伸图像填充整个屏幕，不保持高宽比，忽视对齐方式
	public static final int ADAPTATION_OPTION_SCALE_WIDTHFIRST = 3;// 宽优先，保持高宽比
	public static final int ADAPTATION_OPTION_SCALE_HEIGHTFIRST = 4;// 高优先，保持高宽比
	public static final int ADAPTATION_OPTION_SCALE_MAXUSAGE = 5;// 最大化利用屏幕，保持高宽比，单张图片填充满整个屏幕，并且尽可能显示最多的内容

	/**
	 * Source image
	 */
	private Bitmap sourceImage = null;
	private Point anchorPointBitmap = new Point(0, 0);
	private Point anchorPointScreen = new Point(0, 0);

	private float speed_x = 0;
	private float speed_y = 0;

	private int adaptation = ADAPTATION_PLACE;
	private int option_alignment = ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT;
	private int option_draw = ADAPTATION_OPTION_DRAW_SINGLE;
	private int option_scale = ADAPTATION_OPTION_SCALE_NONE;

	private float xOffset = 0;
	private float yOffset = 0;

	public Background() {

	}

	public void loadFromBitmap(Bitmap bitmap) {
		sourceImage = bitmap;
	}

	// 设置自适应方式
	public void setAdaptation(int adaptation) {
		if (adaptation < 1 || adaptation > 4) {
			throw new IllegalArgumentException();
		}

		this.adaptation = adaptation;

		switch (this.adaptation) {
		case ADAPTATION_PLACE:
			setSpeed(0, 0);
			xOffset = 0;
			yOffset = 0;
			setAlignment(ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT);
			setDrawMode(ADAPTATION_OPTION_DRAW_SINGLE);
			setScaleMode(ADAPTATION_OPTION_SCALE_NONE);
			break;

		case ADAPTATION_STRETCHING:
			setSpeed(0, 0);
			xOffset = 0;
			yOffset = 0;
			setAlignment(ADAPTATION_OPTION_ALIGNMENT_CENTER);
			setDrawMode(ADAPTATION_OPTION_DRAW_SINGLE);
			setScaleMode(ADAPTATION_OPTION_SCALE_NORMAL);
			break;

		case ADAPTATION_PADDING:
			setSpeed(0, 0);
			xOffset = 0;
			yOffset = 0;
			setAlignment(ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT);
			setDrawMode(ADAPTATION_OPTION_DRAW_REPEATING);
			setScaleMode(ADAPTATION_OPTION_SCALE_NONE);
			break;
		}
	}

	// 设置对齐方式
	public void setAlignment(int alignment) {
		if (alignment < 1 || alignment > 16) {
			throw new IllegalArgumentException();
		}

		option_alignment = alignment;
	}

	// 设置绘图方式
	public void setDrawMode(int drawMode) {
		if (drawMode < 1 || drawMode > 2) {
			throw new IllegalArgumentException();
		}

		option_draw = drawMode;
	}

	// 设置拉伸方式
	public void setScaleMode(int scaleMode) {
		if (scaleMode < 1 || scaleMode > 5) {
			throw new IllegalArgumentException();
		}

		option_scale = scaleMode;
	}

	public void setSpeed(float xSpeed, float ySpeed) {
		speed_x = xSpeed;
		speed_y = ySpeed;
	}

	public void setAnchorPoint(Point bitmap, Point screen) {
		anchorPointBitmap = bitmap;
		anchorPointScreen = screen;
	}

	public void doAndDraw(Canvas c, int height, int width) {

		// 先计算缩放
		float xScale = 1;
		float yScale = 1;
		if (option_scale != ADAPTATION_OPTION_SCALE_NONE) {
			float xScaleFull = 1;// x轴方向如果要填充满需要的拉伸大小
			float yScaleFull = 1;// y轴方向如果要填充满需要的拉伸大小
			if (option_alignment == ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT) {
				if (anchorPointBitmap.getX() == 0
						&& sourceImage.getWidth() - anchorPointBitmap.getX() == 0) {

				} else if (anchorPointBitmap.getX() == 0) {
					xScaleFull = (float) (width - anchorPointScreen.getX())
							/ (float) (sourceImage.getWidth() - anchorPointBitmap
									.getX());
				} else if (sourceImage.getWidth() - anchorPointBitmap.getX() == 0) {
					xScaleFull = (float) anchorPointScreen.getX()
							/ (float) anchorPointBitmap.getX();
				} else {
					xScaleFull = Math
							.max((float) anchorPointScreen.getX()
									/ (float) anchorPointBitmap.getX(),
									(float) (width - anchorPointScreen.getX())
											/ (float) (sourceImage.getWidth() - anchorPointBitmap
													.getX()));
				}

				if (anchorPointBitmap.getY() == 0
						&& sourceImage.getHeight() - anchorPointBitmap.getY() == 0) {

				} else if (anchorPointBitmap.getY() == 0) {
					yScaleFull = (float) (height - anchorPointScreen.getY())
							/ (float) (sourceImage.getHeight() - anchorPointBitmap
									.getY());
				} else if (sourceImage.getHeight() - anchorPointBitmap.getY() == 0) {
					yScaleFull = (float) anchorPointScreen.getY()
							/ (float) anchorPointBitmap.getY();
				} else {
					yScaleFull = Math
							.max((float) anchorPointScreen.getY()
									/ (float) anchorPointBitmap.getY(),
									(float) (height - anchorPointScreen.getY())
											/ (float) (sourceImage.getHeight() - anchorPointBitmap
													.getY()));

				}

			} else {
				xScaleFull = (float) width / (float) sourceImage.getWidth();
				yScaleFull = (float) height / (float) sourceImage.getHeight();
			}

			switch (option_scale) {
			case ADAPTATION_OPTION_SCALE_NORMAL:
				xScale = xScaleFull;
				yScale = yScaleFull;
				break;

			case ADAPTATION_OPTION_SCALE_WIDTHFIRST:
				xScale = xScaleFull;
				yScale = xScaleFull;
				break;

			case ADAPTATION_OPTION_SCALE_HEIGHTFIRST:
				xScale = yScaleFull;
				yScale = yScaleFull;
				break;

			case ADAPTATION_OPTION_SCALE_MAXUSAGE:
				xScale = Math.max(xScaleFull, yScaleFull);
				yScale = xScale;
				break;
			}
		}

		int imageWidth = (int) ((float) sourceImage.getWidth() * xScale);
		int imageHeight = (int) ((float) sourceImage.getHeight() * yScale);
		Point anchorPointBitmapScaled = new Point(
				(int) ((float) anchorPointBitmap.getX() * xScale),
				(int) ((float) anchorPointBitmap.getY() * yScale));

		// 计算对齐方式并且获得核心图像的最终位置
		int x = 0;
		int y = 0;

		switch (option_alignment) {
		case ADAPTATION_OPTION_ALIGNMENT_LEFT:
			x = 0;
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;

		case ADAPTATION_OPTION_ALIGNMENT_RIGHT:
			x = width - imageWidth;
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;

		case ADAPTATION_OPTION_ALIGNMENT_TOP:
			x = anchorPointScreen.getX() - anchorPointBitmapScaled.getX();
			y = 0;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_BOTTOM:
			x = anchorPointScreen.getX() - anchorPointBitmapScaled.getX();
			y = height - imageHeight;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_LEFT_TOP:
			x = 0;
			y = 0;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_LEFT_BOTTOM:
			x = 0;
			y = height - imageHeight;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_RIGHT_TOP:
			x = width - imageWidth;
			y = 0;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_RIGHT_BOTTOM:
			x = width - imageWidth;
			y = height - imageHeight;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL:
			x = (width - imageWidth) / 2;
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL:
			x = anchorPointScreen.getX() - anchorPointBitmapScaled.getX();
			y = (height - imageHeight) / 2;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_TOP:
			x = (width - imageWidth) / 2;
			y = 0;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM:
			x = (width - imageWidth) / 2;
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL_LEFT:
			x = 0;
			y = (height - imageHeight) / 2;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER_VERTICAL_RIGHT:
			y = (height - imageHeight) / 2;
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;

		case ADAPTATION_OPTION_ALIGNMENT_CENTER:
			x = (width - imageWidth) / 2;
			y = (height - imageHeight) / 2;
			break;

		case ADAPTATION_OPTION_ALIGNMENT_ANCHORPOINT:
			x = anchorPointScreen.getX() - anchorPointBitmapScaled.getX();
			y = anchorPointScreen.getY() - anchorPointBitmapScaled.getY();
			break;
		}
		// 应用偏移
		x += xOffset;
		y += yOffset;

		// 绘制
		Rect from = new Rect(0, 0, sourceImage.getWidth(),
				sourceImage.getHeight());

		if (option_draw == ADAPTATION_OPTION_DRAW_REPEATING) {
			// 简化位置
			x = x % imageWidth;
			y = y % imageHeight;

			int xt, yt;

			float _x1 = (float) x / (float) imageWidth;
			int x1 = 0;
			if (_x1 > 0)
				x1 = (int) Math.ceil(_x1);

			xt = x - x1 * imageWidth;

			_x1 = (float) (width - (x + imageWidth)) / (float) imageWidth;
			if (_x1 > 0)
				x1 += (int) Math.ceil(_x1);
			x1++;

			float _y1 = (float) y / (float) imageHeight;
			int y1 = 0;
			if (_y1 > 0)
				y1 = (int) Math.ceil(_y1);

			yt = y - y1 * imageHeight;

			_y1 = (float) (height - (y + imageHeight)) / (float) imageHeight;
			if (_y1 > 0)
				y1 += (int) Math.ceil(_y1);
			y1++;

			for (int i = 0; i < x1; i++) {
				for (int j = 0; j < y1; j++) {
					Rect to = new Rect(xt + i * imageWidth, yt + j
							* imageHeight, xt + (i + 1) * imageWidth, yt
							+ (j + 1) * imageHeight);
					c.drawBitmap(sourceImage, from, to, null);
				}
			}

		} else {
			Rect to = new Rect(x, y, x + imageWidth, y + imageHeight);
			c.drawBitmap(sourceImage, from, to, null);
		}

		xOffset += speed_x;
		yOffset += speed_y;

	}
}
