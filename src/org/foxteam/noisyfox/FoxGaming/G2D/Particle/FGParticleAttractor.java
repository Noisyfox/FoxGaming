package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

/**
 * 
 * @ClassName: FGParticleAttractor
 * @Description: 粒子吸引器
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:37:27
 * 
 */
public class FGParticleAttractor {

	protected int _position_x = 0;
	protected int _position_y = 0;

	protected FGParticleForceKind _force_kind = FGParticleForceKind.constant;
	protected float _force_force = 0f;
	protected float _force_distance_max = 100f;
	protected boolean _force_additive = false;

	public void setPosition(int x, int y) {

		_position_x = x;
		_position_y = y;

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

	}

}
