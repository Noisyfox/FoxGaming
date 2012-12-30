/**
 * FileName:     SimpleSoundEffect.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-9 下午4:56:49
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-9      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

/**
 * @ClassName: SimpleSoundEffect
 * @Description: 一个简单的音效类
 * @author: Noisyfox
 * @date: 2012-7-9 下午4:56:49
 * 
 */
public final class FGSimpleSoundEffect {
	public static final int AUDIO_SOUNDPOOL_MAXSTREAMS = 10;
	public static final int AUDIO_SOUNDPOOL_QUALITY = 90;

	private static FGSimpleSoundEffect simpleSoundEffect = new FGSimpleSoundEffect();
	private static SparseIntArray sounds = new SparseIntArray();
	private static SoundPool soundPool = new SoundPool(
			AUDIO_SOUNDPOOL_MAXSTREAMS, AudioManager.STREAM_MUSIC,
			AUDIO_SOUNDPOOL_QUALITY);
	private static int volume = 100;

	private static int lastAudioId = -1;

	private FGSimpleSoundEffect() {
	}

	/**
	 * @Title: loadSoundEffect
	 * @Description: 载入指定 资源id 的音频文件，返回一个内部的 声音id
	 * @param: @param resId
	 * @param: @return
	 * @return: int
	 */
	public static int loadSoundEffect(int resId) {
		lastAudioId++;
		int soundId = soundPool.load(FGGameCore.mainActivity, resId, 1);
		sounds.put(lastAudioId, soundId);
		return lastAudioId;
	}

	/**
	 * @Title: play
	 * @Description: 播放指定的音效（由于是音效故不提供循环播放功能)
	 * @param: @param soundId
	 * @return: void
	 */
	public static void play(int soundId) {
		soundPool.play(sounds.get(soundId), (float) volume / 100f,
				(float) volume / 100f, 1, 0, 1f);
	}

	/**
	 * @Title: setSoundVolume
	 * @Description: 单独设置音效的音量
	 * @param: @param volume 范围0~100
	 * @return: void
	 */
	public static void setSoundVolume(int volume) {
		if (volume < 0 || volume > 100) {
			throw new IllegalArgumentException();
		}
		FGSimpleSoundEffect.volume = volume;
	}

	/**
	 * @Title: getSoundVolume
	 * @Description: 获取音效的音量
	 * @param: @return
	 * @return: int
	 */
	public static int getSoundVolume() {
		return volume;
	}

	/**
	 * @Title: free
	 * @Description: 释放指定id的音效资源
	 * @param: @param soundId
	 * @return: void
	 */
	public static void free(int soundId) {
		soundPool.unload(sounds.get(soundId));
		sounds.delete(soundId);
	}

	/**
	 * @Title: freeAll
	 * @Description: 释放所有音效资源
	 * @param:
	 * @return: void
	 */
	public static void freeAll() {
		for (int i = 0; i <= lastAudioId; i++) {
			if (sounds.get(i, -1) != -1) {
				free(i);
			}
		}
	}

	/**
	 * Call when game is paused
	 */
	public static void onPause() {
		soundPool.autoPause();
	}

	/**
	 * Call when game is resumed
	 */
	public static void onResume() {
		soundPool.autoResume();
	}

	/**
	 * @Title: getInstance
	 * @Description: 得到该类的实例
	 * @param: @return
	 * @return: FGSimpleSoundEffect
	 */
	public static FGSimpleSoundEffect getInstance() {
		return simpleSoundEffect;
	}

}
