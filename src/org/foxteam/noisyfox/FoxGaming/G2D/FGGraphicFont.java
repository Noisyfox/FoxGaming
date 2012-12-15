/**
 * FileName:     GraphicFont.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-18 下午8:08:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ClassName: GraphicFont
 * @Description: 一个利用图片来自定义字体的类
 * @author: Noisyfox
 * @date: 2012-8-18 下午8:08:12
 * 
 */
public final class FGGraphicFont {
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_RIGHT = 2;
	public static final int ALIGN_CENTER = 3;

	HashMap<String, Integer> fontMap = new HashMap<String, Integer>();
	FGFrame frames = new FGFrame();
	float offsetX = 0;
	float offsetY = 0;
	float characterSpacing = 0;
	int frameWidth = 0;
	int frameHeight = 0;
	int alignment = ALIGN_LEFT;

	GL10 gl;

	public void mapFont(GL10 gl, int resId, String chars, boolean cDensityDpi) {
		Bitmap b = null;
		if (cDensityDpi) {
			b = BitmapFactory.decodeResource(FGGameCore.getMainContext()
					.getResources(), resId);
		} else {
			InputStream is = FGGameCore.getMainContext().getResources()
					.openRawResource(resId);
			b = BitmapFactory.decodeStream(is);
		}

		mapFont(gl, b, chars);
	}

	public void mapFont(GL10 gl, Bitmap src, String chars) {
		if (chars.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.gl = gl;
		char[] c = chars.toCharArray();

		int imageW = src.getWidth();
		int imageH = src.getHeight();

		frameWidth = imageW % c.length == 0 ? imageW / c.length
				: (imageW - imageW % c.length) / c.length;
		frameHeight = imageH;

		frames.loadFromBitmap(gl, src, c.length, 1);

		fontMap.clear();

		for (int i = 0; i < c.length; i++) {
			fontMap.put(String.valueOf(c[i]), Integer.valueOf(i));
		}
	}

	public void setOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void setCharacterSpacing(float spc) {
		characterSpacing = spc;
	}

	public void setAlignment(int alignment) {
		if (alignment < 1 || alignment > 3) {
			throw new IllegalArgumentException();
		}

		this.alignment = alignment;
	}

	public int getCharHeight() {
		return frameHeight;
	}

	public int getCharWidth() {
		return (int) (frameWidth + characterSpacing);
	}

	/**
	 * 绘制字体
	 * 
	 * @param c
	 * @param x
	 * @param y
	 * @param scale
	 *            每个字需要缩放的倍数，这不会改变实际绘制时的字间距
	 * @param text
	 */
	public void drawText(float x, float y, float scale, String text) {
		if (scale < 0)
			return;

		char[] stc = text.toCharArray();

		for (int i = 0; i < stc.length; i++) {
			String key = String.valueOf(stc[i]);
			float rX = x - offsetX;

			switch (alignment) {
			case ALIGN_LEFT:
				rX += i * (characterSpacing + (float) frameWidth * scale);
				break;
			case ALIGN_RIGHT:
				rX += (i - text.length() + 1)
						* (characterSpacing + (float) frameWidth * scale);
				break;
			case ALIGN_CENTER:
				rX += (i - text.length() / 2)
						* (characterSpacing + (float) frameWidth * scale);
				break;
			}

			float rY = (y - offsetY);
			Integer charBitmap = fontMap.get(key);
			if (charBitmap != null) {

				FloatBuffer coordBuffer = FGEGLHelper.fBuffer(new float[] { 0,
						0, frames.maxU, 0, 0, frames.maxV, frames.maxU,
						frames.maxV, });
				FloatBuffer verticleBuffer = FGEGLHelper.fBuffer(new float[] {
						rX, rY, rX + frameWidth * scale, rY, rX,
						rY + frameHeight * scale, rX + frameWidth * scale,
						rY + frameHeight * scale });

				FGEGLHelper.useTexture(true);
				frames.gl.glBindTexture(GL10.GL_TEXTURE_2D,
						frames.getFrameTexture(charBitmap));
				frames.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
				frames.gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
				frames.gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				frames.gl.glColor4f(1f, 1f, 1f, 1f);
				FGEGLHelper.useTexture(false);
			}
		}
	}

	public void drawText(float x, float y, String text) {
		drawText(x, y, 1, text);
	}

}
