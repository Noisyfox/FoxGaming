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
	Tcolor1, Tcolor2, Tcolor3, TRGB, THSV
} ColorType;

typedef enum {
	Talpha1, Talpha2, Talpha3
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
ArrayList * particleAttractorList = NULL;
ArrayList * particleChangerList = NULL;
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

	LOGI("Create particle system success! id:%u", (long)p);

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
	return removeElement(particleSystemList, (void*) ps);
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
		desrotyArrList(particleTypeList);
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
		desrotyArrList(particleAttractorList);
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
		desrotyArrList(particleChangerList);
	}
	return JNI_TRUE;
}
