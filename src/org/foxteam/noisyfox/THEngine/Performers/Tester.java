package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleEmitter;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleRegionDistribution;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleRegionShape;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleSystem;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleType;
import org.foxteam.noisyfox.THEngine.GlobalResources;

public class Tester extends FGPerformer {

	FGParticleSystem pSystem = new FGParticleSystem();
	FGParticleType pType = new FGParticleType();
	FGParticleEmitter pEmitter = new FGParticleEmitter();

	@Override
	protected void onDraw() {
		pSystem.draw(getCanvas());
	}

	@Override
	protected void onCreate() {
		FGSprite particleSprite = new FGSprite();
		particleSprite.bindFrames(GlobalResources.FRAMES_PARTICLE_MISSILESMOKE);
		particleSprite.setOffset(3, 3);
		pType.setSprite(particleSprite);
		pType.setLifeTime(15, 25);
		pType.setSize(0.8, 1.5, -0.03, 0.01);
		pType.setOrientation(0, 359.9f, 0, 1, false);
		pType.setAlpha(1.0, 0.0);
		pEmitter.stream(pType, -2);
		pEmitter.setRegion(100, 100, 120, 120, FGParticleRegionShape.rectangle,
				FGParticleRegionDistribution.linear);
		pSystem.bindParticleEmitter(pEmitter);
	}

	@Override
	protected void onStep() {
		pSystem.update();
	}

}
