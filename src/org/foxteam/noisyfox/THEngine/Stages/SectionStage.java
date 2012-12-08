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

import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.THEngine.Performers.EnemyController;
import org.foxteam.noisyfox.THEngine.Performers.GamingMenu;
import org.foxteam.noisyfox.THEngine.Performers.Player;
import org.foxteam.noisyfox.THEngine.Performers.GamingController;

import android.os.Bundle;

/**
 * @ClassName: SectionStage
 * @Description: 章节舞台，特指游戏的关卡舞台
 * @author: Noisyfox
 * @date: 2012-10-1 下午5:01:30
 * 
 */
public abstract class SectionStage extends FGStage {

	private static GamingMenu menu = new GamingMenu();
	private static int sectionCount = 0;// 所有关卡的数量
	private static Bundle savedState = new Bundle();
	private static Player player = null;
	private static GamingController gameController = null;
	private static EnemyController enemyController = null;
	private static float scrollSpeedV = 0;
	private static float scrollSpeedH = 0;

	private int sectionNumber = -1;// 当前关卡的编号

	public SectionStage() {
		super();
		sectionCount++;
		sectionNumber = sectionCount;
	}

	// 初始化游戏时数据
	public final static void initSectionStage() {
		savedState.putLong("THEA_score", 0);
		savedState.putInt("THEA_player_remainLife", 3);
		savedState.putInt("THEA_player_missileType", 0);// 导弹类型，0无1跟踪2非跟踪
		savedState.putInt("THEA_player_missileLevel", 1);
	}

	public final static void setStageScrollSpeed(float hSpeed, float vSpeed) {
		scrollSpeedV = vSpeed;
		scrollSpeedH = hSpeed;
	}

	public final static float getScrollSpeedV() {
		return scrollSpeedV;
	}

	public final static float getScrollSpeedH() {
		return scrollSpeedH;
	}

	public final static GamingMenu getMenu() {
		return menu;
	}

	public final static Player getPlayerInstance() {
		return player;
	}

	public final static int count() {
		return sectionCount;
	}

	public final int index() {
		return sectionNumber;
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

	public final void nextSection() {
		// 先保存当前状态
		player.saveState(savedState);
		savedState.putLong("THEA_score", FGGamingThread.score);
		onSectionEnd(savedState);
		FGStage.restartStage();
	}

	public final void restartSection() {
		FGStage.restartStage();
	}

	@Override
	protected final void onCreate() {

		setStageSpeed(30);
		gameController = new GamingController();

		player = new Player();
		player.perform(getStageIndex());

		FGGamingThread.score = savedState.getLong("THEA_score", 0);
		player.loadState(savedState);

		prepareStage(savedState);

		enemyController = new EnemyController();
		addEnemys(enemyController);
		enemyController.perform(this.getStageIndex());

		menu.perform(this.getStageIndex());
	}

	protected abstract void prepareStage(Bundle savedState);

	protected abstract void onSectionEnd(Bundle savedState);

	protected abstract void addEnemys(EnemyController ec);
}
