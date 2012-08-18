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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

	@Override
	protected void onCreate() {
		mainView = Stage.getCurrentStage().getView(0);
		scoreFont.mapFont(org.foxteam.noisyfox.THEngine.R.drawable.ascii_score,
				"0123456789/.", false);
		scoreFont.setCharacterSpacing(-4);
	}

	@Override
	protected void onDraw() {
		this.setPosition(mainView.getXFromStage(), mainView.getYFromStage());

		Canvas c = this.getCanvas();
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		c.drawText("FPS:" + (int) GamingThread.getSPS(), this.getX() + 10,
				this.getY() + 10, p);
		c.drawText("Life:" + (Player.remainLife <= 0 ? 0 : Player.remainLife)
				+ " Score:" + GamingThread.score, this.getX() + 10,
				this.getY() + 20, p);
		scoreFont.drawText(c, this.getX() + 10, this.getY() + 20,
				GamingThread.score + "");
	}

	public HUD() {
		this.setDepth(-1000);
		this.perform(Stage.getCurrentStage().getStageIndex());
	}

}
