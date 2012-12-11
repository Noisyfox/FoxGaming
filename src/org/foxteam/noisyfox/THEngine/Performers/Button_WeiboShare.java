package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.THEngine.GlobalResources;

public final class Button_WeiboShare extends FGButton {

	public Button_WeiboShare() {
		super(60, 60, GlobalResources.FRAMES_BUTTON_SINAWEIBO);
		setTouchSize(100, 100);
	}

	@Override
	public void onClick() {
		new WeiboShareProcessor().perform(FGStage.getCurrentStage()
				.getStageIndex());
	}

}
