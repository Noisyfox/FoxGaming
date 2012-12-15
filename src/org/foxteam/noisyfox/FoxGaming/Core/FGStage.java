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

import org.foxteam.noisyfox.FoxGaming.G2D.FGBackground;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleSystem;

import android.graphics.Color;

/**
 * @ClassName: Stage
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:29:50
 * 
 */
public abstract class FGStage {
	// 全局参数
	private static List<FGStage> stages = new ArrayList<FGStage>();
	protected static FGStage currentStage = null;// 当前活动的stage
	protected static FGStage targetStage = null;// 要跳转到的stage
	protected static boolean switchStage = false;
	protected static float speed = 30f;// 当前活动的stage的speed
	protected static Comparator<FGPerformer> depthComparator = new Comparator<FGPerformer>() {
		@Override
		public int compare(FGPerformer lhs, FGPerformer rhs) {
			return rhs.depth - lhs.depth;
		}
	};
	protected static Comparator<ManagedParticleSystem> depthComparator2 = new Comparator<ManagedParticleSystem>() {
		@Override
		public int compare(ManagedParticleSystem lhs, ManagedParticleSystem rhs) {
			return rhs.depth - lhs.depth;
		}
	};

	protected List<FGPerformer> performers = new ArrayList<FGPerformer>();
	protected int performerCount = 0;
	protected FGViews activatedView = null;
	protected List<ManagedParticleSystem> managedParticleSystem = new ArrayList<ManagedParticleSystem>();
	protected int managedParticleSystemSize = 0;
	protected int width = 480;// stage 的宽
	protected int height = 800;// stage 的高
	protected float stageSpeed = 30f;
	protected int backgroundColor = Color.WHITE;
	protected FGBackground background = null;
	protected int stageIndex = -1;
	protected boolean closed = false;
	private boolean available = false;
	private List<FGPerformer> employingPerformer = new ArrayList<FGPerformer>();
	private List<FGPerformer> emploiedPerformer = new ArrayList<FGPerformer>();
	private List<FGPerformer> dismissingPerformer = new ArrayList<FGPerformer>();
	private List<FGPerformer> dismissedPerformer = new ArrayList<FGPerformer>();
	private Queue<FGPerformer> collisions = new ConcurrentLinkedQueue<FGPerformer>();

	/**
	 * @Title: onCreate
	 * @Description: Stage 初始化函数，在切换 Stage 时目标 Stage 被载入时执行，用来执行添加 Performer
	 *               以及其它初始化 Stage 的工作
	 * @param:
	 * @return: void
	 */
	protected abstract void onCreate();

	public FGStage() {
		stages.add(this);
		stageIndex = stages.size() - 1;
		available = true;
		// 如果当前创建的stage是游戏中惟一的一个stage则自动将其设置为目标stage
		if (stageIndex == 0) {
			targetStage = this;
			switchStage = true;
		}
	}

	public static final FGStage getCurrentStage() {
		return currentStage;
	}

	public static final FGStage index2Stage(int stageIndex) {
		if (stageIndex < 0 || stageIndex > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}
		return stages.get(stageIndex);
	}

	/**
	 * 静态函数 跳转到指定舞台<br>
	 */
	public static final void switchToStage(int stage) {
		// if (index2Stage(stage) == currentStage)
		// return;
		targetStage = index2Stage(stage);
		switchStage = true;
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
	 * 静态函数 重新开始当前舞台<br>
	 */
	public static final void restartStage() {
		switchStage = true;
	}

	/**
	 * 静态函数 获取当前活动的舞台的speed<br>
	 */
	public static final float getSpeed() {
		return speed;
	}

	/**
	 * 静态函数 获取当前活动的stage的background<br>
	 */
	public static final FGBackground getCurrentBackground() {
		return currentStage.background;
	}

	/**
	 * 静态函数 获取当前活动的stage的中的Performer数量<br>
	 */
	public static final int getPerformerCount() {
		return currentStage.performers.size();
	}

	/**
	 * 静态函数 获取当前活动的 stage 的中所有 Performer
	 */
	public static final FGPerformer[] getPerformers() {
		FGPerformer[] p = new FGPerformer[currentStage.performers.size()];
		return currentStage.performers.toArray(p);
	}

	/**
	 * 静态函数 获取当前活动的 stage 的中所有属于 类型c 的 Performer
	 */
	public static final FGPerformer[] getPerformersByClass(Class<?> c) {
		List<FGPerformer> per = new ArrayList<FGPerformer>();

		for (FGPerformer p : currentStage.performers) {
			if (c.isInstance(p)) {
				per.add(p);
			}
		}

		FGPerformer[] p = new FGPerformer[per.size()];

		return per.toArray(p);
	}

	protected final void sortWithDepth() {
		Collections.sort(performers, depthComparator);
	}

	/**
	 * @Title: updateStageIndex
	 * @Description: 重新构建index
	 * @param:
	 * @return: void
	 */
	private static final void updateStageIndex() {
		synchronized (stages) {
			int i = 0;
			for (FGStage s : stages) {
				s.stageIndex = i;
				synchronized (s.performers) {
					for (FGPerformer p : s.performers) {
						p.stage = i;
					}
				}
				i++;
			}
			// for (int i = 0; i < stages.size(); i++) {
			// stages.get(i).stageIndex = i;
			//
			// synchronized (stages.get(i).performers) {
			// for (FGPerformer p : stages.get(i).performers) {
			// p.stage = i;
			// }
			// }
			//
			// }
		}
	}

	// 初始化该 Stage
	protected void initStage() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		// 注销所有存在的 Performer
		for (FGPerformer p : performers) {
			p.employed = false;
			p.performing = false;
		}

		performers.clear();
		performerCount = 0;
		activatedView = null;
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
		managedParticleSystem.clear();
		managedParticleSystemSize = 0;
	}

