/**
 * FileName:     Button_GameStart.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午2:54:30
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.Button;
import org.foxteam.noisyfox.FoxGaming.Core.GameActivity;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;

import android.view.KeyEvent;

/**
 * @ClassName: Button_GameStart
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 下午2:54:30
 * 
 */
public class Button_GameStart extends Button {

	public Button_GameStart() {
		super(170, 53,
				org.foxteam.noisyfox.THEngine.R.drawable.button_gamestart);
	}

	@Override
	public void onClick() {
		Stage.nextStage();
		Player.remainLife = 3;
	}

	@Override
	protected void onKeyRelease(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GameActivity.getGameCore().gameEnd();
		}
	}

}
