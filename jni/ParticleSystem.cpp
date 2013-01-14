/*
 * ParticleSystem.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */

#include <stdlib.h>

#include <android/log.h>

#include <ParticleSystem.h>
#include <ArrayList.h>
#include <Color.h>

#define  LOG_TAG    "libfoxgaming_particle_system"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
//检查是否成功申请内存
#undef Asert
#define Asert(par,...) if((par)==NULL) {\
	LOGE(__VA_ARGS__);return NULL;}

typedef enum {
	color1, color2, color3, RGB, HSV
} ColorType;

typedef enum {
	alpha1, alpha2, alpha3
} AlphaType;

typedef struct _ParticleType {
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

typedef struct _Particles {
	struct _Particles *next;
	struct _Particles *prev;
} Particles;

typedef struct {
	int maxParticleNumber;
	int aliveParticleCount;
	Particles* particlePool_alive;
	Particles* particlePool_alive_last;
	Particles* particlePool_dead;
} ParticleSystem;

ArrayList * particleSystemList = NULL;
ArrayList * particleTypeList = NULL;

void freeParticles(Particles** startFrom) {
	Particles** curr = startFrom;
	while (*curr) {
		Particles * entry = *curr;
		*curr = entry->next;
		free(entry);
	}
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleSystemNative(
		JNIEnv *env, jclass clazz) {
	if (particleSystemList == NULL) {
		LOGI("Create new particle system list.");
		Asert(particleSystemList = createArrayList(NULL, NULL),
				"Failed to create particle system list!");
	}

	ParticleSystem *p;
	Asert(p = (ParticleSystem *) malloc(sizeof(ParticleSystem)),
			"Failed to malloc new particle system!");

	if ((p->particlePool_dead = (Particles*) malloc(sizeof(Particles))) == NULL) {
		LOGE("Failed to initialize particle system!");
		free(p);
		return NULL;
	}

	if (!addElement(particleSystemList, (void*) p)) {
		LOGE("Failed to add particle system to list!");
		free(p);
		return NULL;
	}

	LOGI("Create particle system success! id:%u", ( long)p);

	return (jlong) (long) p;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlong particle) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	Particles* p = (Particles*) (long) particle;

	if (p == ps->particlePool_alive) {
		ps->particlePool_alive = p->next;
	}
	if (p == ps->particlePool_alive_last) {
		ps->particlePool_alive_last = p->prev;
	}
	if (p->next != NULL) {
		(p->next)->prev = p->prev;
	}
	if (p->prev != NULL) {
		(p->prev)->next = p->next;
	}

	p->next = ps->particlePool_dead;
	if (ps->particlePool_dead != NULL) {
		(ps->particlePool_dead)->prev = p;
	}
	ps->particlePool_dead = p;

	ps->aliveParticleCount--;

	return JNI_TRUE;
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSobtainParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
	if (ps->particlePool_dead != NULL) {
		ps->aliveParticleCount++;

		Particles* p = ps->particlePool_dead;
		ps->particlePool_dead = p->next;
		if (ps->particlePool_dead != NULL) {
			(ps->particlePool_dead)->prev = NULL;
		}

		p->prev = ps->particlePool_alive_last;
		if (ps->particlePool_alive == NULL) {
			ps->particlePool_alive = p;
		}
		if (ps->particlePool_alive_last != NULL) {
			(ps->particlePool_alive_last)->next = p;
		}
		p->next = NULL;
		ps->particlePool_alive_last = p;

		return (jlong) (long) p;
	}
	LOGI("Particle pool is empty!");

	return 0;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSupdateNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (long) particleSystem;
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
		Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleNative(
				env, clazz, particleSystem, (jlong) (long) p);
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
	ps->maxParticleNumber = number;

	int particleCount = 0;

	Particles* p = ps->particlePool_alive;
	ps->particlePool_alive_last = p;

	while (p != NULL && particleCount < ps->maxParticleNumber) {
		particleCount++;
		ps->particlePool_alive_last = p;
		p = p->next;
	}
	ps->aliveParticleCount = particleCount;

	if (p != NULL) {
		freeParticles(&p->next);
		freeParticles(&ps->particlePool_dead);
	} else if (particleCount == ps->maxParticleNumber) {
		freeParticles(&ps->particlePool_dead);
	} else if (ps->particlePool_dead == NULL) {
		if ((ps->particlePool_dead = (Particles*) malloc(sizeof(Particles)))
				== NULL) {
			LOGE("Can't malloc new particle!");
			return JNI_FALSE;
		}
		particleCount++;
	}

	p = ps->particlePool_dead;
	for (int i = particleCount + 1; i <= ps->maxParticleNumber; i++) {
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

	return JNI_TRUE;
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
	removeElement(particleSystemList, (void*) ps);
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSfinalizeParticleSystemNative(
		JNIEnv *env, jclass clazz) {
	//回收内存
	if (particleSystemList != NULL) {
		for (int i = 0; i < particleSystemList->index; i++) {
			ParticleSystem *ps = (ParticleSystem*) particleSystemList->data[i];
			freeParticles(&ps->particlePool_alive);
			freeParticles(&ps->particlePool_dead);
			free(ps);
		}
		desrotyArrList(particleSystemList);
	}
	return JNI_TRUE;
}
/*******************************************************************************************************/
//ParticleType
JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateParticleTypeNative(
		JNIEnv *env, jclass clazz) {
	if (particleTypeList == NULL) {
		LOGI("Create new particle type list.");
		Asert(particleTypeList = createArrayList(NULL, NULL),
				"Failed to create particle type list!");
	}

	ParticleType *p;
	Asert(p = (ParticleType *)malloc(sizeof(ParticleType)),
			"Failed to malloc new particle type!");

	//初始化
	p->_frameAni_enabled = false;
	p->_frameAni_speed = 0.0;
	p->_frameAni_startWithRandomFrame = false;

	p->_size_min = 1.0;
	p->_size_max = 1.0;
	p->_size_incrementPerStep = 0.0;
	p->_size_wiggle = 0.0;

	p->_scale_x = 1;
	p->_scale_y = 1;

	p->_orientation_angle_min = 0;
	p->_orientation_angle_max = 0;
	p->_orientation_incrementPerStep = 0.0;
	p->_orientation_wiggle = 0.0;
	p->_orientation_relative = false;

	p->_color_type = color1;
	p->_color_color1 = WHITE;
	p->_color_color2 = WHITE;
	p->_color_color3 = WHITE;
	p->_color_RGB_R_min = -1;
	p->_color_RGB_R_max = -1;
	p->_color_RGB_G_min = -1;
	p->_color_RGB_G_max = -1;
	p->_color_RGB_B_min = -1;
	p->_color_RGB_B_max = -1;
	p->_color_HSV_H_min = -1;
	p->_color_HSV_H_max = -1;
	p->_color_HSV_S_min = -1;
	p->_color_HSV_S_max = -1;
	p->_color_HSV_V_min = -1;
	p->_color_HSV_V_max = -1;

	p->_alpha_type = alpha1;
	p->_alpha_1 = 1.0;
	p->_alpha_2 = 1.0;
	p->_alpha_3 = 1.0;

	p->_lifeTime_min = 100;
	p->_lifeTime_max = 100;

	p->_particleOnStep_enabled = false;
	p->_particleOnStep_type = NULL;
	p->_particleOnStep_number = 0;

	p->_particleOnDeath_enabled = false;
	p->_particleOnDeath_type = NULL;
	p->_particleOnDeath_number = 0;

	p->_speed_min = 0.0;
	p->_speed_max = 0.0;
	p->_speed_incrementPerStep = 0.0;
	p->_speed_wiggle = 0.0;

	p->_direction_min = 0.0;
	p->_direction_max = 0.0;
	p->_direction_incrementPerStep = 0.0;
	p->_direction_wiggle = 0.0;

	p->_gravity_amount = 0.0;
	p->_gravity_direction = 0.0;

	LOGI("Create particle type success! id:%u", ( long)p);

	return (jlong) (long) p;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpriteFrameAnimationNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enableFrameAni,
		jdouble speed, jboolean startWithRandomFrame) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSizeNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minSize,
		jdouble maxSize, jdouble incrementPerStep, jdouble wiggle) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetScaleNative(
		JNIEnv *env, jclass clazz, jlong particleType, jfloat xScale,
		jfloat yScale) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetOrientationNative(
		JNIEnv *env, jclass clazz, jlong particleType, jfloat minAngle,
		jfloat maxAngle, jdouble incrementPerStep, jdouble wiggle,
		jboolean relative) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JI(
		JNIEnv *env, jclass clazz, jlong particleType, jint color) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JII(
		JNIEnv *env, jclass clazz, jlong particleType, jint color1,
		jint color2) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JIII(
		JNIEnv *env, jclass clazz, jlong particleType, jint color1, jint color2,
		jint color3) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorRGBNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint minR, jint minG,
		jint minB, jint maxR, jint maxG, jint maxB) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorHSVNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint minH, jdouble minS,
		jdouble minV, jint maxH, jdouble maxS, jdouble maxV) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha1,
		jdouble alpha2) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDDD(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble alpha1,
		jdouble alpha2, jdouble alpha3) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetLifeTimeNative(
		JNIEnv *env, jclass clazz, jlong particleType, jint min, jint max) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnStepNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enabled,
		jlong particleType2, jint number) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnDeathNative(
		JNIEnv *env, jclass clazz, jlong particleType, jboolean enabled,
		jlong particleType2, jint number) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpeedNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minSpeed,
		jdouble maxSpeed, jdouble incrementPerStep, jdouble wiggle) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetDirectionNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble minDirection,
		jdouble maxDirection, jdouble incrementPerStep, jdouble wiggle) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetGravityNative(
		JNIEnv *env, jclass clazz, jlong particleType, jdouble amount,
		jdouble direction) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTremoveParticleTypeNative(
		JNIEnv *env, jclass clazz, jlong particleType) {
	ParticleType *p = (ParticleType*) (unsigned long) particleType;
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTfinalizeParticleTypeNative(
		JNIEnv *env, jclass clazz) {
	//回收内存
	if (particleTypeList != NULL) {
		for (int i = 0; i < particleTypeList->index; i++) {
			ParticleType *pt = (ParticleType*) particleTypeList->data[i];
			free(pt);
		}
		desrotyArrList(particleTypeList);
	}
	return JNI_TRUE;
}
