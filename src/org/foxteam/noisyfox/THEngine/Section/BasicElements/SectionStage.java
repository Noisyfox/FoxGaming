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
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGSimpleBGM;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.Core.FGViews;
import org.foxteam.noisyfox.FoxGaming.G2D.FGBackground;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.THEngine.Performers.StageSwitchEffect;

import android.graphics.Bitmap;
import android.graphics.Color;
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
	private static List<SectionStage> allSections = new ArrayList<SectionStage>();
	private static FGStage mainMenuStage = null;
	private static FGStage gameClearStage = null;

	private boolean paused = false;
	private FGBackground pauseCache_Background = null;
	private int pauseCache_stageWidth = 0;
	private int pauseCache_stageHeight = 0;
	private FGViews pauseCache_View;
	private boolean pauseCache_isBGMPlaying = true;

	private int sectionNumber = -1;// 当前关卡的编号

	public SectionStage() {
		super();
		sectionCount++;
		sectionNumber = sectionCount;
		allSections.add(this);
	}

	// 初始化游戏时数据
	public final static void initSectionStage() {
		savedState.putLong("THEA_score", 0);
		savedState.putInt("THEA_player_remainLife", 3);
		savedState.putInt("THEA_player_missileType", 0);// 导弹类型，0无1跟踪2非跟踪
		savedState.putInt("THEA_player_missileLevel", 1);
	}

	public final static void setSpecialStage(FGStage mainMenu, FGStage gameClear) {
		mainMenuStage = mainMenu;
		gameClearStage = gameClear;
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

	public static final void startSection() {
		StageSwitchEffect.switchToStage(allSections.get(0).stageIndex);
	}

	public final void nextSection() {
		// 先保存当前状态
		player.saveState(savedState);
		savedState.putLong("THEA_score", FGGamingThread.score);
		onSectionEnd(savedState);
		StageSwitchEffect.switchToStage(stageIndex);
		player.bindCollisionMask(null);
	}

	public final void restartSection() {
		StageSwitchEffect.switchToStage(stageIndex);
		player.bindCollisionMask(null);
	}
	

	public static final void returnMainMenu() {
		if (mainMenuStage != null) {
			StageSwitchEffect.switchToStage(mainMenuStage.getStageIndex());
			player.bindCollisionMask(null);
		}
	}

	public static final void gameClear() {
		if (gameClearStage != null) {
			StageSwitchEffect.switchToStage(gameClearStage.getStageIndex());
			player.bindCollisionMask(null);
		}
	}

	public final void pauseSection(FGPerformer me) {
		if (paused)
			return;

		Bitmap cache = FGGamingThread.getScreenshots();
		FGFrame f = new FGFrame();
		f.loadFromBitmap(FGEGLHelper.getBindedGL(), cache);
		FGBackground bkg = new FGBackground();
		bkg.bindFrame(f);
		pauseCache_Background = getBackground();
		pauseCache_stageWidth = width;
		pauseCache_stageHeight = height;
		pauseCache_View = activatedView;
		// pauseCache_isBGMPlaying = FGSimpleBGM.;

		activatedView = null;

		setBackground(bkg);
		setSize(FGGamingThread.getScreenHeight(),
				FGGamingThread.getScreenWidth());
		FGSimpleBGM.pause();

		if (me != null) {
			me.freezeAll(true, true);
		} else {
			for (FGPerformer p : performers) {
				p.freezeMe();
			}
		}

		paused = true;

	}

	public final void resumeSection(FGPerformer me) {
		if (!paused)
			return;

		setView(pauseCache_View);

		setBackground(pauseCache_Background);

		setSize(pauseCache_stageHeight, pauseCache_stageWidth);

		if (pauseCache_isBGMPlaying)
			FGSimpleBGM.play();

		if (me != null) {
			me.freezeAll(false, true);
		} else {
			for (FGPerformer p : performers) {
				p.unfreezeMe();
			}
		}

		paused = false;
	}

	public static final boolean hasNextSection() {
		if (SectionStage.class.isInstance(FGStage.getCurrentStage())) {
			SectionStage stage = (SectionStage) FGStage.getCurrentStage();
			return stage.sectionNumber < SectionStage.sectionCount;
		}
		return false;
	}

	@Override
	protected final void onCreate() {
		paused = false;

		setStageSpeed(30);
		// 计算缩放比率
		float k = (float) FGGamingThread.getScreenWidth() * 1.15f / 320f;

		FGStage.getCurrentStage().setSize(
				(int) ((float) FGGamingThread.getScreenHeight() / k), 320);

		FGViews v = new FGViews();

		v.setSizeFromScreen(FGGamingThread.getScreenWidth(),
				FGGamingThread.getScreenHeight());
		v.setSizeFromStage((int) ((float) FGGamingThread.getScreenWidth() / k),
				(int) ((float) FGGamingThread.getScreenHeight() / k));

		v.setPositionFromScreen(0, 0);
		v.setPositionFromStage(
				(FGStage.getCurrentStage().getWidth() - v.getWidthFromStage()) / 2,
				0);

		v.setAngleFromStage(0);
		FGStage.getCurrentStage().setView(v);

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

		setBackgroundColor(Color.BLACK);
		//new BackgroundController().perform(stageIndex);

		new StageSwitchEffect();
	}

	protected abstract void prepareStage(Bundle savedState);

	protected abstract void onSectionEnd(Bundle savedState);

	protected abstract void addEnemys(EnemyController ec);
}
