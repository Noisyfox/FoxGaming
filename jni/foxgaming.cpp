#include <stdlib.h>

#include <android/log.h>

#include <FoxGaming.h>
#include <MathsHelper.h>
#include <MethodDefinations.h>

#define  LOG_TAG    "libfoxgaming"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

bool registerMethodsClazzName(JNIEnv * env, JNINativeMethod * gMethods,
		const char* className) {
	jclass clazz;
	clazz = env->FindClass(className);
	if (clazz == NULL) {
		return JNI_FALSE;
	}
	if (env->RegisterNatives(clazz, gMethods,
			sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

bool registerMethods(JNIEnv * env) {
	if (!registerMethodsClazzName(env, gMethods_ParticleSystem,
			JNIREG_CLASS_ParticleSystem)) {
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

bool initalizeJNI(JNIEnv * env) {
	//开始初始化
	LOGI("JNI loaded! Start initalizing!");
	//注册函数
	if (registerMethods(env)) {
		LOGI("Register methods success!");
	} else {
		LOGI("Register methods failed!");
	}
	//初始化数学函数
	randomGauss();

	LOGI("JNI initalize success!");
	return true;
}

//--------------------------------------------------------------------------------------------
JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_Core_FGNativeHelper_initalizeJNINative(
		JNIEnv * env, jclass clazz) {
	return initalizeJNI(env) ? JNI_TRUE : JNI_FALSE;
}
