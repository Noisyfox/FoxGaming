package org.foxteam.noisyfox.FoxGaming.Core;

import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;

import android.graphics.BitmapFactory;
import android.graphics.Color;

public final class FGSplashStage extends FGStage {

	FGFrame logo = new FGFrame();
	FGSprite logoS = new FGSprite();
	FGSpriteConvertor c = new FGSpriteConvertor();

	@Override
	protected void onCreate() {
		this.setSize(FGGamingThread.getScreenHeight(),
				FGGamingThread.getScreenWidth());
		this.setBackgroundColor(Color.BLACK);

		new splashController().perform(stageIndex);

		try {
			logo.loadFromBitmap(FGEGLHelper.getBindedGL(), BitmapFactory
					.decodeStream(FGGameCore.mainActivity.getAssets().open(
							"foxgaming.png")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		logoS.bindFrames(logo);
		logoS.setOffset(logoS.getWidth() / 2, logoS.getHeight() / 2);
	}

	private class splashController extends FGPerformer {

		@Override
		protected void onCreate() {
			this.setPosition(width / 2, height / 2);
			c.setAlpha(0);
		}

		int tmp = 0;

		@Override
		protected void onStep() {
			switch (tmp) {
			case 0:
				c.setAlpha(c.getAlpha() + 0.05);
				if (c.getAlpha() + 0.04 >= 1) {
					tmp++;
				}
				break;
			case 1:
				this.setAlarm(0, (int) (2.0 * getSpeed()), false);
				this.startAlarm(0);
				tmp++;
				break;
			case 3:
				c.setAlpha(c.getAlpha() - 0.05);
				if (c.getAlpha() - 0.04 <= 0) {
					tmp++;
				}
				break;
			case 4:
				if (getStageCount() > 1) {
					nextStage();
				}
				tmp++;
				break;
			}
		}

		@Override
		protected void onAlarm(int whichAlarm) {
			if (whichAlarm == 0) {
				tmp++;
			}
		}

		@Override
		protected void onDraw() {
			logoS.draw((int) this.getX(), (int) this.getY(), c);
		}

		@Override
		protected void onStageEnd() {
			closeStage(0);
		}

	}

}
