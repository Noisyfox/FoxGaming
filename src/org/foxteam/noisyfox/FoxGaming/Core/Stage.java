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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.foxteam.noisyfox.FoxGaming.G2D.Background;

import android.graphics.Color;

/**
 * @ClassName: Stage
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:29:50
 * 
 */
public abstract class Stage {
	// 全局参数
	private static List<Stage> stages = new ArrayList<Stage>();
	protected static Stage currentStage = null;// 当前活动的stage
	protected static Stage targetStage = null;// 要跳转到的stage
	protected static float speed = 30f;// 当前活动的stage的speed

	protected List<Performer> performers = null;
	protected List<Views> activatedViews = null;
	protected int width = 480;// stage 的宽
	protected int height = 800;// stage 的高
	protected float stageSpeed = 30f;
	protected int backgroundColor = Color.WHITE;
	protected Background background = null;
	protected int stageIndex = -1;
	private boolean available = false;
	private List<Performer> employingPerformer = null;
	private List<Performer> emploiedPerformer = null;
	private List<Performer> dismissingPerformer = null;
	private List<Performer> dismissedPerformer = null;
	private Queue<Performer> collisions = null;

	/*
	 * Stage 初始化函数，在切换 Stage 时目标 Stage 被载入时执行，用来执行添加 Performer 以及其它初始化 Stage 的工作
	 */
	protected abstract void onCreate();

	public Stage() {
		performers = new ArrayList<Performer>();
		activatedViews = new ArrayList<Views>();
		employingPerformer = new ArrayList<Performer>();
		dismissingPerformer = new ArrayList<Performer>();
		emploiedPerformer = new ArrayList<Performer>();
		dismissedPerformer = new ArrayList<Performer>();
		collisions = new ConcurrentLinkedQueue<Performer>();
		stages.add(this);
		stageIndex = stages.size() - 1;
		available = true;
		// 如果当前创建的stage是游戏中惟一的一个stage则自动将其设置为目标stage
		if (stageIndex == 0) {
			targetStage = this;
		}
	}

	public Stage(int index) {
		this();
		setStageIndex(index);
	}

	public static final Stage getCurrentStage() {
		return currentStage;
	}

