package org.foxteam.noisyfox.FoxGaming.Core;

public final class FGEngineConfig {

	public static final int FOXGAMING_VERSION_1 = 1;
	public static final int FOXGAMING_VERSION_2 = 0;
	public static final int FOXGAMING_VERSION_3 = 1;
	public static final String FOXGAMING_VERSION = "Alpha";

	public static final String getVersion() {
		return FOXGAMING_VERSION_1 + "." + FOXGAMING_VERSION_2 + "."
				+ FOXGAMING_VERSION_3 + " " + FOXGAMING_VERSION;
	}

}
