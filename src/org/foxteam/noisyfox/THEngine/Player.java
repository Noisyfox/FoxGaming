/**
 * FileName:     Player.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午11:11:59
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Performer {

	private Point fingerStart = null;
	private Point fingerStartPerformer = null;
	private Point targetPoint = null;
	private Point currentPoint = null;

	private float moveSpeed = 0;

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onStep(Performer performer) {
			currentPoint.setPosition((int) performer.getX(),
					(int) performer.getY());

			Debug.print(currentPoint.squareDistance(targetPoint) + ":"
					+ moveSpeed * moveSpeed);
			if ((float) currentPoint.squareDistance(targetPoint) <= moveSpeed
					* moveSpeed) {
				performer.setPosition(targetPoint.getX(), targetPoint.getY());
			} else {
				float k = (float) Math.sqrt((float) (moveSpeed * moveSpeed)
						/ (float) currentPoint.squareDistance(targetPoint));
				performer.setPosition(
						(float) currentPoint.getX()
								+ k
								* (float) (targetPoint.getX() - currentPoint
										.getX()),
						(float) currentPoint.getY()
								+ k
								* (float) (targetPoint.getY() - currentPoint
										.getY()));
			}
		}

		@Override
		public void onCreate(Performer performer) {
			Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
					.getResources(),
					org.foxteam.noisyfox.THEngine.R.drawable.player);

			Sprite playerSprite = new Sprite();
			playerSprite.loadFromBitmap(b);
			playerSprite.setOffset(playerSprite.getWidth() / 2,
					playerSprite.getHeight() / 2);

			performer.bindSprite(playerSprite);
			performer.setPosition(GamingThread.getScreenWidth() / 2,
					GamingThread.getScreenHeight() - playerSprite.getHeight()
							/ 2);

			moveSpeed = 500f / Stage.getSpeed();

			fingerStart = new Point(0, 0);
			fingerStartPerformer = new Point(0, 0);
			targetPoint = new Point((int) performer.getX(),
					(int) performer.getY());
			currentPoint = new Point((int) performer.getX(),
					(int) performer.getY());

		}

		@Override
		public void onDestory(Performer performer) {
			// TODO Auto-generated method stub
			super.onDestory(performer);
		}

		@Override
		public void onTouch(Performer performer, int whichfinger, int x, int y) {
			if (whichfinger == 0) {
				int xt = fingerStartPerformer.getX() + x - fingerStart.getX();
				int yt = fingerStartPerformer.getY() + y - fingerStart.getY();

				if (xt - performer.getSprite().getOffsetX() < 0) {
					xt = performer.getSprite().getOffsetX();
				} else if (xt + performer.getSprite().getWidth()
						- performer.getSprite().getOffsetX() > GamingThread
						.getScreenWidth()) {
					xt = GamingThread.getScreenWidth()
							- (performer.getSprite().getWidth() - performer
									.getSprite().getOffsetX());
				}

				if (yt - performer.getSprite().getOffsetY() < 0) {
					yt = performer.getSprite().getOffsetY();
				} else if (yt + performer.getSprite().getHeight()
						- performer.getSprite().getOffsetY() > GamingThread
						.getScreenHeight()) {
					yt = GamingThread.getScreenHeight()
							- (performer.getSprite().getHeight() - performer
									.getSprite().getOffsetY());
				}

				targetPoint.setPosition(xt, yt);
			}
		}

		@Override
		public void onTouchPress(Performer performer, int whichfinger, int x,
				int y) {
			if (whichfinger == 0) {
				fingerStart.setPosition(x, y);
				fingerStartPerformer.setPosition(targetPoint.getX(),
						targetPoint.getY());
			}
		}

	};

	public Player() {
		this.setEventsListener(eventsListener);

	}

	@Override
	protected void onDraw(Performer performer) {
		super.onDraw(performer);

		Paint p = new Paint();
		p.setColor(Color.BLACK);
		Canvas c = performer.getCanvas();
		c.drawCircle(targetPoint.getX(), targetPoint.getY(), 3, p);

	}

}
