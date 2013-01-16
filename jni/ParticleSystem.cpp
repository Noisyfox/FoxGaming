/*
 * ParticleSystem.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#include <stdlib.h>
#include <math.h>

#include <android/log.h>

#include <ParticleSystem.h>
#include <ArrayList.h>
#include <Color.h>
#include <MathsHelper.h>

#define  LOG_TAG    "libfoxgaming_particle_system"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
//检查是否成功申请内存
#undef Asert
#define Asert(par,...) if((par)==NULL) {\
	LOGE(__VA_ARGS__);return NULL;}

typedef enum {
	Tcolor1, Tcolor2, Tcolor3, TRGB, THSV
} ColorType;

typedef enum {
	Talpha1, Talpha2, Talpha3
} AlphaType;

typedef enum {
	Tbrust, Tstream
} EmitType;

typedef struct _ParticleType {
	int _sprite_frameCount;

	bool _frameAni_enabled;
	double _frameAni_speed;
	bool _frameAni_startWithRandomFrame;

	double _size_min;
	double _size_max;
	double _size_incrementPerStep;
	double _size_wiggle;

	float _scale_x;
	float _scale_y;

	float _orientation_angle_min;
	float _orientation_angle_max;
	double _orientation_incrementPerStep;
	double _orientation_wiggle;
	bool _orientation_relative;

	ColorType _color_type;
	int _color_color1;
	int _color_color2;
	int _color_color3;
	char _color_RGB_R_min;
	char _color_RGB_R_max;
	char _color_RGB_G_min;
	char _color_RGB_G_max;
	char _color_RGB_B_min;
	char _color_RGB_B_max;
	char _color_HSV_H_min;
	char _color_HSV_H_max;
	double _color_HSV_S_min;
	double _color_HSV_S_max;
	double _color_HSV_V_min;
	double _color_HSV_V_max;

	AlphaType _alpha_type;
	double _alpha_1;
	double _alpha_2;
	double _alpha_3;

	int _lifeTime_min;
	int _lifeTime_max;

	bool _particleOnStep_enabled;
	_ParticleType *_particleOnStep_type;
	int _particleOnStep_number;

	bool _particleOnDeath_enabled;
	_ParticleType *_particleOnDeath_type;
	int _particleOnDeath_number;

	double _speed_min;
	double _speed_max;
	double _speed_incrementPerStep;
	double _speed_wiggle;

	double _direction_min;
	double _direction_max;
	double _direction_incrementPerStep;
	double _direction_wiggle;

	double _gravity_amount;
	double _gravity_direction;
} ParticleType;

typedef struct _ParticleAttractor {
	int _position_x;
	int _position_y;

	int _force_kind;
	float _force_force;
	float _force_distance_max;
	bool _force_additive;
} ParticleAttractor;

typedef struct _ParticleChanger {
	int _region_x_min;
	int _region_x_max;
	int _region_y_min;
	int _region_y_max;
	int _region_shape;

	ParticleType * _changeType_target;
	ParticleType * _changeType_final;

	int _changeKind;
} ParticleChanger;

typedef struct _ParticleDestroyer {
	int _region_x_min;
	int _region_x_max;
	int _region_y_min;
	int _region_y_max;
	int _region_shape;
} ParticleDestroyer;

typedef struct _ParticleDeflector {

} ParticleDeflector;

typedef struct _ParticleEmitter {
	int _region_x_min;
	int _region_x_max;
	int _region_y_min;
	int _region_y_max;
	int _region_shape;
	int _region_distribution;
	EmitType emitType;
	ParticleType * _emit_particle_type;
	int _emit_particle_number;
} ParticleEmitter;

typedef struct _Particles {
	ParticleType * type;
	ParticleType * motionBaseType;
	ParticleType * shapeBaseType;

	double frame;

	double angle;
	double baseAngle;

	double size;

	int x;
	int y;

	double speed;
	double direction;

	int color;

	double alpha;

	int lifeTime;
	int stayTime;

	int counter;
	int trigger;

	struct _Particles *next;
	struct _Particles *prev;
} Particles;

typedef struct _Emitters {
	ParticleEmitter * emitter;
	int counter;
	int trigger;
} Emitters;

typedef struct _ParticleSystem {
	ArrayList* particleEmitters;
	ArrayList* particleAttractors;
	ArrayList* particleDestroyers;
	ArrayList* particleDeflectors;
	ArrayList* particleChangers;

	int maxParticleNumber;
	int aliveParticleCount;
	Particles* particlePool_alive;
	Particles* particlePool_alive_last;
	Particles* particlePool_dead;
} ParticleSystem;

ArrayList * particleSystemList = NULL;
ArrayList * particleAttractorList = NULL;
ArrayList * particleChangerList = NULL;
ArrayList * particleDestoryerList = NULL;
ArrayList * particleEmitterList = NULL;
ArrayList * particleTypeList = NULL;

//----------------------------------------------------------------------------------------------------------------------
//内部函数
bool removeParticle(ParticleSystem* particleSystem, Particles* particle) {

	if (particle == particleSystem->particlePool_alive) {
		particleSystem->particlePool_alive = particle->next;
	}
	if (particle == particleSystem->particlePool_alive_last) {
		particleSystem->particlePool_alive_last = particle->prev;
	}
	if (particle->next != NULL) {
		(particle->next)->prev = particle->prev;
	}
	if (particle->prev != NULL) {
		(particle->prev)->next = particle->next;
	}

	particle->next = particleSystem->particlePool_dead;
	if (particleSystem->particlePool_dead != NULL) {
		(particleSystem->particlePool_dead)->prev = particle;
	}
	particleSystem->particlePool_dead = particle;

	particleSystem->aliveParticleCount--;

	return true;
}

Particles* obtainParticle(ParticleSystem* particleSystem) {
	if (particleSystem->particlePool_dead != NULL) {
		particleSystem->aliveParticleCount++;

		Particles* p = particleSystem->particlePool_dead;
		particleSystem->particlePool_dead = p->next;
		if (particleSystem->particlePool_dead != NULL) {
			(particleSystem->particlePool_dead)->prev = NULL;
		}

		p->prev = particleSystem->particlePool_alive_last;
		if (particleSystem->particlePool_alive == NULL) {
			particleSystem->particlePool_alive = p;
		}
		if (particleSystem->particlePool_alive_last != NULL) {
			(particleSystem->particlePool_alive_last)->next = p;
		}
		p->next = NULL;
		particleSystem->particlePool_alive_last = p;

		return p;
	}
	LOGI("Particle pool is empty!");

	return NULL;
}

bool createParticleColor(ParticleSystem *particleSystem, ParticleType *type,
		int x, int y, int color, int number) {

	bool fullySuccess = true;

	for (int i = 0; i < number; i++) {
		Particles *p;
		if ((p = obtainParticle(particleSystem)) == NULL) {
			fullySuccess = false;
			continue;
		}

		p->type = type;
		p->shapeBaseType = type;
		p->motionBaseType = type;
		p->x = x;
		p->y = y;
		p->color = color;
		p->counter = -1;
		p->trigger = 0;
		p->stayTime = 0;

		// 初始化粒子
		if ((p->shapeBaseType)->_frameAni_enabled
				&& (p->shapeBaseType)->_frameAni_startWithRandomFrame) {
			p->frame = randomInt((p->shapeBaseType)->_sprite_frameCount);
		}

		p->size = randomDoubleR((p->shapeBaseType)->_size_min,
				(p->shapeBaseType)->_size_max);

		p->baseAngle = randomDoubleR((p->shapeBaseType)->_orientation_angle_min,
				(p->shapeBaseType)->_orientation_angle_max);
		p->angle = p->baseAngle;

		if ((type->_color_type == Tcolor1 && type->_color_color1 != WHITE)
				|| type->_color_type == Tcolor2
				|| type->_color_type == Tcolor3) {
			p->color = type->_color_color1;
		} else if (type->_color_type == TRGB) {
			p->color = rgb(
					randomIntR(type->_color_RGB_R_min, type->_color_RGB_R_max),
					randomIntR(type->_color_RGB_G_min, type->_color_RGB_G_max),
					randomIntR(type->_color_RGB_B_min, type->_color_RGB_B_max));
		} else if (type->_color_type == THSV) {
			float hsv[] = { randomDoubleR(type->_color_HSV_H_min,
					type->_color_HSV_H_max), randomDoubleR(
					type->_color_HSV_S_min, type->_color_HSV_S_max),
					randomDoubleR(type->_color_HSV_V_min,
							type->_color_HSV_V_max) };
			p->color = HSVToColor(hsv);
		}

		p->alpha = type->_alpha_1;

		p->lifeTime = randomIntR(type->_lifeTime_min, type->_lifeTime_max);

		p->speed = randomDoubleR((p->motionBaseType)->_speed_min,
				(p->motionBaseType)->_speed_max);

		p->direction = randomDoubleR((p->motionBaseType)->_direction_min,
				(p->motionBaseType)->_direction_max);

	}

	return fullySuccess;
}

bool createParticle(ParticleSystem *particleSystem, ParticleType *type, int x,
		int y, int number) {
	return createParticleColor(particleSystem, type, x, y, WHITE, number);
}

void freeParticles(Particles** startFrom) {
	Particles** curr = startFrom;
	while (*curr) {
		Particles * entry = *curr;
		*curr = entry->next;
		free(entry);
	}
}

void freeArrayList(ArrayList *list) {
	if (list != NULL) {
		for (int i = 0; i < list->index; i++) {
			free(list->data[i]);
		}
		destroyArrayList(list);
	}
}

void freeParticleSystem(ParticleSystem* particleSystem) {
	if (particleSystem == NULL)
		return;

	if (particleSystemList != NULL) {
		removeElement(particleSystemList, particleSystem);
	}

	freeArrayList(particleSystem->particleAttractors);
	freeArrayList(particleSystem->particleChangers);
	freeArrayList(particleSystem->particleDestroyers);
	freeArrayList(particleSystem->particleDeflectors);
	freeArrayList(particleSystem->particleEmitters);

	freeParticles(&particleSystem->particlePool_alive);
	freeParticles(&particleSystem->particlePool_dead);
}

bool setMaxParticleNumber(ParticleSystem* particleSystem, int number) {
	particleSystem->maxParticleNumber = number;

	int particleCount = 0;

	Particles* p = particleSystem->particlePool_alive;
	particleSystem->particlePool_alive_last = p;

	while (p != NULL && particleCount < particleSystem->maxParticleNumber) {
		particleCount++;
		particleSystem->particlePool_alive_last = p;
		p = p->next;
	}
	particleSystem->aliveParticleCount = particleCount;

	if (p != NULL) {
		freeParticles(&p->next);
		freeParticles(&particleSystem->particlePool_dead);
	} else if (particleCount == particleSystem->maxParticleNumber) {
		freeParticles(&particleSystem->particlePool_dead);
	} else if (particleSystem->particlePool_dead == NULL) {
		if ((particleSystem->particlePool_dead = (Particles*) malloc(
				sizeof(Particles))) == NULL) {
			LOGE("Can't malloc new particle!");
			return JNI_FALSE;
		}
		particleCount++;
	}

	p = particleSystem->particlePool_dead;
	for (int i = particleCount + 1; i <= particleSystem->maxParticleNumber;
			i++) {
		if (p->next == NULL) {
			if ((p->next = (Particles*) malloc(sizeof(Particles))) == NULL) {
				LOGE("Can't malloc new particle!");
				return JNI_FALSE;
			}
			(p->next)->prev = p;
		}
		p = p->next;
	}

	if (p != NULL) {
		freeParticles(&p->prev);
	}

	return true;
}

// 判断指定点是否在一个以a为长半轴长，b为短半轴长，中心在原点的菱形内部
bool pointInDiamond(float x, float y, float a, float b) {
	return a != 0 && b != 0 && a * fabs(y) + b * fabs(x) <= a * b;
}

// 判断指定点是否在一个以a为长半轴长，b为短半轴长，圆心在原点的椭圆形内部
bool pointInEllipse(float x, float y, float a, float b) {
	return a != 0 && b != 0 && x * x / a / a + y * y / b / b <= 1;
}

bool pointInSpecifiedRegion(float x, float y, int minX, int minY, int maxX,
		int maxY, int shape) {
	bool isIn = false;
	if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
		switch (shape) {
		case org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE:
			isIn = true;
			break;
		case org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_ELLIPSE:
			isIn = pointInEllipse(x - (float) (maxX + minX) / 2,
					y - (float) (maxY + minY) / 2, (float) (maxX - minX) / 2,
					(float) (maxY - minY) / 2);
			break;
		case org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_DIAMOND:
			isIn = pointInDiamond(x - (float) (maxX + minX) / 2,
					y - (float) (maxY + minY) / 2, (float) (maxX - minX) / 2,
					(float) (maxY - minY) / 2);
			break;
		default:
			break;
		}
	}
	return isIn;
}

//----------------------------------------------------------------------------------------------------------------------
//ParticleSystem
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleSystemNative(
		JNIEnv *env, jclass clazz) {
	if (particleSystemList == NULL) {
		LOGI("Create new particle system list.");
		Asert(particleSystemList = createArrayList(NULL, NULL),
				"Failed to create particle system list!");
	}

	ParticleSystem *ps;
	Asert(ps = (ParticleSystem *) malloc(sizeof(ParticleSystem)),
			"Failed to malloc new particle system!");

	//初始化
	if ((ps->particlePool_dead = (Particles*) malloc(sizeof(Particles))) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if ((ps->particleAttractors = createArrayList(NULL, NULL)) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if ((ps->particleChangers = createArrayList(NULL, NULL)) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if ((ps->particleDestroyers = createArrayList(NULL, NULL)) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if ((ps->particleDeflectors = createArrayList(NULL, NULL)) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if ((ps->particleEmitters = createArrayList(NULL, NULL)) == NULL) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	if (!setMaxParticleNumber(ps, 200)) {
		LOGE("Failed to initialize particle system!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	//加入全局列表
	if (!addElement(particleSystemList, (void*) ps)) {
		LOGE("Failed to add particle system to list!");
		freeParticleSystem(ps);
		free(ps);
		return NULL;
	}

	LOGI("Create particle system success! id:%u", (long)ps);

	return (jlong) (long) ps;

}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSupdateNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {

	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	int _poolSize = ps->aliveParticleCount;
	Particles *p = ps->particlePool_alive;
	Particles *p_next = p;
	bool needToChange = false;
	for (int i = 0; i < _poolSize;) {
		p = p_next;
		p_next = p->next;

		// 先判断该粒子是否还在存活期内
		if (p->stayTime > p->lifeTime) {
			_poolSize--;
			removeParticle(ps, p);
			if ((p->type)->_particleOnDeath_enabled) {
				createParticle(ps, (p->type)->_particleOnDeath_type, p->x, p->y,
						(p->type)->_particleOnDeath_number);
			}
			continue;
		}

		// 处理所有现存粒子，仅处理该step之前生成的粒子
		p->stayTime++;
		// 判断是否应被破坏器破坏
		needToChange = false;
		for (int i = 0; i < (ps->particleDestroyers)->index; i++) {
			ParticleDestroyer* pd =
					(ParticleDestroyer*) (ps->particleDestroyers)->data[i];
			if (pointInSpecifiedRegion(p->x, p->y, pd->_region_x_min,
					pd->_region_x_max, pd->_region_y_min, pd->_region_y_max,
					pd->_region_shape)) {
				needToChange = true;
				removeParticle(ps, p);
				_poolSize--;
				break;
			}
		}
		if (needToChange) {
			continue;
		}

		// 如果没有破坏，则判断有无被转换
		needToChange = false;

	}

	return JNI_TRUE;
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlongArray arg) {

	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;

	return -1;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSclearNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {

	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	Particles *p = ps->particlePool_alive;
	Particles *p_next;
	while (p != NULL) {
		p_next = p->next;
		removeParticle(ps, p);
		p = p_next;
	}
	ps->aliveParticleCount = 0;

	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScountNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {

	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;

	return ps->aliveParticleCount;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSsetMaxParticleNumberNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jint number) {

	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;

	return setMaxParticleNumber(ps, number);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleEmitterNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong emitter) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleAttractorNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong attractor) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindPraticleDestroyerNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong destroyer) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindPraticleDeflectorNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong deflector) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleChangerNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong changer) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleEmitterNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong emitter) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleAttractorNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong attractor) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindPraticleDestroyerNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong destroyer) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindPraticleDeflectorNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong deflector) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleChangerNative(
		JNIEnv * env, jclass clazz, jlong particleSystem, jlong changer) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleSystemNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	return removeElement(particleSystemList, (void*) ps);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSfinalizeParticleSystemNative(
		JNIEnv *env, jclass clazz) {
	//回收内存
	if (particleSystemList != NULL) {
		for (int i = 0; i < particleSystemList->index; i++) {
			ParticleSystem *ps = (ParticleSystem*) particleSystemList->data[i];
			freeParticleSystem(ps);
			free(ps);
		}
		destroyArrayList(particleSystemList);
	}
	return JNI_TRUE;
}
//----------------------------------------------------------------------------------------------------------------------
//ParticleType
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateParticleTypeNative(
		JNIEnv *env, jclass clazz) {
	if (particleTypeList == NULL) {
		LOGI("Create new particle type list.");
		Asert(particleTypeList = createArrayList(NULL, NULL),
				"Failed to create particle type list!");
	}

	ParticleType *pt;
	Asert(pt = (ParticleType *)malloc(sizeof(ParticleType)),
			"Failed to malloc new particle type!");

	if (!addElement(particleTypeList, (void*) pt)) {
		LOGE("Failed to add particle type to list!");
		free(pt);
		return NULL;
	}

	//初始化
	pt->_sprite_frameCount = 0;

	pt->_frameAni_enabled = false;
	pt->_frameAni_speed = 0.0;
	pt->_frameAni_startWithRandomFrame = false;

	pt->_size_min = 1.0;
	pt->_size_max = 1.0;
	pt->_size_incrementPerStep = 0.0;
	pt->_size_wiggle = 0.0;

	pt->_scale_x = 1;
	pt->_scale_y = 1;

	pt->_orientation_angle_min = 0;
	pt->_orientation_angle_max = 0;
	pt->_orientation_incrementPerStep = 0.0;
	pt->_orientation_wiggle = 0.0;
	pt->_orientation_relative = false;

	pt->_color_type = Tcolor1;
	pt->_color_color1 = WHITE;
	pt->_color_color2 = WHITE;
	pt->_color_color3 = WHITE;
	pt->_color_RGB_R_min = -1;
	pt->_color_RGB_R_max = -1;
	pt->_color_RGB_G_min = -1;
	pt->_color_RGB_G_max = -1;
	pt->_color_RGB_B_min = -1;
	pt->_color_RGB_B_max = -1;
	pt->_color_HSV_H_min = -1;
	pt->_color_HSV_H_max = -1;
	pt->_color_HSV_S_min = -1;
	pt->_color_HSV_S_max = -1;
	pt->_color_HSV_V_min = -1;
	pt->_color_HSV_V_max = -1;

	pt->_alpha_type = Talpha1;
	pt->_alpha_1 = 1.0;
	pt->_alpha_2 = 1.0;
	pt->_alpha_3 = 1.0;

	pt->_lifeTime_min = 100;
	pt->_lifeTime_max = 100;

	pt->_particleOnStep_enabled = false;
	pt->_particleOnStep_type = NULL;
	pt->_particleOnStep_number = 0;

	pt->_particleOnDeath_enabled = false;
	pt->_particleOnDeath_type = NULL;
	pt->_particleOnDeath_number = 0;

	pt->_speed_min = 0.0;
	pt->_speed_max = 0.0;
	pt->_speed_incrementPerStep = 0.0;
	pt->_speed_wiggle = 0.0;

	pt->_direction_min = 0.0;
	pt->_direction_max = 0.0;
	pt->_direction_incrementPerStep = 0.0;
	pt->_direction_wiggle = 0.0;

	pt->_gravity_amount = 0.0;
	pt->_gravity_direction = 0.0;

	LOGI("Create particle type success! id:%u", (long)pt);

	return (jlong) (long) pt;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpriteParameterNative(
		JNIEnv * env, jclass clazz, jlong particleType, jint frameCount) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_sprite_frameCount = frameCount;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpriteFrameAnimationNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enableFrameAni,
		jdouble speed, jboolean startWithRandomFrame) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_frameAni_enabled = enableFrameAni;
	pt->_frameAni_speed = speed;
	pt->_frameAni_startWithRandomFrame = startWithRandomFrame;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSizeNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minSize,
		jdouble maxSize, jdouble incrementPerStep, jdouble wiggle) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_size_min = minSize;
	pt->_size_max = maxSize;
	pt->_size_incrementPerStep = incrementPerStep;
	pt->_size_wiggle = wiggle;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetScaleNative(
		JNIEnv *env, jclass clazz, jlong particleType, jfloat xScale,
		jfloat yScale) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_scale_x = xScale;
	pt->_scale_y = yScale;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetOrientationNative(
		JNIEnv *env, jclass clazz, jlong particleType, jfloat minAngle,
		jfloat maxAngle, jdouble incrementPerStep, jdouble wiggle,
		jboolean relative) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_orientation_angle_min = minAngle;
	pt->_orientation_angle_max = maxAngle;
	pt->_orientation_incrementPerStep = incrementPerStep;
	pt->_orientation_wiggle = wiggle;
	pt->_orientation_relative = relative;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JI(
		JNIEnv *env, jclass clazz, jlong particleType, jint color) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_color_type = Tcolor1;
	pt->_color_color1 = color;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JII(
		JNIEnv *env, jclass clazz, jlong particleType, jint color1,
		jint color2) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_color_type = Tcolor2;
	pt->_color_color1 = color1;
	pt->_color_color2 = color2;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JIII(
		JNIEnv *env, jclass clazz, jlong particleType, jint color1, jint color2,
		jint color3) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_color_type = Tcolor3;
	pt->_color_color1 = color1;
	pt->_color_color2 = color2;
	pt->_color_color3 = color3;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorRGBNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint minR, jint minG,
		jint minB, jint maxR, jint maxG, jint maxB) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_color_type = TRGB;
	pt->_color_RGB_R_min = minR;
	pt->_color_RGB_R_max = maxR;
	pt->_color_RGB_G_min = minG;
	pt->_color_RGB_G_max = maxG;
	pt->_color_RGB_B_min = minB;
	pt->_color_RGB_B_max = maxB;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorHSVNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint minH, jdouble minS,
		jdouble minV, jint maxH, jdouble maxS, jdouble maxV) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_color_type = THSV;
	pt->_color_HSV_H_min = minH;
	pt->_color_HSV_H_max = maxH;
	pt->_color_HSV_S_min = minS;
	pt->_color_HSV_S_max = maxS;
	pt->_color_HSV_V_min = minV;
	pt->_color_HSV_V_max = maxV;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_alpha_type = Talpha1;
	pt->_alpha_1 = alpha;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha1,
		jdouble alpha2) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_alpha_type = Talpha2;
	pt->_alpha_1 = alpha1;
	pt->_alpha_2 = alpha2;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDDD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha1,
		jdouble alpha2, jdouble alpha3) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_alpha_type = Talpha3;
	pt->_alpha_1 = alpha1;
	pt->_alpha_2 = alpha2;
	pt->_alpha_3 = alpha3;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetLifeTimeNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint min, jint max) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_lifeTime_min = min;
	pt->_lifeTime_max = max;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnStepNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enabled,
		jlong particleType2, jint number) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_particleOnStep_enabled = enabled;
	pt->_particleOnStep_type = (ParticleType*) (unsigned long) particleType;
	pt->_particleOnStep_number = number;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnDeathNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enabled,
		jlong particleType2, jint number) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_particleOnDeath_enabled = enabled;
	pt->_particleOnDeath_type = (ParticleType*) (unsigned long) particleType;
	pt->_particleOnDeath_number = number;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpeedNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minSpeed,
		jdouble maxSpeed, jdouble incrementPerStep, jdouble wiggle) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_speed_min = minSpeed;
	pt->_speed_max = maxSpeed;
	pt->_speed_incrementPerStep = incrementPerStep;
	pt->_speed_wiggle = wiggle;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetDirectionNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minDirection,
		jdouble maxDirection, jdouble incrementPerStep, jdouble wiggle) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_direction_min = minDirection;
	pt->_direction_max = maxDirection;
	pt->_direction_incrementPerStep = incrementPerStep;
	pt->_direction_wiggle = wiggle;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetGravityNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble amount,
		jdouble direction) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;
	pt->_gravity_amount = amount;
	pt->_gravity_direction = direction;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTremoveParticleTypeNative(
		JNIEnv *env, jclass clazz, jlong particleType) {

	ParticleType *pt = (ParticleType*) (unsigned long) particleType;

	return removeElement(particleSystemList, (void*) pt);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTfinalizeParticleTypeNative(
		JNIEnv *env, jclass clazz) {
	//回收内存
	if (particleTypeList != NULL) {
		for (int i = 0; i < particleTypeList->index; i++) {
			ParticleType *pt = (ParticleType*) particleTypeList->data[i];
			free(pt);
		}
		destroyArrayList(particleTypeList);
	}

	return JNI_TRUE;
}

//-------------------------------------------------------------------------------------------------------------------
//ParticleAttractor
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAcreateParticleAttractorNative(
		JNIEnv * env, jclass clazz) {
	if (particleAttractorList == NULL) {
		LOGI("Create new particle attractor list.");
		Asert(particleAttractorList = createArrayList(NULL, NULL),
				"Failed to create particle attractor list!");
	}

	ParticleAttractor *pa;
	Asert(pa = (ParticleAttractor *)malloc(sizeof(ParticleAttractor)),
			"Failed to malloc new particle attractor!");

	if (!addElement(particleAttractorList, (void*) pa)) {
		LOGE("Failed to add particle attractor to list!");
		free(pa);
		return NULL;
	}

	//初始化
	pa->_position_x = 0;
	pa->_position_y = 0;
	pa->_force_kind =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_CONSTANT;
	pa->_force_force = 0;
	pa->_force_distance_max = 100;
	pa->_force_additive = false;

	LOGI("Create particle attractor success! id:%u", (long)pa);

	return (jlong) (long) pa;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAsetPositionNative(
		JNIEnv * env, jclass clazz, jlong particleAttractor, jint x, jint y) {

	ParticleAttractor *pa =
			(ParticleAttractor*) (unsigned long) particleAttractor;
	pa->_position_x = x;
	pa->_position_y = y;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAsetForceNative(
		JNIEnv * env, jclass clazz, jlong particleAttractor, jint kind,
		jfloat force, jfloat maxDistance, jboolean additive) {

	ParticleAttractor *pa =
			(ParticleAttractor*) (unsigned long) particleAttractor;
	pa->_force_kind = kind;
	pa->_force_force = force;
	pa->_force_distance_max = maxDistance;
	pa->_force_additive = additive;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAremoveParticleAttractorNative(
		JNIEnv * env, jclass clazz, jlong particleAttractor) {

	ParticleAttractor *pa =
			(ParticleAttractor*) (unsigned long) particleAttractor;

	return removeElement(particleAttractorList, (void*) pa);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAfinalizeParticleAttractorNative(
		JNIEnv * env, jclass clazz) {
	//回收内存
	if (particleAttractorList != NULL) {
		for (int i = 0; i < particleAttractorList->index; i++) {
			ParticleAttractor *pa =
					(ParticleAttractor*) particleAttractorList->data[i];
			free(pa);
		}
		destroyArrayList(particleAttractorList);
	}

	return JNI_TRUE;
}

//----------------------------------------------------------------------------------------------------------------
//ParticleChanger
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCcreateParticleChangerNative(
		JNIEnv * env, jclass clazz) {
	if (particleChangerList == NULL) {
		LOGI("Create new particle changer list.");
		Asert(particleChangerList = createArrayList(NULL, NULL),
				"Failed to create particle changer list!");
	}

	ParticleChanger *pc;
	Asert(pc = (ParticleChanger *)malloc(sizeof(ParticleChanger)),
			"Failed to malloc new particle changer!");

	if (!addElement(particleChangerList, (void*) pc)) {
		LOGE("Failed to add particle changer to list!");
		free(pc);
		return NULL;
	}

	//初始化
	pc->_region_x_min = 0;
	pc->_region_x_max = 0;
	pc->_region_y_min = 0;
	pc->_region_y_max = 0;
	pc->_region_shape =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE;

	pc->_changeType_target = NULL;
	pc->_changeType_final = NULL;

	pc->_changeKind =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_ALL;

	LOGI("Create particle changer success! id:%u", (long)pc);

	return (jlong) (long) pc;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCsetRegionNative(
		JNIEnv * env, jclass clazz, jlong particleChanger, jint minX, jint minY,
		jint maxX, jint maxY, jint shape) {

	ParticleChanger *pc = (ParticleChanger*) (unsigned long) particleChanger;
	pc->_region_x_min = minX;
	pc->_region_x_max = maxX;
	pc->_region_y_min = minY;
	pc->_region_y_max = maxY;
	pc->_region_shape = shape;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCsetParticleTypesNative(
		JNIEnv * env, jclass clazz, jlong particleChanger, jlong targetType,
		jlong finalType) {

	ParticleChanger *pc = (ParticleChanger*) (unsigned long) particleChanger;
	pc->_changeType_target = (ParticleType*) (unsigned long) targetType;
	pc->_changeType_final = (ParticleType*) (unsigned long) finalType;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCsetChangerKindNative(
		JNIEnv * env, jclass clazz, jlong particleChanger, jint kind) {

	ParticleChanger *pc = (ParticleChanger*) (unsigned long) particleChanger;
	pc->_changeKind = kind;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCremoveParticleChangerNative(
		JNIEnv * env, jclass clazz, jlong particleChanger) {

	ParticleChanger *pc = (ParticleChanger*) (unsigned long) particleChanger;

	return removeElement(particleChangerList, (void*) pc);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PCfinalizeParticleChangerNative(
		JNIEnv * env, jclass clazz) {
	//回收内存
	if (particleChangerList != NULL) {
		for (int i = 0; i < particleChangerList->index; i++) {
			ParticleChanger *pc =
					(ParticleChanger*) particleChangerList->data[i];
			free(pc);
		}
		destroyArrayList(particleChangerList);
	}
	return JNI_TRUE;
}

//---------------------------------------------------------------------------------------------------------------
//ParticleDestroyer
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PDcreateParticleDestroyerNative(
		JNIEnv * env, jclass clazz) {
	if (particleDestoryerList == NULL) {
		LOGI("Create new particle destroyer list.");
		Asert(particleDestoryerList = createArrayList(NULL, NULL),
				"Failed to create particle destroyer list!");
	}

	ParticleDestroyer *pd;
	Asert(pd = (ParticleDestroyer *)malloc(sizeof(ParticleDestroyer)),
			"Failed to malloc new particle destroyer!");

	if (!addElement(particleDestoryerList, (void*) pd)) {
		LOGE("Failed to add particle destroyer to list!");
		free(pd);
		return NULL;
	}

	//初始化
	pd->_region_x_min = 0;
	pd->_region_x_max = 0;
	pd->_region_y_min = 0;
	pd->_region_y_max = 0;
	pd->_region_shape =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE;

	LOGI("Create particle destroyer success! id:%u", (long)pd);

	return (jlong) (long) pd;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PDsetRegionNative(
		JNIEnv * env, jclass clazz, jlong particleDestroyer, jint minX,
		jint minY, jint maxX, jint maxY, jint shape) {

	ParticleDestroyer *pd =
			(ParticleDestroyer*) (unsigned long) particleDestroyer;
	pd->_region_x_min = minX;
	pd->_region_x_max = maxX;
	pd->_region_y_min = minY;
	pd->_region_y_max = maxY;
	pd->_region_shape = shape;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PDremoveParticleDestroyerNative(
		JNIEnv * env, jclass clazz, jlong particleDestroyer) {

	ParticleDestroyer *pd =
			(ParticleDestroyer*) (unsigned long) particleDestroyer;

	return removeElement(particleDestoryerList, (void*) pd);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PDfinalizeParticleDestroyerNative(
		JNIEnv * env, jclass clazz) {
	//回收内存
	if (particleDestoryerList != NULL) {
		for (int i = 0; i < particleDestoryerList->index; i++) {
			ParticleDestroyer *pd =
					(ParticleDestroyer*) particleDestoryerList->data[i];
			free(pd);
		}
		destroyArrayList(particleDestoryerList);
	}
	return JNI_TRUE;
}

//----------------------------------------------------------------------------------------------------------------
//ParticleEmitter
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEcreateParticleEmitterNative(
		JNIEnv * env, jclass clazz) {

	if (particleEmitterList == NULL) {
		LOGI("Create new particle emitter list.");
		Asert(particleEmitterList = createArrayList(NULL, NULL),
				"Failed to create particle emitter list!");
	}

	ParticleEmitter *pe;
	Asert(pe = (ParticleEmitter *)malloc(sizeof(ParticleEmitter)),
			"Failed to malloc new particle emitter!");

	if (!addElement(particleEmitterList, (void*) pe)) {
		LOGE("Failed to add particle emitter to list!");
		free(pe);
		return NULL;
	}

	//初始化
	pe->_region_x_min = 0;
	pe->_region_x_max = 0;
	pe->_region_y_min = 0;
	pe->_region_y_max = 0;
	pe->_region_shape =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE;
	pe->_region_distribution =
			org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_LINEAR;
	pe->emitType = Tstream;
	pe->_emit_particle_type = NULL;
	pe->_emit_particle_number = 0;

	LOGI("Create particle emitter success! id:%u", (long)pe);

	return (jlong) (long) pe;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEsetRegionNative(
		JNIEnv * env, jclass clazz, jlong particleEmitter, jint minX, jint minY,
		jint maxX, jint maxY, jint shape) {

	ParticleEmitter *pe = (ParticleEmitter*) (unsigned long) particleEmitter;
	pe->_region_x_min = minX;
	pe->_region_x_max = maxX;
	pe->_region_y_min = minY;
	pe->_region_y_max = maxY;
	pe->_region_shape = shape;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEbrustNative(
		JNIEnv * env, jclass clazz, jlong particleEmitter, jlong particleType,
		jint number) {

	ParticleEmitter *pe = (ParticleEmitter*) (unsigned long) particleEmitter;
	pe->emitType = Tbrust;
	pe->_emit_particle_type = (ParticleType*) (unsigned long) particleType;
	pe->_emit_particle_number = number;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEstreamNative(
		JNIEnv * env, jclass clazz, jlong particleEmitter, jlong particleType,
		jint number) {

	ParticleEmitter *pe = (ParticleEmitter*) (unsigned long) particleEmitter;
	pe->emitType = Tstream;
	pe->_emit_particle_type = (ParticleType*) (unsigned long) particleType;
	pe->_emit_particle_number = number;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEremoveParticleEmitterNative(
		JNIEnv * env, jclass clazz, jlong particleEmitter) {

	ParticleEmitter *pe = (ParticleEmitter*) (unsigned long) particleEmitter;

	return removeElement(particleEmitterList, (void*) pe);
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PEfinalizeParticleEmitterNative(
		JNIEnv * env, jclass clazz) {
	//回收内存
	if (particleEmitterList != NULL) {
		for (int i = 0; i < particleEmitterList->index; i++) {
			ParticleEmitter *pe =
					(ParticleEmitter*) particleEmitterList->data[i];
			free(pe);
		}
		destroyArrayList(particleEmitterList);
	}
	return JNI_TRUE;
}
