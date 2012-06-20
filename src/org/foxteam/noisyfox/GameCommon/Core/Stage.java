/**
 * FileName:     Stage.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:29:50
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.GameCommon.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName: Stage
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:29:50
 * 
 */
public class Stage {
	// 全局参数
	private static List<Stage> stages = new ArrayList<Stage>();
	private static int currentStage = -1;// 当前活动的stage
	private static double speed = 30;// 当前活动的stage的speed

	private List<Performer> performers = null;
	private double stageSpeed = 30;

	public Stage() {
		performers = new ArrayList<Performer>();
	}

	public static int getCurrentStage() {
		return currentStage;
	}

	protected static Stage index2Stage(int stageIndex) {
		return stages.get(stageIndex);
	}

	/**
	 * 静态函数 跳转到指定舞台<br>
	 */
	public static void switchToStage(int stage) {
		if (stage == currentStage)
			return;
		currentStage = stage;
	}

	/**
	 * 静态函数 跳转到下一个舞台<br>
	 */
	public static void nextStage() {
		switchToStage(currentStage + 1);
	}

	/**
	 * 静态函数 跳转到上一个舞台<br>
	 */
	public static void previousStage() {
		switchToStage(currentStage - 1);
	}

	/**
	 * 静态函数 获取当前活动的stage的speed<br>
	 */
	public static double getSpeed() {
		return speed;
	}

	private void sortWithDeepth() {
		synchronized (performers) {
			Comparator<Performer> cmp = new Comparator<Performer>() {
				@Override
				public int compare(Performer lhs, Performer rhs) {
					int p1 = lhs.getDeepth();
					int p2 = rhs.getDeepth();
					if (p1 > p2)
						return -1;
					if (p1 < p2)
						return 1;
					return 0;
				}
			};
			Collections.sort(performers, cmp);
		}
	}

	public void employPerformer(Performer performer) {
		synchronized (performers) {
			if (performers.contains(performer))
				return;
			performers.add(performer);
			sortWithDeepth();
		}
	}

	public void broadcastEvent(int event, Object... args) {
		synchronized (performers) {
			for (Performer p : performers) {
				p.callEvent(event, args);
			}
		}
	}

	public void setStageSpeed(double stageSpeed) {
		this.stageSpeed = stageSpeed;
	}

	public double getStageSpeed() {
		return stageSpeed;
	}
}
