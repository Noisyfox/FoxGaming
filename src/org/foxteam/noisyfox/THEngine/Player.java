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

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Performer {

	Views mainView = null;

	Point meOnScreen = null;
	Point fingerPressStart = null;
	Point meStart = null;

	@Override
	protected void onStep() {
		float per = meOnScreen.getX() / mainView.getWidthFromScreen();

		mainView.setPositionFromStage(
				(Stage.getCurrentStage().getWidth() - mainView
						.getWidthFromStage()) * per, 0);

		this.setPosition(
				mainView.coordinateScreen2Stage_X(meOnScreen.getX(),
						meOnScreen.getY()),
				mainView.coordinateScreen2Stage_Y(meOnScreen.getX(),
						meOnScreen.getY()));
	}

	@Override
	protected void onCreate() {
		mainView = Stage.getCurrentStage().getView(0);

		Sprite playerSprite = new Sprite();
		playerSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.player, false);
		playerSprite.setOffset(playerSprite.getWidth() / 2,
				playerSprite.getHeight() / 2);
		this.bindSprite(playerSprite);

		meOnScreen = new Point((int) mainView.getWidthFromScreen() / 2,
				(int) mainView.getHeightFromScreen() - playerSprite.getHeight()
						+ playerSprite.getOffsetY());

		this.setPosition(
				mainView.coordinateScreen2Stage_X(meOnScreen.getX(),
						meOnScreen.getY()),
				mainView.coordinateScreen2Stage_Y(meOnScreen.getX(),
						meOnScreen.getY()));

		fingerPressStart = new Point();
		meStart = new Point();

		this.setAlarm(0, (int) (Stage.getSpeed() * 0.5f), true);
		this.startAlarm(0);

		// 添加碰撞检测遮罩
		GraphicCollision co = new GraphicCollision();
		int[][] vertex1 = { { -21, -7 }, { -23, 4 }, { 23, 4 }, { 21, -7 } };
		co.addPolygon(vertex1, true);
		int[][] vertex2 = { { -11, 4 }, { -10, 19 }, { 0, 28 }, { 10, 19 },
				{ 11, 4 } };
		co.addPolygon(vertex2, true);
		int[][] vertex3 = { { 0, -28 }, { -17, -7 }, { 17, -7 } };
		co.addPolygon(vertex3, true);
		this.bindCollisionMask(co);

	}

	@Override
	protected void onDestory() {
		// TODO Auto-generated method stub
		super.onDestory();
	}

	@Override
	protected void onTouch(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			int dx = x - fingerPressStart.getX();
			int dy = y - fingerPressStart.getY();
			int myX = meStart.getX() + dx;
			int myY = meStart.getY() + dy;
			int realX = mainView.coordinateScreen2Stage_X(myX, myY);
			int realY = mainView.coordinateScreen2Stage_Y(myX, myY);

			if (realX < this.getSprite().getOffsetX()) {

				if (realX < this.getSprite().getOffsetX() - 5) {
					int fingerXReal = mainView.coordinateScreen2Stage_X(
							fingerPressStart.getX(), fingerPressStart.getY());
					int fingerYReal = mainView.coordinateScreen2Stage_Y(
							fingerPressStart.getX(), fingerPressStart.getY());

					fingerPressStart.setPosition(mainView
							.coordinateStage2Screen_X(fingerXReal
									- this.getSprite().getOffsetX() + realX,
									fingerYReal), fingerPressStart.getY());
				}

				realX = this.getSprite().getOffsetX();

			} else if (realX > Stage.getCurrentStage().getWidth()
					- this.getSprite().getWidth()
					+ this.getSprite().getOffsetX()) {

				if (realX > Stage.getCurrentStage().getWidth()
						- this.getSprite().getWidth()
						+ this.getSprite().getOffsetX() + 5) {
					int fingerXReal = mainView.coordinateScreen2Stage_X(
							fingerPressStart.getX(), fingerPressStart.getY());
					int fingerYReal = mainView.coordinateScreen2Stage_Y(
							fingerPressStart.getX(), fingerPressStart.getY());

					fingerPressStart.setPosition(mainView
							.coordinateStage2Screen_X(
									fingerXReal
											+ realX
											- (Stage.getCurrentStage()
													.getWidth()
													- this.getSprite()
															.getWidth() + this
													.getSprite().getOffsetX()),
									fingerYReal), fingerPressStart.getY());
				}

				realX = Stage.getCurrentStage().getWidth()
						- this.getSprite().getWidth()
						+ this.getSprite().getOffsetX();

			}

			if (realY < this.getSprite().getOffsetY()) {

				if (realY < this.getSprite().getOffsetY() - 5) {
					int fingerXReal = mainView.coordinateScreen2Stage_X(
							fingerPressStart.getX(), fingerPressStart.getX());
					int fingerYReal = mainView.coordinateScreen2Stage_Y(
							fingerPressStart.getY(), fingerPressStart.getY());

					fingerPressStart.setPosition(fingerPressStart.getX(),
							mainView.coordinateStage2Screen_Y(fingerXReal,
									fingerYReal - this.getSprite().getOffsetY()
											+ realY));
				}

				realY = this.getSprite().getOffsetY();

			} else if (realY > Stage.getCurrentStage().getHeight()
					- this.getSprite().getHeight()
					+ this.getSprite().getOffsetY()) {

				if (realY > Stage.getCurrentStage().getHeight()
						- this.getSprite().getHeight()
						+ this.getSprite().getOffsetY() + 5) {
					int fingerXReal = mainView.coordinateScreen2Stage_X(
							fingerPressStart.getX(), fingerPressStart.getX());
					int fingerYReal = mainView.coordinateScreen2Stage_Y(
							fingerPressStart.getY(), fingerPressStart.getY());

					fingerPressStart
							.setPosition(
									fingerPressStart.getX(),
									mainView.coordinateStage2Screen_Y(
											fingerXReal,
											fingerYReal
													+ realY
													- (Stage.getCurrentStage()
															.getHeight()
															- this.getSprite()
																	.getHeight() + this
															.getSprite()
															.getOffsetY())));
				}

				realY = Stage.getCurrentStage().getHeight()
						- this.getSprite().getHeight()
						+ this.getSprite().getOffsetY();

			}

			myX = mainView.coordinateStage2Screen_X(realX, realY);
			myY = mainView.coordinateStage2Screen_Y(realX, realY);

			meOnScreen.setPosition(myX, myY);
		}
	}

	@Override
	protected void onTouchPress(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			fingerPressStart.setPosition(x, y);
			meStart.setPosition(meOnScreen.getX(), meOnScreen.getY());
		}
	}

	@Override
	protected void onTouchRelease(int whichfinger) {
		if (whichfinger == 0) {

		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			Bullet_Player b = new Bullet_Player((int) this.getX(),
					(int) this.getY() - this.getSprite().getOffsetY());
			b.setDepth(this.getDepth() + 1);
		}
	}

	@Override
	protected void onDraw() {
		super.onDraw();
		this.getCollisionMask().draw(this.getCanvas());
	}

	public Player() {
	}

}
