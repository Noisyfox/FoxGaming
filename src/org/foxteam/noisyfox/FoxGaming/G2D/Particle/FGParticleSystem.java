package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 
 * @ClassName: FGParticleSystem
 * @Description: 粒子系统
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:38:27
 *
 */
public class FGParticleSystem {

	public void update() {

	}

	public void draw(Canvas c, int x, int y) {

	}

	public void updateAndDraw(Canvas c, int x, int y) {
		update();
		draw(c, x, y);
	}

	public void setDrawOrder(boolean old2new) {

	}

	public void createParticle(FGParticleType type, int x, int y, int number) {

	}

	public void createParticle(FGParticleType type, int x, int y, Color color,
			int number) {

	}

	public void clear() {

	}

	public int count() {
		return 0;
	}

}
