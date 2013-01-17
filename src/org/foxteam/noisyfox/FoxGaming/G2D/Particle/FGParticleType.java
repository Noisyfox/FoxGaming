package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;

import android.graphics.Color;

/**
 * 
 * @ClassName: FGParticleType
 * @Description: 粒子类型
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:38:15
 * 
 */
public final class FGParticleType {

	protected FGSprite _particleSprite = null;

	protected boolean _frameAni_enabled = false;
	protected double _frameAni_speed = 0.0;
	protected boolean _frameAni_startWithRandomFrame = false;

	protected double _size_min = 1.0;
	protected double _size_max = 1.0;
	protected double _size_incrementPerStep = 0.0;
	protected double _size_wiggle = 0.0;

	protected float _scale_x = 1f;
	protected float _scale_y = 1f;

	protected float _orientation_angle_min = 0f;
	protected float _orientation_angle_max = 0f;
	protected double _orientation_incrementPerStep = 0.0;
	protected double _orientation_wiggle = 0.0;
	protected boolean _orientation_relative = false;

	protected enum ColorType {
		color1, color2, color3, RGB, HSV
	}

	protected ColorType _color_type = ColorType.color1;
	protected int _color_color1 = Color.WHITE;
	protected int _color_color2 = Color.WHITE;
	protected int _color_color3 = Color.WHITE;
	protected int _color_RGB_R_min = -1;
	protected int _color_RGB_R_max = -1;
	protected int _color_RGB_G_min = -1;
	protected int _color_RGB_G_max = -1;
	protected int _color_RGB_B_min = -1;
	protected int _color_RGB_B_max = -1;
	protected int _color_HSV_H_min = -1;
	protected int _color_HSV_H_max = -1;
	protected double _color_HSV_S_min = -1;
	protected double _color_HSV_S_max = -1;
	protected double _color_HSV_V_min = -1;
	protected double _color_HSV_V_max = -1;

	protected enum AlphaType {
		alpha1, alpha2, alpha3
	}

	protected AlphaType _alpha_type = AlphaType.alpha1;
	protected double _alpha_1 = 1.0;
	protected double _alpha_2 = 1.0;
	protected double _alpha_3 = 1.0;

	protected int _lifeTime_min = 100;
	protected int _lifeTime_max = 100;

	protected boolean _particleOnStep_enabled = false;
	protected FGParticleType _particleOnStep_type = null;
	protected int _particleOnStep_number = 0;

	protected boolean _particleOnDeath_enabled = false;
	protected FGParticleType _particleOnDeath_type = null;
	protected int _particleOnDeath_number = 0;

	protected double _speed_min = 0.0;
	protected double _speed_max = 0.0;
	protected double _speed_incrementPerStep = 0.0;
	protected double _speed_wiggle = 0.0;

	protected double _direction_min = 0.0;
	protected double _direction_max = 0.0;
	protected double _direction_incrementPerStep = 0.0;
	protected double _direction_wiggle = 0.0;

	protected double _gravity_amount = 0.0;
	protected double _gravity_direction = 0.0;

	protected long nid = -1;

	public FGParticleType() {
		nid = FGParticleNative.PTcreateParticleTypeNative();
	}

	@Override
	protected void finalize() throws Throwable {
		FGParticleNative.PTremoveParticleTypeNative(nid);
		super.finalize();
	}

	public void setSprite(FGSprite sprite) {

		_particleSprite = sprite;

		FGParticleNative
				.PTsetSpriteParameterNative(nid, sprite.getFrameCount());

	}

	public void setSpriteFrameAnimation(boolean enableFrameAni, double speed,
			boolean startWithRandomFrame) {

		_frameAni_enabled = enableFrameAni;
		_frameAni_speed = speed;
		_frameAni_startWithRandomFrame = startWithRandomFrame;

		FGParticleNative.PTsetSpriteFrameAnimationNative(nid, enableFrameAni,
				speed, startWithRandomFrame);

	}

	public void setSize(double minSize, double maxSize,
			double incrementPerStep, double wiggle) {
		if (minSize < 0 || minSize > maxSize || wiggle < 0) {
			throw new IllegalArgumentException();
		}

		_size_min = minSize;
		_size_max = maxSize;
		_size_incrementPerStep = incrementPerStep;
		_size_wiggle = wiggle;

		FGParticleNative.PTsetSizeNative(nid, minSize, maxSize,
				incrementPerStep, wiggle);

	}

	public void setScale(float xScale, float yScale) {

		_scale_x = xScale;
		_scale_y = yScale;

		FGParticleNative.PTsetScaleNative(nid, xScale, yScale);

	}

	public void setOrientation(float minAngle, float maxAngle,
			double incrementPerStep, double wiggle, boolean relative) {

		if (minAngle < 0 || minAngle > maxAngle || maxAngle >= 360
				|| wiggle < 0) {
			throw new IllegalArgumentException();
		}

		_orientation_angle_min = minAngle;
		_orientation_angle_max = maxAngle;
		_orientation_incrementPerStep = incrementPerStep;
		_orientation_wiggle = wiggle;
		_orientation_relative = relative;

		FGParticleNative.PTsetOrientationNative(nid, minAngle, maxAngle,
				incrementPerStep, wiggle, relative);

	}

	public void setColor(int color) {

		_color_type = ColorType.color1;
		_color_color1 = color;

		FGParticleNative.PTsetColorNative(nid, color);

	}

	public void setColor(int color1, int color2) {

		_color_type = ColorType.color2;
		_color_color1 = color1;
		_color_color2 = color2;

		FGParticleNative.PTsetColorNative(nid, color1, color2);

	}

