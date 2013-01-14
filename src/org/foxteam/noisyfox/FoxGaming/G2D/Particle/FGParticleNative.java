package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

public class FGParticleNative {

	static {
		System.loadLibrary("foxgaming");
	}

	public static final int PAR_REGION_DISTRIBUTION_LINEAR = 1;
	public static final int PAR_REGION_DISTRIBUTION_GAUSSIAN = 2;
	public static final int PAR_REGION_DISTRIBUTION_INVGAUSSIAN = 3;

	public static final int PAR_REGION_SHAPE_RECTANGLE = 4;
	public static final int PAR_REGION_SHAPE_ELLIPSE = 5;
	public static final int PAR_REGION_SHAPE_DIAMOND = 6;

	public static final int PAR_FORCE_CONSTANT = 7;
	public static final int PAR_FORCE_LINEAR = 8;
	public static final int PAR_FORCE_QUADRATIC = 9;

	public static final int PAR_CHANGE_MOTION = 10;
	public static final int PAR_CHANGE_SHAPE = 11;
	public static final int PAR_CHANGE_ALL = 12;

	protected static native long createParticleSystemNative();

	protected static native void removeParticleNative(long particleSystem,
			long particle);

	protected static native long obtainParticleNative(long particleSystem);

	protected static native void updateNative(long particleSystem);

	protected static native long createParticleNative(long particleSystem,
			long[] arg);

	protected static native void clearNative(long particleSystem);

	protected static native int countNative(long particleSystem);

	protected static native boolean setMaxParticleNumberNative(
			long particleSystem, int number);

	protected static native void finalizeNative();

}
