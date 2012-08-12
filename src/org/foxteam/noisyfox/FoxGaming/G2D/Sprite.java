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

import java.io.InputStream;

import org.foxteam.noisyfox.FoxGaming.Core.GameCore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	 * list of frames
	 */
	private Bitmap[] frames;

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

	public void loadFromBitmap(int resId, boolean cDensityDpi) {
		Bitmap b = null;
		if (cDensityDpi) {
			b = BitmapFactory.decodeResource(GameCore.getMainContext()
					.getResources(), resId);
		} else {
			InputStream is = GameCore.getMainContext().getResources()
					.openRawResource(resId);
			b = BitmapFactory.decodeStream(is);
		}

		loadFromBitmap(b);
	}

	public void loadFromBitmap(int resId, boolean cDensityDpi,
			int horizontalNumber, int verticalNumber) {
		Bitmap b = null;
		if (cDensityDpi) {
			b = BitmapFactory.decodeResource(GameCore.getMainContext()
					.getResources(), resId);
		} else {
			InputStream is = GameCore.getMainContext().getResources()
					.openRawResource(resId);
			b = BitmapFactory.decodeStream(is);
		}

		loadFromBitmap(b, horizontalNumber, verticalNumber);
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

		frames = new Bitmap[numberFrames];

		int currentFrame = 0;

		for (int yy = 0; yy < imageH; yy += frameHeight) {
			for (int xx = 0; xx < imageW; xx += frameWidth) {
				frames[currentFrame] = Bitmap.createBitmap(sourceImage, xx, yy,
						srcFrameWidth, srcFrameHeight);
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
		draw(c, x, y, null);
	}

	public final void draw(Canvas c, int x, int y,
			SpriteConvertor spriteConvertor) {

		if (spriteConvertor != null) {
			Paint paint = new Paint();
			Matrix m = spriteConvertor.getConvertMatrix(offsetX, offsetY);
			m.postTranslate(x - offsetX, y - offsetY);
			paint.setAlpha((int) (255.0 * spriteConvertor.getAlpha()));
			c.drawBitmap(frames[currentFrame], m, paint);
		} else {
			c.drawBitmap(frames[currentFrame], x - offsetX, y - offsetY, null);
		}

	}
}
