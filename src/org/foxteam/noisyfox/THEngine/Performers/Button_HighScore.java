package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.THEngine.GlobalResources;

public final class Button_HighScore extends FGButton {

	static HighScore highScore = new HighScore();

	public Button_HighScore() {
		super(170, 53, GlobalResources.FRAMES_BUTTON_HIGHSCORE);
		setTouchSize(200, 100);
	}

	@Override
	public void onClick() {
		highScore.hide();
		highScore.show();
	}

}
