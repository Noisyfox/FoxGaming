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
import java.util.HashMap;

import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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

	HashMap<String, Bitmap> fontMap = new HashMap<String, Bitmap>();
	float offsetX = 0;
	float offsetY = 0;
	float characterSpacing = 0;
	int frameWidth = 0;
	int frameHeight = 0;
	int alignment = ALIGN_LEFT;

	public void mapFont(int resId, String chars, boolean cDensityDpi) {
		Bitmap b = null;
		if (cDensityDpi) {
			b = BitmapFactory.decodeResource(FGGameCore.getMainContext()
					.getResources(), resId);
		} else {
			InputStream is = FGGameCore.getMainContext().getResources()
					.openRawResource(resId);
			b = BitmapFactory.decodeStream(is);
		}

		mapFont(b, chars);
	}

	public void mapFont(Bitmap src, String chars) {
		if (chars.length() == 0) {
			throw new IllegalArgumentException();
		}
		char[] c = chars.toCharArray();

		int imageW = src.getWidth();
		int imageH = src.getHeight();

		frameWidth = imageW % c.length == 0 ? imageW / c.length
				: (imageW - imageW % c.length) / c.length;
		frameHeight = imageH;

		fontMap.clear();

		for (int i = 0; i < c.length; i++) {
			String key = String.valueOf(c[i]);
			Bitmap fontBitmap = Bitmap.createBitmap(src, i * frameWidth, 0,
					frameWidth, imageH);
			fontMap.put(key, fontBitmap);
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

	public void drawText(Canvas c, float x, float y, String text) {
		char[] stc = text.toCharArray();
		for (int i = 0; i < stc.length; i++) {
			String key = String.valueOf(stc[i]);
			float rX = x - offsetX;

			switch (alignment) {
			case ALIGN_LEFT:
				rX += i * (characterSpacing + (float) frameWidth);
				break;
			case ALIGN_RIGHT:
				rX += (i - text.length() + 1)
						* (characterSpacing + (float) frameWidth);
				break;
			case ALIGN_CENTER:
				rX += (i - text.length() / 2)
						* (characterSpacing + (float) frameWidth);
				break;
			}

			float rY = (y - offsetY);
			Bitmap charBitmap = fontMap.get(key);
			if (charBitmap != null) {
				c.drawBitmap(charBitmap, rX, rY, null);
			}
		}
	}

}
