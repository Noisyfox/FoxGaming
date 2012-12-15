package org.foxteam.noisyfox.FoxGaming.G2D;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;

import android.graphics.Color;

/**
 * 
 * @ClassName: FGDraw
 * @Description: 一个用来绘制常用形状的类
 * @author: Noisyfox
 * @date: 2012-12-15 下午7:43:02
 * 
 */
public class FGDraw {

	private static int color = Color.WHITE;
	private static float alpha = 1;

	public final static void setAlpha(float alpha) {
		FGDraw.alpha = alpha;
	}

	public final static void setColor(int color) {
		FGDraw.color = color;
	}

	public final static void drawRect(GL10 gl, float left, float top,
			float right, float bottom) {

		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(
				2,
				GL10.GL_FLOAT,
				0,
				FGEGLHelper.fBuffer(new float[] { left, top, left, bottom,
						right, bottom, right, top }));

		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawRectFill(GL10 gl, float left, float top,
			float right, float bottom) {

		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(
				2,
				GL10.GL_FLOAT,
				0,
				FGEGLHelper.fBuffer(new float[] { left, top, left, bottom,
						right, bottom, right, top }));

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawPoint(GL10 gl, float x, float y) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0,
				FGEGLHelper.fBuffer(new float[] { x, y }));
		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawLine(GL10 gl, float x1, float y1, float x2,
			float y2) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0,
				FGEGLHelper.fBuffer(new float[] { x1, y1, x2, y2 }));
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 2);

		gl.glColor4f(1, 1, 1, 1);
	}

	private static int iS = 360;

	public final static void drawCircle(GL10 gl, float x, float y, float r) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		float[] verticle = new float[iS * 2];

		for (int i = 0; i < iS; i++) {
			verticle[i * 2] = (float) (Math.cos(Math.toRadians(i * 360f / iS)) * r)
					+ x;
			verticle[i * 2 + 1] = (float) (Math.sin(Math.toRadians(i * 360f
					/ iS)) * r)
					+ y;
		}

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, FGEGLHelper.fBuffer(verticle));
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, iS);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawCircleFill(GL10 gl, float x, float y, float r) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		float[] verticle = new float[iS * 2];

		for (int i = 0; i < iS; i++) {
			verticle[i * 2] = (float) (Math.cos(Math.toRadians(i * 360f / iS)) * r)
					+ x;
			verticle[i * 2 + 1] = (float) (Math.sin(Math.toRadians(i * 360f
					/ iS)) * r)
					+ y;
		}

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, FGEGLHelper.fBuffer(verticle));
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, iS);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawPolygon(GL10 gl, float[] vertex) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, FGEGLHelper.fBuffer(vertex));
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, vertex.length / 2);

		gl.glColor4f(1, 1, 1, 1);
	}

	public final static void drawPolygonFill(GL10 gl, float[] vertex) {
		gl.glColor4f(Color.red(color) / 255f, Color.green(color) / 255f,
				Color.blue(color) / 255f, alpha);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, FGEGLHelper.fBuffer(vertex));
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertex.length / 2);

		gl.glColor4f(1, 1, 1, 1);
	}

}
