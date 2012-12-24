/**
 * FileName:     SimpleBGM.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-9 下午4:57:02
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-9      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.SparseIntArray;

/**
 * @ClassName: SimpleBGM
 * @Description: 一个简单的 BGM 类，保证在同一时刻只会播放一首音乐
 * @author: Noisyfox
 * @date: 2012-7-9 下午4:57:02
 * 
 */
public final class FGSimpleBGM {

	private static FGSimpleBGM simpleBGM = new FGSimpleBGM();
	private static MediaPlayer mediaPlayer = null;
	private static SparseIntArray sounds = new SparseIntArray();

	private static int lastAudioId = -1;
	private static boolean mustResume = false;

	private FGSimpleBGM() {
	}

	/**
	 * @Title: loadBGM
	 * @Description: 载入指定 资源id 的音频文件，返回一个内部的 声音id
	 * @param: @param resId
	 * @param: @return
	 * @return: int
	 */
	public static int loadBGM(int resId) {
		lastAudioId++;
		sounds.put(lastAudioId, resId);
		return lastAudioId;
	}

	/**
	 * Plays a file from loaded sound, no looping
	 * 
	 * @param soundId
	 */
	public static void play(int soundId) {
		play(soundId, false);
	}

	/**
	 * Plays a file from loaded sound
	 * 
	 * @param soundId
	 * @param loop
	 *            TRUE to loop the file, FALSE to play once
	 */
	public static void play(int soundId, boolean loop) {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

		mediaPlayer = MediaPlayer.create(FGGameCore.mainActivity,
				sounds.get(soundId));
		mediaPlayer.setLooping(loop);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayer.start();
	}

	/**
	 * Plays the current music, if paused or stopped
	 */
	public static void play() {
		if (mediaPlayer == null)
			return;
		mediaPlayer.start();
	}

	/**
	 * Stops the current music file from playing
	 */
	public static void stop() {
		if (mediaPlayer == null)
			return;
		mediaPlayer.stop();
	}

	/**
	 * Pauses the current music file, resume with start()
	 */
	public static void pause() {
		if (mediaPlayer == null)
			return;
		mediaPlayer.pause();
	}

	/**
	 * Call when game is paused
	 */
	public static void onPause() {
		if (mediaPlayer == null)
			return;
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mustResume = true;
		}
	}

	/**
	 * Call when game is resumed
	 */
	public static void onResume() {
		if (mediaPlayer == null)
			return;
		if (mustResume) {
			mediaPlayer.start();
			mustResume = false;
		}
	}

	/**
	 * @Title: freeAll
	 * @Description: 释放所有 BGM 资源
	 * @param:
	 * @return: void
	 */
	public static void freeAll() {
		sounds.clear();
		if (mediaPlayer == null)
			return;
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * @Title: getInstance
	 * @Description: 得到该类的实例
	 * @param: @return
	 * @return: FGSimpleBGM
	 */
	public static FGSimpleBGM getInstance() {
		return simpleBGM;
	}
}
