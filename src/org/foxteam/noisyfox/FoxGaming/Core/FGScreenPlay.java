/**
 * FileName:     ScreenPlay.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-16 下午9:17:40
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-16      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: ScreenPlay
 * @Description: 剧本，可以应用在 Performer 上，按顺序执行指定动作
 * @author: Noisyfox
 * @date: 2012-8-16 下午9:17:40
 * 
 */
public final class FGScreenPlay {

	private static final int MOVEMETN_WAIT = 1;
	private static final int MOVEMETN_STOP = 2;
	private static final int MOVEMETN_JUMPTO = 3;
	private static final int MOVEMETN_MOVETOWARDS_DS = 4;
	private static final int MOVEMETN_MOVETOWARDS_XY = 5;
	private static final int MOVEMETN_MOVETOWARDSWAIT = 6;

	private FGPerformer bindPerformer = null;

	private ArrayList<ArrayList<Float>> movements = new ArrayList<ArrayList<Float>>();
	private ConcurrentLinkedQueue<ConcurrentLinkedQueue<Float>> movements_tmp = new ConcurrentLinkedQueue<ConcurrentLinkedQueue<Float>>();

	private int wait_step_remain = 0;

	private boolean isPlaying = false;

	/**
	 * @Title: clear
	 * @Description: 清空该 screenplay 包含的所有动作
	 * @param:
	 * @return: void
	 */
	public void clear() {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		movements.clear();
		movements_tmp.clear();
	}

	/**
	 * @Title: wait
	 * @Description: 添加动作 -- 等待指定的 step,阻塞
	 * @param: @param steps
	 * @return: void
	 */
	public void wait(int steps) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		if (steps <= 0) {
			throw new IllegalArgumentException();
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_WAIT));
		m.add(Float.valueOf(steps));
		movements.add(m);
	}

	/**
	 * @Title: stop
	 * @Description: 添加动作 -- 停止运动,非阻塞
	 * @param:
	 * @return: void
	 */
	public void stop() {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_STOP));
		movements.add(m);
	}

	/**
	 * @Title: jumpTo
	 * @Description: 添加动作 -- 直接设置 performer 的坐标到指定位置,非阻塞
	 * @param: @param x
	 * @param: @param y
	 * @return: void
	 */
	public void jumpTo(int x, int y) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_JUMPTO));
		m.add(Float.valueOf(x));
		m.add(Float.valueOf(y));
		movements.add(m);
	}

	/**
	 * @Title: moveTowards
	 * @Description: 添加动作 -- 以指定速度朝指定的方向运动,非阻塞
	 * @param: @param dir 角度制
	 * @param: @param speed
	 * @return: void
	 */
	public void moveTowards(float dir, float speed) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_MOVETOWARDS_DS));
		m.add(Float.valueOf(dir));
		m.add(Float.valueOf(speed));
		movements.add(m);
	}

	/**
	 * @Title: moveTowards
	 * @Description: 添加动作 -- 以指定速度朝指定点方向运动,非阻塞
	 * @param: @param x
	 * @param: @param y
	 * @param: @param speed
	 * @return: void
	 */
	public void moveTowards(int x, int y, float speed) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_MOVETOWARDS_XY));
		m.add(Float.valueOf(x));
		m.add(Float.valueOf(y));
		m.add(Float.valueOf(speed));
		movements.add(m);
	}

	/**
	 * @Title: moveTowardsWait
	 * @Description: 添加动作 -- 在指定的 step 里运动到指定点（注意在到达指定点后并不会停止运动，只是会继续应用当前
	 *               screenplay 的下一个动作),阻塞
	 * @param: @param x
	 * @param: @param y
	 * @param: @param totalSteps
	 * @return: void
	 */
	public void moveTowardsWait(int x, int y, int totalSteps) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		if (totalSteps <= 0) {
			throw new IllegalArgumentException();
		}

		ArrayList<Float> m = new ArrayList<Float>();
		m.add(Float.valueOf(MOVEMETN_MOVETOWARDSWAIT));
		m.add(Float.valueOf(x));
		m.add(Float.valueOf(y));
		m.add(Float.valueOf(totalSteps));
		movements.add(m);
	}

	/**
	 * @Title: prepareToPlay
	 * @Description: 系统函数，应用 screenplay 到指定的 performer 上
	 * @param: @param bindPerformer
	 * @return: void
	 */
	protected void prepareToPlay(FGPerformer bindPerformer) {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}

		this.bindPerformer = bindPerformer;
		movements_tmp.clear();
		wait_step_remain = 0;

		for (ArrayList<Float> mv : movements) {
			ConcurrentLinkedQueue<Float> mv_tmp = new ConcurrentLinkedQueue<Float>();
			for (Float v : mv) {
				mv_tmp.offer(Float.valueOf(v.floatValue()));
			}
			movements_tmp.offer(mv_tmp);
		}
	}

	// 返回是否执行完毕
	protected boolean play() {
		isPlaying = true;
		while (wait_step_remain <= 0 && !movements_tmp.isEmpty()) {
			ConcurrentLinkedQueue<Float> mv_tmp = movements_tmp.poll();

			switch (mv_tmp.poll().intValue()) {
			case MOVEMETN_WAIT:
				wait_step_remain = mv_tmp.poll().intValue();
				break;
			case MOVEMETN_STOP:
				bindPerformer.motion_set(bindPerformer.direction, 0);
				break;
			case MOVEMETN_JUMPTO:
				bindPerformer.setPosition(mv_tmp.poll().floatValue(), mv_tmp
						.poll().floatValue());
				break;
			case MOVEMETN_MOVETOWARDS_DS:
				bindPerformer.motion_set(mv_tmp.poll().floatValue(), mv_tmp
						.poll().floatValue());
				break;
			case MOVEMETN_MOVETOWARDS_XY: {
				float x = mv_tmp.poll().floatValue();
				float y = mv_tmp.poll().floatValue();
				float dir = (float) Math.toDegrees(Math.atan2(
						bindPerformer.getY() - y, x - bindPerformer.getX()));
				bindPerformer.motion_set(dir, mv_tmp.poll().floatValue());
			}
				break;
			case MOVEMETN_MOVETOWARDSWAIT: {
				float x = mv_tmp.poll().floatValue();
				float y = mv_tmp.poll().floatValue();
				wait_step_remain = mv_tmp.poll().intValue();
				float dir = (float) Math.toDegrees(Math.atan2(
						bindPerformer.getY() - y, x - bindPerformer.getX()));
				float speed = (float) Math.sqrt((x - bindPerformer.getX())
						* (x - bindPerformer.getX())
						+ (y - bindPerformer.getY())
						* (y - bindPerformer.getY()))
						/ (float) wait_step_remain;
				bindPerformer.motion_set(dir, speed);
			}
				break;
			}

		}
		wait_step_remain -= wait_step_remain > 0 ? 1 : 0;
		isPlaying = !movements_tmp.isEmpty() || wait_step_remain > 0;
		return !isPlaying;
	}

	/**
	 * @Title: getRemainNumber
	 * @Description: 得到剩余动作的数量
	 * @param: @return
	 * @return: int
	 */
	public int getRemainNumber() {
		return movements_tmp.size();
	}

}
