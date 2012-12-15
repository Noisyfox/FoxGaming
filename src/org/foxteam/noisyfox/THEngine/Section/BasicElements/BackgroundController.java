package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGDraw;

import android.graphics.Color;

public final class BackgroundController extends FGPerformer {

	private static List<BKG> bkgs = new ArrayList<BKG>();
	private static int SQUARE_COUNT_MAX = 20;

	@Override
	protected void onDraw() {

		for (BKG b : bkgs) {
			if (b.y + b.height <= 0)
				continue;

			FGDraw.setColor(b.color);
			FGDraw.drawRect(FGEGLHelper.getBufferGL(), b.x, b.y, b.x + b.width,
					b.y + b.height);
		}
	}

	@Override
	protected void onCreate() {
		for (int i = bkgs.size(); i < SQUARE_COUNT_MAX * 2; i++) {
			BKG b = new BKG();
			b.y = FGMathsHelper.random(-FGStage.getCurrentStage().getHeight(),
					FGStage.getCurrentStage().getHeight());
			randomBKG(b);
			bkgs.add(b);
		}
	}

	private void randomBKG(BKG bkg) {
		bkg.x = (float) FGMathsHelper.random(-5.0, (double) FGStage
				.getCurrentStage().getWidth());
		bkg.speed = (float) FGMathsHelper.random(20f / FGStage
				.getCurrentStage().getStageSpeed(), 40f / FGStage
				.getCurrentStage().getStageSpeed());
		bkg.width = (float) FGMathsHelper.random(30.0, 70.0);
		bkg.height = bkg.width;
		int value = FGMathsHelper.random(75, 130);
		bkg.color = Color.rgb(value, value, value);
	}

	@Override
	protected void onStep() {
		for (BKG b : bkgs) {
			b.y += b.speed;
			if (b.y >= FGStage.getCurrentStage().getHeight()) {
				b.y = -FGMathsHelper.random(0, FGStage.getCurrentStage()
						.getHeight());
				randomBKG(b);
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
