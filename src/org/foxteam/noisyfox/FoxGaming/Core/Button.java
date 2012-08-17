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

import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.FoxGaming.G2D.SpriteConvertor;

/**
 * @ClassName: Button
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午10:39:16
 * 
 */
public abstract class Button extends Performer {

	private GraphicCollision myMask = new GraphicCollision();
	private SpriteConvertor mySC = new SpriteConvertor();
	private Sprite mySprite = new Sprite();
	private boolean enable = true;
	private int touchDown = -1;

	public Button(int width, int height, int resId) {
		mySprite.loadFromBitmap(resId, 3, 1, false);
		mySprite.setOffset(mySprite.getWidth() / 2, mySprite.getHeight() / 2);
		mySC.setScale((double) width / (double) mySprite.getWidth(),
				(double) height / (double) mySprite.getHeight());
		myMask.addRectangle(-width / 2, -height / 2, width, height);
		this.bindCollisionMask(myMask);
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
		mySprite.draw(getCanvas(), (int) this.getX(), (int) this.getY(), mySC);
	}

	public abstract void onClick();

}
