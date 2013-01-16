package org.foxteam.noisyfox.FoxGaming.Core;

public class FGNativeHelper {

	public static boolean loadNativeLibrary() {
		System.loadLibrary("foxgaming");
		return initalizeJNINative();
	}

	private static native boolean initalizeJNINative();
}
