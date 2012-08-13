/**
 * FileName:     Bullet.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-19 下午3:09:41
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

/**
 * @ClassName: Bullet
 * @Description: 基础子弹类
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:09:41
 * 
 */
public abstract class Bullet extends Performer {

	public float damage = 0f;
	public float speed = 0f;
	public float angle = 0f;

	public void movement() {

	}

	public Bullet() {

	}

	@Override
	protected void onStep() {
		movement();
	}

	@Override
	protected void onCollisionWith(Performer target) {

	}

}
