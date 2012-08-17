/**
 * FileName:     _00_TestStage.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-16 上午11:04:33
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-16      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Stages;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.SystemControl;

/**
 * @ClassName: _00_TestStage
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-16 上午11:04:33
 * 
 */
public final class _01_TestStage extends Stage {

	@Override
	protected void onCreate() {
		setStageSpeed(30);
		Performer p = new SystemControl();
		p.perform(getStageIndex());

		Background bkg = new Background();

		bkg.loadFromBitmap(org.foxteam.noisyfox.THEngine.R.drawable.background,
				false);
		bkg.setAdaptation(Background.ADAPTATION_SMART);
		bkg.setSpeed(0, 30f / getStageSpeed());
		bkg.setAlignment(Background.ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM);
		bkg.setDrawMode(Background.ADAPTATION_OPTION_DRAW_REPEATING);
		bkg.setScaleMode(Background.ADAPTATION_OPTION_SCALE_WIDTHFIRST);
		setBackground(bkg);
	}

}
