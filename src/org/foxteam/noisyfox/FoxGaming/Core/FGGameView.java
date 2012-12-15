package org.foxteam.noisyfox.FoxGaming.Core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class FGGameView extends SurfaceView {
	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public FGGameView(Context context) {
		super(context);
		init();
	}

	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public FGGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

}
