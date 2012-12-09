package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;
import org.foxteam.noisyfox.THEngine.Section.Stages.Section_01_TestStage;
import org.foxteam.noisyfox.THEngine.Stages.*;

public class THEngineMainActivity extends FGGameActivity {

	@Override
	public void onEngineReady() {
		GlobalResources.loadResources();
		SectionStage.initSectionStage();

		new _00_MainMenu();
		new Section_01_TestStage();
	}

	@Override
	public void onCreate() {
		this.forcePortrait();
		normalMode();
	}

}
