#include <android/log.h>

#include <FoxGaming.h>
#include <MathsHelper.h>

#define  LOG_TAG    "libfoxgaming_particle_system"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

bool initalizeJNI() {
	//开始初始化
	LOGI("JNI loaded! Start initalizing!");
	//初始化数学函数
	randomGauss();

	LOGI("JNI initalize success!");
	return true;
}

//--------------------------------------------------------------------------------------------
JNIEXPORT jboolean JNICALL Java_org_foxteam_noisyfox_FoxGaming_Core_FGNativeHelper_initalizeJNINative(
		JNIEnv * env, jclass clazz) {
	return initalizeJNI() ? JNI_TRUE : JNI_FALSE;
}
