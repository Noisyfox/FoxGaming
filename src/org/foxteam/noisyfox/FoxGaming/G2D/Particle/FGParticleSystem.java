package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleEmitter.EmitType;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleType.ColorType;

import android.graphics.Color;
import android.util.Log;

/**
 * 
 * @ClassName: FGParticleSystem
 * @Description: 粒子系统
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:38:27
 * 
 */
public final class FGParticleSystem {

	private boolean _drawOrder_old2new = true;
	private int _position_x = 0;
	private int _position_y = 0;

	private HashMap<Long, Emitters> particleEmitters = new HashMap<Long, Emitters>();
	private HashMap<Long, FGParticleAttractor> particleAttractors = new HashMap<Long, FGParticleAttractor>();
	private HashMap<Long, FGParticleDestroyer> particleDestroyers = new HashMap<Long, FGParticleDestroyer>();
	private HashMap<Long, FGParticleDeflector> particleDeflectors = new HashMap<Long, FGParticleDeflector>();
	private HashMap<Long, FGParticleChanger> particleChangers = new HashMap<Long, FGParticleChanger>();

	protected long nid = -1;

	public FGParticleSystem() {
		Log.d("sss", "wwwwwwwwwwww");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nid = FGParticleNative.PScreateParticleSystemNative();
		// setMaxParticleNumber(200);
	}

	@Override
	protected void finalize() throws Throwable {
		FGParticleNative.PSremoveParticleSystemNative(nid);
		super.finalize();
	}

	public void setPosition(int x, int y) {

		_position_x = x;
		_position_y = y;

	}

	public void update() {
		FGParticleNative.PSupdateNative(nid);
	}

	public void draw() {
		// Particles p;
		// if (_drawOrder_old2new) {
		// p = particlePool_alive;
		// for (int i = 0; i < aliveParticleCount; i++) {
		//
		// if (p.shapeBaseType._particleSprite != null) {
		// p.convertor.setAlpha(p.alpha);
		// p.convertor.setRotation(p.angle);
		// p.convertor.setScale(p.shapeBaseType._scale_x * p.size,
		// p.shapeBaseType._scale_y * p.size);
		//
		// p.shapeBaseType._particleSprite
		// .setCurrentFrame((int) p.frame);
		// p.shapeBaseType._particleSprite.draw(_position_x + p.x,
		// _position_y + p.y, p.convertor, p.color);
		// }
		// p = p.next;
		// }
		//
		// } else {
		// p = particlePool_alive_last;
		// for (int i = aliveParticleCount - 1; i >= 0; i--) {
		//
		// if (p.shapeBaseType._particleSprite != null) {
		// p.convertor.setAlpha(p.alpha);
		// p.convertor.setRotation(p.angle);
		// p.convertor.setScale(p.shapeBaseType._scale_x * p.size,
		// p.shapeBaseType._scale_y * p.size);
		//
		// p.shapeBaseType._particleSprite
		// .setCurrentFrame((int) p.frame);
		// p.shapeBaseType._particleSprite.draw(_position_x + p.x,
		// _position_y + p.y, p.convertor, p.color);
		// }
		// p = p.prev;
		// }
		//
		// }

	}

	public void updateAndDraw() {
		update();
		draw();
	}

	public void setDrawOrder(boolean old2new) {

		_drawOrder_old2new = old2new;

	}

	public void createParticle(FGParticleType type, int x, int y, int number) {
		createParticle(type, x, y, Color.WHITE, number);
	}

	public void createParticle(FGParticleType type, int x, int y, int color,
			int number) {
		FGParticleNative.PScreateParticleNative(nid, type.nid, x, y, color,
				number);
	}

	public void clear() {

		FGParticleNative.PSclearNative(nid);

		particleAttractors.clear();
		particleDestroyers.clear();
		particleDeflectors.clear();
		particleChangers.clear();

	}

	public int count() {

		return FGParticleNative.PScountNative(nid);

	}

	public void bindParticleEmitter(FGParticleEmitter emitter) {
		if (emitter != null) {
			Emitters e = new Emitters();
			e.emitter = emitter;
			if (!particleEmitters.containsKey(Long.valueOf(emitter.nid))) {
				particleEmitters.put(Long.valueOf(emitter.nid), e);
				FGParticleNative.PSbindParticleEmitterNative(nid, emitter.nid);
			}
		}
	}

