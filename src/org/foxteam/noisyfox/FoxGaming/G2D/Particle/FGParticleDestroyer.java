package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleDestroyer
 * @Description: 粒子破坏器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:39:49
 *
 */
public class FGParticleDestroyer {

	protected int _region_x_min = 0;
	protected int _region_x_max = 0;
	protected int _region_y_min = 0;
	protected int _region_y_max = 0;
	protected FGParticleRegionShape _region_shape = FGParticleRegionShape.rectangle;

	public void setRegion(int minX, int minY, int maxX, int maxY,
			FGParticleRegionShape shape) {

		if (minX > maxX || minY > maxY) {
			throw new IllegalArgumentException();
		}

		_region_x_min = minX;
		_region_x_max = maxX;
		_region_y_min = minY;
		_region_y_max = maxY;
		_region_shape = shape;

	}

}
