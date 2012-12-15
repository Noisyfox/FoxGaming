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

/**
 * @ClassName: performer
 * @Description: 游戏运行时操作的主体对象
 * @author: Noisyfox
 * @date: 2012-6-19 下午9:21:05
 * 
 */
public class FGPerformer extends FGEventsListener {

	private static final int ALARM_COUNT_MAX = 10;

	private Alarm[] alarms = new Alarm[ALARM_COUNT_MAX];
	private boolean visible = true;
	private float x = 0, y = 0;
	public float xprevious = 0;// Performer 以前的 x 坐标
	public float yprevious = 0;// Performer 以前的 y 坐标
	protected float hspeed = 0;// 速度的水平部分，即水平速度
	protected float vspeed = 0;// 速度的垂直部分，即垂直速度
	protected float direction = 0; // Performer 当前运动方向（ 0-360 度，逆时针， 0 = 朝右）
	protected float speed = 0;// Performer 当前速度（像素每步）
	protected static float friction = 0;// 当前阻力（像素每步）
	protected static float gravity = 0;// 当前重力（像素每步）
	protected static float gravity_direction = 270; // 重力方向（ 270 朝下）

	protected int depth = 0;
	protected boolean frozen = false;
	private FGSprite sprite = null;
	protected boolean employed = false;
	protected boolean performing = false;
	protected int stage = -1;
	protected FGGraphicCollision collisionMask = null;
	protected List<FGPerformer> requiredCollisionDetection = new ArrayList<FGPerformer>();

	protected List<Class<?>> requiredClassCollisionDetection = new ArrayList<Class<?>>();
	protected FGScreenPlay myScreenPlay = null;
	private static FGGraphicCollision gc_tmp = new FGGraphicCollision();

	public String description = "";// 不产生实际作用，仅在调试、编辑时做参考用

	public FGPerformer() {
		// 初始化 alarm
		for (int i = 0; i < ALARM_COUNT_MAX; i++) {
			alarms[i] = new Alarm();
		}
	}

	// 没有重载前负责绘制默认精灵，重载后如果不手动调用绘图则会使该performer不绘制默认精灵
	@Override
	protected void onDraw() {
		if (sprite != null) {
			sprite.draw((int) x, (int) y);
		}
	}

