package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.Background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class THEngineMainActivity extends GameActivity {

	@Override
	public void onEngineReady() {
		Stage s = new Stage();
		Performer p = new TestPerformer();
		s.employPerformer(p);
		Background bkg = new Background();

		Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
				.getResources(),
				org.foxteam.noisyfox.THEngine.R.drawable.background_1);
		bkg.loadFromBitmap(b);
		bkg.setAdaptation(Background.ADAPTATION_SMART);
		bkg.setSpeed(1, 0);
		bkg.setAlignment(Background.ADAPTATION_OPTION_ALIGNMENT_RIGHT_BOTTOM);
		bkg.setDrawMode(Background.ADAPTATION_OPTION_DRAW_REPEATING);
		bkg.setScaleMode(Background.ADAPTATION_OPTION_SCALE_MAXUSAGE);
		s.setBackground(bkg);
	}

	@Override
	public void onCreate() {
	}

}