package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGDraw;

import android.graphics.Color;

public final class StageSwitchEffect extends FGPerformer {

	boolean isOnStageStart = false;
	boolean exitStage = false;
	boolean aniOK = false;
	boolean freezed = false;

	private float alpha = 1.0f;
	private int targetStage = -1;

	private static StageSwitchEffect activatedInstance = null;

	public StageSwitchEffect() {
		perform(FGStage.getCurrentStage().getStageIndex());
	}

	@Override
	protected void onCreate() {
		setDepth(-10000);
		activatedInstance = this;
		exitStage = false;

		requireEventFeature(FGEventsListener.EVENT_ONDRAW
				| FGEventsListener.EVENT_ONSTAGESTART
				| FGEventsListener.EVENT_ONSTEPSTART);
	}

	@Override
	protected void onDraw() {
		if (alpha > 0.001f) {
			FGDraw.setAlpha(alpha);
			FGDraw.setColor(Color.BLACK);
			FGDraw.drawRectFill(FGEGLHelper.getBindedGL(), 0, 0, FGStage
					.getCurrentStage().getWidth(), FGStage.getCurrentStage()
					.getHeight());
		}
	}

	@Override
	protected void onStageStart() {
		isOnStageStart = true;
	}

	@Override
	protected void onStepStart() {
		if (!aniOK) {
			if (!exitStage) {
				if (!isOnStageStart) {
					alpha = 0;
					aniOK = true;
				} else if (alpha > 0) {
					if (!freezed) {
						// freezeAll(true, true);
						freezed = true;
					}
					alpha -= 0.1f;
					if (alpha < 0)
						alpha = 0;
				} else {
					aniOK = true;
					// freezeAll(false, true);
					freezed = false;
				}
			} else {
				if (alpha < 1) {
					alpha += 0.1f;
					if (alpha > 1)
						alpha = 1;
				} else {
					FGStage.switchToStage(targetStage);
				}
			}
		}
	}

	protected void exitStage() {
		if (aniOK && !exitStage) {
			exitStage = true;
			aniOK = false;
			freezed = false;
		}
	}

	public static void switchToStage(int index) {
		if (activatedInstance == null || !activatedInstance.isPerforming()) {
			FGStage.switchToStage(index);
		} else {
			activatedInstance.exitStage();
			activatedInstance.targetStage = index;
		}
	}

}
