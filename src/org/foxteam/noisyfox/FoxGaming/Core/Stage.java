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
public final class Stage {
	// 全局参数
	private static List<Stage> stages = new ArrayList<Stage>();
	private static Stage currentStage = null;// 当前活动的stage
	protected static float speed = 30f;// 当前活动的stage的speed

	protected List<Performer> performers = null;
	protected List<Views> activatedViews = null;
	protected int width = 480;// stage 的宽
	protected int height = 800;// stage 的高
	private float stageSpeed = 30f;
	private int backgroundColor = Color.WHITE;
	private Background background = null;
	private int stageIndex = -1;
	private boolean available = false;
	private List<Performer> employingPerformer = null;
	private List<Performer> dismissingPerformer = null;

	public Stage() {
		performers = new ArrayList<Performer>();
		activatedViews = new ArrayList<Views>();
		employingPerformer = new ArrayList<Performer>();
		dismissingPerformer = new ArrayList<Performer>();
		stages.add(this);
		stageIndex = stages.size() - 1;
		available = true;
		// 如果当前创建的stage是游戏中惟一的一个stage则自动将其设置为活动的stage
		if (stageIndex == 0) {
			currentStage = this;
		}
	}

	public Stage(int index) {
		this();
		setStageIndex(index);
	}

	public static Stage getCurrentStage() {
		return currentStage;
	}

	public static Stage index2Stage(int stageIndex) {
		if (stageIndex < 0 || stageIndex > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
		return stages.get(stageIndex);
	}

	/**
	 * 静态函数 跳转到指定舞台<br>
	 */
	public static void switchToStage(int stage) {
		if (index2Stage(stage) == currentStage)
			return;
		currentStage = index2Stage(stage);
	}

	/**
	 * 静态函数 跳转到下一个舞台<br>
	 */
	public static void nextStage() {
		switchToStage(currentStage.stageIndex + 1);
	}

	/**
	 * 静态函数 跳转到上一个舞台<br>
	 */
	public static void previousStage() {
		switchToStage(currentStage.stageIndex - 1);
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
		return currentStage.getBackground();
	}

	/**
	 * 静态函数 获取当前活动的stage的中的Performer数量<br>
	 */
	public static int getPerformerCount() {
		return currentStage.performers.size();
	}

	/**
	 * 静态函数 获取当前活动的 stage 的中所有属于 类型c 的 Performer
	 */
	public static Performer[] getPerformersByClass(Class<Performer> c) {
		List<Performer> per = new ArrayList<Performer>();

		for (Performer p : currentStage.performers) {
			if (c.isInstance(p)) {
				per.add(p);
			}
		}

		return (Performer[]) per.toArray();
	}

	protected void sortWithDepth() {
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

	/**
	 * @Title: updateStageIndex
	 * @Description: 重新构建index
	 * @param:
	 * @return: void
	 * @throws
	 */
	private static void updateStageIndex() {
		synchronized (stages) {
			for (int i = 0; i < stages.size(); i++) {
				stages.get(i).stageIndex = i;

				synchronized (stages.get(i).performers) {
					for (Performer p : stages.get(i).performers) {
						p.stage = i;
					}
				}

			}
		}
	}

	// 保证该stage不被异常调用
	private void ensureAvailable() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");
	}

	protected void employPerformer(Performer performer) {
		ensureAvailable();
		synchronized (employingPerformer) {
			if (employingPerformer.contains(performer)) {
				return;
			}
			employingPerformer.add(performer);
		}
	}

	protected void employPerformer() {
		synchronized (performers) {
			synchronized (employingPerformer) {
				for (Performer p : employingPerformer) {
					if (performers.contains(p))
						continue;
					performers.add(p);
					p.callEvent(EventsListener.EVENT_ONCREATE);
				}
				sortWithDepth();
				employingPerformer.clear();
			}
		}
	}

	protected void dismissPerformer(Performer performer) {
		ensureAvailable();
		synchronized (dismissingPerformer) {
			if (dismissingPerformer.contains(performer)) {
				return;
			}
			dismissingPerformer.add(performer);
		}
	}

	protected void dismissPerformer() {
		synchronized (performers) {
			synchronized (dismissingPerformer) {
				for (Performer p : dismissingPerformer) {
					if (!performers.contains(p))
						continue;
					performers.remove(p);
					p.callEvent(EventsListener.EVENT_ONDESTORY);
				}
				sortWithDepth();
				dismissingPerformer.clear();
			}
		}
	}

	public void broadcastEvent(int event, Object... args) {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				p.callEvent(event, args);
			}
		}
	}

	public void addView(Views view) {
		if (activatedViews.contains(view)) {
			MyDebug.warning("View already activated!");
			return;
		}
		activatedViews.add(view);
	}

	public int getViewNumber() {
		return activatedViews.size();
	}

	public Views getView(int index) {
		return activatedViews.get(index);
	}

	public void removeView(Views view) {
		if (!activatedViews.contains(view)) {
			MyDebug.warning("View not activated!");
			return;
		}
		activatedViews.remove(view);
	}

	public void removeView(int index) {
		activatedViews.remove(index);
	}

	public void setSize(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setStageSpeed(float stageSpeed) {
		ensureAvailable();
		this.stageSpeed = stageSpeed;
	}

	public float getStageSpeed() {
		return stageSpeed;
	}

	public void setBackgroundColor(int color) {
		backgroundColor = color;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public Background getBackground() {
		return background;
	}

	public int getStageIndex() {
		ensureAvailable();
		return stageIndex;
	}

	public void setStageIndex(int index) {
		ensureAvailable();
		if (index < 0 || index > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
		if (currentStage.equals(this)) {
			throw new IllegalArgumentException("无法改变当前活动的stage");
		}
		stages.remove(this);
		stages.add(index, this);
		updateStageIndex();
	}

	public void closeStage() {
		ensureAvailable();
		if (currentStage.equals(this)) {
			throw new IllegalArgumentException("无法移除当前活动的stage");
		}

		// 移除stage中所有performer
		for (Performer p : performers) {
			p.dismiss();
		}
		performers.clear();

		stages.remove(this);// 移除stage记录

		updateStageIndex();
		available = false;
	}

	/**
	 * 静态函数 移除指定index的stage<br>
	 */
	public static void closeStage(int stageIndex) {
		index2Stage(stageIndex).closeStage();
	}

	// 处理定时器
	protected void operateAlarm() {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				p.goAlarm();
			}
		}
	}

	protected void operateCollision() {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				synchronized (p) {

					if (p.collisionMask != null && !p.frozen) {
						for (Performer tp : p.requiredCollisionDetection) {

							if (index2Stage(tp.stage) == currentStage
									&& !tp.frozen && tp.collisionMask != null) {

								if (p.collisionMask
										.isCollisionWith(tp.collisionMask)) {

									p.callEvent(
											EventsListener.EVENT_ONCOLLISIONWITH,
											tp);
								}
							}
						}
					}
				}
			}
		}
	}

}
