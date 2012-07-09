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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @ClassName: SimpleSoundEffect
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-9 下午4:56:49
 * 
 */
public final class SimpleSoundEffect implements
		SoundPool.OnLoadCompleteListener {

	private static SimpleSoundEffect simpleSoundEffect = new SimpleSoundEffect();
	private static HashMap<Integer, Integer> sounds = new HashMap<Integer, Integer>();
	private static List<Integer> soundLoading = new ArrayList<Integer>();
	private static SoundPool soundPool = null;
	public static int AUDIO_SOUNDPOOL_MAXSTREAMS = 10;
	public static int AUDIO_SOUNDPOOL_QUALITY = 90;
	private static int volume = 100;

	private static int lastAudioId = -1;

	private SimpleSoundEffect() {
		soundPool = new SoundPool(AUDIO_SOUNDPOOL_MAXSTREAMS,
				AudioManager.STREAM_MUSIC, AUDIO_SOUNDPOOL_QUALITY);
		soundPool.setOnLoadCompleteListener(this);
	}

	public static int loadSoundEffect(int resId) {
		lastAudioId++;
		int soundId = soundPool.load(GameCore.mainActivity, resId, 1);
		// if (soundId)
		soundLoading.add(soundId);
		sounds.put(lastAudioId, soundId);
		while (soundLoading.contains(soundId)) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return lastAudioId;
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		soundLoading.remove((Integer) sampleId);
	}

	public static void play(int soundId) {
		soundPool.play(sounds.get(soundId), (float) volume / 100f,
				(float) volume / 100f, 1, 0, 1f);
	}

	public static void setSoundVolume(int volume) {
		if (volume < 0 || volume > 100) {
			throw new IllegalArgumentException();
		}
		SimpleSoundEffect.volume = volume;
	}

	public static int getSoundVolume() {
		return volume;
	}

	public static void free(int soundId) {
		soundPool.unload(sounds.get(soundId));
		sounds.remove(soundId);
	}

	public static void freeAll() {
		for (int i = 0; i <= lastAudioId; i++) {
			if (sounds.containsKey(i)) {
				free(i);
			}
		}
	}
	
}
