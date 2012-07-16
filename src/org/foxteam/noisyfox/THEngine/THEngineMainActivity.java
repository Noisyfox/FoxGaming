package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

import android.app.Activity;
import android.os.Bundle;

public class THEngineMainActivity extends Activity {
	GameCore gc;

	@Override
	protected void onPause() {
		super.onPause();
		gc.gamePause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		gc.gameResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gc = new GameCore(this);
		GameView v = gc.getGameView();
		setContentView(v);
		Stage s = new Stage();
		Performer p = new TestPerformer();
		s.employPerformer(p);
		gc.gameStart();
	}

}