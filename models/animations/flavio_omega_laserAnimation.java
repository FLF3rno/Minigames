// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.1.6 Exported for Minecraft version 1.19 or later with
 * Mojang mappings
 * 
 * @author Author
 */
public class flavio_omega_laserAnimation {
	public static final AnimationDefinition fire = AnimationDefinition.Builder.withLength(6.0F)
			.addAnimation("progress",
					new AnimationChannel(AnimationChannel.Targets.SCALE,
							new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.3333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 0.8429F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.125F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 0.8799F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(6.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR)))
			.build();
}