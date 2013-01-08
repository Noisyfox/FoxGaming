package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleEmitter.EmitType;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleType.ColorType;

import android.graphics.Color;

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

	private List<Emitters> particleEmitters = new ArrayList<Emitters>();
	private List<FGParticleAttractor> particleAttractors = new ArrayList<FGParticleAttractor>();
	private List<FGParticleDestroyer> particleDestroyers = new ArrayList<FGParticleDestroyer>();
	private List<FGParticleDeflector> particleDeflectors = new ArrayList<FGParticleDeflector>();
	private List<FGParticleChanger> particleChangers = new ArrayList<FGParticleChanger>();
	private static Random random = new Random();

	private int maxParticleNumber = 200;
	private int aliveParticleCount = 0;

	private Particles particlePool_alive = null;
	private Particles particlePool_alive_last = null;
	private Particles particlePool_dead = new Particles();

	public FGParticleSystem() {
		setMaxParticleNumber(200);
	}

	public void setPosition(int x, int y) {

		_position_x = x;
		_position_y = y;

	}

	private final void removeParticle(Particles p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}

		if (p == particlePool_alive) {// 解链
			particlePool_alive = p.next;
		}
		if (p == particlePool_alive_last) {
			particlePool_alive_last = p.prev;
		}
		if (p.next != null) {
			p.next.prev = p.prev;
		}
		if (p.prev != null) {
			p.prev.next = p.next;
		}

		p.next = particlePool_dead;
		if (particlePool_dead != null) {
			particlePool_dead.prev = p;
		}
		particlePool_dead = p;

		p.prev = null;

		aliveParticleCount--;
	}

	private final Particles addParticle() {
		if (particlePool_dead != null) {
			aliveParticleCount++;

			Particles p = particlePool_dead;// 解链
			particlePool_dead = p.next;
			if (particlePool_dead != null) {
				particlePool_dead.prev = null;
			}

			p.prev = particlePool_alive_last;
			if (particlePool_alive == null) {
				particlePool_alive = p;
			}
			if (particlePool_alive_last != null) {
				particlePool_alive_last.next = p;
			}
			p.next = null;
			particlePool_alive_last = p;
			return p;
		}
		return null;
	}

	public void update() {

		// 先清除所有已经死亡的particle
		int _poolSize = aliveParticleCount;// 统计所有上一轮剩余的粒子数量
		Particles p = particlePool_alive;
		Particles p_next = p;
		for (int i = 0; i < _poolSize;) {
			p = p_next;
			p_next = p.next;
			if (p.stayTime > p.lifeTime) {
				_poolSize--;
				removeParticle(p);
				if (p.type._particleOnDeath_enabled) {
					createParticle(p.type._particleOnDeath_type, p.x, p.y,
							p.type._particleOnDeath_number);
				}
			} else {
				i++;
			}
		}

		// 处理所有现存粒子，仅处理该step之前生成的粒子
		p = particlePool_alive;
		p_next = p;
		for (int i = 0; i < _poolSize;) {
			p = p_next;
			p_next = p.next;
			p.stayTime++;
			// 判断是否应被破坏器破坏
			boolean needToChange = false;
			for (FGParticleDestroyer pd : particleDestroyers) {
				needToChange = pointInSpecifiedRegion(p.x, p.y,
						pd._region_x_min, pd._region_x_max, pd._region_y_min,
						pd._region_y_max, pd._region_shape);
				if (needToChange) {
					removeParticle(p);
					_poolSize--;
					break;
				}
			}
			if (needToChange) {
				continue;
			}

			// 如果没有破坏，则判断有无被转换
			needToChange = false;
			for (FGParticleChanger pc : particleChangers) {
				if (pointInSpecifiedRegion(p.x, p.y, pc._region_x_min,
						pc._region_x_max, pc._region_y_min, pc._region_y_max,
						pc._region_shape)
						&& p.type == pc._changeType_target) {

					switch (pc._changeKind) {
					case motion: {
						p.motionBaseType = pc._changeType_final;
						break;
					}
					case shape: {
						p.shapeBaseType = pc._changeType_final;
						break;
					}
					case all: {
						removeParticle(p);
						_poolSize--;
						createParticle(pc._changeType_final, p.x, p.y, 1);
						needToChange = true;
						break;
					}
					}

				}
			}
			if (needToChange) {
				continue;
			}

			needToChange = false;

			// 计算形状
			if (p.shapeBaseType._frameAni_enabled) {
				p.frame += p.shapeBaseType._frameAni_speed;
			} else {
				p.frame = 0;
			}

			p.size += p.shapeBaseType._size_incrementPerStep
					+ FGMathsHelper.random(-p.shapeBaseType._size_wiggle,
							p.shapeBaseType._size_wiggle);
			if (p.size < 0) {
				p.size = 0;
			}

			double k = (double) p.stayTime / (double) p.lifeTime;
			// 计算颜色
			switch (p.type._color_type) {
			case color2: {
				p.color = colorGradation(p.type._color_color1,
						p.type._color_color2, (float) k);
				break;
			}
			case color3: {
				if (k <= 0.5) {
					p.color = colorGradation(p.type._color_color1,
							p.type._color_color2, (float) (k * 2.0));
				} else {
					p.color = colorGradation(p.type._color_color2,
							p.type._color_color3, (float) (k * 2.0 - 1.0));
				}
				break;
			}
			}

			// 计算 alpha
			switch (p.type._alpha_type) {
			case alpha2: {
				p.alpha = k * (p.type._alpha_2 - p.type._alpha_1)
						+ p.type._alpha_1;
				break;
			}
			case alpha3: {
				if (k <= 0.5) {
					p.alpha = k * 2.0 * (p.type._alpha_2 - p.type._alpha_1)
							+ p.type._alpha_1;
				} else {
					p.alpha = (k * 2.0 + 1.0)
							* (p.type._alpha_3 - p.type._alpha_2)
							+ p.type._alpha_2;
				}
				break;
			}
			}

			float x = p.x;
			float y = p.y;
			double speedx = FGMathsHelper.lengthdir_x((float) p.speed,
					(float) p.direction);
			double speedy = -FGMathsHelper.lengthdir_y((float) p.speed,
					(float) p.direction);

			// 计算吸引器
			for (FGParticleAttractor fa : particleAttractors) {
				float distance = FGMathsHelper.point_distance(p.x, p.y,
						fa._position_x, fa._position_y);
				if (distance > fa._force_distance_max)
					continue;

				// 计算力大小
				double force = fa._force_force;
				switch (fa._force_kind) {
				case linear: {
					force *= 1.0 - distance / fa._force_distance_max;
					break;
				}
				case quadratic: {
					force *= 1.0 - (distance / fa._force_distance_max)
							* (distance / fa._force_distance_max);
					break;
				}
				}
				// 计算力的x y分量
				float dir = FGMathsHelper.point_direction(p.x, p.y,
						fa._position_x, fa._position_y);
				float fx = FGMathsHelper.lengthdir_x((float) force, dir);
				float fy = -FGMathsHelper.lengthdir_y((float) force, dir);

				// 应用力
				if (fa._force_additive) {
					speedx += fx;
					speedy += fy;
				} else {
					x += fx;
					y += fy;
				}
			}

			// 计算重力
			speedx += FGMathsHelper.lengthdir_x(
					(float) p.motionBaseType._gravity_amount,
					(float) p.motionBaseType._gravity_direction);
			speedy += -FGMathsHelper.lengthdir_y(
					(float) p.motionBaseType._gravity_amount,
					(float) p.motionBaseType._gravity_direction);

			// 计算速度
			double direction = Math.toDegrees(Math.atan2(-speedy, speedx));

			if (Math.sqrt(speedx * speedx + speedy * speedy)
					+ p.motionBaseType._speed_incrementPerStep <= 0) {
				speedx = 0;
				speedy = 0;
			} else {
				speedx += FGMathsHelper.lengthdir_x(
						(float) p.motionBaseType._speed_incrementPerStep,
						(float) direction);
				speedy += -FGMathsHelper.lengthdir_y(
						(float) p.motionBaseType._speed_incrementPerStep,
						(float) direction);
				direction = Math.toDegrees(Math.atan2(-speedy, speedx));
			}

			// 计算角度
			direction = direction
					+ p.motionBaseType._direction_incrementPerStep;
			float speed = (float) Math.sqrt(speedx * speedx + speedy * speedy);

			if (speedx != 0 || speedy != 0) {
				speedx = FGMathsHelper.lengthdir_x(speed, (float) direction);
				speedy = -FGMathsHelper.lengthdir_y(speed, (float) direction);
			}

			// 计算位置
			x += speedx;
			y += speedy;

			// 计算偏转器

			// 最后完成所有计算
			direction += FGMathsHelper.random(
					-p.shapeBaseType._direction_wiggle,
					p.shapeBaseType._direction_wiggle);
			p.x = (int) x;
			p.y = (int) y;
			p.speed = speed;
			// 计算图像旋转角度
			if (p.shapeBaseType._orientation_relative) {
				p.angle = p.baseAngle
						+ direction
						+ FGMathsHelper.random(
								-p.shapeBaseType._orientation_wiggle,
								p.shapeBaseType._orientation_wiggle);
			} else {
				p.angle += p.shapeBaseType._orientation_incrementPerStep
						+ FGMathsHelper.random(
								-p.shapeBaseType._orientation_wiggle,
								p.shapeBaseType._orientation_wiggle);
			}
			p.angle = FGMathsHelper.degreeIn360((float) p.angle);
			p.direction = FGMathsHelper.degreeIn360((float) direction);

			// 发射每步都会生成的粒子
			if (p.type._particleOnStep_enabled) {
				if (p.type._particleOnStep_number < 0) {

					if (p.counter >= -p.type._particleOnStep_number) {
						p.trigger = random
								.nextInt(-p.type._particleOnStep_number);
						p.counter = 0;
					} else {
						p.counter++;
					}

					if (p.trigger == p.counter) {
						this.createParticle(p.type._particleOnStep_type, p.x,
								p.y, 1);
					}
				} else {
					p.counter = -1;
					p.trigger = 0;
					this.createParticle(p.type._particleOnStep_type, p.x, p.y,
							p.type._particleOnStep_number);
				}
			}

			// 循环变量加1
			i++;
		}

		// 最后由发射器发射粒子
		for (int i = 0; i < particleEmitters.size();) {
			Emitters pe = particleEmitters.get(i);

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
					if (pe.emitter.emitType == EmitType.burst) {
						particleEmitters.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			} else {
				pe.counter = -1;
				pe.trigger = 0;
				createParticlesRegion(pe);
				if (pe.emitter.emitType == EmitType.burst) {
					particleEmitters.remove(i);
				} else {
					i++;
				}
			}

		}

	}

	private int colorGradation(int color1, int color2, float k) {
		int r = (int) ((float) (Color.red(color2) - Color.red(color1)) * k + (float) Color
				.red(color1));

		int g = (int) ((float) (Color.green(color2) - Color.green(color1)) * k + (float) Color
				.green(color1));

		int b = (int) ((float) (Color.blue(color2) - Color.blue(color1)) * k + (float) Color
				.blue(color1));

		return Color.rgb(r, g, b);

	}

	private boolean pointInSpecifiedRegion(float x, float y, int minX,
			int minY, int maxX, int maxY, FGParticleRegionShape shape) {
		boolean isIn = false;
		if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
			switch (shape) {
			case rectangle:
				isIn = true;
				break;
			case ellipse:
				isIn = pointInEllipse(x - (float) (maxX + minX) / 2f, y
						- (float) (maxY + minY) / 2f,
						(float) (maxX - minX) / 2f, (float) (maxY - minY) / 2f);
				break;
			case diamond:
				isIn = pointInDiamond(x - (float) (maxX + minX) / 2f, y
						- (float) (maxY + minY) / 2f,
						(float) (maxX - minX) / 2f, (float) (maxY - minY) / 2f);
				break;
			}
		}
		return isIn;
	}

	// 判断指定点是否在一个以a为长半轴长，b为短半轴长，中心在原点的菱形内部
	private boolean pointInDiamond(float x, float y, float a, float b) {
		x = Math.abs(x);
		y = Math.abs(y);
		return a != 0 && b != 0 && a * y + b * x <= a * b;
	}

	// 判断指定点是否在一个以a为长半轴长，b为短半轴长，圆心在原点的椭圆形内部
	private boolean pointInEllipse(float x, float y, float a, float b) {
		return a != 0 && b != 0 && x * x / a / a + y * y / b / b <= 1f;
	}

	private void createParticlesRegion(Emitters emitter) {

		int number = emitter.emitter._emit_particle_number;
		if (number < 0) {
			number = 1;
		}

		for (int i = 0; i < number; i++) {
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
					(float) _degree) + (emitter.emitter._region_x_max + emitter.emitter._region_x_min) / 2.0);
			int y = (int) (FGMathsHelper.lengthdir_y((float) _length,
					(float) _degree) + (emitter.emitter._region_y_max + emitter.emitter._region_y_min) / 2.0);

			createParticle(emitter.emitter._emit_particle_type, x, y, 1);

		}
	}

	public void draw() {
		Particles p;
		if (_drawOrder_old2new) {
			p = particlePool_alive;
			for (int i = 0; i < aliveParticleCount; i++) {

				if (p.shapeBaseType._particleSprite != null) {
					p.convertor.setAlpha(p.alpha);
					p.convertor.setRotation(p.angle);
					p.convertor.setScale(p.shapeBaseType._scale_x * p.size,
							p.shapeBaseType._scale_y * p.size);

					p.shapeBaseType._particleSprite
							.setCurrentFrame((int) p.frame);
					p.shapeBaseType._particleSprite.draw(_position_x + p.x,
							_position_y + p.y, p.convertor, p.color);
				}
				p = p.next;
			}

		} else {
			p = particlePool_alive_last;
			for (int i = aliveParticleCount - 1; i >= 0; i--) {

				if (p.shapeBaseType._particleSprite != null) {
					p.convertor.setAlpha(p.alpha);
					p.convertor.setRotation(p.angle);
					p.convertor.setScale(p.shapeBaseType._scale_x * p.size,
							p.shapeBaseType._scale_y * p.size);

					p.shapeBaseType._particleSprite
							.setCurrentFrame((int) p.frame);
					p.shapeBaseType._particleSprite.draw(_position_x + p.x,
							_position_y + p.y, p.convertor, p.color);
				}
				p = p.prev;
			}

		}

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

		for (int i = 0; i < number; i++) {
			Particles p = addParticle();
			if (p == null) {
				continue;
			}

			p.type = type;
			p.shapeBaseType = type;
			p.motionBaseType = type;
			p.x = x;
			p.y = y;
			p.color = color;
			p.counter = -1;
			p.trigger = 0;
			p.stayTime = 0;

			// 初始化粒子
			if (p.shapeBaseType._frameAni_enabled
					&& p.shapeBaseType._frameAni_startWithRandomFrame) {
				p.frame = random.nextInt(p.shapeBaseType._particleSprite
						.getFrameCount());
			}

			p.size = FGMathsHelper.random(p.shapeBaseType._size_min,
					p.shapeBaseType._size_max);

			p.baseAngle = FGMathsHelper.random(
					p.shapeBaseType._orientation_angle_min,
					p.shapeBaseType._orientation_angle_max);
			p.angle = p.baseAngle;

			if ((type._color_type == ColorType.color1 && type._color_color1 != Color.WHITE)
					|| type._color_type == ColorType.color2
					|| type._color_type == ColorType.color3) {
				p.color = type._color_color1;
			} else if (type._color_type == ColorType.RGB) {
				p.color = Color.rgb(FGMathsHelper.random(type._color_RGB_R_min,
						type._color_RGB_R_max), FGMathsHelper.random(
						type._color_RGB_G_min, type._color_RGB_G_max),
						FGMathsHelper.random(type._color_RGB_B_min,
								type._color_RGB_B_max));
			} else if (type._color_type == ColorType.HSV) {
				p.color = Color.HSVToColor(new float[] {
						(float) (FGMathsHelper.random(type._color_HSV_H_min,
								type._color_HSV_H_max)),
						(float) FGMathsHelper.random(type._color_HSV_S_min,
								type._color_HSV_S_max),
						(float) (FGMathsHelper.random(type._color_HSV_V_min,
								type._color_HSV_V_max)) });
			}

			p.alpha = type._alpha_1;

			p.lifeTime = FGMathsHelper.random(type._lifeTime_min,
					type._lifeTime_max);

			p.speed = FGMathsHelper.random(p.motionBaseType._speed_min,
					p.motionBaseType._speed_max);

			p.direction = FGMathsHelper.random(p.motionBaseType._direction_min,
					p.motionBaseType._direction_max);

		}

	}

	public void clear() {

		aliveParticleCount = 0;// 确保链表初始化
		Particles p = particlePool_alive;
		Particles p_next;
		while (p != null) {
			p_next = p.next;
			removeParticle(p);
			p = p_next;
		}

		particleEmitters.clear();
		particleAttractors.clear();
		particleDestroyers.clear();
		particleDeflectors.clear();
		particleChangers.clear();

	}

	public int count() {

		return aliveParticleCount;

	}

	public void bindParticleEmitter(FGParticleEmitter emitter) {
		if (emitter != null) {
			Emitters e = new Emitters();
			e.emitter = emitter;
			if (!particleEmitters.contains(e)) {
				particleEmitters.add(e);
			}
		}
	}

	public void bindParticleAttractor(FGParticleAttractor attractor) {
		if (attractor != null && !particleAttractors.contains(attractor)) {
			particleAttractors.add(attractor);
		}
	}

	public void bindPraticleDestroyer(FGParticleDestroyer destroyer) {
		if (destroyer != null && !particleDestroyers.contains(destroyer)) {
			particleDestroyers.add(destroyer);
		}
	}

	public void bindPraticleDeflector(FGParticleDeflector deflector) {
		if (deflector != null && !particleDeflectors.contains(deflector)) {
			particleDeflectors.add(deflector);
		}
	}

	public void bindParticleChanger(FGParticleChanger changer) {
		if (changer != null && !particleChangers.contains(changer)) {
			particleChangers.add(changer);
		}
	}

	public void unbindParticleEmitter(FGParticleEmitter emitter) {
		Emitters e = new Emitters();
		e.emitter = emitter;
		particleEmitters.remove(e);
	}

	public void unbindParticleAttractor(FGParticleAttractor attractor) {
		particleAttractors.remove(attractor);
	}

	public void unbindPraticleDestroyer(FGParticleDestroyer destroyer) {
		particleDestroyers.remove(destroyer);
	}

	public void unbindPraticleDeflector(FGParticleDeflector deflector) {
		particleDeflectors.remove(deflector);
	}

	public void unbindParticleChanger(FGParticleChanger changer) {
		particleChangers.remove(changer);
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

		maxParticleNumber = number;

		int particleCount = 0;

		Particles p = particlePool_alive;
		particlePool_alive_last = p;

		while (p != null && particleCount < maxParticleNumber) {
			particleCount++;
			particlePool_alive_last = p;
			p = p.next;
		}
		aliveParticleCount = particleCount;

		if (p != null) {
			p.next = null;
			particlePool_dead = null;
		} else if (particleCount == maxParticleNumber) {
			particlePool_dead = null;
		} else if (particlePool_dead == null) {
			particlePool_dead = new Particles();
			particleCount++;
		}

		p = particlePool_dead;
		for (int i = particleCount + 1; i <= maxParticleNumber; i++) {
			if (p.next == null) {
				p.next = new Particles();
				p.next.prev = p.next;
			}
			p = p.next;
		}

		if (p != null) {
			p.prev.next = null;
		}

		// Particles[] p = particlePool;
		// particlePool = new Particles[maxParticleNumber];
		// if (p != null) {
		// for (int i = 0; i < Math.min(maxParticleNumber, p.length); i++) {
		// particlePool[i] = p[i];
		// }
		// for (int i = Math.min(maxParticleNumber, p.length) - 1; i <
		// maxParticleNumber; i++) {
		// particlePool[i] = new Particles();
		// }
		// } else {
		// for (int i = 0; i < maxParticleNumber; i++) {
		// particlePool[i] = new Particles();
		// }
		// }
		//
		// if (aliveParticlePoint > maxParticleNumber - 1) {
		// aliveParticlePoint = maxParticleNumber - 1;
		// }
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
