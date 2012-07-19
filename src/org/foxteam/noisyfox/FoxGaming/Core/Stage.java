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
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.G2D.Background;

import android.graphics.Color;

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
	protected static float speed = 30f;// 当前活动的stage的speed

	private List<Performer> performers = null;
	private float stageSpeed = 30f;
	private int backgroundColor = Color.WHITE;
	private Background background = null;
	private int stageIndex = -1;
	private boolean available = false;

	public Stage() {
		performers = new ArrayList<Performer>();
		stages.add(this);
		stageIndex = stages.size() - 1;
		available = true;
		// 如果当前创建的stage是游戏中惟一的一个stage则自动将其设置为活动的stage
		if (stageIndex == 0) {
			currentStage = 0;
		}
	}

	public Stage(int index) {
		this();
		setStageIndex(index);
	}

	public static int getCurrentStage() {
		return currentStage;
	}

	protected static Stage index2Stage(int stageIndex) {
		if (stageIndex < 0 || stageIndex > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
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
	public static float getSpeed() {
		return speed;
	}

	/**
	 * 静态函数 获取当前活动的stage的background<br>
	 */
	public static Background getCurrentBackground() {
		return index2Stage(currentStage).getBackground();
	}

	private final void sortWithDepth() {
		synchronized (performers) {
			Comparator<Performer> cmp = new Comparator<Performer>() {
				@Override
				public int compare(Performer lhs, Performer rhs) {
					int p1 = lhs.getDepth();
					int p2 = rhs.getDepth();
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

	// 保证该stage不被异常调用
	private final void ensureAvailable() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");
	}

	public final void employPerformer(Performer performer) {
		ensureAvailable();
		synchronized (performers) {
			if (performers.contains(performer))
				return;
			performers.add(performer);
			sortWithDepth();
		}
	}

	public final void broadcastEvent(int event, Object... args) {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				p.callEvent(event, args);
			}
		}
	}

	public final void setStageSpeed(float stageSpeed) {
		ensureAvailable();
		this.stageSpeed = stageSpeed;
	}

	public final float getStageSpeed() {
		ensureAvailable();
		return stageSpeed;
	}

	public final void setBackgroundColor(int color) {
		ensureAvailable();
		backgroundColor = color;
	}

	public final int getBackgroundColor() {
		ensureAvailable();
		return backgroundColor;
	}

	public final void setBackground(Background background) {
		ensureAvailable();
		this.background = background;
	}

	public final Background getBackground() {
		ensureAvailable();
		return background;
	}

	public final int getStageIndex() {
		ensureAvailable();
		return stageIndex;
	}

	public final void setStageIndex(int index) {
		ensureAvailable();
		if (index < 0 || index > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
		if (index2Stage(currentStage).equals(this)) {
			throw new IllegalArgumentException("无法改变当前活动的stage");
		}
		stages.remove(this);
		stages.add(index, this);
		for (int i = Math.min(index, stageIndex); i <= Math.max(index,
				stageIndex); i++) {
			stages.get(i).stageIndex = i;
		}
	}

	public final void closeStage() {
		ensureAvailable();
		available = false;
		if (index2Stage(currentStage).equals(this)) {
			throw new IllegalArgumentException("无法移除当前活动的stage");
		}
		stages.remove(this);// 移除stage记录
		for (int i = stageIndex; i < stages.size(); i++) {// 重新构建index
			stages.get(i).stageIndex = i;
		}
		// 移除stage中所有performer
		for (Performer p : performers) {
			p.dismiss();
		}
		performers.clear();
	}

	/**
	 * 静态函数 移除指定index的stage<br>
	 */
	public final static void closeStage(int stageIndex) {
		index2Stage(stageIndex).closeStage();
	}

	// 处理定时器
	protected final void processAlarm() {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				p.goAlarm();
			}
		}
	}
}
