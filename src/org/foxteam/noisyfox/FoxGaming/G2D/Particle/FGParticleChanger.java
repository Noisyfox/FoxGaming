package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleChanger
 * @Description: 粒子转换器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:40:28
 * 
 */
public class FGParticleChanger {

	protected int _region_x_min = 0;
	protected int _region_x_max = 0;
	protected int _region_y_min = 0;
	protected int _region_y_max = 0;
	protected FGParticleRegionShape _region_shape = FGParticleRegionShape.rectangle;

	protected FGParticleType _changeType_target = null;
	protected FGParticleType _changeType_final = null;

	protected FGParticleChangeKind _changeKind = FGParticleChangeKind.all;

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

	public void setParticleTypes(FGParticleType targetType,
			FGParticleType finalType) {

		if (targetType == null || finalType == null) {
			throw new IllegalArgumentException();
		}

		_changeType_target = targetType;
		_changeType_final = finalType;

	}

	public void setChangerKind(FGParticleChangeKind kind) {

		_changeKind = kind;

	}
}
