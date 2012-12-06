/**
 * FileName:     SectionStage.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-10-1 下午5:01:30
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-10-1      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Stages;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.THEngine.Performers.EnemyController;
import org.foxteam.noisyfox.THEngine.Performers.GamingMenu;
import org.foxteam.noisyfox.THEngine.Performers.Player;
import org.foxteam.noisyfox.THEngine.Performers.GamingController;

/**
 * @ClassName: SectionStage
 * @Description: 章节舞台，特指游戏的关卡舞台
 * @author: Noisyfox
 * @date: 2012-10-1 下午5:01:30
 * 
 */
public abstract class SectionStage extends FGStage {

	private static GamingMenu menu = new GamingMenu();

	private Player player = null;
	private GamingController gameController = null;
	private EnemyController enemyController = null;

	public static GamingMenu getMenu() {
		return menu;
	}

	public final void stageClear() {
		gameController.stageClear();
		enemyController.pause();
	}

	public final void gameOver() {
		gameController.gameOver();
		enemyController.pause();
		if (background != null)
			background.setSpeed(0, 0);
	}

	@Override
	protected final void onCreate() {

		setStageSpeed(30);
		gameController = new GamingController();

		player = new Player();
		player.perform(getStageIndex());

		prepareStage();

		enemyController = new EnemyController();
		addEnemys(enemyController);
		enemyController.perform(this.getStageIndex());

		menu.perform(this.getStageIndex());
	}

	protected abstract void prepareStage();

	protected abstract void addEnemys(EnemyController ec);
}