	public void setColor(int color1, int color2, int color3) {

		_color_type = ColorType.color3;
		_color_color1 = color1;
		_color_color2 = color2;
		_color_color3 = color3;

		FGParticleNative.PTsetColorNative(nid, color1, color2, color3);

	}

	public void setColorRGB(int minR, int minG, int minB, int maxR, int maxG,
			int maxB) {

		if (minR < 0 || minR > maxR || maxR > 255 || minG < 0 || minG > maxG
				|| maxG > 255 || minB < 0 || minB > maxB || maxB > 255) {
			throw new IllegalArgumentException();
		}

		_color_type = ColorType.RGB;
		_color_RGB_R_min = minR;
		_color_RGB_R_max = maxR;
		_color_RGB_G_min = minG;
		_color_RGB_G_max = maxG;
		_color_RGB_B_min = minB;
		_color_RGB_B_max = maxB;

		FGParticleNative.PTsetColorRGBNative(nid, minR, minG, minB, maxR, maxG,
				maxB);

	}

	public void setColorHSV(int minH, double minS, double minV, int maxH,
			double maxS, double maxV) {

		if (minH < 0 || minH > maxH || maxH > 359 || minS < 0 || minS > maxS
				|| maxS > 1 || minV < 0 || minV > maxV || maxV > 1) {
			throw new IllegalArgumentException();
		}

		_color_type = ColorType.HSV;
		_color_HSV_H_min = minH;
		_color_HSV_H_max = maxH;
		_color_HSV_S_min = minS;
		_color_HSV_S_max = maxS;
		_color_HSV_V_min = minV;
		_color_HSV_V_max = maxV;

		FGParticleNative.PTsetColorHSVNative(nid, minH, minS, minV, maxH, maxS,
				maxV);

	}

	public void setAlpha(double alpha) {

		if (alpha < 0.0 || alpha > 1.0) {
			throw new IllegalArgumentException();
		}

		_alpha_type = AlphaType.alpha1;
		_alpha_1 = alpha;

		FGParticleNative.PTsetAlphaNative(nid, alpha);

	}

	public void setAlpha(double alpha1, double alpha2) {

		if (alpha1 < 0.0 || alpha1 > 1.0 || alpha2 < 0.0 || alpha2 > 1.0) {
			throw new IllegalArgumentException();
		}

		_alpha_type = AlphaType.alpha2;
		_alpha_1 = alpha1;
		_alpha_2 = alpha2;

		FGParticleNative.PTsetAlphaNative(nid, alpha1, alpha2);

	}

	public void setAlpha(double alpha1, double alpha2, double alpha3) {

		if (alpha1 < 0.0 || alpha1 > 1.0 || alpha2 < 0.0 || alpha2 > 1.0
				|| alpha3 < 0.0 || alpha3 > 1.0) {
			throw new IllegalArgumentException();
		}

		_alpha_type = AlphaType.alpha3;
		_alpha_1 = alpha1;
		_alpha_2 = alpha2;
		_alpha_3 = alpha3;

		FGParticleNative.PTsetAlphaNative(nid, alpha1, alpha2, alpha3);

	}

	public void setLifeTime(int min, int max) {

		if (min < 0 || min > max) {
			throw new IllegalArgumentException();
		}

		_lifeTime_min = min;
		_lifeTime_max = max;

		FGParticleNative.PTsetLifeTimeNative(nid, min, max);

	}

	public void createNewParticleOnStep(boolean enabled, FGParticleType type,
			int number) {

		if (number < 0) {
			throw new IllegalArgumentException();
		}

		_particleOnStep_enabled = enabled;
		_particleOnStep_type = type;
		_particleOnStep_number = number;

		FGParticleNative.PTcreateNewParticleOnStepNative(nid, enabled,
				type.nid, number);

	}

	public void createNewParticleOnDeath(boolean enabled, FGParticleType type,
			int number) {

		if (number < 0) {
			throw new IllegalArgumentException();
		}

		_particleOnDeath_enabled = enabled;
		_particleOnDeath_type = type;
		_particleOnDeath_number = number;

		FGParticleNative.PTcreateNewParticleOnDeathNative(nid, enabled,
				type.nid, number);

	}

	public void setSpeed(double minSpeed, double maxSpeed,
			double incrementPerStep, double wiggle) {

		if (minSpeed < 0 || minSpeed > maxSpeed || wiggle < 0) {
			throw new IllegalArgumentException();
		}

		_speed_min = minSpeed;
		_speed_max = maxSpeed;
		_speed_incrementPerStep = incrementPerStep;
		_speed_wiggle = wiggle;

		FGParticleNative.PTsetSpeedNative(nid, minSpeed, maxSpeed,
				incrementPerStep, wiggle);

	}

	public void setDirection(double minDirection, double maxDirection,
			double incrementPerStep, double wiggle) {

		if (minDirection < 0 || minDirection > maxDirection
				|| maxDirection >= 360 || wiggle < 0) {
			throw new IllegalArgumentException();
		}

		_direction_min = minDirection;
		_direction_max = maxDirection;
		_direction_incrementPerStep = incrementPerStep;
		_direction_wiggle = wiggle;

		FGParticleNative.PTsetDirectionNative(nid, minDirection, maxDirection,
				incrementPerStep, wiggle);

	}

	public void setGravity(double amount, double direction) {

		_gravity_amount = amount;
		_gravity_direction = direction;

		FGParticleNative.PTsetGravityNative(nid, amount, direction);

	}
}
