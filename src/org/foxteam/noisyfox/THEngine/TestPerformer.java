/**
 * FileName:     TestPerformer.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-22 下午2:37:23
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-22      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @ClassName: TestPerformer
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-22 下午2:37:23
 * 
 */
public class TestPerformer extends Performer {

	int x1 = 0;
	int y1 = 0;

	int x2 = 0;
	int y2 = 0;

	int x3 = 0;
	int y3 = 0;

	private int soundId;
	private int bgmId;

	Sprite s;
	SpriteConvertor sc;
	GraphicCollision g1, g2, g3;

	@Override
	protected void onCreate() {
		soundId = SimpleSoundEffect
				.loadSoundEffect(org.foxteam.noisyfox.THEngine.R.raw.test_soundeffect);
		bgmId = SimpleBGM.loadBGM(org.foxteam.noisyfox.THEngine.R.raw.test_bgm);
		SimpleBGM.play(bgmId, true);
	}

	@Override
	protected void onTouchPress(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			x1 = x;
			y1 = y;
		} else if (whichfinger == 1) {
			x2 = x;
			y2 = y;
		} else {
			x3 = x;
			y3 = y;
		}
		SimpleSoundEffect.play(soundId);
	}

	@Override
	protected void onTouch(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			x1 = x;
			y1 = y;
		} else if (whichfinger == 1) {
			x2 = x;
			y2 = y;
		} else {
			x3 = x;
			y3 = y;
		}
	}

	@Override
	protected void onDraw() {
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		this.getCanvas().drawText(GamingThread.getSPS() + "", 50, 50, p);
		this.getCanvas().drawText("1", x1, y1, p);
		this.getCanvas().drawText("2", x2, y2, p);
		this.getCanvas().drawText("3", x3, y3, p);
		s.draw(this.getCanvas(), x1, y1, sc);
		this.getCanvas().drawPoint(x1, y1, p);
		this.getCanvas().drawCircle(190, 190, 70, p);
		this.getCanvas().drawRect(190, 600, 220, 670, p);
		g1.setPosition(x1, y1);
		if (g1.isCollisionWith(g2)) {
			Paint p2 = new Paint();
			p2.setColor(Color.RED);
			this.getCanvas().drawCircle(190, 190, 70, p2);
		}
		if (g1.isCollisionWith(g3)) {
			Paint p2 = new Paint();
			p2.setColor(Color.RED);
			this.getCanvas().drawRect(190, 600, 220, 670, p2);
		}
		this.getCanvas().drawCircle(x1, y1, 10, p);
	}

	public TestPerformer() {
		s = new Sprite();
		Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
				.getResources(),
				org.foxteam.noisyfox.THEngine.R.drawable.button);
		s.loadFromBitmap(b);
		s.setOffset(s.getWidth() / 2, 0);
		sc = new SpriteConvertor();
		sc.setRotation(60);
		sc.setScale(1, 0.5);
		sc.setAlpha(0.5);

		g1 = new GraphicCollision();
		// g1.addPoint(0, 0);
		g1.addCircle(0, 0, 10);

		g2 = new GraphicCollision();
		g2.addCircle(190, 190, 70);

		g3 = new GraphicCollision();
		int[][] v = { { 190, 600 }, { 220, 600 }, { 220, 670 }, { 190, 670 } };
		g3.addPolygon(v, true);
	}

}
