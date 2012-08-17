/**
 * FileName:     _00_MainMenu.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 上午11:30:37
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Stages;

import org.foxteam.noisyfox.FoxGaming.Core.Button;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.THEngine.Performers.Button_GameStart;

/**
 * @ClassName: _00_MainMenu
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 上午11:30:37
 * 
 */
public class _00_MainMenu extends Stage {

	@Override
	protected void onCreate() {
		Button bGameStart = new Button_GameStart();
		bGameStart.perform(this.getStageIndex());
		bGameStart.setPosition(this.getWidth() / 2, this.getHeight() / 2);
	}

}
