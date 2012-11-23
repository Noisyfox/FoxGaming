package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;

import android.graphics.Color;

public class FGParticleType {

	FGSprite particleSprite = null;

	public void setSprite(FGSprite sprite) {

	}

	public void setSpriteFrameAnimation(boolean enableFrameAni, double speed,
			boolean startWithRandomFrame) {

	}

	public void setSize(double minSize, double maxSize,
			double incrementPerStep, double wiggle) {

	}

	public void setScale(float xScale, float yScale) {

	}

	public void setOrientation(float minAngle, float maxAngle,
			double incrementPerStep, double wiggle, boolean relative) {

	}

	public void setColor(Color color) {

	}

	public void setColor(Color color1, Color color2) {

	}

	public void setColor(Color color1, Color color2, Color color3) {

	}

	public void setColorRGB(int minR, int minG, int minB, int maxR, int maxG,
			int maxB) {

	}

	public void setColorHSV(int minH, int minS, int minV, int maxH, int maxS,
			int maxV) {

	}

	public void setLifeTime(int min, int max) {

	}

	public void createNewParticleOnStep(boolean enabled, FGParticleType type,
			int number) {
	}

	public void createNewParticleOnDeath(boolean enabled, FGParticleType type,
			int number) {
	}

	public void setSpeed(double minSpeed, double maxSpeed,
			double incrementPerStep, double wiggle) {

	}

	public void setDirection(double minDirection, double maxDirection,
			double incrementPerStep, double wiggle) {

	}

	public void setGravity(double amount, double direction) {

	}
}
