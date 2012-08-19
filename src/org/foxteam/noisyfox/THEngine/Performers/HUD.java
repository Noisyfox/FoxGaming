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
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicFont;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;

import android.graphics.Canvas;

/**
 * @ClassName: HUD
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-12 下午3:56:55
 * 
 */
public class HUD extends Performer {

	Views mainView = null;
	GraphicFont scoreFont = new GraphicFont();
	Sprite mySmallIcon = new Sprite();

	@Override
	protected void onCreate() {
		mainView = Stage.getCurrentStage().getView(0);
		scoreFont.mapFont(org.foxteam.noisyfox.THEngine.R.drawable.ascii_score,
				"0123456789/.", false);
		scoreFont.setCharacterSpacing(-4);
		scoreFont.setAlignment(GraphicFont.ALIGN_RIGHT);

		mySmallIcon.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.player_icon, false);
	}

	@Override
	protected void onDraw() {
		this.setPosition(mainView.getXFromStage(), mainView.getYFromStage());

		Canvas c = this.getCanvas();
		// 绘制分数
		scoreFont.drawText(c, this.getX() + 100, this.getY() + 2,
				GamingThread.score + "");
		// 绘制剩余自机
		for (int i = 0; i < Player.remainLife; i++) {
			mySmallIcon.draw(c,
					(int) getX() + i * (mySmallIcon.getWidth() + 1),
					(int) getY() + 4 + scoreFont.getCharHeight());
		}

	}

	public HUD() {
		this.setDepth(-1000);
		this.perform(Stage.getCurrentStage().getStageIndex());
	}

}
