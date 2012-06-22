package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

import android.app.Activity;
import android.os.Bundle;

public class THEngineMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameCore fg = new GameCore(this);
		GameView v = fg.getGameView();
		setContentView(v);
	}

	//private THEngineMainThread mainThread;

	/** Called when the activity is first created. */
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// SurfaceView v = new THEngineMainView(this);
	// setContentView(v);
	// mainThread = new THEngineMainThread(v.getHolder());
	// v.getHolder().addCallback(mainThread);
	// v.setOnTouchListener(mainThread);
	// }
	
	
}