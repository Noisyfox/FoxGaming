/**
 * FileName:     _01_TestStage.java
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

import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.EnemyController;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Boss_Pig;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Box_Score;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Butterfly;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Doll_Blue;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Duck;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy_Fly;

/**
 * @ClassName: _01_TestStage
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-16 上午11:04:33
 * 
 */
public final class _01_TestStage extends SectionStage {

	@Override
	protected void prepareStage() {

		FGBackground bkg = new FGBackground();
		bkg.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.background_teststage,
				false);
		bkg.setAdaptation(FGBackground.ADAPTATION_SMART);
		bkg.setSpeed(0, 30f / getStageSpeed());
		bkg.setAlignment(FGBackground.ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM);
		bkg.setDrawMode(FGBackground.ADAPTATION_OPTION_DRAW_REPEATING);
		bkg.setScaleMode(FGBackground.ADAPTATION_OPTION_SCALE_WIDTHFIRST);
		setBackground(bkg);

	}

	@Override
	protected final void addEnemys(EnemyController ec) {
		ec.addEnemy(120, Enemy_Duck.class, 0, 90, 0);
		ec.addEnemy(150, Enemy_Duck.class, 0, 100, 1);
		ec.addEnemy(200, Enemy_Fly.class, 50, 0, 0, 100);
		ec.addEnemy(250, Enemy_Fly.class, 200, 0, 0, 200);
		ec.addEnemy(260, Enemy_Box_Score.class, 200, 0, 0, 100);
		ec.addEnemy(300, Enemy_Butterfly.class, 100, 0);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 0, 4, 1);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 0, 4, 2);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 0, 4, 3);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 0, 4, 4);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 1, 4, 1);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 1, 4, 2);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 1, 4, 3);
		ec.addEnemy(400, Enemy_Doll_Blue.class, 0, 0, 1, 4, 4);
		ec.addEnemy(730, Enemy_Boss_Pig.class, 0, 0);
	}

}
