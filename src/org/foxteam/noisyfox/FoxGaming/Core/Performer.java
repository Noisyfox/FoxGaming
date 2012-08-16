/**
 * FileName:     performer.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午9:21:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.G2D.*;

import android.graphics.Canvas;

/**
 * @ClassName: performer
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午9:21:05
 * 
 */
public class Performer extends EventsListener {

	private List<Alarm> alarms;
	private boolean visible = true;
	private float x = 0, y = 0;
	public float xprevious = 0;// Performer 以前的 x 坐标
	public float yprevious = 0;// Performer 以前的 y 坐标
	protected float hspeed = 0;// 速度的水平部分，即水平速度
	protected float vspeed = 0;// 速度的垂直部分，即垂直速度
	protected float direction = 0; // Performer 当前运动方向（ 0-360 度，逆时针， 0 = 朝右）
	protected float speed = 0;// Performer 当前速度（像素每步）
	public float friction = 0;// 当前阻力（像素每步）
	public float gravity = 0;// 当前重力（像素每步）
	public float gravity_direction = 270; // 重力方向（ 270 朝下）

	protected int depth = 0;
	protected boolean frozen = false;
	private Sprite sprite = null;
	protected boolean employed = false;
	protected boolean performing = false;
	protected int stage = -1;
	protected GraphicCollision collisionMask = null;
	protected List<Performer> requiredCollisionDetection = new ArrayList<Performer>();
	@SuppressWarnings("rawtypes")
	protected List<Class> requiredClassCollisionDetection = new ArrayList<Class>();
	protected ScreenPlay myScreenPlay = null;

	public String description = "";// 不产生实际作用，仅在调试、编辑时做参考用

	public Performer() {
		alarms = new ArrayList<Alarm>();
		for (int i = 0; i < 10; i++) {
			Alarm a = new Alarm();
			alarms.add(a);
		}
	}

	// 没有重载前负责绘制默认精灵，重载后如果不手动调用绘图则会使该performer不绘制默认精灵
	@Override
	protected void onDraw() {
		if (sprite != null) {
			Canvas c = GamingThread.bufferCanvas;
			sprite.draw(c, (int) x, (int) y);
		}
	}