	protected final void employPerformer(FGPerformer performer) {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		synchronized (employingPerformer) {
			if (employingPerformer.contains(performer)) {
				return;
			}
			employingPerformer.add(performer);
		}
	}

	protected final void employPerformer() {
		synchronized (employingPerformer) {
			for (FGPerformer p : employingPerformer) {
				if (performers.contains(p))
					continue;
				performers.add(p);
				performerCount++;
				emploiedPerformer.add(p);
			}
			sortWithDepth();
			employingPerformer.clear();
		}

		for (FGPerformer p : emploiedPerformer) {
			p.employed = true;
			p.performing = true;
			p.stage = stageIndex;
			p.callEvent(FGEventsListener.EVENT_ONCREATE);
		}
		emploiedPerformer.clear();
	}

	protected final void dismissPerformer(FGPerformer performer) {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		synchronized (dismissingPerformer) {
			if (dismissingPerformer.contains(performer)) {
				return;
			}
			dismissingPerformer.add(performer);
		}
	}

	protected final void dismissPerformer() {
		synchronized (dismissingPerformer) {
			for (FGPerformer p : dismissingPerformer) {
				if (!performers.contains(p))
					continue;
				performers.remove(p);
				performerCount--;
				dismissedPerformer.add(p);
			}
			sortWithDepth();
			dismissingPerformer.clear();
		}

		for (FGPerformer p : dismissedPerformer) {
			p.callEvent(FGEventsListener.EVENT_ONDESTORY);
			p.employed = false;
			p.performing = false;
		}
		dismissedPerformer.clear();
	}

	public final void broadcastEvent(int event, Object... args) {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		for (int i = 0; i < performerCount; i++) {
			FGPerformer p = performers.get(i);
			p.callEvent(event, args);
		}
		// for (FGPerformer p : performers) {
		// p.callEvent(event, args);
		// }
	}

	public final void setView(FGViews view) {
		activatedView = view;
	}

	public final FGViews getView() {
		return activatedView;
	}

	public final void setSize(int height, int width) {
		if (height <= 0 || width <= 0) {
			throw new IllegalArgumentException(
					"Stage's height and width must larger than 0!");
		}
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
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

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

	public final void setBackground(FGBackground background) {
		this.background = background;
	}

	public final FGBackground getBackground() {
		return background;
	}

	public final int getStageIndex() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		return stageIndex;
	}

	public final void setStageIndex(int index) {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		if (index < 0 || index > stages.size() - 1) {
			throw new IllegalArgumentException("不存在的stage");
		}

		stages.remove(this);
		stages.add(index, this);
		updateStageIndex();
	}

	public final void closeStage() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		closed = true;