	public static final Stage index2Stage(int stageIndex) {
		if (stageIndex < 0 || stageIndex > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
		return stages.get(stageIndex);
	}

	/**
	 * 静态函数 跳转到指定舞台<br>
	 */
	public static final void switchToStage(int stage) {
		if (index2Stage(stage) == currentStage)
			return;
		targetStage = index2Stage(stage);
	}

	/**
	 * 静态函数 跳转到下一个舞台<br>
	 */
	public static final void nextStage() {
		switchToStage(currentStage.stageIndex + 1);
	}

	/**
	 * 静态函数 跳转到上一个舞台<br>
	 */
	public static final void previousStage() {
		switchToStage(currentStage.stageIndex - 1);
	}

	/**
	 * 静态函数 获取当前活动的stage的speed<br>
	 */
	public static final float getSpeed() {
		return speed;
	}

	/**
	 * 静态函数 获取当前活动的stage的background<br>
	 */
	public static final Background getCurrentBackground() {
		return currentStage.background;
	}

	/**
	 * 静态函数 获取当前活动的stage的中的Performer数量<br>
	 */
	public static final int getPerformerCount() {
		return currentStage.performers.size();
	}

	/**
	 * 静态函数 获取当前活动的 stage 的中所有属于 类型c 的 Performer
	 */
	@SuppressWarnings("rawtypes")
	public static final Performer[] getPerformersByClass(Class c) {
		List<Performer> per = new ArrayList<Performer>();

		for (Performer p : currentStage.performers) {
			if (c.isInstance(p)) {
				per.add(p);
			}
		}

		Performer[] p = new Performer[per.size()];

		return per.toArray(p);
	}

	protected final void sortWithDepth() {
		synchronized (performers) {
			Comparator<Performer> cmp = new Comparator<Performer>() {
				@Override
				public int compare(Performer lhs, Performer rhs) {
					if (lhs.depth > rhs.depth)
						return -1;
					if (lhs.depth < rhs.depth)
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
	private static final void updateStageIndex() {
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
	private final void ensureAvailable() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");
	}

	// 初始化该 Stage
	protected void initStage() {
		ensureAvailable();

		performers.clear();
		activatedViews.clear();
		width = 480;
		height = 800;
		stageSpeed = 30f;
		backgroundColor = Color.WHITE;
		background = null;
		employingPerformer.clear();
		emploiedPerformer.clear();
		dismissingPerformer.clear();
		dismissedPerformer.clear();
		collisions.clear();
	}

	protected final void employPerformer(Performer performer) {
		ensureAvailable();
		synchronized (employingPerformer) {
			if (employingPerformer.contains(performer)) {
				return;
			}
			employingPerformer.add(performer);
		}
	}

	protected final void employPerformer() {
		synchronized (performers) {
			synchronized (employingPerformer) {
				for (Performer p : employingPerformer) {
					if (performers.contains(p))
						continue;
					performers.add(p);
					emploiedPerformer.add(p);
				}
				sortWithDepth();
				employingPerformer.clear();
			}
		}
		for (Performer p : emploiedPerformer) {
			p.employed = true;
			p.stage = stageIndex;
			p.callEvent(EventsListener.EVENT_ONCREATE);
		}
		emploiedPerformer.clear();
	}

	protected final void dismissPerformer(Performer performer) {
		ensureAvailable();
		synchronized (dismissingPerformer) {
			if (dismissingPerformer.contains(performer)) {
				return;
			}
			dismissingPerformer.add(performer);
		}
	}

	protected final void dismissPerformer() {
		synchronized (performers) {
			synchronized (dismissingPerformer) {
				for (Performer p : dismissingPerformer) {
					if (!performers.contains(p))
						continue;
					performers.remove(p);

					dismissedPerformer.add(p);
				}
				sortWithDepth();
				dismissingPerformer.clear();
			}
		}
		for (Performer p : dismissedPerformer) {
			p.callEvent(EventsListener.EVENT_ONDESTORY);
			p.employed = false;
			p.performing = false;
		}
		dismissedPerformer.clear();
	}

	public final void broadcastEvent(int event, Object... args) {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				p.callEvent(event, args);
			}
		}
	}

	public final void addView(Views view) {
		if (activatedViews.contains(view)) {
			MyDebug.warning("View already activated!");
			return;
		}
		activatedViews.add(view);
	}

	public final int getViewNumber() {
		return activatedViews.size();
	}

	public final Views getView(int index) {
		return activatedViews.get(index);
	}

	public final void removeView(Views view) {
		if (!activatedViews.contains(view)) {
			MyDebug.warning("View not activated!");
			return;
		}
		activatedViews.remove(view);
	}

	public final void removeView(int index) {
		activatedViews.remove(index);
	}

	public final void setSize(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getWidth() {
		return width;
	}

	public final void setStageSpeed(float stageSpeed) {
		ensureAvailable();
		this.stageSpeed = stageSpeed;
	}

	public final float getStageSpeed() {
		return stageSpeed;
	}

	public final void setBackgroundColor(int color) {
		backgroundColor = color;
	}

	public final int getBackgroundColor() {
		return backgroundColor;
	}

	public final void setBackground(Background background) {
		this.background = background;
	}

	public final Background getBackground() {
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
		if (currentStage.equals(this)) {
			throw new IllegalArgumentException("无法改变当前活动的stage");
		}
		stages.remove(this);
		stages.add(index, this);
		updateStageIndex();
	}

	public final void closeStage() {
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
	public static final void closeStage(int stageIndex) {
		index2Stage(stageIndex).closeStage();
	}

	// 处理定时器
	protected final void operateAlarm() {
		ensureAvailable();
		synchronized (performers) {
			for (Performer p : performers) {
				if (!p.frozen) {
					p.goAlarm();
				}
			}
		}
	}

	// 处理碰撞检测
	@SuppressWarnings("rawtypes")
	protected final void operateCollision() {
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
									collisions.offer(p);
									collisions.offer(tp);
								}
							}
						}

						for (Performer p2 : performers) {
							if (p2 != p && p2.collisionMask != null
									&& !p2.frozen) {
								for (Class c : p.requiredClassCollisionDetection) {
									if (c.isInstance(p2)) {
										if (p.collisionMask
												.isCollisionWith(p2.collisionMask)) {

											collisions.offer(p);
											collisions.offer(p2);
										}
									}
								}
							}
						}

					}
				}
			}
		}

		while (!collisions.isEmpty()) {
			collisions.poll().callEvent(EventsListener.EVENT_ONCOLLISIONWITH,
					collisions.poll());
		}
	}

	// 检测 Performer 是否离开 Stage
	protected final void detectOutOfStage() {
		synchronized (performers) {
			for (Performer p : performers) {
				if (!p.frozen && p.isOutOfStage()) {
					p.callEvent(EventsListener.EVENT_ONOUTOFSTAGE);
				}
			}
		}
	}

	// 更新每个 Performer 的位置
	protected final void updateMovement() {

		synchronized (performers) {
			for (Performer p : performers) {
				if (!p.frozen) {
					p.updateMovement();
				}
			}
		}
	}

	// 执行每个 Performer 的 ScreenPlay
	protected final void playScreenPlay() {

		synchronized (performers) {
			for (Performer p : performers) {
				if (!p.frozen && p.myScreenPlay != null) {
					if (p.myScreenPlay.play()) {
						p.myScreenPlay = null;
					}
				}
			}
		}
	}

}
