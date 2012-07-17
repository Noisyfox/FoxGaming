package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

public class THEngineMainActivity extends GameActivity {

	@Override
	public void onEngineReady() {
		Stage s = new Stage();
		Performer p = new TestPerformer();
		s.employPerformer(p);
	}

	@Override
	public void onCreate() {
	}

}