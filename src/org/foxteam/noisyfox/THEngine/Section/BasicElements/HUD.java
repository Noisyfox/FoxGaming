/**
 * FileName:     HUD.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-12 下午3:56:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-12      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;

/**
 * @ClassName: HUD
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-12 下午3:56:55
 * 
 */
public class HUD extends FGPerformer {

	FGViews mainView = null;
	FGSprite mySmallIcon = new FGSprite();
	FGSprite flashText_STAGECLEAR = new FGSprite();
	FGSprite flashText_GAMEOVER = new FGSprite();

	int flashTextType = 0;
	boolean textFlash = false;

	@Override
	protected void onCreate() {
		mainView = FGStage.getCurrentStage().getView();

		mySmallIcon.bindFrames(GlobalResources.FRAMES_PLAYER_ICON);

		flashText_STAGECLEAR
				.bindFrames(GlobalResources.FRAMES_FLASHTEXT_STAGE_CLEAR);
		flashText_STAGECLEAR.setOffset(flashText_STAGECLEAR.getWidth() / 2,
				flashText_STAGECLEAR.getHeight());

		flashText_GAMEOVER
				.bindFrames(GlobalResources.FRAMES_FLASHTEXT_GAME_OVER);
		flashText_GAMEOVER.setOffset(flashText_STAGECLEAR.getWidth() / 2,
				flashText_STAGECLEAR.getHeight());

		setAlarm(0, (int) (FGStage.getSpeed() * 0.7f), true);
		startAlarm(0);
	}

	@Override
	protected void onDraw() {
		this.setPosition(mainView.getXFromStage(), mainView.getYFromStage());
		// 绘制分数
		GlobalResources.GRAPHICFONT_SCORE.drawText(this.getX() + 100,
				this.getY() + 2, FGGamingThread.score + "");
		// 绘制剩余自机
		for (int i = 0; i < Player.remainLife; i++) {
			mySmallIcon
					.draw((int) getX() + i * (mySmallIcon.getWidth() + 1),
							(int) getY()
									+ 4
									+ GlobalResources.GRAPHICFONT_SCORE
											.getCharHeight());
		}

		if (textFlash) {
			switch (flashTextType) {
			case 0:
				break;
			case 1:
				flashText_STAGECLEAR.draw(
						(int) (getX() + mainView.getWidthFromStage() / 2),
						(int) (getY() + mainView.getHeightFromStage() / 2 - 5));
				break;
			case 2:
				flashText_GAMEOVER.draw(
						(int) (getX() + mainView.getWidthFromStage() / 2),
						(int) (getY() + mainView.getHeightFromStage() / 2 - 5));
				break;
			}
		}

	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			textFlash = !textFlash;
		}
	}

	/**
	 * @Title: toggleFlashText
	 * @Description: 切换闪烁文字的类型
	 * @param: @param textType 0-不显示 1-stage clear 2-game over
	 * @return: void
	 */
	public void toggleFlashText(int textType) {
		if (textType < 0 || textType > 2) {
			throw new IllegalArgumentException();
		}

		flashTextType = textType;
	}

	public HUD() {
		this.setDepth(-1000);
		this.perform(FGStage.getCurrentStage().getStageIndex());
	}

}
