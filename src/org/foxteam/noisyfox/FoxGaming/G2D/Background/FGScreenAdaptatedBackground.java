package org.foxteam.noisyfox.FoxGaming.G2D.Background;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;

public class FGScreenAdaptatedBackground implements FGBackground {

	private Horizon_Align_Type horizonAlignType = Horizon_Align_Type.Left;
	private Vertical_Align_Type verticalAlignType = Vertical_Align_Type.Top;

	public enum Horizon_Align_Type {
		Right, Left, Center, Stretch
	}

	public enum Vertical_Align_Type {
		Top, Bottom, Center, Stretch
	}

	private FGFrame sourceImage = null;

	@Override
	public void doAndDraw(int left, int top, int height, int width) {
		if (sourceImage == null)
			return;

		int imageWidth = sourceImage.getWidth();
		int imageHeight = sourceImage.getHeight();
		float xOffset = 0;
		float yOffset = 0;

		if (horizonAlignType == Horizon_Align_Type.Stretch) {
			if (verticalAlignType == Vertical_Align_Type.Stretch) {
				imageWidth = width;
				imageHeight = height;
			} else {
				imageHeight *= (float) width / (float) imageWidth;
				imageWidth = width;
				switch (verticalAlignType) {
				case Bottom:
					yOffset = height - imageHeight;
					break;
				case Center:
					yOffset = (float) (height - imageHeight) / 2;
					break;
				}
			}
		} else {
			if (verticalAlignType == Vertical_Align_Type.Stretch) {
				imageWidth *= (float) height / (float) imageHeight;
				imageHeight = height;
			} else {
				switch (verticalAlignType) {
				case Bottom:
					yOffset = height - imageHeight;
					break;
				case Center:
					yOffset = (float) (height - imageHeight) / 2;
					break;
				}
			}
			switch (horizonAlignType) {
			case Right:
				xOffset = width - imageWidth;
				break;
			case Center:
				xOffset = (float) (width - imageWidth) / 2;
				break;
			}
		}

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

	public void setAlignType(Horizon_Align_Type horizonAlignType,
			Vertical_Align_Type verticalAlignType) {
		this.horizonAlignType = horizonAlignType;
		this.verticalAlignType = verticalAlignType;
	}

}
