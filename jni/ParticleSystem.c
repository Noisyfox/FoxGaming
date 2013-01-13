/*
 * ParticleSystem.c
 *
 *  Created on: 2013-1-13
 *      Author: Noisyfox
 */
#include <stdlib.h>

#include <ParticleSystem.h>
#include <ArrayList.h>

typedef struct {

} ParticleSystem;

ArrayList * particleSystem = NULL;

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_createParticleSystemNative(
		JNIEnv *env, jclass clazz) {
	if (particleSystem == NULL) {
		if ((particleSystem = createArrayList(NULL, NULL)) == NULL)
			return -1;
	}

	ParticleSystem *p = (ParticleSystem *) malloc(sizeof(ParticleSystem));

	if (p == NULL)
		return -1;

	if (!addElement(particleSystem, (void*) p)) {
		free(p);
		return -1;
	}

	return (jlong) (unsigned long) p;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_removeParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlong particle) {
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_obtainParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {

	return -1;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_updateNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
}

JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_createParticleNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jlongArray arg) {
	return -1;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_clearNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
}

JNIEXPORT jint JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_countNative(
		JNIEnv *env, jclass clazz, jlong particleSystem) {
	return -1;
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_setMaxParticleNumberNative(
		JNIEnv *env, jclass clazz, jlong particleSystem, jint number) {
}

JNIEXPORT void JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_finalizeNative(
		JNIEnv *env, jclass clazz) {
	//回收内存
	if (particleSystem != NULL) {
		int i;
		for (i = 0; i < particleSystem->index; i++) {
			free(particleSystem->data[i]);
		}
		desrotyArrList(particleSystem);
	}
}
