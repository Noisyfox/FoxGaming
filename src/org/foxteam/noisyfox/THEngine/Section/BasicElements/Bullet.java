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
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.*;

/**
 * @ClassName: Bullet
 * @Description: 基础子弹类
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:09:41
 * 
 */
public abstract class Bullet extends FGPerformer {

	protected float damage = 0f;

	// public float speed = 0f;
	// public float angle = 0f;
	//
	// public void movement() {
	// this.setPosition(this.getX() + MathsHelper.lengthdir_x(speed, angle),
	// this.getY() + MathsHelper.lengthdir_y(speed, angle));
	// }
	//
	// public Bullet() {
	//
	// }
	//
	// @Override
	// protected void onStep() {
	// movement();
	// }
	//
	// @Override
	// protected void onCollisionWith(Performer target) {
	//
	// }

	// 设置子弹伤害，正值为伤血，负值为加血
	public final void setDamage(float damage) {
		this.damage = damage;
	}

	public final float getDamage() {
		return damage;
	}

	@Override
	protected void onDestory() {
		BulletPool.recycleBullet(this);
	}

	public abstract void hitOn(Hitable target);

	public abstract void createBullet(int x, int y, float speed,
			float direction, float... extraConfig);

	public abstract void recycleBullet();

}