	/**
	 * @Title: callEvent
	 * @Description: 用来触发该 performer 的事件
	 * @param: @param event FGEventsListener中包含的事件类别
	 * @param: @param args 具体的参数
	 * @return: void
	 */
	public final void callEvent(int event, Object... args) {
		if (frozen || !employed || !performing)
			return;
		switch (event) {
		case FGEventsListener.EVENT_ONCREATE:
			this.onCreate();
			break;
		case FGEventsListener.EVENT_ONDESTORY:
			this.onDestory();
			break;
		case FGEventsListener.EVENT_ONTOUCH:
			this.onTouch((Integer) args[0], (Integer) args[1],
					(Integer) args[2]);
			break;
		case FGEventsListener.EVENT_ONTOUCHPRESS:
			this.onTouchPress((Integer) args[0], (Integer) args[1],
					(Integer) args[2]);
			break;
		case FGEventsListener.EVENT_ONTOUCHRELEASE:
			this.onTouchRelease((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONKEY:
			this.onKey((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONKEYPRESS:
			this.onKeyPress((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONKEYRELEASE:
			this.onKeyRelease((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONALARM:
			this.onAlarm((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONGAMESTART:
			this.onGameStart();
			break;
		case FGEventsListener.EVENT_ONGAMEPAUSE:
			this.onGamePause();
			break;
		case FGEventsListener.EVENT_ONGAMERESUME:
			this.onGameResume();
			break;
		case FGEventsListener.EVENT_ONGAMEEND:
			this.onGameEnd();
			break;
		case FGEventsListener.EVENT_ONSTAGECHANGE:
			this.onStageChange();
			break;
		case FGEventsListener.EVENT_ONSTAGESTART:
			this.onStageStart();
			break;
		case FGEventsListener.EVENT_ONSTAGEEND:
			this.onStageEnd();
			break;
		case FGEventsListener.EVENT_ONDRAW:
			if (visible) {
				onDraw();
			}
			if (FGDebug.debugMode && collisionMask != null) {// 便于调试
				collisionMask.draw();
			}
			break;
		case FGEventsListener.EVENT_ONSTEP:
			this.onStep();
			break;
		case FGEventsListener.EVENT_ONSTEPSTART:
			this.onStepStart();
			break;
		case FGEventsListener.EVENT_ONSTEPEND:
			this.onStepEnd();
			break;
		case FGEventsListener.EVENT_ONCOLLISIONWITH:
			this.onCollisionWith((FGPerformer) args[0]);
			break;
		case FGEventsListener.EVENT_ONUSERDEFINEDEVENT:
			this.onUserDefinedEvent((Integer) args[0]);
			break;
		case FGEventsListener.EVENT_ONSCREENSIZECHANGED:
			this.onScreenSizeChanged((Integer) args[0], (Integer) args[1]);
			break;
		case FGEventsListener.EVENT_ONOUTOFSTAGE:
			this.onOutOfStage();
			break;
		}
	}

	/**
	 * @Title: isPerforming
	 * @Description: 用来获取该 performer 有没有在活动
	 * @param: @return
	 * @return: boolean
	 */
	public final boolean isPerforming() {
		return performing;
	}

	/**
	 * @Title: isOutOfStage
	 * @Description: 判断是否离开 Stage 的函数，默认为坐标/Sprite 的最外围矩形超出 Stage
	 *               范围，建议重载该函数以精确考虑 Sprite 的情况
	 * @param: @return
	 * @return: boolean
	 */
	public boolean isOutOfStage() {
		if (!employed) {
			FGDebug.error("Performer is not on any stage!");
		}
		FGStage s = FGStage.index2Stage(stage);
		if (s != FGStage.currentStage) {
			FGDebug.warning("Performer is not performing now!");
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

	/**
	 * @Title: perform
	 * @Description: 将这个 performer 添加在指定的 stage 上
	 * @param: @param stage
	 * @return: void
	 */
	public final void perform(int stage) {
		if (employed) {
			FGDebug.warning("Performer already been employed!");
			return;
		}

		FGStage.index2Stage(stage).employPerformer(this);
	}

	/**
	 * @Title: dismiss
	 * @Description: 从舞台上移除这个 performer
	 * @param:
	 * @return: void
	 */
	public final void dismiss() {
		if (!employed) {
			FGDebug.warning("Can't dismiss an unemployed performer!");
			return;
		}

		FGStage.index2Stage(stage).dismissPerformer(this);
	}

	/**
	 * @Title: freezeMe
	 * @Description: 冻结这个 performer 使其不响应任何 event
	 * @param:
	 * @return: void
	 */
	public final void freezeMe() {
		frozen = true;
	}

	/**
	 * @Title: unfreezeMe
	 * @Description: 解冻这个 performer
	 * @param:
	 * @return: void
	 */
	public final void unfreezeMe() {
		frozen = false;
	}

	public final void freezeAll(boolean freeze, boolean notMe) {
		FGPerformer ps[] = FGStage.getPerformers();
		if (freeze) {
			for (FGPerformer p : ps) {
				if (p != this || !notMe) {
					p.freezeMe();
				}
			}
		} else {
			for (FGPerformer p : ps) {
				p.unfreezeMe();
			}
		}
	}

	/**
	 * @Title: bindSprite
	 * @Description: 为这个 performer 绑定一个 sprite，系统会自动更新及绘制
	 * @param: @param sprite
	 * @return: void
	 */
	public final void bindSprite(FGSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * @Title: getSprite
	 * @Description: 获取这个 performer 绑定的 sprite
	 * @param: @return
	 * @return: FGSprite
	 */
	public final FGSprite getSprite() {
		return sprite;
	}

	// 系统函数--更新碰撞遮罩位置
	private void updateCollisionMask() {
		if (collisionMask != null) {
			collisionMask.setPosition((int) x, (int) y);
		}
	}

	/**
	 * @Title: bindCollisionMask
	 * @Description: 绑定一个碰撞遮罩，使其能够触发碰撞事件
	 * @param: @param collisionMask
	 * @return: void
	 */
	public final void bindCollisionMask(FGGraphicCollision collisionMask) {
		this.collisionMask = collisionMask;
		updateCollisionMask();
	}

	/**
	 * @Title: getCollisionMask
	 * @Description: 获取绑定的碰撞遮罩
	 * @param: @return
	 * @return: FGGraphicCollision
	 */
	public final FGGraphicCollision getCollisionMask() {
		return collisionMask;
	}

	/**
	 * @Title: requireCollisionDetection
	 * @Description: 申请碰撞检测
	 * @param: @param target 需要检测是否相撞的 performer
	 * @return: void
	 */
	public final void requireCollisionDetection(FGPerformer target) {
		if (requiredCollisionDetection.contains(target)) {
			return;
		}

		requiredCollisionDetection.add(target);
	}

	/**
	 * @Title: requireCollisionDetection
	 * @Description: 申请碰撞检测并且检测所有属于 targer 类的 Performer
	 * @param: @param target 需要检测是否相撞的 class
	 * @return: void
	 */
	public final void requireCollisionDetection(Class<?> target) {
		if (requiredClassCollisionDetection.contains(target)) {
			return;
		}

		requiredClassCollisionDetection.add(target);
	}

	/**
	 * @Title: cancelCollisionDetection
	 * @Description: 取消与指定 performer 的碰撞检测
	 * @param: @param target
	 * @return: void
	 */
	public final void cancelCollisionDetection(FGPerformer target) {
		requiredCollisionDetection.remove(target);
	}

	/**
	 * @Title: cancelClassCollisionDetection
	 * @Description: 取消与属于指定 class 的 performer 的碰撞检测
	 * @param: @param target
	 * @return: void
	 */
	public final void cancelClassCollisionDetection(Class<?> target) {
		requiredClassCollisionDetection.remove(target);
	}

	/**
	 * @Title: clearCollisionDetection
	 * @Description: 取消所有的碰撞检测请求
	 * @param:
	 * @return: void
	 */
	public final void clearCollisionDetection() {
		requiredCollisionDetection.clear();
		requiredClassCollisionDetection.clear();
	}

	/**
	 * @Title: setDepth
	 * @Description: 设置深度，这会影响绘制以及处理的顺序
	 * @param: @param depth 深度，越大则越深（越在下方）
	 * @return: void
	 */
	public final void setDepth(int depth) {
		this.depth = depth;
		if (employed) {
			FGStage.index2Stage(this.stage).sortWithDepth();
		}
	}

	/**
	 * @Title: getDepth
	 * @Description: 获取该 performer 的深度
	 * @param: @return
	 * @return: int
	 */
	public final int getDepth() {
		return depth;
	}

	/**
	 * @Title: setVisible
	 * @Description: 设置该 performer 是否可见，不可见的 performer 的 onDraw
	 *               事件不会被触发，系统也不会自动绘制绑定的 sprite
	 * @param: @param visible
	 * @return: void
	 */
	public final void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @Title: isVisible
	 * @Description: 获取该 performer 是否可见
	 * @param: @return
	 * @return: boolean
	 */
	public final boolean isVisible() {
		return visible;
	}

	/**
	 * @Title: setPosition
	 * @Description: 设置该 performer 当前的位置
	 * @param: @param x
	 * @param: @param y
	 * @return: void
	 */
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

	// 设定阻力（像素每步）
	public static final void motion_setFriction(float friction) {
		if (friction < 0) {
			throw new IllegalArgumentException(
					"Friction can't be smaller than zero!");
		}
		FGPerformer.friction = friction;
	}

	// 以参数（方向，重力大小（像素每步））设定重力
	public static final void motion_setGravity(float direction, float gravity) {
		if (gravity < 0) {
			throw new IllegalArgumentException(
					"Gravity can't be smaller than zero!");
		}
		FGPerformer.gravity = gravity;
		FGPerformer.gravity_direction = direction;
	}

	// 以参数（横向速度，纵向速度）设定运动
	public final void motion_setSpeed(float hspeed, float vspeed) {
		this.hspeed = hspeed;
		this.vspeed = vspeed;
		this.speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		direction = FGMathsHelper.degreeIn360((float) Math.toDegrees(Math
				.atan2(-vspeed, hspeed)));
	}

	// 以参数（横向速度，纵向速度）对当前运动做改变
	public final void motion_addSpeed(float hspeed, float vspeed) {
		this.hspeed += hspeed;
		this.vspeed += vspeed;
		this.speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		direction = FGMathsHelper.degreeIn360((float) Math.toDegrees(Math
				.atan2(-vspeed, hspeed)));
	}

	// 以参数（方向，速度）设定运动
	public final void motion_set(float dir, float speed) {
		hspeed = FGMathsHelper.lengthdir_x(speed, dir);
		vspeed = -FGMathsHelper.lengthdir_y(speed, dir);
		this.speed = speed;
		direction = dir;
	}

	// 以参数（方向，速度）对当前运动做改变
	public final void motion_add(float dir, float speed) {
		hspeed += FGMathsHelper.lengthdir_x(speed, dir);
		vspeed -= FGMathsHelper.lengthdir_y(speed, dir);
		this.speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		direction = FGMathsHelper.degreeIn360((float) Math.toDegrees(Math
				.atan2(-vspeed, hspeed)));
	}

	protected void updateMovement() {
		// 先计算重力影响
		hspeed += FGMathsHelper.lengthdir_x(gravity, gravity_direction);
		vspeed -= FGMathsHelper.lengthdir_y(gravity, gravity_direction);

		direction = FGMathsHelper.degreeIn360((float) Math.toDegrees(Math
				.atan2(-vspeed, hspeed)));
		speed = (float) Math.sqrt(hspeed * hspeed + vspeed * vspeed);
		// 再计算阻力影响
		if (speed > friction) {
			speed -= friction;
		} else {
			speed = 0;
		}
		hspeed = FGMathsHelper.lengthdir_x(speed, direction);
		vspeed = -FGMathsHelper.lengthdir_y(speed, direction);

		if (hspeed != 0 || vspeed != 0) {
			this.setPosition(x + hspeed, y + vspeed);
		}
	}

	/**
	 * @Title: playAScreenPlay
	 * @Description: 让该 performer 执行一个剧本。一个 performer 在同一时间只能执行一个剧本，执行期间对该
	 *               performer 进行的任何移动操作都有可能会导致不正确的结果。
	 * @param: @param screenPlay
	 * @return: void
	 */
	public void playAScreenPlay(FGScreenPlay screenPlay) {
		myScreenPlay = screenPlay;
		if (myScreenPlay != null) {
			myScreenPlay.prepareToPlay(this);
		}
	}

	/**
	 * @Title: setAlarm
	 * @Description: 设置 alarm 的预设参数，该改动仅在启动或重复该 alarm 时生效
	 * @param: @param alarm 预置的 alarm 编号,0~9共10个可用的 alarm
	 * @param: @param step 该 alarm 的倒计时 step 数
	 * @param: @param repeat 是否重复
	 * @return: void
	 */
	public final void setAlarm(int alarm, int step, boolean repeat) {
		Alarm a = alarms[alarm];
		a.setSteps = step;
		a.repeat = repeat;
	}

	/**
	 * @Title: startAlarm
	 * @Description: 启动指定的 alarm ，开始到计时
	 * @param: @param alarm
	 * @return: void
	 */
	public final void startAlarm(int alarm) {
		Alarm a = alarms[alarm];
		a.remainSteps = a.setSteps;
		a.start = true;
	}

	/**
	 * @Title: stopAlarm
	 * @Description: 停止指定 alarm，注意这会重置指定 alarm 但是不会改变其预设参数
	 * @param: @param alarm
	 * @return: void
	 */
	public final void stopAlarm(int alarm) {
		Alarm a = alarms[alarm];
		a.start = false;
	}

	/**
	 * @Title: getAlarmRemainStep
	 * @Description: 获取指定 alarm 的剩余 step 数
	 * @param: @param alarm
	 * @param: @return
	 * @return: int
	 */
	public final int getAlarmRemainStep(int alarm) {
		Alarm a = alarms[alarm];
		return a.remainSteps;
	}

	/**
	 * @Title: isAlarmStart
	 * @Description: 返回指定的 alarm 是否已经启动
	 * @param: @param alarm
	 * @param: @return
	 * @return: boolean
	 */
	public final boolean isAlarmStart(int alarm) {
		Alarm a = alarms[alarm];
		return a.start;
	}

	/**
	 * @Title: goAlarm
	 * @Description: 系统函数，用来操作所有的 alarm
	 * @param:
	 * @return: void
	 */
	protected final void goAlarm() {
		for (int i = 0; i < ALARM_COUNT_MAX; i++) {
			Alarm a = alarms[i];
			if (a.start) {
				if (a.remainSteps > 0) {
					a.remainSteps -= 1;
					if (a.remainSteps == 0) {
						callEvent(FGEventsListener.EVENT_ONALARM, i);
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

	/**
	 * @Title: collision_point
	 * @Description: 这个函数测试在（x, y）位置是否和 Performer target 有碰撞。<br>
	 *               如果有碰撞，返回碰撞的 Performer， 否则返回 null
	 * @param: @param x
	 * @param: @param y
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_point(int x, int y, FGPerformer target,
			boolean notme) {
		if (target.frozen || target.collisionMask == null
				|| (target == this && notme)) {
			return null;
		}

		gc_tmp.clear();
		gc_tmp.addPoint(x, y);

		return gc_tmp.isCollisionWith(target.collisionMask) ? target : null;
	}

	/**
	 * @Title: collision_point
	 * @Description: 这个函数测试在（x, y）位置是否和属于 target类 的 performer 有碰撞。<br>
	 *               如果有碰撞，返回第一个检测到的产生碰撞的 Performer， 否则返回 null
	 * @param: @param x
	 * @param: @param y
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_point(int x, int y, Class<?> target,
			boolean notme) {
		for (FGPerformer p : FGStage.getPerformersByClass(target)) {
			if (collision_point(x, y, p, notme) != null) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @Title: collision_rectangle
	 * @Description: 这个函数测试指定对角的矩形（已填充）是否和 Performer target 有碰撞。<br>
	 *               举例来说，你可以使用这个函数测试某个区域里是否没有障碍物。<br>
	 *               如果有碰撞，返回碰撞的 Performer， 否则返回 null
	 * @param: @param left
	 * @param: @param top
	 * @param: @param width
	 * @param: @param height
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_rectangle(int left, int top, int width,
			int height, FGPerformer target, boolean notme) {
		if (target.frozen || target.collisionMask == null
				|| (target == this && notme)) {
			return null;
		}

		gc_tmp.clear();
		gc_tmp.addRectangle(left, top, width, height);

		return gc_tmp.isCollisionWith(target.collisionMask) ? target : null;
	}

	/**
	 * @Title: collision_rectangle
	 * @Description: 这个函数测试指定对角的矩形（已填充）是否和属于 target类 的 performer 有碰撞。<br>
	 *               举例来说，你可以使用这个函数测试某个区域里是否没有障碍物。<br>
	 *               如果有碰撞，返回第一个检测到的产生碰撞的 Performer， 否则返回 null
	 * @param: @param left
	 * @param: @param top
	 * @param: @param width
	 * @param: @param height
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_rectangle(int left, int top, int width,
			int height, Class<?> target, boolean notme) {
		for (FGPerformer p : FGStage.getPerformersByClass(target)) {
			if (collision_rectangle(left, top, width, height, p, notme) != null) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @Title: collision_circle
	 * @Description: 这个函数测试指定圆心（xc,yc）的圆（已填充）是否和 Performer target 有碰撞。<br>
	 *               举例来说，你可以使用这个函数测试某 Performer 是否靠近某特定位置。<br>
	 *               如果有碰撞，返回碰撞的 Performer， 否则返回 null
	 * @param: @param xc
	 * @param: @param yc
	 * @param: @param radius
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_circle(int xc, int yc, int radius,
			FGPerformer target, boolean notme) {
		if (target.frozen || target.collisionMask == null
				|| (target == this && notme)) {
			return null;
		}

		gc_tmp.clear();
		gc_tmp.addCircle(xc, yc, radius);

		return gc_tmp.isCollisionWith(target.collisionMask) ? target : null;
	}

	/**
	 * @Title: collision_circle
	 * @Description: 这个函数测试指定圆心（xc,yc）的圆（已填充）是否和属于 target类 的 performer 有碰撞。<br>
	 *               举例来说，你可以使用这个函数测试某 Performer 是否靠近某特定位置。<br>
	 *               如果有碰撞，返回第一个检测到的产生碰撞的 Performer， 否则返回 null
	 * @param: @param xc
	 * @param: @param yc
	 * @param: @param radius
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_circle(int xc, int yc, int radius,
			Class<?> target, boolean notme) {
		for (FGPerformer p : FGStage.getPerformersByClass(target)) {
			if (collision_circle(xc, yc, radius, p, notme) != null) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @Title: collision_line
	 * @Description: 这个函数测试线段（x1,y1）到 (x2,y2) 是否和 Performer target 有碰撞。<br>
	 *               这是个强大的函数。你可以这样使用这个函数，通过检测线段是否与他们之间的墙相交来测试某 Performer
	 *               是否可以看到另一 Performer 。<br>
	 *               如果有碰撞，返回碰撞的 Performer， 否则返回 null
	 * @param: @param x1
	 * @param: @param y1
	 * @param: @param x2
	 * @param: @param y2
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_line(int x1, int y1, int x2, int y2,
			FGPerformer target, boolean notme) {
		if (target.frozen || target.collisionMask == null
				|| (target == this && notme)) {
			return null;
		}

		gc_tmp.clear();
		gc_tmp.addLine(x1, y1, x2, y2);

		return gc_tmp.isCollisionWith(target.collisionMask) ? target : null;
	}

	/**
	 * @Title: collision_line
	 * @Description: 这个函数测试线段（x1,y1）到 (x2,y2) 是否和属于 target类 的 performer 有碰撞。<br>
	 *               这是个强大的函数。你可以这样使用这个函数，通过检测线段是否与他们之间的墙相交来测试某 Performer
	 *               是否可以看到另一 Performer 。<br>
	 *               如果有碰撞，返回第一个检测到的产生碰撞的 Performer， 否则返回 null
	 * @param: @param x1
	 * @param: @param y1
	 * @param: @param x2
	 * @param: @param y2
	 * @param: @param target
	 * @param: @param notme 是否检测自己
	 * @param: @return
	 * @return: FGPerformer
	 */
	public final FGPerformer collision_line(int x1, int y1, int x2, int y2,
			Class<?> target, boolean notme) {
		for (FGPerformer p : FGStage.getPerformersByClass(target)) {
			if (collision_line(x1, y1, x2, y2, p, notme) != null) {
				return p;
			}
		}
		return null;
	}

}
