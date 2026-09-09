package net.mcreator.minigames.client.model.animations;

import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.AnimationChannel;

// Save this class in your mod and generate all required imports
/**
 * Made with Blockbench 5.1.6 Exported for Minecraft version 1.19 or later with
 * Mojang mappings
 * 
 * @author Author
 */
public class laser_statueAnimation {
	public static final AnimationDefinition active = AnimationDefinition.Builder.withLength(1.5417F)
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(150.4302F, 16.1277F, 31.8484F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -60.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -52.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.5F, KeyframeAnimations.posVec(-1.1475F, -0.6625F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(142.7864F, -33.1048F, -41.3526F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 62.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_hand",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.0676F, -2.9058F, 92.4333F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.5F, KeyframeAnimations.posVec(1.2524F, -0.6802F, 0.025F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gem", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.3265F, -2.4786F, 14.9929F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gem",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.3203F, -1.5588F, 1.2391F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.posVec(3.4408F, -1.1005F, 1.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_wings_2",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.2749F, 69.8507F, 6.834F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_wings_3",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.3943F, 79.952F, -5.8079F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_wings2",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-3.5632F, -57.4767F, 1.3978F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_wings_3",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.2616F, -87.7064F, 19.8419F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-3.0454F, -79.8145F, 2.6877F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.1667F, KeyframeAnimations.degreeVec(1.5806F, -54.6061F, -0.8629F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-1.7063F, -45.8789F, 0.8299F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-0.3755F, -28.5786F, -0.3006F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.3705F, -19.9559F, -0.2486F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.build();
	public static final AnimationDefinition damage = AnimationDefinition.Builder.withLength(1.6667F)
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -60.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-2.5048F, -4.3288F, -59.9054F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(43.8454F, 50.1904F, -38.6517F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(98.2558F, 34.5317F, 7.887F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.6667F, KeyframeAnimations.degreeVec(150.4302F, 16.1277F, 31.8484F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_hand",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -52.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(-15.6412F, -6.9268F, -70.0615F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.9675F, -1.5211F, -117.397F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-1.1475F, -0.6625F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 62.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(3.4787F, -6.6486F, 62.2978F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(12.1512F, -22.0163F, 50.1275F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(89.1805F, -50.9486F, -3.5539F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.375F, KeyframeAnimations.degreeVec(142.7864F, -33.1048F, -41.3526F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_hand",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-2.0676F, -2.9058F, 92.4333F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(1.2524F, -0.6802F, 0.025F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gem", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3265F, -2.4786F, 14.9929F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gem",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(3.4408F, -1.1005F, 1.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.4583F, KeyframeAnimations.posVec(0.3203F, -1.5588F, 1.2391F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gem",
					new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.9F, 0.9F, 0.9F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.4583F, KeyframeAnimations.scaleVec(0.6F, 0.6F, 0.6F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_wings_1",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 30.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_wings_2",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(2.8241F, 39.9421F, 3.0646F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.degreeVec(4.9135F, 59.8992F, 4.6881F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5417F, KeyframeAnimations.degreeVec(7.2749F, 69.8507F, 6.834F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_wings_3",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.4922F, 7.489F, -0.1766F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-1.4219F, 40.0147F, -2.2073F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-5.3943F, 79.952F, -5.8079F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_wings2",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.6955F, -22.4824F, 0.8134F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(-3.1355F, -44.9794F, 1.0624F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-3.5632F, -57.4767F, 1.3978F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_wings_3",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(-1.803F, -12.5944F, 1.8758F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.4033F, -40.0107F, -1.0548F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.375F, KeyframeAnimations.degreeVec(-1.4026F, -71.7678F, 4.4071F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-20.2616F, -87.7064F, 19.8419F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_wings_1", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.build();
}