	public void bindParticleAttractor(FGParticleAttractor attractor) {
		if (attractor != null
				&& !particleAttractors.containsKey(Long.valueOf(attractor.nid))) {
			particleAttractors.put(Long.valueOf(attractor.nid), attractor);
			FGParticleNative.PSbindParticleAttractorNative(nid, attractor.nid);
		}
	}

	public void bindPraticleDestroyer(FGParticleDestroyer destroyer) {
		if (destroyer != null
				&& !particleDestroyers.containsKey(Long.valueOf(destroyer.nid))) {
			particleDestroyers.put(Long.valueOf(destroyer.nid), destroyer);
			FGParticleNative.PSbindPraticleDestroyerNative(nid, destroyer.nid);
		}
	}

	public void bindPraticleDeflector(FGParticleDeflector deflector) {
		if (deflector != null
				&& !particleDeflectors.containsKey(Long.valueOf(deflector.nid))) {
			particleDeflectors.put(Long.valueOf(deflector.nid), deflector);
			FGParticleNative.PSbindPraticleDeflectorNative(nid, deflector.nid);
		}
	}

	public void bindParticleChanger(FGParticleChanger changer) {
		if (changer != null
				&& !particleChangers.containsKey(Long.valueOf(changer.nid))) {
			particleChangers.put(Long.valueOf(changer.nid), changer);
			FGParticleNative.PSbindParticleChangerNative(nid, changer.nid);
		}
	}

	public void unbindParticleEmitter(FGParticleEmitter emitter) {
		particleEmitters.remove(Long.valueOf(emitter.nid));
		FGParticleNative.PSunbindParticleEmitterNative(nid, emitter.nid);
	}

	public void unbindParticleAttractor(FGParticleAttractor attractor) {
		particleAttractors.remove(Long.valueOf(attractor.nid));
		FGParticleNative.PSunbindParticleAttractorNative(nid, attractor.nid);
	}

	public void unbindPraticleDestroyer(FGParticleDestroyer destroyer) {
		particleDestroyers.remove(Long.valueOf(destroyer.nid));
		FGParticleNative.PSunbindPraticleDestroyerNative(nid, destroyer.nid);
	}

	public void unbindPraticleDeflector(FGParticleDeflector deflector) {
		particleDeflectors.remove(Long.valueOf(deflector.nid));
		FGParticleNative.PSunbindPraticleDeflectorNative(nid, deflector.nid);
	}

	public void unbindParticleChanger(FGParticleChanger changer) {
		particleChangers.remove(Long.valueOf(changer.nid));
		FGParticleNative.PSunbindParticleChangerNative(nid, changer.nid);
	}

	/**
	 * 设置该粒子系统最多可容纳的粒子数量
	 * 
	 * @param number
	 */
	public void setMaxParticleNumber(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException();
		}

		FGParticleNative.PSsetMaxParticleNumberNative(nid, number);
	}

	/**
	 * 
	 * @ClassName: Paritcles
	 * @Description: 储存每一个粒子的状态
	 * @author: Noisyfox
	 * @date: 2012-11-25 下午2:27:32
	 * 
	 */
	private class Particles {

		FGParticleType type = null;
		FGParticleType motionBaseType = null;
		FGParticleType shapeBaseType = null;
		FGSpriteConvertor convertor = new FGSpriteConvertor();

		double frame = 0.0;

		double angle = 0.0;
		double baseAngle = 0.0;

		double size = 1.0;

		int x = 0;
		int y = 0;

		double speed = 0.0;
		double direction = 0.0;

		int color = Color.WHITE;

		double alpha = 0.0;

		int lifeTime = 0;
		int stayTime = 0;

		int counter = -1;
		int trigger = 0;

		Particles prev = null;
		Particles next = null;

	}

	/**
	 * 
	 * @ClassName: Emitters
	 * @Description: 储存每一个发射器的状态
	 * @author: Noisyfox
	 * @date: 2012-11-25 下午8:00:55
	 * 
	 */
	private class Emitters {

		FGParticleEmitter emitter = null;
		int counter = -1;
		int trigger = 0;

		@Override
		public boolean equals(Object o) {
			if (o.getClass().isInstance(this)) {
				return ((Emitters) o).emitter == emitter;
			}

			return super.equals(o);
		}

	}
}
