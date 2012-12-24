package org.foxteam.noisyfox.FoxGaming.G2D.Background;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;

/**
 * 
 * @ClassName: FGSimpleBackground
 * @Description: 一个简单的背景类，只是单纯的绘制图片
 * @author: Noisyfox
 * @date: 2012-12-24 下午9:58:33
 * 
 */
public class FGSimpleBackground implements FGBackground {

	private FGFrame sourceImage = null;
	private float xOffset = 0;
	private float yOffset = 0;

	@Override
	public void doAndDraw(int left, int top, int height, int width) {
		if (sourceImage == null)
			return;

		int imageWidth = sourceImage.getWidth();
		int imageHeight = sourceImage.getHeight();
		FGEGLHelper.useTexture(true);
		FloatBuffer verticleBuffer;
		sourceImage.getTargetGl().glBindTexture(GL10.GL_TEXTURE_2D,
				sourceImage.getFrameTexture(0));
		sourceImage.getTargetGl().glTexCoordPointer(2, GL10.GL_FLOAT, 0,
				sourceImage.getCoordBuffer());
		verticleBuffer = sourceImage.getFloatBuffer(new float[] {
				left + xOffset, top + yOffset, left + xOffset + imageWidth,
				top + yOffset, left + xOffset, top + yOffset + imageHeight,
				left + xOffset + imageWidth, top + yOffset + imageHeight });
		sourceImage.getTargetGl().glVertexPointer(2, GL10.GL_FLOAT, 0,
				verticleBuffer);
		sourceImage.getTargetGl().glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		FGEGLHelper.useTexture(false);

	}

	public void bindFrame(FGFrame frame) {
		sourceImage = frame;
	}

	public void setOffset(float offsetX, float offsetY) {
		xOffset = offsetX;
		yOffset = offsetY;
	}

}
