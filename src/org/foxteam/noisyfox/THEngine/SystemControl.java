/**
 * FileName:     SystemControl.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午11:19:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @ClassName: SystemControl
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:19:55
 * 
 */
public class SystemControl extends Performer {

	private int bgmId;

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {
			bgmId = SimpleBGM
					.loadBGM(org.foxteam.noisyfox.THEngine.R.raw.test_bgm);
			SimpleBGM.play(bgmId, true);
			Views v = new Views();
			v.setPositionFromScreen(0, 0);
			v.setPositionFromStage(0, 0);
			v.setSizeFromScreen(GamingThread.getScreenWidth(),
					GamingThread.getScreenHeight());
			v.setSizeFromStage(Stage.getCurrentStage().getWidth(), Stage
					.getCurrentStage().getHeight());
			v.setAngleFromStage(0);
			Stage.getCurrentStage().addView(v);
		}
	};

	@Override
	protected void onDraw(Performer performer) {
		Canvas c = performer.getCanvas();
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		c.drawText(Stage.getPerformerCount() + "", 10, 10, p);
	}

	public SystemControl() {
		this.setEventsListener(eventsListener);
	}

}
