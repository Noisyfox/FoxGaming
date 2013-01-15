/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative */

#ifndef _Included_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
#define _Included_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
#ifdef __cplusplus
extern "C" {
#endif
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_LINEAR
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_LINEAR 1L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_GAUSSIAN
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_GAUSSIAN 2L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_INVGAUSSIAN
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_DISTRIBUTION_INVGAUSSIAN 3L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_RECTANGLE 4L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_ELLIPSE
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_ELLIPSE 5L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_DIAMOND
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_REGION_SHAPE_DIAMOND 6L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_CONSTANT
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_CONSTANT 7L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_LINEAR
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_LINEAR 8L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_QUADRATIC
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_FORCE_QUADRATIC 9L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_MOTION
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_MOTION 10L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_SHAPE
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_SHAPE 11L
#undef org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_ALL
#define org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAR_CHANGE_ALL 12L
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PScreateParticleSystemNative
 * Signature: ()J
 */JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleSystemNative(
		JNIEnv *, jclass);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSremoveParticleNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSobtainParticleNative
 * Signature: (J)J
 */JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSobtainParticleNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSupdateNative
 * Signature: (J)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSupdateNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PScreateParticleNative
 * Signature: (J[J)J
 */JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScreateParticleNative(
		JNIEnv *, jclass, jlong, jlongArray);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSclearNative
 * Signature: (J)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSclearNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PScountNative
 * Signature: (J)I
 */JNIEXPORT jint JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PScountNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSsetMaxParticleNumberNative
 * Signature: (JI)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSsetMaxParticleNumberNative(
		JNIEnv *, jclass, jlong, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSbindParticleEmitterNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleEmitterNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSbindParticleAttractorNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleAttractorNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSbindPraticleDestroyerNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindPraticleDestroyerNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSbindPraticleDeflectorNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindPraticleDeflectorNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSbindParticleChangerNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSbindParticleChangerNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSunbindParticleEmitterNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleEmitterNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSunbindParticleAttractorNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleAttractorNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSunbindPraticleDestroyerNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindPraticleDestroyerNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSunbindPraticleDeflectorNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindPraticleDeflectorNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSunbindParticleChangerNative
 * Signature: (JJ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSunbindParticleChangerNative(
		JNIEnv *, jclass, jlong, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSremoveParticleSystemNative
 * Signature: (J)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSremoveParticleSystemNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PSfinalizeParticleSystemNative
 * Signature: ()Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PSfinalizeParticleSystemNative(
		JNIEnv *, jclass);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTcreateParticleTypeNative
 * Signature: ()J
 */JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateParticleTypeNative(
		JNIEnv *, jclass);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetSpriteFrameAnimationNative
 * Signature: (JZDZ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpriteFrameAnimationNative(
		JNIEnv *, jclass, jlong, jboolean, jdouble, jboolean);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetSizeNative
 * Signature: (JDDDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSizeNative(
		JNIEnv *, jclass, jlong, jdouble, jdouble, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetScaleNative
 * Signature: (JFF)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetScaleNative(
		JNIEnv *, jclass, jlong, jfloat, jfloat);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetOrientationNative
 * Signature: (JFFDDZ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetOrientationNative(
		JNIEnv *, jclass, jlong, jfloat, jfloat, jdouble, jdouble, jboolean);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetColorNative
 * Signature: (JI)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JI(
		JNIEnv *, jclass, jlong, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetColorNative
 * Signature: (JII)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JII(
		JNIEnv *, jclass, jlong, jint, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetColorNative
 * Signature: (JIII)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorNative__JIII(
		JNIEnv *, jclass, jlong, jint, jint, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetColorRGBNative
 * Signature: (JIIIIII)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorRGBNative(
		JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetColorHSVNative
 * Signature: (JIDDIDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetColorHSVNative(
		JNIEnv *, jclass, jlong, jint, jdouble, jdouble, jint, jdouble,
		jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetAlphaNative
 * Signature: (JD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JD(
		JNIEnv *, jclass, jlong, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetAlphaNative
 * Signature: (JDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDD(
		JNIEnv *, jclass, jlong, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetAlphaNative
 * Signature: (JDDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetAlphaNative__JDDD(
		JNIEnv *, jclass, jlong, jdouble, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetLifeTimeNative
 * Signature: (JII)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetLifeTimeNative(
		JNIEnv *, jclass, jlong, jint, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTcreateNewParticleOnStepNative
 * Signature: (JZJI)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnStepNative(
		JNIEnv *, jclass, jlong, jboolean, jlong, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTcreateNewParticleOnDeathNative
 * Signature: (JZJI)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTcreateNewParticleOnDeathNative(
		JNIEnv *, jclass, jlong, jboolean, jlong, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetSpeedNative
 * Signature: (JDDDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetSpeedNative(
		JNIEnv *, jclass, jlong, jdouble, jdouble, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetDirectionNative
 * Signature: (JDDDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetDirectionNative(
		JNIEnv *, jclass, jlong, jdouble, jdouble, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTsetGravityNative
 * Signature: (JDD)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTsetGravityNative(
		JNIEnv *, jclass, jlong, jdouble, jdouble);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTremoveParticleTypeNative
 * Signature: (J)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTremoveParticleTypeNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PTfinalizeParticleTypeNative
 * Signature: ()Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PTfinalizeParticleTypeNative(
		JNIEnv *, jclass);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PAcreateParticleAttractorNative
 * Signature: ()J
 */JNIEXPORT jlong JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAcreateParticleAttractorNative(
		JNIEnv *, jclass);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PAsetPositionNative
 * Signature: (JII)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAsetPositionNative(
		JNIEnv *, jclass, jlong, jint, jint);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PAsetForceNative
 * Signature: (JIFFZ)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAsetForceNative(
		JNIEnv *, jclass, jlong, jint, jfloat, jfloat, jboolean);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PAremoveParticleAttractorNative
 * Signature: (J)Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAremoveParticleAttractorNative(
		JNIEnv *, jclass, jlong);
/*
 * Class:     org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative
 * Method:    PAfinalizeParticleAttractorNative
 * Signature: ()Z
 */JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_G2D_Particle_FGParticleNative_PAfinalizeParticleAttractorNative(
		JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
