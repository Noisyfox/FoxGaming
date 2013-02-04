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
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.THEngine.Section.Enemys.Enemy;

import android.util.SparseArray;

/**
 * @ClassName: EnemyController
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-22 下午1:46:12
 * 
 */
public final class EnemyController extends FGPerformer {

	private int totalStep = 0;
	SparseArray<List<EnemyDef>> enemys = new SparseArray<List<EnemyDef>>();
	private boolean paused = false;
	private List<Enemy> boss = new ArrayList<Enemy>();
	private int bossCount = 0;
	private int maxStep = 0;
	private boolean cleared = false;

	public EnemyController() {
		requireEventFeature(FGEventsListener.EVENT_ONSTEPEND);
	}

	public void addEnemy(int step, boolean isBoss, Class<?> enemyType, int x,
			int y, int... extraConfig) {
		int key = step;
		List<EnemyDef> enemyList = null;
		if (enemys.get(key) != null) {
			enemyList = enemys.get(key);
		} else {
			enemyList = new ArrayList<EnemyDef>();
			enemys.put(key, enemyList);
		}
		EnemyDef ed = new EnemyDef();
		ed.x = x;
		ed.y = y;
		ed.extraConfig = extraConfig;
		ed.isBoss = isBoss;
		try {
			ed.instance = (Enemy) enemyType.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ed.instance.prepareEnemy();
		enemyList.add(ed);

		if (step > maxStep)
			maxStep = step;
	}

	@Override
	protected void onStepEnd() {
		if (paused)
			return;

		if (bossCount == 0) {
			int key = ++totalStep;
			if (key > maxStep) {
				if (!cleared) {
					((SectionStage) FGStage.getCurrentStage()).stageClear();
					cleared = true;
				}
			} else {
				if (enemys.get(key) != null) {
					List<EnemyDef> enemyList = enemys.get(key);
					for (EnemyDef ed : enemyList) {
						ed.instance.createEnemy(ed.x, ed.y, ed.extraConfig);
						if (ed.isBoss) {
							boss.add(ed.instance);
							bossCount++;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < bossCount;) {
				Enemy b = boss.get(i);
				if (!b.isPerforming()) {
					boss.remove(i);
					bossCount--;
				} else {
					i++;
				}
			}
		}

	}

	public void jumpTo(int step) {
		if (step < 0) {
			throw new IllegalArgumentException();
		}
		totalStep = step;
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	private class EnemyDef {
		private int x = 0;
		private int y = 0;
		private int[] extraConfig = null;
		private boolean isBoss = false;
		private Enemy instance = null;
	}

}
