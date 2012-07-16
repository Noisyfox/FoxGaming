/**
 * FileName:     Sprite.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-25 下午11:23:07
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-25      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @ClassName: Sprite
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-25 下午11:23:07
 * 
 */
public class Sprite {
	/**
	 * Source image
	 */
	private Bitmap sourceImage;

	/**
	 * The number of frames
	 */
	private int numberFrames; // = 0;

	/**
	 * list of X coordinates of individual frames
	 */
	private int[] frameCoordsX;
	/**
	 * list of Y coordinates of individual frames
	 */
	private int[] frameCoordsY;

	/**
	 * Width of each frame in the source image
	 */
	private int srcFrameWidth;

	/**
	 * Height of each frame in the source image
	 */
	private int srcFrameHeight;

	private int currentFrame = 0;// start from 0, smaller than numberFrames

	private int offsetX = 0;
	private int offsetY = 0;
	private boolean visible = true;

	public Sprite() {

	}

	public final void loadFromBitmap(Bitmap bitmap) {
		loadFromBitmap(bitmap, 1, 1);
	}

	public final void loadFromBitmap(Bitmap bitmap, int horizontalNumber,
			int verticalNumber) {
		initializeFrames(bitmap, horizontalNumber, verticalNumber);
	}

	private void initializeFrames(Bitmap image, int horizontalNumber,
			int verticalNumber) {

		if (image == null || horizontalNumber <= 0 || verticalNumber <= 0) {
			throw new IllegalArgumentException();
		}

		int imageW = image.getWidth();
		int imageH = image.getHeight();

		int frameWidth = imageW % horizontalNumber == 0 ? imageW
				/ horizontalNumber : (imageW - imageW % horizontalNumber)
				/ horizontalNumber;
		int frameHeight = imageH % verticalNumber == 0 ? imageH
				/ verticalNumber : (imageH - imageH % verticalNumber)
				/ verticalNumber;

		if (frameWidth <= 0 || frameHeight <= 0) {
			throw new IllegalArgumentException();
		}

		srcFrameWidth = frameWidth;
		srcFrameHeight = frameHeight;

		sourceImage = image;
		numberFrames = horizontalNumber * verticalNumber;

		frameCoordsX = new int[numberFrames];
		frameCoordsY = new int[numberFrames];

		int currentFrame = 0;

		for (int yy = 0; yy < imageH; yy += frameHeight) {
			for (int xx = 0; xx < imageW; xx += frameWidth) {
				frameCoordsX[currentFrame] = xx;
				frameCoordsY[currentFrame] = yy;
				currentFrame++;
			}
		}
	}

	public final int getHeight() {
		return srcFrameHeight;
	}

	public final int getWidth() {
		return srcFrameWidth;
	}

	public final void setOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}

	public final int getOffsetX() {
		return offsetX;
	}

	public final int getOffsetY() {
		return offsetY;
	}

	public final void setVisible(boolean visible) {
		this.visible = visible;
	}

	public final boolean isVisible() {
		return visible;
	}

	public final int getFrameCount() {
		return numberFrames;
	}

	public final int getCurrentFrame() {
		return currentFrame;
	}

	public final void setCurrentFrame(int frameIndex) {
		while (frameIndex < 0)
			frameIndex += numberFrames;
		while (frameIndex >= numberFrames)
			frameIndex -= numberFrames;
		currentFrame = frameIndex;
	}

	public final void draw(Canvas c, int x, int y) {
		draw(c, x, y, new SpriteConvertor());
	}

	public final void draw(Canvas c, int x, int y,
			SpriteConvertor spriteConvertor) {
		Bitmap bmp = Bitmap.createBitmap(sourceImage,
				frameCoordsX[currentFrame], frameCoordsY[currentFrame],
				srcFrameWidth, srcFrameHeight);
		Paint paint = new Paint();
		Matrix m = spriteConvertor.getConvertMatrix(offsetX, offsetY);
		m.postTranslate(x - offsetX, y - offsetY);
		paint.setAlpha((int) (255.0 * spriteConvertor.getAlpha()));
		c.drawBitmap(bmp, m, paint);
	}
}