		// 如果当前 Stage 不在活动状态则立即销毁，否则在当前 Stage 转为不活动时再销毁
		if (!this.equals(FGStage.currentStage)
				&& !this.equals(FGStage.targetStage)) {
			// 移除stage中所有performer
			for (FGPerformer p : performers) {
				p.employed = false;
				p.performing = false;
			}
			performers.clear();
			performerCount = 0;

			stages.remove(this);// 移除stage记录

			updateStageIndex();
			available = false;
		}
	}

	/**
	 * 静态函数 移除指定index的stage<br>
	 */
	public static final void closeStage(int stageIndex) {
		index2Stage(stageIndex).closeStage();
	}

	// 处理定时器
	protected final void operateAlarm() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		for (FGPerformer p : performers) {
			if (!p.frozen) {
				p.goAlarm();
			}
		}
	}

	// 处理碰撞检测
	protected final void operateCollision() {
		if (!available)
			throw new RuntimeException("无法操作一个已经不存在的stage");

		for (FGPerformer p : performers) {
			synchronized (p) {

				if (p.collisionMask != null && !p.frozen) {
					for (FGPerformer tp : p.requiredCollisionDetection) {

						if (index2Stage(tp.stage) == currentStage && !tp.frozen
								&& tp.collisionMask != null) {

							if (p.collisionMask
									.isCollisionWith(tp.collisionMask)) {
								collisions.offer(p);
								collisions.offer(tp);
							}
						}
					}

					for (FGPerformer p2 : performers) {
						if (p2 != p && p2.collisionMask != null && !p2.frozen) {
							for (Class<?> c : p.requiredClassCollisionDetection) {
								if (c.isInstance(p2)) {
									if (p.collisionMask
											.isCollisionWith(p2.collisionMask)) {

										collisions.offer(p);
										collisions.offer(p2);
									}
									break;
								}
							}
						}
					}

				}
			}
		}

		while (!collisions.isEmpty()) {
			collisions.poll().callEvent(FGEventsListener.EVENT_ONCOLLISIONWITH,
					collisions.poll());
		}
	}

	// 检测 Performer 是否离开 Stage
	protected final void detectOutOfStage() {
		for (FGPerformer p : performers) {
			if (!p.frozen && p.isOutOfStage()) {
				p.callEvent(FGEventsListener.EVENT_ONOUTOFSTAGE);
			}
		}
	}

	// 更新每个 Performer 的位置
	protected final void updateMovement() {

		for (FGPerformer p : performers) {
			if (!p.frozen) {
				p.updateMovement();
			}
		}
	}

	// 执行每个 Performer 的 ScreenPlay
	protected final void playScreenPlay() {

		for (FGPerformer p : performers) {
			if (!p.frozen && p.myScreenPlay != null) {
				if (p.myScreenPlay.play()) {
					p.myScreenPlay = null;
				}
			}
		}
	}

	// 托管的粒子系统
	protected class ManagedParticleSystem {
		FGParticleSystem particleSystem = null;
		int depth = 0;

		public ManagedParticleSystem(FGParticleSystem particleSystem, int depth) {
			if (particleSystem == null) {
				throw new IllegalArgumentException();
			}

			this.particleSystem = particleSystem;
			this.depth = depth;
		}

		@Override
		public boolean equals(Object o) {
			if (this.getClass().isInstance(o)) {
				return ((ManagedParticleSystem) o).particleSystem == particleSystem;
			}
			return super.equals(o);
		}

	}

	protected void updateParticleSystems() {
		for (ManagedParticleSystem mps : managedParticleSystem) {
			mps.particleSystem.update();
		}
	}

	public void managedParticleSystem_requireManaged(
			FGParticleSystem particleSystem, int depth) {
		ManagedParticleSystem mps = new ManagedParticleSystem(particleSystem,
				depth);
		if (!managedParticleSystem.contains(mps)) {
			managedParticleSystem.add(mps);
			managedParticleSystemSize++;
		}

		Collections.sort(managedParticleSystem, depthComparator2);
	}

	public int managedParticleSystem_managedCount() {
		return managedParticleSystemSize;
	}

	public FGParticleSystem managedParticleSystem_getParticleSystem(int index) {
		return managedParticleSystem.get(index).particleSystem;
	}

	public int managedParticleSystem_getDepth(int index) {
		return managedParticleSystem.get(index).depth;
	}

	public void managedParticleSystem_setDepth(int index, int depth) {
		managedParticleSystem.get(index).depth = depth;
		Collections.sort(managedParticleSystem, depthComparator2);
	}

	public void managedParticleSystem_removeParticleSystem(int index) {
		managedParticleSystem.remove(index);
		managedParticleSystemSize--;
	}

	public void managedParticleSystem_removeParticleSystem(
			FGParticleSystem particleSystem) {
		if (managedParticleSystem.contains(particleSystem)) {
			managedParticleSystem.remove(particleSystem);
			managedParticleSystemSize--;
		}
	}

}
