package org.foxteam.noisyfox.FoxGaming.G2D;

import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
	 * list of frameTextures
	 */
	protected int[] frameTextures;

	/**
	 * list of frameTextures
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

	protected float maxU = 0;

	protected float maxV = 0;

	protected GL10 gl;

	protected FloatBuffer coordBuffer;

	private void initializeFrames(Bitmap image, int horizontalNumber,
			int verticalNumber) {

		if (image == null || horizontalNumber <= 0 || verticalNumber <= 0) {
			throw new IllegalArgumentException();
		}

		deleteTexture();

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

		frameTextures = new int[numberFrames];
		frames = new Bitmap[numberFrames];

		int currentFrame = 0;

		for (int yy = 0; yy < imageH; yy += frameHeight) {
			for (int xx = 0; xx < imageW; xx += frameWidth) {
				frames[currentFrame] = Bitmap.createBitmap(sourceImage, xx, yy,
						srcFrameWidth, srcFrameHeight);
				frameTextures[currentFrame] = bitmap2texture(frames[currentFrame]);
				currentFrame++;
			}
		}

		coordBuffer = FGEGLHelper.fBuffer(new float[] { 0, 0, maxU, 0, 0, maxV,
				maxU, maxV });
	}

	public final void loadFromBitmap(GL10 gl, Bitmap bitmap) {
		loadFromBitmap(gl, bitmap, 1, 1);
	}

	public final void loadFromBitmap(GL10 gl, Bitmap bitmap,
			int horizontalNumber, int verticalNumber) {
		this.gl = gl;

		initializeFrames(bitmap, horizontalNumber, verticalNumber);
	}

	public void loadFromBitmap(GL10 gl, int resId, boolean cDensityDpi) {
		loadFromBitmap(gl, resId, 1, 1, cDensityDpi);
	}

	public void loadFromBitmap(GL10 gl, int resId, int horizontalNumber,
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

		loadFromBitmap(gl, b, horizontalNumber, verticalNumber);
	}

	public void loadFromBitmap(GL10 gl, byte[] data) {
		loadFromBitmap(gl, data, 1, 1);
	}

	public void loadFromBitmap(GL10 gl, byte[] data, int horizontalNumber,
			int verticalNumber) {
		Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
		loadFromBitmap(gl, b, horizontalNumber, verticalNumber);
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

	public int getFrameTexture(int frameIndex) {
		return frameTextures[frameIndex % numberFrames];
	}

	private final int bitmap2texture(Bitmap bitmap) {

		// 确保长宽为2的指数次
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int mPow2Height = pow2(height);
		int mPow2Width = pow2(width);

		maxU = width / (float) mPow2Width;
		maxV = height / (float) mPow2Height;

		Bitmap _bitmap = Bitmap.createBitmap(mPow2Width, mPow2Height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(_bitmap);
		canvas.drawARGB(0, 255, 255, 255);
		canvas.drawBitmap(bitmap, 0, 0, null);

		// 创建texture
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		int textureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		myTexImage2D(gl, _bitmap);
		_bitmap.recycle();

		return textureId;
	}

	public final void deleteTexture() {
		if (gl != null && frameTextures != null) {
			gl.glDeleteTextures(1, frameTextures, 0);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// deleteTexture();

		super.finalize();
	}

	private static int pow2(int size) {
		int small = (int) (Math.log((double) size) / Math.log(2.0f));
		if ((1 << small) >= size)
			return 1 << small;
		else
			return 1 << (small + 1);
	}

	private static final boolean IS_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);

	private void myTexImage2D(GL10 gl, Bitmap bitmap) {
		// Don't loading using GLUtils, load using gl-method directly
		// GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		int[] pixels = extractPixels(bitmap);
		for (int i = pixels.length - 1; i >= 0; i--) {
			int p = pixels[i];
			int r = ((p >> 16) & 0xFF);
			int g = ((p >> 8) & 0xFF); // green
			int b = ((p) & 0xFF); // blue
			int a = (p >> 24); // alpha
			if (IS_LITTLE_ENDIAN) {
				pixels[i] = a << 24 | b << 16 | g << 8 | r;
			} else {
				pixels[i] = r << 24 | g << 16 | b << 8 | a;
			}
		}
		IntBuffer pixelBuffer = IntBuffer.wrap(pixels);

		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(),
				bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				pixelBuffer);
	}

	public static int[] extractPixels(Bitmap src) {
		int x = 0;
		int y = 0;
		int w = src.getWidth();
		int h = src.getHeight();
		int[] colors = new int[w * h];
		src.getPixels(colors, 0, w, x, y, w, h);
		return colors;
	}

	FloatBuffer cachedFloatBuffer = null;
	int cachedFloatBufferLength = 0;

	public final FloatBuffer getFloatBuffer(float[] a) {
		if (cachedFloatBuffer == null || a.length != cachedFloatBufferLength) {
			cachedFloatBufferLength = a.length;
			cachedFloatBuffer = FGEGLHelper.fBuffer(a);
		} else {
			cachedFloatBuffer.position(0);
			for (int i = 0; i < cachedFloatBufferLength; i++) {
				cachedFloatBuffer.put(a[i]);
			}
			cachedFloatBuffer.position(0);
		}

		return cachedFloatBuffer;
	}

	public GL10 getTargetGl() {
		return gl;
	}

	public FloatBuffer getCoordBuffer() {
		return coordBuffer;
	}
}
