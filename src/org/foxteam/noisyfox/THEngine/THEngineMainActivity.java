package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.THEngine.Stages.*;

public class THEngineMainActivity extends GameActivity {

	@Override
	public void onEngineReady() {
		new _00_TestStage();
	}

	@Override
	public void onCreate() {
		this.forcePortrait();
	}

}
