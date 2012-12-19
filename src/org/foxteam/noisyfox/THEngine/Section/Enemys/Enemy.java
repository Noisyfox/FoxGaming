/**
 * FileName:     Enemy.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-12 下午7:03:48
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-12      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.Enemys;

import org.foxteam.noisyfox.THEngine.Section.BasicElements.Hitable;

/**
 * @ClassName: Enemy
 * @Description: 基础敌机类
 * @author: Noisyfox
 * @date: 2012-8-12 下午7:03:48
 * 
 */
public abstract class Enemy extends Hitable {

	public abstract void createEnemy(int x, int y, int... extraConfig);

	public abstract void prepareEnemy();

}
