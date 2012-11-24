package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleDeflector
 * @Description: 粒子偏转器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:37:54
 * 
 */
public class FGParticleDeflector {

	protected int _region_x_min = 0;
	protected int _region_x_max = 0;
	protected int _region_y_min = 0;
	protected int _region_y_max = 0;

	protected FGParticleDeflectKind _deflect_kind = FGParticleDeflectKind.horizontal;

	protected double _friction = 0.0;

	public void setRegion(int minX, int minY, int maxX, int maxY) {

		if (minX > maxX || minY > maxY) {
			throw new IllegalArgumentException();
		}

		_region_x_min = minX;
		_region_x_max = maxX;
		_region_y_min = minY;
		_region_y_max = maxY;

	}

	public void setDeflectKind(FGParticleDeflectKind kind) {

		_deflect_kind = kind;

	}

	public void setFriction(double friction) {

		if (friction < 0) {
			throw new IllegalArgumentException();
		}

		_friction = friction;

	}

}
