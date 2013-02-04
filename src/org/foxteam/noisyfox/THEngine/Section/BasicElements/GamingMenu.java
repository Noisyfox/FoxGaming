package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGDraw;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.ButtonGroup;
import org.foxteam.noisyfox.THEngine.GlobalResources;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;

public class GamingMenu extends FGPerformer {

	private FGFrame ssf = new FGFrame();
	private FGSprite sss = new FGSprite();
	private FGSpriteConvertor sssc = new FGSpriteConvertor();
	private SectionStage cStage = null;
	private MenuState menuState = MenuState.hided;
	private MenuType menuType;
	private FGButton button_common_returnMainMenu = null;
	private FGButton button_pause_resumeGame = null;
	private FGButton button_common_restart = null;
	private FGButton button_stageClear_nextStage = null;
	private boolean aniOK = false;
	private boolean drawMe = false;
	private ButtonGroup buttonGroup_pause = new ButtonGroup();
	private ButtonGroup buttonGroup_gameOver = new ButtonGroup();
	private ButtonGroup buttonGroup_stageClear = new ButtonGroup();
	private ButtonGroup buttonGroup_current = null;
	private float k = 0;

	private enum MenuState {
		showing, shown, hiding, hided;
	}

	public enum MenuType {
		pause, gameover, stageclear;
	}

	public GamingMenu() {
		button_common_returnMainMenu = this.new Button_ReturnMainMenu();
		button_pause_resumeGame = this.new Button_ResumeGame();
		button_common_restart = this.new Button_Restart();
		button_stageClear_nextStage = this.new Button_NextStage();
		buttonGroup_pause.addButton(button_pause_resumeGame);
		buttonGroup_pause.addButton(button_common_restart);
		buttonGroup_pause.addButton(button_common_returnMainMenu);
		buttonGroup_pause.setDirection(false);

		buttonGroup_gameOver.addButton(button_common_restart);
		buttonGroup_gameOver.addButton(button_common_returnMainMenu);
		buttonGroup_gameOver.setDirection(false);

		buttonGroup_stageClear.addButton(button_stageClear_nextStage);
		buttonGroup_stageClear.addButton(button_common_restart);
		buttonGroup_stageClear.addButton(button_common_returnMainMenu);
		buttonGroup_stageClear.setDirection(false);
	}

	public void show(MenuType type) {
		if (menuState != MenuState.hided || menuState == MenuState.shown)
			return;

		switch (type) {
		case pause:
			buttonGroup_current = buttonGroup_pause;
			break;
		case gameover:
			buttonGroup_current = buttonGroup_gameOver;
			break;
		case stageclear:
			buttonGroup_current = buttonGroup_stageClear;
			break;

		}

		buttonGroup_current.setPlaceRegion(-100,
				FGGamingThread.getScreenHeight() / 2 - 120,
				FGGamingThread.getScreenWidth() + 100,
				FGGamingThread.getScreenHeight() / 2 + 120,
				FGGamingThread.getScreenWidth() / 2,
				FGGamingThread.getScreenHeight() / 2);

		menuType = type;
		cStage = (SectionStage) FGStage.getCurrentStage();
		Bitmap currentScreen = FGGamingThread.getScreenshots();
		ssf.loadFromBitmap(FGEGLHelper.getBindedGL(), currentScreen);
		sss.bindFrames(ssf);
		setDepth(-1001);
		menuState = MenuState.showing;
		sssc.setAlpha(1);
		aniOK = false;

		// 暂停所有
		cStage.pauseSection(this);

	}

	public void hide() {
		if (menuState != MenuState.shown)
			return;

		menuState = MenuState.hiding;
		aniOK = false;
		k = 1;
	}

	@Override
	protected void onDraw() {
		if (!drawMe)
			return;
		FGDraw.setAlpha(1);
		FGDraw.setColor(Color.BLACK);
		FGDraw.drawRectFill(FGEGLHelper.getBindedGL(), 0, 0, FGStage
				.getCurrentStage().getWidth(), FGStage.getCurrentStage()
				.getHeight());
		sss.draw(0, 0, sssc);
	}

	@Override
	protected void onCreate() {

		requireEventFeature(FGEventsListener.EVENT_ONDRAW
				| FGEventsListener.EVENT_ONSTEPSTART
				| FGEventsListener.EVENT_ONDESTORY
				| FGEventsListener.EVENT_ONKEYRELEASE
				| FGEventsListener.EVENT_ONTOUCHRELEASE);
	}

	@Override
	protected void onTouchRelease(int whichfinger) {
	}

	@Override
	protected void onKeyRelease(int keyCode) {
		if (menuState != MenuState.shown)
			return;

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (menuType == MenuType.pause) {
				hide();
			}
		}
	}

	@Override
	protected void onStepStart() {

		switch (menuState) {
		case showing: {
			// 添加按钮
			buttonGroup_current.performAll(FGStage.getCurrentStage()
					.getStageIndex(), depth - 1);

			buttonGroup_current.control(0);

			drawMe = true;
			menuState = MenuState.shown;

			break;
		}
		case shown: {
			if (!aniOK) {
				if (sssc.getAlpha() > 0.5) {
					sssc.setAlpha(sssc.getAlpha() - 0.1);// 屏幕变暗
					k = 0;
				} else if (k < 1) {
					k += 0.3;
					buttonGroup_current.control(k);
				} else {
					aniOK = true;
				}
			}
			break;
		}
		case hiding: {
			if (k > 0) {
				k -= 0.3;
				buttonGroup_current.control(k);
			} else if (sssc.getAlpha() < 1) {
				sssc.setAlpha(sssc.getAlpha() + 0.1);
			} else {
				// 恢复所有
				cStage.resumeSection(this);

				buttonGroup_current.dismissAll();// 清除按钮

				drawMe = false;

				menuState = MenuState.hided;
			}
			break;
		}
		case hided: {
			break;
		}
		}

	}

	@Override
	protected void onDestory() {
		menuState = MenuState.hided;
		aniOK = false;
		drawMe = false;
	}

	private class Button_ReturnMainMenu extends FGButton {

		public Button_ReturnMainMenu() {
			super(200, 60, GlobalResources.FRAMES_BUTTON_RETURNMAINMENU);
		}

		@Override
		public void onClick() {
			freezeAll(false, true);
			SectionStage.returnMainMenu();
			// FGStage.switchToStage(0);
		}
	}

	private class Button_ResumeGame extends FGButton {

		public Button_ResumeGame() {
			super(200, 60, GlobalResources.FRAMES_BUTTON_RESUMEGAME);
		}

		@Override
		public void onClick() {
			hide();
		}
	}

	private class Button_NextStage extends FGButton {

		public Button_NextStage() {
			super(200, 60, GlobalResources.FRAMES_BUTTON_NEXTSTAGE);
		}

		@Override
		public void onClick() {
			freezeAll(false, true);
			cStage.nextSection();
		}
	}

	private class Button_Restart extends FGButton {

		public Button_Restart() {
			super(200, 60, GlobalResources.FRAMES_BUTTON_RESTART);
		}

		@Override
		public void onClick() {
			freezeAll(false, true);
			cStage.restartSection();
		}
	}

}
