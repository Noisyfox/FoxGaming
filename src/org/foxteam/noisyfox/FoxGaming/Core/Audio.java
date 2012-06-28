/**
 * FileName:     Audio.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-28 下午5:35:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-28      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * @ClassName: Audio
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-28 下午5:35:57
 * 
 */
public class Audio {
	public static final int AUDIO_BACKGROUNDMUSIC = 1;
	public static final int AUDIO_SOUNDEFFECT = 2;

	public static AudioManager audioManager = null;

	public Audio(int resId, int audioType, boolean preLoad) {

	}

	public static void initAudio(Activity mainActivity) {
		audioManager = (AudioManager) mainActivity
				.getSystemService(Context.AUDIO_SERVICE);
		mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}
