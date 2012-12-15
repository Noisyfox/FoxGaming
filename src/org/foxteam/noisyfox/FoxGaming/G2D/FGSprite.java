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

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;

import android.graphics.Color;
import android.graphics.Matrix;

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

	public final void draw(int x, int y) {
		draw(x, y, null);
	}

	public final void draw(int x, int y, FGSpriteConvertor spriteConvertor) {

		draw(x, y, spriteConvertor, Color.WHITE);

	}

	public final void draw(int x, int y, FGSpriteConvertor spriteConvertor,
			int color) {

		if (frames == null) {
			return;
		}

		float R = Color.red(color) / 255f;
		float G = Color.green(color) / 255f;
		float B = Color.blue(color) / 255f;

		FloatBuffer coordBuffer = FGEGLHelper.fBuffer(new float[] { 0, 0,
				frames.maxU, 0, 0, frames.maxV, frames.maxU, frames.maxV });

		float[] verticle = null;

		if (spriteConvertor != null) {

			verticle = new float[] { 0, 0, frames.srcFrameWidth, 0, 0,
					frames.srcFrameHeight, frames.srcFrameWidth,
					frames.srcFrameHeight };

			Matrix m = spriteConvertor.getConvertMatrix(offsetX, offsetY);
			m.postTranslate(x - offsetX, y - offsetY);
			m.mapPoints(verticle);
			frames.gl.glColor4f(R, G, B, (float) spriteConvertor.getAlpha());
		} else {
			verticle = new float[] { x - offsetX, y - offsetY,
					frames.srcFrameWidth + x - offsetX, y - offsetY,
					x - offsetX, frames.srcFrameHeight + y - offsetY,
					frames.srcFrameWidth + x - offsetX,
					frames.srcFrameHeight + y - offsetY };
			frames.gl.glColor4f(R, G, B, 1f);
		}

		FloatBuffer verticleBuffer = FGEGLHelper.fBuffer(verticle);
		FGEGLHelper.useTexture(true);
		frames.gl.glBindTexture(GL10.GL_TEXTURE_2D,
				frames.getFrameTexture(currentFrame));
		frames.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
		frames.gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
		frames.gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		frames.gl.glColor4f(1f, 1f, 1f, 1f);
		FGEGLHelper.useTexture(false);
		// frames.gl.gl

	}

}
