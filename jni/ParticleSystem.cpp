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

#define  LOG_TAG    "libfoxgaming_particle_system"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
//检查是否成功申请内存
#undef Asert
#define Asert(par,...) if((par)==NULL) {\
	LOGE(__VA_ARGS__);return NULL;}

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

	LOGI("Create particle system success! id:%u", (unsigned long)p);

	return (jlong) (unsigned long) p;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlong particle) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
	Particles* p = (Particles*) (unsigned long) particle;

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
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSobtainParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
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

		return (jlong) (unsigned long) p;
	}
	LOGI("Particle pool is empty!");

	return 0;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSupdateNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlongArray arg) {
	return -1;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSclearNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
	Particles *p = ps->particlePool_alive;
	Particles *p_next;
	while (p != NULL) {
		p_next = p->next;
		Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleNative(
				env, clazz, particleSystem, (jlong) (unsigned long) p);
		p = p_next;
	}
	ps->aliveParticleCount = 0;

}

JNIEXPORT jint JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScountNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
	return ps->aliveParticleCount;
}

JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSsetMaxParticleNumberNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jint number) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
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

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleSystemNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	ParticleSystem* ps = (ParticleSystem*) (unsigned long) particleSystem;
	removeElement(particleSystemList, (void*) ps);
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSfinalizeParticleSystemNative(
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
}
