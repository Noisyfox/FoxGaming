/**
 * FileName:     MainView.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:02:22
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.content.Context;
import android.view.SurfaceView;

/**
 * @ClassName: MainView
 * @Description: 内建view
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:02:22
 * 
 */
public class FGGameView extends SurfaceView {

	protected FGGameView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

}
