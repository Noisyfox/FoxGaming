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

	// ParticleSystem
	protected static native long PScreateParticleSystemNative();

	protected static native void PSremoveParticleNative(long particleSystem,
			long particle);

	protected static native long PSobtainParticleNative(long particleSystem);

	protected static native void PSupdateNative(long particleSystem);

	protected static native long PScreateParticleNative(long particleSystem,
			long[] arg);

	protected static native void PSclearNative(long particleSystem);

	protected static native int PScountNative(long particleSystem);

	protected static native boolean PSsetMaxParticleNumberNative(
			long particleSystem, int number);

	protected static native boolean PSbindParticleEmitterNative(
			long particleSystem, long emitter);

	protected static native boolean PSbindParticleAttractorNative(
			long particleSystem, long attractor);

	protected static native boolean PSbindPraticleDestroyerNative(
			long particleSystem, long destroyer);

	protected static native boolean PSbindPraticleDeflectorNative(
			long particleSystem, long deflector);

	protected static native boolean PSbindParticleChangerNative(
			long particleSystem, long changer);

	protected static native boolean PSunbindParticleEmitterNative(
			long particleSystem, long emitter);

	protected static native boolean PSunbindParticleAttractorNative(
			long particleSystem, long attractor);

	protected static native boolean PSunbindPraticleDestroyerNative(
			long particleSystem, long destroyer);

	protected static native boolean PSunbindPraticleDeflectorNative(
			long particleSystem, long deflector);

	protected static native boolean PSunbindParticleChangerNative(
			long particleSystem, long changer);

	protected static native void PSremoveParticleSystemNative(
			long particleSystem);

	protected static native void PSfinalizeParticleSystemNative();

	// End of ParticleSystem

	// ParticleType
	protected static native long PTcreateParticleTypeNative();

	protected static native void PTsetSpriteFrameAnimationNative(
			long particleType, boolean enableFrameAni, double speed,
			boolean startWithRandomFrame);

	protected static native void PTsetSizeNative(long particleType,
			double minSize, double maxSize, double incrementPerStep,
			double wiggle);

	protected static native void PTsetScaleNative(long particleType,
			float xScale, float yScale);

	protected static native void PTsetOrientationNative(long particleType,
			float minAngle, float maxAngle, double incrementPerStep,
			double wiggle, boolean relative);

	protected static native void PTsetColorNative(long particleType, int color);

	protected static native void PTsetColorNative(long particleType,
			int color1, int color2);

	protected static native void PTsetColorNative(long particleType,
			int color1, int color2, int color3);

	protected static native void PTsetColorRGBNative(long particleType,
			int minR, int minG, int minB, int maxR, int maxG, int maxB);

	protected static native void PTsetColorHSVNative(long particleType,
			int minH, double minS, double minV, int maxH, double maxS,
			double maxV);

	protected static native void PTsetAlphaNative(long particleType,
			double alpha);

	protected static native void PTsetAlphaNative(long particleType,
			double alpha1, double alpha2);

	protected static native void PTsetAlphaNative(long particleType,
			double alpha1, double alpha2, double alpha3);

	protected static native void PTsetLifeTimeNative(long particleType,
			int min, int max);

	protected static native void PTcreateNewParticleOnStepNative(
			long particleType, boolean enabled, long particleType2, int number);

	protected static native void PTcreateNewParticleOnDeathNative(
			long particleType, boolean enabled, long particleType2, int number);

	protected static native void PTsetSpeedNative(long particleType,
			double minSpeed, double maxSpeed, double incrementPerStep,
			double wiggle);

	protected static native void PTsetDirectionNative(long particleType,
			double minDirection, double maxDirection, double incrementPerStep,
			double wiggle);

	protected static native void PTsetGravityNative(long particleType,
			double amount, double direction);

	protected static native void PTremoveParticleTypeNative(long particleType);

	protected static native void PTfinalizeParticleTypeNative();
	// End of ParticleType

}
