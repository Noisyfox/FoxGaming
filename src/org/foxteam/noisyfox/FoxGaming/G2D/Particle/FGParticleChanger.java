package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleChanger
 * @Description: 粒子转换器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:40:28
 * 
 */
public final class FGParticleChanger {

	protected int _region_x_min = 0;
	protected int _region_x_max = 0;
	protected int _region_y_min = 0;
	protected int _region_y_max = 0;
	protected FGParticleRegionShape _region_shape = FGParticleRegionShape.rectangle;

	protected FGParticleType _changeType_target = null;
	protected FGParticleType _changeType_final = null;

	protected FGParticleChangeKind _changeKind = FGParticleChangeKind.all;

	protected long nid = -1;

	public FGParticleChanger() {
		nid = FGParticleNative.PCcreateParticleChangerNative();
	}

	@Override
	protected void finalize() throws Throwable {
		FGParticleNative.PCremoveParticleChangerNative(nid);
		super.finalize();
	}

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

		int shape2 = FGParticleNative.PAR_REGION_SHAPE_RECTANGLE;

		switch (shape) {
		case diamond:
			shape2 = FGParticleNative.PAR_REGION_SHAPE_DIAMOND;
			break;
		case ellipse:
			shape2 = FGParticleNative.PAR_REGION_SHAPE_ELLIPSE;
			break;
		case rectangle:
			shape2 = FGParticleNative.PAR_REGION_SHAPE_RECTANGLE;
			break;
		default:
			break;
		}

		FGParticleNative.PCsetRegionNative(nid, minX, minY, maxX, maxY, shape2);

	}

	public void setParticleTypes(FGParticleType targetType,
			FGParticleType finalType) {

		if (targetType == null || finalType == null) {
			throw new IllegalArgumentException();
		}

		_changeType_target = targetType;
		_changeType_final = finalType;

		FGParticleNative.PCsetParticleTypesNative(nid, targetType.nid,
				finalType.nid);

	}

	public void setChangerKind(FGParticleChangeKind kind) {

		_changeKind = kind;

		int kind2 = FGParticleNative.PAR_CHANGE_ALL;

		switch (kind) {
		case all:
			kind2 = FGParticleNative.PAR_CHANGE_ALL;
			break;
		case motion:
			kind2 = FGParticleNative.PAR_CHANGE_MOTION;
			break;
		case shape:
			kind2 = FGParticleNative.PAR_CHANGE_SHAPE;
			break;
		default:
			break;
		}

		FGParticleNative.PCsetChangerKindNative(nid, kind2);

	}
}
