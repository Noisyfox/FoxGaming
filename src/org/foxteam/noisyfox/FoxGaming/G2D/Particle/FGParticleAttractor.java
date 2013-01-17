package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleAttractor
 * @Description: 粒子吸引器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:37:27
 * 
 */
public final class FGParticleAttractor {

	protected int _position_x = 0;
	protected int _position_y = 0;

	protected FGParticleForceKind _force_kind = FGParticleForceKind.constant;
	protected float _force_force = 0f;
	protected float _force_distance_max = 100f;
	protected boolean _force_additive = false;

	protected long nid = -1;

	public FGParticleAttractor() {
		nid = FGParticleNative.PAcreateParticleAttractorNative();
	}

	@Override
	protected void finalize() throws Throwable {
		FGParticleNative.PAremoveParticleAttractorNative(nid);
		super.finalize();
	}

	public void setPosition(int x, int y) {

		_position_x = x;
		_position_y = y;

		FGParticleNative.PAsetPositionNative(nid, x, y);

	}

	public void setForce(FGParticleForceKind kind, float force,
			float maxDistance, boolean additive) {

		if (force < 0 || maxDistance < 0) {
			throw new IllegalArgumentException();
		}

		_force_kind = kind;
		_force_force = force;
		_force_distance_max = maxDistance;
		_force_additive = additive;

		int kind2 = FGParticleNative.PAR_FORCE_CONSTANT;

		switch (kind) {
		case constant:
			kind2 = FGParticleNative.PAR_FORCE_CONSTANT;
			break;
		case linear:
			kind2 = FGParticleNative.PAR_FORCE_LINEAR;
			break;
		case quadratic:
			kind2 = FGParticleNative.PAR_FORCE_QUADRATIC;
			break;
		default:
			break;
		}
		FGParticleNative.PAsetForceNative(nid, kind2, force, maxDistance,
				additive);

	}

}
