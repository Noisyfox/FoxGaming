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

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onStep(Performer performer) {
			float per = meOnScreen.getX() / mainView.getWidthFromScreen();
			mainView.setPositionFromStage(
					(Stage.getCurrentStage().getWidth() - GamingThread
							.getScreenWidth()) * per, 0);

			performer.setPosition(mainView.coordinateScreen2Stage_X(
					meOnScreen.getX(), meOnScreen.getY()), mainView
					.coordinateScreen2Stage_Y(meOnScreen.getX(),
							meOnScreen.getY()));
		}

		@Override
		public void onCreate(Performer performer) {
			mainView = Stage.getCurrentStage().getView(0);

			//Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
			//		.getResources(),
			//		org.foxteam.noisyfox.THEngine.R.drawable.player);

			Sprite playerSprite = new Sprite();
			playerSprite.loadFromBitmap(
					org.foxteam.noisyfox.THEngine.R.drawable.player, false);
			playerSprite.setOffset(playerSprite.getWidth() / 2,
					playerSprite.getHeight() / 2);

			performer.bindSprite(playerSprite);

			meOnScreen = new Point((int) mainView.getWidthFromScreen() / 2,
					(int) mainView.getHeightFromScreen()
							- playerSprite.getHeight()
							+ playerSprite.getOffsetY());

			performer.setPosition(mainView.coordinateScreen2Stage_X(
					meOnScreen.getX(), meOnScreen.getY()), mainView
					.coordinateScreen2Stage_Y(meOnScreen.getX(),
							meOnScreen.getY()));

			fingerPressStart = new Point();
			meStart = new Point();

			performer.setAlarm(0, (int) (Stage.getSpeed() * 0.5f), true);
			performer.startAlarm(0);

		}

		@Override
		public void onDestory(Performer performer) {
			// TODO Auto-generated method stub
			super.onDestory(performer);
		}

		@Override
		public void onTouch(Performer performer, int whichfinger, int x, int y) {
			if (whichfinger == 0) {
				int dx = x - fingerPressStart.getX();
				int dy = y - fingerPressStart.getY();
				int myX = meStart.getX() + dx;
				int myY = meStart.getY() + dy;
				int realX = mainView.coordinateScreen2Stage_X(myX, myY);
				int realY = mainView.coordinateScreen2Stage_Y(myX, myY);

				if (realX < performer.getSprite().getOffsetX()) {

					if (realX < performer.getSprite().getOffsetX() - 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getY());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getX(),
								fingerPressStart.getY());

						fingerPressStart.setPosition(
								mainView.coordinateStage2Screen_X(fingerXReal
										- performer.getSprite().getOffsetX()
										+ realX, fingerYReal),
								fingerPressStart.getY());
					}

					realX = performer.getSprite().getOffsetX();

				} else if (realX > Stage.getCurrentStage().getWidth()
						- performer.getSprite().getWidth()
						+ performer.getSprite().getOffsetX()) {

					if (realX > Stage.getCurrentStage().getWidth()
							- performer.getSprite().getWidth()
							+ performer.getSprite().getOffsetX() + 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getY());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getX(),
								fingerPressStart.getY());

						fingerPressStart.setPosition(mainView
								.coordinateStage2Screen_X(fingerXReal
										+ realX
										- (Stage.getCurrentStage().getWidth()
												- performer.getSprite()
														.getWidth() + performer
												.getSprite().getOffsetX()),
										fingerYReal), fingerPressStart.getY());
					}

					realX = Stage.getCurrentStage().getWidth()
							- performer.getSprite().getWidth()
							+ performer.getSprite().getOffsetX();

				}

				if (realY < performer.getSprite().getOffsetY()) {

					if (realY < performer.getSprite().getOffsetY() - 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getX());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getY(),
								fingerPressStart.getY());

						fingerPressStart.setPosition(fingerPressStart.getX(),
								mainView.coordinateStage2Screen_Y(fingerXReal,
										fingerYReal
												- performer.getSprite()
														.getOffsetY() + realY));
					}

					realY = performer.getSprite().getOffsetY();

				} else if (realY > Stage.getCurrentStage().getHeight()
						- performer.getSprite().getHeight()
						+ performer.getSprite().getOffsetY()) {

					if (realY > Stage.getCurrentStage().getHeight()
							- performer.getSprite().getHeight()
							+ performer.getSprite().getOffsetY() + 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getX());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getY(),
								fingerPressStart.getY());

						fingerPressStart
								.setPosition(
										fingerPressStart.getX(),
										mainView.coordinateStage2Screen_Y(
												fingerXReal,
												fingerYReal
														+ realY
														- (Stage.getCurrentStage()
																.getHeight()
																- performer
																		.getSprite()
																		.getHeight() + performer
																.getSprite()
																.getOffsetY())));
					}

					realY = Stage.getCurrentStage().getHeight()
							- performer.getSprite().getHeight()
							+ performer.getSprite().getOffsetY();

				}

				myX = mainView.coordinateStage2Screen_X(realX, realY);
				myY = mainView.coordinateStage2Screen_Y(realX, realY);

				meOnScreen.setPosition(myX, myY);
			}
		}

		@Override
		public void onTouchPress(Performer performer, int whichfinger, int x,
				int y) {
			if (whichfinger == 0) {
				fingerPressStart.setPosition(x, y);
				meStart.setPosition(meOnScreen.getX(), meOnScreen.getY());
			}
		}

		@Override
		public void onTouchRelease(Performer performer, int whichfinger) {
			if (whichfinger == 0) {

			}
		}

		@Override
		public void onAlarm(Performer performer, int whichAlarm) {
			if (whichAlarm == 0) {
				PlayerBullet b = new PlayerBullet((int) performer.getX(),
						(int) performer.getY()
								- performer.getSprite().getOffsetY());
				b.setDepth(1);
			}
		}
	};

	public Player() {
		this.setEventsListener(eventsListener);
	}

	@Override
	protected void onDraw(Performer performer) {
		super.onDraw(performer);
	}

}
