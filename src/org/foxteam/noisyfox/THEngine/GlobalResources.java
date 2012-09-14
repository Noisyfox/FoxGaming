/**
 * FileName:     GlobalResources.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-9-10 下午8:19:35
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-9-10      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.G2D.FGPathBezier3;

/**
 * @ClassName: GlobalResources
 * @Description: 全局资源
 * @author: Noisyfox
 * @date: 2012-9-10 下午8:19:35
 * 
 */
public class GlobalResources {

	public static FGPathBezier3 PATHBEZIER3_ENEMY_DOOL_BLUE = null;

	public static void loadResources() {
		// 初始化 敌机 玩偶怪 的移动路径
		PATHBEZIER3_ENEMY_DOOL_BLUE = new FGPathBezier3();
		PATHBEZIER3_ENEMY_DOOL_BLUE.startPath(0, 50);
		PATHBEZIER3_ENEMY_DOOL_BLUE.addNode(50, 0);
		PATHBEZIER3_ENEMY_DOOL_BLUE.endPath(320, 0, true);
	}
}
