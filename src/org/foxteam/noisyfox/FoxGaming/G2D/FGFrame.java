package org.foxteam.noisyfox.FoxGaming.G2D;

import java.io.InputStream;

import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @ClassName: FGFrame
 * @Description: 一个用来定义动画帧的类
 * @author: Noisyfox
 * @date: 2012-11-27 上午10:46:24
 * 
 */
public final class FGFrame {
	/**
	 * Source image
	 */
	protected Bitmap sourceImage;

	/**
	 * The number of frames
	 */
	protected int numberFrames = 0;

	/**
	 * list of frames
	 */
	protected Bitmap[] frames;

	/**
	 * Width of each frame in the source image
	 */
	protected int srcFrameWidth = 0;

	/**
	 * Height of each frame in the source image
	 */
	protected int srcFrameHeight = 0;

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

	public final void loadFromBitmap(Bitmap bitmap) {
		loadFromBitmap(bitmap, 1, 1);
	}

	public final void loadFromBitmap(Bitmap bitmap, int horizontalNumber,
			int verticalNumber) {
		initializeFrames(bitmap, horizontalNumber, verticalNumber);
	}

	public void loadFromBitmap(int resId, boolean cDensityDpi) {
		loadFromBitmap(resId, 1, 1, cDensityDpi);
	}

	public void loadFromBitmap(int resId, int horizontalNumber,
			int verticalNumber, boolean cDensityDpi) {
		Bitmap b = null;
		if (cDensityDpi) {
			b = BitmapFactory.decodeResource(FGGameCore.getMainContext()
					.getResources(), resId);
		} else {
			InputStream is = FGGameCore.getMainContext().getResources()
					.openRawResource(resId);
			b = BitmapFactory.decodeStream(is);
		}

		loadFromBitmap(b, horizontalNumber, verticalNumber);
	}

	public final int getHeight() {
		return srcFrameHeight;
	}

	public final int getWidth() {
		return srcFrameWidth;
	}

	public final int getFrameCount() {
		return numberFrames;
	}

	public Bitmap getFrame(int frameIndex) {
		return frames[frameIndex % numberFrames];
	}
}
