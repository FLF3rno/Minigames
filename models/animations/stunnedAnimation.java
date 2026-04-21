// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.1.3 Exported for Minecraft version 1.19 or later with
 * Mojang mappings
 * 
 * @author Author
 */
public class stunnedAnimation {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(1.9167F).looping()
			.addAnimation("bone",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.9167F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR)))
			.build();
}