package org.foxteam.noisyfox.THEngine.Stages;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGBackground;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleEmitter;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleRegionDistribution;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleRegionShape;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleSystem;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.HighScore;
import org.foxteam.noisyfox.THEngine.Performers.StageSwitchEffect;

public final class _01_GameClear extends FGStage {

	FGButton returnMainMenu = new Button_ReturnMainMenu();
	FGParticleSystem fireWorkParticleSystem = new FGParticleSystem();

	@Override
	protected void onCreate() {
		setSize(FGGamingThread.getScreenHeight(),
				FGGamingThread.getScreenWidth());
		FGBackground bkg = new FGBackground();

		bkg.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.background_gameclear,
				false);
		bkg.setAdaptation(FGBackground.ADAPTATION_SMART);
		bkg.setDrawMode(FGBackground.ADAPTATION_OPTION_DRAW_SINGLE);
		bkg.setAlignment(FGBackground.ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_TOP);
		bkg.setScaleMode(FGBackground.ADAPTATION_OPTION_SCALE_MAXUSAGE);
		setBackground(bkg);
		setStageSpeed(30);

		new StageSwitchEffect();
		new stagePerformer().perform(stageIndex);

	}

	private class stagePerformer extends FGPerformer {

		private boolean canReact = false;
		private boolean highscoreRecorded = false;

		@Override
		protected void onCreate() {
			setAlarm(0, (int) (stageSpeed * 2f), false);
			startAlarm(0);

			setAlarm(1, (int) (stageSpeed * 2f), true);
			startAlarm(1);

			managedParticleSystem_requireManaged(fireWorkParticleSystem,
					depth - 10);
		}

		@Override
		protected void onDraw() {
		}

		@Override
		protected void onDestory() {
			managedParticleSystem_removeParticleSystem(fireWorkParticleSystem);
		}

		@Override
		protected void onTouchRelease(int whichfinger) {
			if (!canReact)
				return;

			if (!highscoreRecorded) {
				HighScore.requireHighScoreRecordedHandled();
				highscoreRecorded = true;

				returnMainMenu.perform(stageIndex);
				returnMainMenu.setPosition(getWidth() / 2, getHeight()
						- returnMainMenu.getHeight() / 2 - 5);
			}
		}

		@Override
		protected void onKeyRelease(int keyCode) {
			if (!canReact)
				return;

			if (!highscoreRecorded) {
				HighScore.requireHighScoreRecordedHandled();
				highscoreRecorded = true;

				returnMainMenu.perform(stageIndex);
				returnMainMenu.setPosition(getWidth() / 2, getHeight()
						- returnMainMenu.getHeight() / 2 - 5);
			}
		}

		@Override
		protected void onAlarm(int whichAlarm) {
			if (whichAlarm == 0) {
				canReact = true;
			} else if (whichAlarm == 1) {
				fireWork f = new fireWork();
				f.perform(stageIndex);
			}
		}

		@Override
		protected void onStep() {

		}

	}

	private class fireWork extends FGPerformer {

		FGParticleEmitter emitter_launch = new FGParticleEmitter();// 发射轨迹
		FGParticleEmitter emitter_boom = new FGParticleEmitter();// 最后爆炸效果
		float boomSpeed = 0;

		@Override
		protected void onCreate() {
			emitter_launch.stream(GlobalResources.PARTICLE_TYPE_FIREWORKS_PATH,
					1);
			emitter_boom.burst(GlobalResources.PARTICLE_TYPE_FIREWORKS_BOOM,
					100);

			fireWorkParticleSystem.bindParticleEmitter(emitter_launch);
			setPosition(width / 2, height - 1);
			this.motion_set(90, 35);

			boomSpeed = (float) FGMathsHelper.random(-10.0, 10.0);
		}

		@Override
		protected void onDestory() {
			fireWorkParticleSystem.unbindParticleEmitter(emitter_launch);
		}

		@Override
		protected void onAlarm(int whichAlarm) {
		}

		@Override
		protected void onOutOfStage() {
			this.dismiss();
		}

		@Override
		protected void onStep() {
			if (this.vspeed > boomSpeed) {
				fireWorkParticleSystem.bindParticleEmitter(emitter_boom);
				emitter_boom.setRegion((int) getX() - 1, (int) getY() - 1,
						(int) getX() + 1, (int) getY() + 1,
						FGParticleRegionShape.ellipse,
						FGParticleRegionDistribution.linear);
				this.dismiss();
			} else {
				emitter_launch.setRegion((int) getX() - 1, (int) getY() - 1,
						(int) getX() + 1, (int) getY() + 1,
						FGParticleRegionShape.ellipse,
						FGParticleRegionDistribution.linear);
				this.motion_add(270, 0.98f);
			}
		}

	}

	private class Button_ReturnMainMenu extends FGButton {

		public Button_ReturnMainMenu() {
			super(200, 60, GlobalResources.FRAMES_BUTTON_RETURNMAINMENU);

		}

		@Override
		public void onClick() {
			StageSwitchEffect.switchToStage(0);
		}
	}

}
