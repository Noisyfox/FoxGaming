/**
 * FileName:     Hitable.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-15 上午11:18:17
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-15      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;

/**
 * @ClassName: Hitable
 * @Description: 可被子弹击中的单位的基础类，负责通用计算HP，爆炸效果等
 * @author: Noisyfox
 * @date: 2012-8-15 上午11:18:17
 * 
 */
public class Hitable extends FGPerformer {

	private float hp = 0f;
	protected boolean invincible = false;

	public final void setHP(float hp) {
		this.hp = hp;
	}

	public final float remainHP() {
		return hp;
	}

	public final void hitBy(Bullet bullet) {
		if (!invincible && hp > 0) {
			hp -= bullet.damage;

			bullet.hitOn(this);

			if (hp <= 0f) {
				Explosion(bullet);
			}
		}
	}

	// 生命值小于等于0时触发该函数，默认为直接 dismiss 该 performer，请重载该函数以自定义特效、分数统计等
	protected void Explosion(Bullet bullet) {
		this.dismiss();
	}

	@Override
	protected void onCollisionWith(FGPerformer target) {
		if (Bullet.class.isInstance(target)) {
			this.hitBy((Bullet) target);
		}
	}

	public final boolean isInvincible() {
		return invincible;
	}

}
