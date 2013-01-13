package org.foxteam.noisyfox.FoxGaming.G2D.Particle;

public class FGParticleNative {

	protected static native long createParticleSystemNative();

	protected static native void removeParticleNative(long particleSystem,
			long particle);

	protected static native long obtainParticleNative(long particleSystem);

	protected static native void updateNative(long particleSystem);

	protected static native long createParticleNative(long particleSystem,
			long[] arg);

	protected static native void clearNative(long particleSystem);

	protected static native int countNative(long particleSystem);

	protected static native void setMaxParticleNumberNative(long particleSystem,int number);

}
