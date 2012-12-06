package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGSimpleBGM;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.Core.FGViews;
import org.foxteam.noisyfox.FoxGaming.G2D.FGBackground;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.ButtonGroup;
import org.foxteam.noisyfox.THEngine.GlobalResources;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;

public class GamingMenu extends FGPerformer {

	private FGViews mainView = null;
	private FGFrame ssf = new FGFrame();
	private FGSprite sss = new FGSprite();
	private FGSpriteConvertor sssc = new FGSpriteConvertor();
	private FGStage cStage = null;
	private FGBackground cBKG = null;
	private int stage_width = 0;
	private int stage_height = 0;
	private MenuState menuState = MenuState.hided;
	private MenuType menuType;
	private static FGButton button_pause_returnMainMenu = null;
	private static FGButton button_pause_resumegame = null;
	private boolean aniOK = false;
	private boolean drawMe = false;
	private ButtonGroup buttonGroup_pause = new ButtonGroup();
	private float k = 0;

	private enum MenuState {
		showing, shown, hiding, hided;
	}

	public enum MenuType {
		pause, gameover, stageclear;
	}

	public GamingMenu() {
		button_pause_returnMainMenu = this.new Button_ReturnMainMenu();
		button_pause_resumegame = this.new Button_ResumeGame();
		buttonGroup_pause.addButton(button_pause_resumegame);
		buttonGroup_pause.addButton(button_pause_returnMainMenu);
		buttonGroup_pause.setDirection(false);
	}

	public void show(MenuType type) {
		if (menuState != MenuState.hided)
			return;

		buttonGroup_pause.setPlaceRegion(-100,
				FGGamingThread.getScreenHeight() / 2 - 120,
				FGGamingThread.getScreenWidth() + 100,
				FGGamingThread.getScreenHeight() / 2 + 120,
				FGGamingThread.getScreenWidth() / 2,
				FGGamingThread.getScreenHeight() / 2);

		menuType = type;
		cStage = FGStage.getCurrentStage();
		mainView = cStage.getView(0);
		stage_width = cStage.getWidth();
		stage_height = cStage.getHeight();
		Bitmap currentScreen = FGGamingThread.getScreenshots();
		ssf.loadFromBitmap(currentScreen);
		sss.bindFrames(ssf);
		setDepth(-1001);
		menuState = MenuState.showing;
		sssc.setAlpha(1);
		aniOK = false;
	}

	public void hide() {
		menuState = MenuState.hiding;
		aniOK = false;
		k = 1;
	}

	@Override
	protected void onDraw() {
		if (!drawMe)
			return;

		getCanvas().drawColor(Color.BLACK);
		sss.draw(getCanvas(), 0, 0, sssc);
	}

	@Override
	protected void onCreate() {
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
			// 暂停所有
			cBKG = cStage.getBackground();
			cStage.setBackground(null);// 暂停背景
			cStage.removeView(0);
			cStage.setSize(FGGamingThread.getScreenHeight(),
					FGGamingThread.getScreenWidth());

			FGPerformer ps[] = FGStage.getPerformers();
			for (FGPerformer p : ps) {
				if (p != this)
					p.freezeMe();
			}// 冻结其它所有 Performer

			FGSimpleBGM.pause();// 暂停声音

			// 添加按钮
			button_pause_returnMainMenu.perform(FGStage.getCurrentStage()
					.getStageIndex());
			button_pause_returnMainMenu.setDepth(depth - 1);

			button_pause_resumegame.perform(FGStage.getCurrentStage()
					.getStageIndex());
			button_pause_resumegame.setDepth(depth - 1);

			buttonGroup_pause.control(0);

			drawMe = true;
			menuState = MenuState.shown;

			break;
		}
		case shown: {
			if (!aniOK) {
				if (sssc.getAlpha() > 0.5) {
					sssc.setAlpha(sssc.getAlpha() - 0.1);// 屏幕变暗
					// } else if (button_pause_returnMainMenu.getX() + 100 <
					// FGGamingThread
					// .getScreenWidth() / 2) {
					// button_pause_returnMainMenu.setPosition(
					// button_pause_returnMainMenu.getX() + 100,
					// button_pause_returnMainMenu.getY());
					// } else if (button_pause_returnMainMenu.getX() + 1 <
					// FGGamingThread
					// .getScreenWidth() / 2) {
					// button_pause_returnMainMenu.setPosition(
					// FGGamingThread.getScreenWidth() / 2,
					// button_pause_returnMainMenu.getY());
					// button_pause_returnMainMenu.setEnabled(true);
					// aniOK = true;
					k = 0;
				} else if (k < 1) {
					k += 0.3;
					buttonGroup_pause.control(k);
				} else {
					aniOK = true;
				}
			}
			break;
		}
		case hiding: {
			if (k>0) {
				k -= 0.3;
				buttonGroup_pause.control(k);
			} else if (sssc.getAlpha() < 1) {
				sssc.setAlpha(sssc.getAlpha() + 0.1);
			} else {
				// 恢复所有
				cStage.setBackground(cBKG);
				cStage.addView(mainView);// 恢复 Stage
				cStage.setSize(stage_height, stage_width);

				FGPerformer ps[] = FGStage.getPerformers();
				for (FGPerformer p : ps) {
					p.unfreezeMe();
				}// 恢复所有 Performer

				FGSimpleBGM.play();// 恢复声音

				button_pause_returnMainMenu.dismiss();
				button_pause_resumegame.dismiss();

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
			FGStage.previousStage();
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

}
