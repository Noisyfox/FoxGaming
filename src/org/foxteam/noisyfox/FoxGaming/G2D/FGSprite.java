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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @ClassName: Sprite
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-25 下午11:23:07
 * 
 */
public final class FGSprite {

	private FGFrame frames = null;

	private int currentFrame = 0;// start from 0, smaller than numberFrames

	private int offsetX = 0;
	private int offsetY = 0;
	private boolean visible = true;

	public FGSprite() {

	}

	public void setOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void bindFrames(FGFrame frame) {
		frames = frame;
	}

	public FGFrame getFrames() {
		return frames;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int frameIndex) {
		while (frameIndex < 0)
			frameIndex += frames.numberFrames;
		while (frameIndex >= frames.numberFrames)
			frameIndex -= frames.numberFrames;
		currentFrame = frameIndex;
	}

	public void previousFrame() {
		setCurrentFrame(currentFrame - 1);
	}

	public void nextFrame() {
		setCurrentFrame(currentFrame + 1);
	}

	public final int getHeight() {
		if (frames != null) {
			return frames.srcFrameHeight;
		}
		return 0;
	}

	public final int getWidth() {
		if (frames != null) {
			return frames.srcFrameWidth;
		}
		return 0;
	}

	public final int getFrameCount() {
		if (frames != null) {
			return frames.numberFrames;
		}
		return 0;
	}

	public void draw(Canvas c, int x, int y) {
		draw(c, x, y, null);
	}

	public void draw(Canvas c, int x, int y, FGSpriteConvertor spriteConvertor) {

		draw(c, x, y, spriteConvertor, Color.WHITE);

	}

	public void draw(Canvas c, int x, int y, FGSpriteConvertor spriteConvertor,
			int color) {

		if (frames == null) {
			return;
		}

		Paint paint = new Paint();

		if (color != Color.WHITE) {
			float hsv[] = new float[3];
			Color.colorToHSV(color, hsv);
			float k = (hsv[2] - hsv[1] + 1f) * 0.5f;
			int R = Color.red(color);
			int G = Color.green(color);
			int B = Color.blue(color);

			ColorMatrix cm = new ColorMatrix();
			float carray[] = { k, 0, 0, 0, (1 - k) * (float) R, 0, k, 0, 0,
					(1 - k) * (float) G, 0, 0, k, 0, (1 - k) * (float) B, 0, 0,
					0, 1, 0 };
			cm.set(carray);
			paint.setColorFilter(new ColorMatrixColorFilter(cm));
		}

		if (spriteConvertor != null) {
			Matrix m = spriteConvertor.getConvertMatrix(offsetX, offsetY);
			m.postTranslate(x - offsetX, y - offsetY);
			paint.setAlpha((int) (255.0 * spriteConvertor.getAlpha()));
			c.drawBitmap(frames.getFrame(currentFrame), m, paint);
		} else {
			c.drawBitmap(frames.getFrame(currentFrame), x - offsetX, y
					- offsetY, paint);
		}

	}
}
