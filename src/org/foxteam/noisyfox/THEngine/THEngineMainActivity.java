package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

import android.app.Activity;
import android.os.Bundle;

public class THEngineMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameCore gc = new GameCore(this);
		GameView v = gc.getGameView();
		setContentView(v);
		Stage s = new Stage();
		Performer p = new TestPerformer();
		s.employPerformer(p);
		gc.gameStart();
	}

}