	public final void callEvent(int event, Object... args) {
		if (frozen)
			return;
		switch (event) {
		case EventsListener.EVENT_ONCREATE:
			this.onCreate();
			break;
		case EventsListener.EVENT_ONDESTORY:
			this.onDestory();
			break;
		case EventsListener.EVENT_ONTOUCH:
			this.onTouch((Integer) args[0], (Integer) args[1],
					(Integer) args[2]);
			break;
		case EventsListener.EVENT_ONTOUCHPRESS:
			this.onTouchPress((Integer) args[0], (Integer) args[1],
					(Integer) args[2]);
			break;
		case EventsListener.EVENT_ONTOUCHRELEASE:
			this.onTouchRelease((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONKEY:
			this.onKey((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONKEYPRESS:
			this.onKeyPress((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONKEYRELEASE:
			this.onKeyRelease((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONALARM:
			this.onAlarm((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONGAMESTART:
			this.onGameStart();
			break;
		case EventsListener.EVENT_ONGAMEPAUSE:
			this.onGamePause();
			break;
		case EventsListener.EVENT_ONGAMERESUME:
			this.onGameResume();
			break;
		case EventsListener.EVENT_ONGAMEEND:
			this.onGameEnd();
			break;
		case EventsListener.EVENT_ONSTAGECHANGE:
			this.onStageChange();
			break;
		case EventsListener.EVENT_ONSTAGESTART:
			this.onStageStart();
			break;
		case EventsListener.EVENT_ONSTAGEEND:
			this.onStageEnd();
			break;
		case EventsListener.EVENT_ONDRAW:
			if (visible)
				onDraw();
			break;
		case EventsListener.EVENT_ONSTEP:
			this.onStep();
			break;
		case EventsListener.EVENT_ONSTEPSTART:
			this.onStepStart();
			break;
		case EventsListener.EVENT_ONSTEPEND:
			this.onStepEnd();
			break;
		case EventsListener.EVENT_ONCOLLISIONWITH:
			this.onCollisionWith((Performer) args[0]);
			break;
		case EventsListener.EVENT_ONUSERDEFINEDEVENT:
			this.onUserDefinedEvent((Integer) args[0]);
			break;
		case EventsListener.EVENT_ONSCREENSIZECHANGED:
			this.onScreenSizeChanged((Integer) args[0], (Integer) args[1]);
			break;
		case EventsListener.EVENT_ONOUTOFSTAGE:
			this.onOutOfStage();
			break;
		}
	}

	// 判断是否离开 Stage 的函数，默认为坐标/Sprite 的最外围矩形超出 Stage 范围，建议重载该函数以精确考虑 Sprite 的情况
	public boolean isOutOfStage() {
		if (!employed) {
			MyDebug.error("Performer is not on any stage!");
		}
		Stage s = Stage.index2Stage(stage);
		if (s != Stage.currentStage) {
			MyDebug.warning("Performer is not performing now!");
		}

		if (sprite == null) {
			return x < 0 || y < 0 || x > s.width || y > s.height;
		} else {
			return x + sprite.getWidth() - sprite.getOffsetX() < 0
					|| y + sprite.getHeight() - sprite.getOffsetY() < 0
					|| x - sprite.getOffsetX() > s.width
					|| y - sprite.getOffsetY() > s.height;
		}
	}

	public final void perform(int stage) {
		if (employed) {
			MyDebug.warning("Performer already been employed!");
			return;
		}

		Stage.index2Stage(stage).employPerformer(this);
	}

	public final void dismiss() {
		if (!employed) {
			MyDebug.warning("Can't dismiss an unemployed performer!");
			return;
		}

		Stage.index2Stage(stage).dismissPerformer(this);
	}

	public final void freezeMe() {
		frozen = true;
	}

	public final void unfreezeMe() {
		frozen = false;
	}

	public final void bindSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public final Sprite getSprite() {
		return sprite;
	}

	private void updateCollisionMask() {
		if (collisionMask != null) {
			collisionMask.setPosition((int) x, (int) y);
		}
	}

	public final void bindCollisionMask(GraphicCollision collisionMask) {
		this.collisionMask = collisionMask;
		updateCollisionMask();
	}

	public final GraphicCollision getCollisionMask() {
		return collisionMask;
	}

	public final void requireCollisionDetection(Performer target) {
		if (requiredCollisionDetection.contains(target)) {
			return;
		}

		requiredCollisionDetection.add(target);
	}

	// 检测所有属于 targer 类的 Performer
	@SuppressWarnings("rawtypes")
	public final void requireCollisionDetection(Class target) {
		if (requiredClassCollisionDetection.contains(target)) {
			return;
		}

		requiredClassCollisionDetection.add(target);
	}

	public final void cancelCollisionDetection(Performer target) {
		requiredCollisionDetection.remove(target);
	}

	@SuppressWarnings("rawtypes")
	public final void cancelClassCollisionDetection(Class target) {
		requiredClassCollisionDetection.remove(target);
	}

	public final void clearCollisionDetection() {
		requiredCollisionDetection.clear();
		requiredClassCollisionDetection.clear();
	}

	public final void setDepth(int depth) {
		this.depth = depth;
		if (employed) {
			Stage.index2Stage(this.stage).sortWithDepth();
		}
	}

	public final int getDepth() {
		return depth;
	}

	public final void setVisible(boolean visible) {
		this.visible = visible;
	}

	public final boolean isVisible() {
		return visible;
	}

	public final void setPosition(float x, float y) {
		xprevious = this.x;
		yprevious = this.y;
		this.x = x;
		this.y = y;
		updateCollisionMask();
	}

	public final float getX() {
		return x;
	}

	public final float getY() {
		return y;
	}

	// 以参数（方向，速度）设定运动
	public final void motion_set(float dir, float speed) {
		hspeed = MathsHelper.lengthdir_x(speed, dir);
		vspeed = -MathsHelper.lengthdir_y(speed, dir);
		this.speed = speed;
		direction = dir;
	}

	// 以参数（方向，速度）对当前运动做改变
	public final void motion_add(float dir, float speed) {
		hspeed += MathsHelper.lengthdir_x(speed, dir);
		vspeed -= MathsHelper.lengthdir_y(speed, dir);
		this.speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		direction = (float) Math.toDegrees(Math.atan2(-vspeed, hspeed));
	}

	protected void updateMovement() {
		// 先计算重力影响
		hspeed += MathsHelper.lengthdir_x(gravity, gravity_direction);
		vspeed -= MathsHelper.lengthdir_y(gravity, gravity_direction);
		// 再计算阻力影响
		hspeed -= MathsHelper.lengthdir_x(friction, direction);
		vspeed += MathsHelper.lengthdir_y(friction, direction);
		// 计算总速度及角度
		speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		direction = (float) Math.toDegrees(Math.atan2(-vspeed, hspeed));

		if (hspeed != 0 || vspeed != 0) {
			this.setPosition(x + hspeed, y + vspeed);
		}
	}

	public void playAScreenPlay(ScreenPlay screenPlay) {
		myScreenPlay = screenPlay;
		if (myScreenPlay != null) {
			myScreenPlay.prepareToPlay(this);
		}
	}

	public final Canvas getCanvas() {
		return GamingThread.bufferCanvas;
	}

	public final void setAlarm(int alarm, int step, boolean repeat) {
		Alarm a = alarms.get(alarm);
		a.setSteps = step;
		a.repeat = repeat;
	}

	public final void startAlarm(int alarm) {
		Alarm a = alarms.get(alarm);
		a.remainSteps = a.setSteps;
		a.start = true;
	}

	public final void stopAlarm(int alarm) {
		Alarm a = alarms.get(alarm);
		a.start = false;
	}

	public final int getAlarmRemainStep(int alarm) {
		Alarm a = alarms.get(alarm);
		return a.remainSteps;
	}

	public final boolean isAlarmStart(int alarm) {
		Alarm a = alarms.get(alarm);
		return a.start;
	}

	protected final void goAlarm() {
		for (int i = 0; i < alarms.size(); i++) {
			Alarm a = alarms.get(i);
			if (a.start) {
				if (a.remainSteps > 0) {
					a.remainSteps -= 1;
					if (a.remainSteps == 0) {
						callEvent(EventsListener.EVENT_ONALARM, i);
						if (a.repeat) {
							a.remainSteps = a.setSteps;
						} else {
							a.start = false;
						}
					}
				} else {
					a.start = false;
					a.remainSteps = 0;
				}
			}
		}
	}

	private class Alarm {
		public boolean start = false;
		public int setSteps = 0;
		public int remainSteps = 0;
		public boolean repeat = false;
	}

}
