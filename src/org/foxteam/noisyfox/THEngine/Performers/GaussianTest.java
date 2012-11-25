package org.foxteam.noisyfox.THEngine.Performers;

import java.util.Random;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;

import android.graphics.Paint;

public class GaussianTest extends FGPerformer {

	@Override
	protected void onDraw() {
		for (int i = 0; i < 10000; i++) {
			Random random = new Random();
			int x, y;

			double dir = random.nextDouble() * 360.0;
			double length = (1.0-FGMathsHelper.randomGaussian()) * 100;

			x = (int) FGMathsHelper.lengthdir_x((float) length, (float) dir);
			y = (int) FGMathsHelper.lengthdir_y((float) length, (float) dir);

			getCanvas().drawPoint(getX() + x, getY() + y, new Paint());

		}
	}

}
