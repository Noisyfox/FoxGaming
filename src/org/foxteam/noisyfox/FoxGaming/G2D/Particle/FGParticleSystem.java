package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 
 * @ClassName: FGParticleSystem
 * @Description: 粒子系统
 * @author: Noisyfox
 * @date: 2012-11-24 下午5:38:27
 * 
 */
public class FGParticleSystem {

	private boolean _drawOrder_old2new = true;

	private List<Particles> particlePool = new LinkedList<Particles>();
	private List<Emitters> particleEmitters = new LinkedList<Emitters>();
	private List<FGParticleAttractor> particleAttractors = new LinkedList<FGParticleAttractor>();
	private List<FGParticleDestroyer> particleDestroyers = new LinkedList<FGParticleDestroyer>();
	private List<FGParticleDeflector> particleDeflectors = new LinkedList<FGParticleDeflector>();
	private List<FGParticleChanger> particleChangers = new LinkedList<FGParticleChanger>();
	private static Random random = new Random();

	public FGParticleSystem() {

	}

	public void update() {

		// 先清除所有已经死亡的particle
		int _poolSize = particlePool.size();
		for (int i = 0; i < _poolSize;) {
			Particles p = particlePool.get(i);
			if (p.stayTime > p.lifeTime) {
				_poolSize--;
				particlePool.remove(i);
				if (p.type._particleOnDeath_enabled) {
					createParticle(p.type._particleOnDeath_type, p.x, p.y,
							p.type._particleOnDeath_number);
				}
			} else {
				i++;
			}
		}

		// 发射器发射粒子
		for (Emitters pe : particleEmitters) {
			if (pe.emitter._emit_particle_number < 0) {
				if (pe.counter >= -pe.emitter._emit_particle_number) {
					pe.trigger = random
							.nextInt(-pe.emitter._emit_particle_number);
					pe.counter = 0;
				} else {
					pe.counter++;
				}

				if (pe.trigger == pe.counter) {
					createParticlesRegion(pe);
				}
			} else {
				pe.counter = -1;
				pe.trigger = 0;
				createParticlesRegion(pe);
			}
		}

	}

	private void createParticlesRegion(Emitters emitter) {

		for (int i = 0; i < emitter.emitter._emit_particle_number; i++) {
			double _degree = random.nextDouble() * 360.0;
			double _length = 0.0;

			switch (emitter.emitter._region_distribution) {
			case linear:
				_length = random.nextDouble();
				break;
			case gaussian:
				_length = FGMathsHelper.randomGaussian();
				break;
			case invgaussian:
				_length = 1.0 - FGMathsHelper.randomGaussian();
				break;
			}

			// 计算最远距离
			double _lengthMax = 0.0;
			double a = (emitter.emitter._region_x_max - emitter.emitter._region_x_min) / 2.0;
			double b = (emitter.emitter._region_y_max - emitter.emitter._region_y_min) / 2.0;
			double sin = Math.abs(Math.sin(Math.toRadians(_degree)));
			double cos = Math.abs(Math.cos(Math.toRadians(_degree)));

			switch (emitter.emitter._region_shape) {
			case rectangle: {
				if (sin < 0.00001) {
					_lengthMax = a;
				} else if (cos < 0.00001) {
					_lengthMax = b;
				} else {
					double l1 = a / cos;
					double l2 = b / sin;
					_lengthMax = Math.min(l1, l2);
				}
				break;
			}
			case ellipse: {
				_lengthMax = Math.sqrt(a * a * b * b
						/ (a * a * sin * sin + b * b * cos * cos));
				break;
			}
			case diamond: {
				_lengthMax = Math.sqrt(a * a * b * b * (sin * sin + cos * cos)
						/ ((a * sin + b * cos) * (a * sin + b * cos)));
				break;
			}
			}

			_length *= _lengthMax;

			// 计算坐标
			int x = (int) (FGMathsHelper.lengthdir_x((float) _length,
					(float) _degree) + a);
			int y = (int) (FGMathsHelper.lengthdir_y((float) _length,
					(float) _degree) + b);

			createParticle(emitter.emitter._emit_particle_type, x, y, 1);

		}
	}

	public void draw(Canvas c, int x, int y) {

		if (_drawOrder_old2new) {

			for (int i = 0; i < particlePool.size(); i++) {

				Particles p = particlePool.get(i);

				if (p.type._particleSprite != null) {
					p.convertor.setAlpha(p.alpha);
					p.convertor.setRotation(p.angle);
					p.convertor.setScale(p.type._scale_x * p.size,
							p.type._scale_y * p.size);

					p.type._particleSprite.setCurrentFrame((int) p.frame);
					p.type._particleSprite.draw(c, x + p.x, y + p.y,
							p.convertor, p.color);
				}

			}

		} else {

			for (int i = particlePool.size() - 1; i >= 0; i--) {

				Particles p = particlePool.get(i);

				if (p.type._particleSprite != null) {
					p.convertor.setAlpha(p.alpha);
					p.convertor.setRotation(p.angle);
					p.convertor.setScale(p.type._scale_x * p.size,
							p.type._scale_y * p.size);

					p.type._particleSprite.setCurrentFrame((int) p.frame);
					p.type._particleSprite.draw(c, x + p.x, y + p.y,
							p.convertor, p.color);
				}
			}

		}

	}

	public void updateAndDraw(Canvas c, int x, int y) {
		update();
		draw(c, x, y);
	}

	public void setDrawOrder(boolean old2new) {

		_drawOrder_old2new = old2new;

	}

	public void createParticle(FGParticleType type, int x, int y, int number) {

	}

	public void createParticle(FGParticleType type, int x, int y, int color,
			int number) {

		for (int i = 0; i < number; i++) {
			Particles p = new Particles();
			p.x = x;
			p.y = y;
			p.color = color;
			
			//初始化粒子
			
			
		}
	}

	public void clear() {

		particlePool.clear();
		particleEmitters.clear();
		particleAttractors.clear();
		particleDestroyers.clear();
		particleDeflectors.clear();
		particleChangers.clear();

	}

	public int count() {

		return particlePool.size();

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
		FGSpriteConvertor convertor = new FGSpriteConvertor();

		double frame = 0.0;

		double angle = 0.0;

		double size = 1.0;

		int x = 0;
		int y = 0;

		double speed = 0.0;
		double direction = 0.0;

		int color = Color.WHITE;

		double alpha = 0.0;

		int lifeTime = 0;
		int stayTime = 0;

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

	}

}
