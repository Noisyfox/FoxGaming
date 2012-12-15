package org.foxteam.noisyfox.FoxGaming.Core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLDebugHelper;
import android.opengl.GLU;
import android.util.Log;
import android.view.SurfaceView;

/**
 * 
 * @ClassName: FGEGLHelper
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-12-15 下午3:01:27
 * 
 */
public class FGEGLHelper {
	protected static final int[] CONFIGSPEC = { EGL10.EGL_RED_SIZE, 5,
			EGL10.EGL_GREEN_SIZE, 6, EGL10.EGL_BLUE_SIZE, 5,
			EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };

	// main OpenGL variables
	protected static GL10 mBinded_GL;
	protected static EGL10 mBinded_EGL;
	protected static EGLDisplay mBinded_GLDisplay;
	protected static EGLConfig mBinded_GLConfig;
	protected static EGLSurface mBinded_GLSurface;
	protected static EGLContext mBinded_GLContext;
	protected static SurfaceView mBinded_View;

	public final static GL10 getBufferGL() {
		return mBinded_GL;
	}

	public final static void bindSurfaceView(SurfaceView view) {
		if (view == null)
			return;

		if (mBinded_View == null) {
			mBinded_View = view;

			mBinded_EGL = (EGL10) GLDebugHelper.wrap(EGLContext.getEGL(),
					GLDebugHelper.CONFIG_CHECK_GL_ERROR
							| GLDebugHelper.CONFIG_CHECK_THREAD, null);

			mBinded_GLDisplay = mBinded_EGL
					.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

			int[] curGLVersion = new int[2];
			mBinded_EGL.eglInitialize(mBinded_GLDisplay, curGLVersion);

			Log.i("GL", "GL version = " + curGLVersion[0] + "."
					+ curGLVersion[1]);

			EGLConfig[] configs = new EGLConfig[1];
			int[] num_config = new int[1];
			mBinded_EGL.eglChooseConfig(mBinded_GLDisplay, CONFIGSPEC, configs,
					1, num_config);
			mBinded_GLConfig = configs[0];

			mBinded_GLSurface = mBinded_EGL.eglCreateWindowSurface(
					mBinded_GLDisplay, mBinded_GLConfig,
					mBinded_View.getHolder(), null);

			mBinded_GLContext = mBinded_EGL.eglCreateContext(mBinded_GLDisplay,
					mBinded_GLConfig, EGL10.EGL_NO_CONTEXT, null);

			mBinded_EGL.eglMakeCurrent(mBinded_GLDisplay, mBinded_GLSurface,
					mBinded_GLSurface, mBinded_GLContext);
			mBinded_GL = (GL10) GLDebugHelper.wrap(mBinded_GLContext.getGL(),
					GLDebugHelper.CONFIG_CHECK_GL_ERROR
							| GLDebugHelper.CONFIG_CHECK_THREAD
							| GLDebugHelper.CONFIG_LOG_ARGUMENT_NAMES, null);

			int width = mBinded_View.getWidth();
			int height = mBinded_View.getHeight();
			mBinded_GL.glClear(GL10.GL_COLOR_BUFFER_BIT
					| GL10.GL_DEPTH_BUFFER_BIT);
			mBinded_GL.glShadeModel(GL10.GL_SMOOTH);
			mBinded_GL.glMatrixMode(GL10.GL_PROJECTION);
			mBinded_GL.glLoadIdentity();
			// float aspect = (float) width / height;
			GLU.gluOrtho2D(mBinded_GL, 0, width, height, 0);
			mBinded_GL.glMatrixMode(GL10.GL_MODELVIEW);
			mBinded_GL.glLoadIdentity();
			// mBinded_GL.glViewport(0, 0, width, height);
			// GLU.gluPerspective(mBinded_GL, 45.0f, aspect, 1.0f, 30.0f);

			// mBinded_GL.glMatrixMode(GL10.GL_PROJECTION);
			// mBinded_GL.glLoadIdentity();
			mBinded_GL.glClearColor(0f, 0f, 0f, 1f);

			mBinded_GL.glEnable(GL10.GL_ALPHA_TEST); // Enable Alpha Testing (To
														// Make BlackTansparent)

			mBinded_GL.glAlphaFunc(GL10.GL_GREATER, 0.1f); // Set Alpha Testing
															// (To
															// Make Black
															// Transparent)

			// the only way to draw primitives with OpenGL ES
			mBinded_GL.glEnable(GL10.GL_BLEND);
			mBinded_GL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			mBinded_GL.glBlendFunc(GL10.GL_SRC_ALPHA,
					GL10.GL_ONE_MINUS_SRC_ALPHA);
			Log.i("GL", "GL initialized");
		} else {

			mBinded_View = view;

			mBinded_GLSurface = mBinded_EGL.eglCreateWindowSurface(
					mBinded_GLDisplay, mBinded_GLConfig,
					mBinded_View.getHolder(), null);

			mBinded_EGL.eglMakeCurrent(mBinded_GLDisplay, mBinded_GLSurface,
					mBinded_GLSurface, mBinded_GLContext);

		}
	}

	public final static SurfaceView getBindedView() {
		return mBinded_View;
	}

	public final static GL10 getBindedGL() {
		return mBinded_GL;
	}

	public final static void renderOnScreen() {
		// mBinded_GL.glClearColor(1f, 1f, 1f, 1f);
		mBinded_EGL.eglSwapBuffers(mBinded_GLDisplay, mBinded_GLSurface);
	}

	public final static void useTexture(boolean useTexture) {
		if (useTexture) {
			getBindedGL().glEnable(GL10.GL_TEXTURE_2D);
			getBindedGL().glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		} else {
			getBindedGL().glDisable(GL10.GL_TEXTURE_2D);
			getBindedGL().glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}

	public static FloatBuffer fBuffer(float[] a) {
		// 先初始化buffer,数组的长度*4,因为一个float占4个字节
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// 数组排列用nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = mbb.asFloatBuffer();
		floatBuffer.put(a);
		floatBuffer.position(0);
		return floatBuffer;
	}
}
