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
import java.util.List;
import java.util.Queue;
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

	private List<List<Float>> movements = new ArrayList<List<Float>>();
	private Queue<Queue<Float>> movements_tmp = new ConcurrentLinkedQueue<Queue<Float>>();

	private int wait_step_remain = 0;

	private boolean isPlaying = false;

	private void ensureAvaliable() {
		if (isPlaying) {
			throw new IllegalStateException(
					"Can't change the activited screenplay!");
		}
	}

	public void clear() {
		ensureAvaliable();

		movements.clear();
		movements_tmp.clear();
	}

	public void wait(int steps) {
		ensureAvaliable();

		if (steps <= 0) {
			throw new IllegalArgumentException();
		}

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_WAIT));
		m.add(new Float((float) steps));
		movements.add(m);
	}

	public void stop() {
		ensureAvaliable();

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_STOP));
		movements.add(m);
	}

	public void jumpTo(int x, int y) {
		ensureAvaliable();

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_JUMPTO));
		m.add(new Float((float) x));
		m.add(new Float((float) y));
		movements.add(m);
	}

	public void moveTowards(float dir, float speed) {
		ensureAvaliable();

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_MOVETOWARDS_DS));
		m.add(new Float((float) dir));
		m.add(new Float((float) speed));
		movements.add(m);
	}

	public void moveTowards(int x, int y, float speed) {
		ensureAvaliable();

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_MOVETOWARDS_XY));
		m.add(new Float((float) x));
		m.add(new Float((float) y));
		m.add(new Float((float) speed));
		movements.add(m);
	}

	public void moveTowardsWait(int x, int y, int totalSteps) {
		ensureAvaliable();

		if (totalSteps <= 0) {
			throw new IllegalArgumentException();
		}

		List<Float> m = new ArrayList<Float>();
		m.add(new Float((float) MOVEMETN_MOVETOWARDSWAIT));
		m.add(new Float((float) x));
		m.add(new Float((float) y));
		m.add(new Float((float) totalSteps));
		movements.add(m);
	}

	protected void prepareToPlay(FGPerformer bindPerformer) {
		ensureAvaliable();

		this.bindPerformer = bindPerformer;
		movements_tmp.clear();
		wait_step_remain = 0;

		for (List<Float> mv : movements) {
			Queue<Float> mv_tmp = new ConcurrentLinkedQueue<Float>();
			for (Float v : mv) {
				mv_tmp.offer(new Float(v.floatValue()));
			}
			movements_tmp.offer(mv_tmp);
		}
	}

	// 返回是否执行完毕
	protected boolean play() {
		isPlaying = true;
		while (wait_step_remain <= 0 && !movements_tmp.isEmpty()) {
			Queue<Float> mv_tmp = movements_tmp.poll();

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

	// 得到剩余动作的数量
	public int getRemainNumber() {
		return movements_tmp.size();
	}

}
