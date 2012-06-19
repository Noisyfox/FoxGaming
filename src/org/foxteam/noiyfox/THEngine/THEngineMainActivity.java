package org.foxteam.noiyfox.THEngine;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

public class THEngineMainActivity extends Activity {

	private THEngineMainThread mainThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SurfaceView v = new THEngineMainView(this);
		setContentView(v);
		mainThread = new THEngineMainThread(v.getHolder());
		v.getHolder().addCallback(mainThread);
		v.setOnTouchListener(mainThread);
	}
}