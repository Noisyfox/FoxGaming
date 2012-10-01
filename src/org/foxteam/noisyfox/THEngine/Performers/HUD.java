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
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;

import android.graphics.Canvas;

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
		mainView = FGStage.getCurrentStage().getView(0);

		mySmallIcon.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.player_icon, false);

		flashText_STAGECLEAR.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.flashtext_stage_clear,
				false);
		flashText_STAGECLEAR.setOffset(flashText_STAGECLEAR.getWidth() / 2,
				flashText_STAGECLEAR.getHeight());

		flashText_GAMEOVER.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.flashtext_game_over,
				false);
		flashText_GAMEOVER.setOffset(flashText_STAGECLEAR.getWidth() / 2,
				flashText_STAGECLEAR.getHeight());

		setAlarm(0, (int) (FGStage.getSpeed() * 0.7f), true);
		startAlarm(0);
	}

	@Override
	protected void onDraw() {
		this.setPosition(mainView.getXFromStage(), mainView.getYFromStage());

		Canvas c = this.getCanvas();
		// 绘制分数
		GlobalResources.GRAPHICFONT_SCORE.drawText(c, this.getX() + 100,
				this.getY() + 2, FGGamingThread.score + "");
		// 绘制剩余自机
		for (int i = 0; i < Player.remainLife; i++) {
			mySmallIcon
					.draw(c,
							(int) getX() + i * (mySmallIcon.getWidth() + 1),
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
				flashText_STAGECLEAR.draw(c,
						(int) (getX() + mainView.getWidthFromStage() / 2),
						(int) (getY() + mainView.getHeightFromStage() / 2 - 5));
				break;
			case 2:
				flashText_GAMEOVER.draw(c,
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
