package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.Background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class THEngineMainActivity extends GameActivity {

	@Override
	public void onEngineReady() {
		Stage s = new Stage();
		s.setStageSpeed(30);
		Performer p = new SystemControl();
		p.perform(s.getStageIndex());
		p = new Player();
		p.perform(s.getStageIndex());

		Background bkg = new Background();

		Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
				.getResources(),
				org.foxteam.noisyfox.THEngine.R.drawable.background);
		bkg.loadFromBitmap(b);
		bkg.setAdaptation(Background.ADAPTATION_SMART);
		bkg.setSpeed(0, 1);
		bkg.setAlignment(Background.ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM);
		bkg.setDrawMode(Background.ADAPTATION_OPTION_DRAW_REPEATING);
		bkg.setScaleMode(Background.ADAPTATION_OPTION_SCALE_WIDTHFIRST);
		s.setBackground(bkg);
	}

	@Override
	public void onCreate() {
		this.forcePortrait();
	}

}