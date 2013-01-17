package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleEmitter
 * @Description: 粒子发射器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:39:20
 * 
 */
public final class FGParticleEmitter {

	protected int _region_x_min = 0;
	protected int _region_x_max = 0;
	protected int _region_y_min = 0;
	protected int _region_y_max = 0;
	protected FGParticleRegionShape _region_shape = FGParticleRegionShape.rectangle;
	protected FGParticleRegionDistribution _region_distribution = FGParticleRegionDistribution.linear;

	protected enum EmitType {
		burst, stream;
	}

	protected EmitType emitType = EmitType.stream;
	protected FGParticleType _emit_particle_type = null;
	protected int _emit_particle_number = 0;

	protected long nid = -1;

	public FGParticleEmitter() {
		nid = FGParticleNative.PEcreateParticleEmitterNative();
	}

	@Override
	protected void finalize() throws Throwable {
		FGParticleNative.PEremoveParticleEmitterNative(nid);
		super.finalize();
	}

	public void setRegion(int minX, int minY, int maxX, int maxY,
			FGParticleRegionShape shape,
			FGParticleRegionDistribution distribution) {

		if (minX > maxX || minY > maxY) {
			throw new IllegalArgumentException();
		}

		_region_x_min = minX;
		_region_x_max = maxX;
		_region_y_min = minY;
		_region_y_max = maxY;
		_region_shape = shape;
		_region_distribution = distribution;

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

		int distribution2 = FGParticleNative.PAR_REGION_DISTRIBUTION_LINEAR;

		switch (distribution) {
		case gaussian:
			distribution2 = FGParticleNative.PAR_REGION_DISTRIBUTION_GAUSSIAN;
			break;
		case invgaussian:
			distribution2 = FGParticleNative.PAR_REGION_DISTRIBUTION_INVGAUSSIAN;
			break;
		case linear:
			distribution2 = FGParticleNative.PAR_REGION_DISTRIBUTION_LINEAR;
			break;
		default:
			break;
		}

		FGParticleNative.PEsetRegionNative(nid, minX, minY, maxX, maxY, shape2,
				distribution2);

	}

	public void burst(FGParticleType type, int number) {

		if (number < 0) {
			throw new IllegalArgumentException();
		}

		emitType = EmitType.burst;
		_emit_particle_type = type;
		_emit_particle_number = number;

		FGParticleNative.PEburstNative(nid, type.nid, number);

	}

	public void stream(FGParticleType type, int number) {

		emitType = EmitType.stream;
		_emit_particle_type = type;
		_emit_particle_number = number;

		FGParticleNative.PEstreamNative(nid, type.nid, number);
	}
}
