/**
 * FileName:     EnemyController.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-22 下午1:46:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-22      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.Performer;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;

/**
 * @ClassName: EnemyController
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-22 下午1:46:12
 * 
 */
public final class EnemyController extends Performer {

	private int totalStep = 0;
	HashMap<Integer, List<enemyDef>> enemys = new HashMap<Integer, List<enemyDef>>();

	public void addEnemy(int step, Class<?> enemyType, int x, int y,
			int... extraConfig) {
		Integer key = new Integer(step);
		List<enemyDef> enemyList = null;
		if (enemys.containsKey(key)) {
			enemyList = enemys.get(key);
		} else {
			enemyList = new ArrayList<enemyDef>();
			enemys.put(key, enemyList);
		}
		enemyDef ed = new enemyDef();
		ed.enemyClass = enemyType;
		ed.x = x;
		ed.y = y;
		ed.extraConfig = extraConfig;
		enemyList.add(ed);
	}

	@Override
	protected void onStepEnd() {
		Integer key = new Integer(++totalStep);
		if (enemys.containsKey(key)) {
			List<enemyDef> enemyList = enemys.get(key);
			for (enemyDef ed : enemyList) {
				try {
					Enemy enemy = (Enemy) ed.enemyClass.newInstance();
					enemy.createEnemy(ed.x, ed.y, ed.extraConfig);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class enemyDef {
		private Class<?> enemyClass = null;
		private int x = 0;
		private int y = 0;
		private int[] extraConfig = null;
	}

}
