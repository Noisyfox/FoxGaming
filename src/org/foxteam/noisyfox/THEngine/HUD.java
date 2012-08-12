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
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

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

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {
			mainView = Stage.getCurrentStage().getView(0);
		}

	};

	@Override
	protected void onDraw(Performer performer) {
		this.setPosition(mainView.getXFromStage(), mainView.getYFromStage());

		Canvas c = performer.getCanvas();
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		c.drawText(Stage.getPerformerCount() + "," + GamingThread.getSPS(),
				performer.getX() + 10, performer.getY() + 10, p);
	}

	public HUD() {
		this.setEventsListener(eventsListener);
		this.setDepth(-1000);
		this.perform(Stage.getCurrentStage().getStageIndex());
	}

}
