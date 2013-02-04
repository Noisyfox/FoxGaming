/**
 * FileName:     Button.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午10:39:16
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;

/**
 * @ClassName: Button
 * @Description: 利用Performer做的按钮实用类
 * @author: Noisyfox
 * @date: 2012-7-18 下午10:39:16
 * 
 */
public abstract class FGButton extends FGPerformer {

	private FGGraphicCollision myMask = new FGGraphicCollision();// 碰撞遮罩
	private FGSpriteConvertor mySC = new FGSpriteConvertor();// 精灵变换器
	private FGSprite mySprite = new FGSprite();
	private boolean enable = true;
	private int touchDown = -1;
	private int height = 0;
	private int width = 0;
	private int touchSize_height = 0;
	private int touchSize_width = 0;

	/**
	 * @Title: Button
	 * @Description: 构造函数
	 * @param: @param width
	 * @param: @param height
	 * @param: @param frames 要绑定的 frames,必须是3帧
	 */
	public FGButton(int width, int height, FGFrame frames) {
		if (frames == null || frames.getFrameCount() != 3) {
			throw new IllegalArgumentException();
		}

		mySprite.bindFrames(frames);
		mySprite.setOffset(mySprite.getWidth() / 2, mySprite.getHeight() / 2);
		this.bindCollisionMask(myMask);
		setSize(width, height);
		setTouchSize(width, height);

		requireEventFeature(FGEventsListener.EVENT_ONDRAW
				| FGEventsListener.EVENT_ONTOUCH
				| FGEventsListener.EVENT_ONTOUCHPRESS
				| FGEventsListener.EVENT_ONTOUCHRELEASE);
	}

	/**
	 * @Title: setSize
	 * @Description: 设置尺寸
	 * @param: @param width
	 * @param: @param height
	 * @return: void
	 */
	public final void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		mySC.setScale((double) width / (double) mySprite.getWidth(),
				(double) height / (double) mySprite.getHeight());
	}

	public final void setTouchSize(int width, int height) {
		touchSize_width = Math.abs(width);
		touchSize_height = Math.abs(height);
		myMask.clear();
		myMask.addRectangle(-touchSize_width / 2, -touchSize_height / 2,
				touchSize_width, touchSize_height);
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getTouchSizeWidth() {
		return touchSize_width;
	}

	public final int getTouchSizeHeight() {
		return touchSize_height;
	}

	public final void setEnabled(boolean enable) {
		this.enable = enable;
		if (enable) {
			mySprite.setCurrentFrame(0);
		} else {
			mySprite.setCurrentFrame(2);
		}
		touchDown = -1;
	}

	@Override
	protected final void onTouch(int whichfinger, int x, int y) {
		if (!enable) {
			touchDown = -1;
			return;
		}

		if (touchDown == whichfinger) {
			if (this.collision_point(x, y, this, false) == null) {
				mySprite.setCurrentFrame(0);
				touchDown = -1;
			}
		}

	}

	@Override
	protected final void onTouchPress(int whichfinger, int x, int y) {
		if (!enable) {
			touchDown = -1;
			return;
		}
		if (touchDown == -1 && this.collision_point(x, y, this, false) == this) {
			touchDown = whichfinger;
			mySprite.setCurrentFrame(1);
			// MyDebug.print("d");
		}
	}

	@Override
	protected final void onTouchRelease(int whichfinger) {
		if (!enable) {
			touchDown = -1;
			return;
		}

		if (touchDown == whichfinger) {
			mySprite.setCurrentFrame(0);
			touchDown = -1;
			onClick();
		}
	}

	@Override
	protected final void onDraw() {
		mySprite.draw((int) this.getX(), (int) this.getY(), mySC);
	}

	/**
	 * @Title: onClick
	 * @Description: 触发的动作
	 * @param:
	 * @return: void
	 */
	public abstract void onClick();

}
