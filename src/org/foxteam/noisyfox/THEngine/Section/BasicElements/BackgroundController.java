package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGDraw;

import android.graphics.Color;

public final class BackgroundController extends FGPerformer {

	private static final int SQUARE_COUNT_MAX = 40;
	private static BKG[] bkgs = new BKG[SQUARE_COUNT_MAX * 2];

	@Override
	protected void onDraw() {

		for (int i = 0; i < bkgs.length; i++) {
			BKG b = bkgs[i];

			if (b.y + b.height <= 0)
				continue;

			FGDraw.setColor(b.color);
			FGDraw.setAlpha(1);
			FGDraw.drawRectFill(FGEGLHelper.getBindedGL(), b.x, b.y, b.x
					+ b.width, b.y + b.height);
		}
	}

	@Override
	protected void onCreate() {
		for (int i = 0; i < bkgs.length; i++) {
			BKG b = new BKG();
			b.y = FGMathsHelper.random(-FGStage.getCurrentStage().getHeight(),
					FGStage.getCurrentStage().getHeight());
			randomBKG(b);
			bkgs[i] = b;
		}
		this.setDepth(10000);

		requireEventFeature(FGEventsListener.EVENT_ONDRAW
				| FGEventsListener.EVENT_ONSTEP);
	}

	private void randomBKG(BKG bkg) {
		bkg.x = (float) FGMathsHelper.random(-5.0, (double) FGStage
				.getCurrentStage().getWidth());
		bkg.speed = (float) FGMathsHelper.random(20f / FGStage
				.getCurrentStage().getStageSpeed(), 40f / FGStage
				.getCurrentStage().getStageSpeed());
		// bkg.width = (float) FGMathsHelper.random(30.0, 70.0);
		bkg.width = 70;
		bkg.height = bkg.width;
		int value = FGMathsHelper.random(30, 100);
		bkg.color = Color.rgb(value, value, value);
	}

	@Override
	protected void onStep() {

		for (int i = 0; i < bkgs.length; i++) {
			BKG b = bkgs[i];
			b.y += b.speed;
			if (b.y >= FGStage.getCurrentStage().getHeight()) {
				randomBKG(b);
				b.y = -FGMathsHelper.random((int) b.height, FGStage
						.getCurrentStage().getHeight());
			}
		}
	}

	private class BKG {
		int color = Color.WHITE;
		float width = 0f;
		float height = 0f;
		float speed = 0f;
		float x = 0f;
		float y = 0f;
	}
}
