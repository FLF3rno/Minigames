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
public class gravediggerAnimation {
	public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
			.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-29.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-43.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-57.4753F, 2.1083F, 1.3439F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.375F, KeyframeAnimations.degreeVec(-50.4852F, 1.265F, 0.8063F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-29.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-43.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-57.4753F, 2.1083F, 1.3439F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.875F, KeyframeAnimations.degreeVec(-50.4852F, 1.265F, 0.8063F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.5305F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-2.1544F, 26.303F, 4.7841F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.degreeVec(1.5421F, 30.3035F, 5.297F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(2.0934F, 19.2075F, 5.2433F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(25.5305F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-2.1544F, 26.303F, 4.7841F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7917F, KeyframeAnimations.degreeVec(1.5421F, 30.3035F, 5.297F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.degreeVec(2.0934F, 19.2075F, 5.2433F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.5305F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-0.8F, -1.3F, -2.1F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2083F, KeyframeAnimations.posVec(-0.8F, -1.3F, -0.7F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, KeyframeAnimations.posVec(-0.8F, -0.6F, -2.26F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.posVec(-0.8F, -1.3F, -2.1F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, KeyframeAnimations.posVec(-0.8F, -1.3F, -0.7F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7917F, KeyframeAnimations.posVec(-0.8F, -0.6F, -2.26F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.posVec(-0.8F, -1.3F, -2.1F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 47.5F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, -37.5F, 47.5F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-4.3966F, -30.2117F, 56.1872F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 47.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, -37.5F, 47.5F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-4.3966F, -30.2117F, 56.1872F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 47.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-3.4159F, -4.5461F, 1.8914F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2083F, KeyframeAnimations.posVec(-5.22F, -3.7566F, 2.4988F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.posVec(-5.3991F, -4.1093F, 2.26F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.posVec(-3.4159F, -4.5461F, 1.8914F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7083F, KeyframeAnimations.posVec(-5.22F, -3.7566F, 2.4988F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7917F, KeyframeAnimations.posVec(-5.3991F, -4.1093F, 2.26F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.posVec(-3.4159F, -4.5461F, 1.8914F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Lantern",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(12.5F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.1667F, KeyframeAnimations.degreeVec(41.9001F, 8.2674F, -67.8024F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.degreeVec(88.3275F, 3.5389F, -67.7428F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(77.9965F, 2.1233F, -62.6457F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4167F, KeyframeAnimations.degreeVec(70.2338F, 5.7336F, -48.3408F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-29.1947F, -6.8638F, -68.3695F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-28.0999F, 8.2674F, -67.8024F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.75F, KeyframeAnimations.degreeVec(2.362F, 8.2554F, -58.0641F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7917F, KeyframeAnimations.degreeVec(6.4623F, 7.2019F, -63.1626F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.degreeVec(-5.1894F, 2.3865F, -50.8668F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("hat",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("hat",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.6F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.4F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.6F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.4F), AnimationChannel.Interpolations.LINEAR)))
			.build();
	public static final AnimationDefinition dig = AnimationDefinition.Builder.withLength(1.5F)
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.4583F, KeyframeAnimations.degreeVec(5.0122F, -3.9848F, -0.3492F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("head",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.9F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.4583F, KeyframeAnimations.degreeVec(4.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.7083F, KeyframeAnimations.degreeVec(6.4694F, -2.4759F, -0.6947F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("body",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.4F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -0.3F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -0.3F, -1.8F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.4F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("RightArm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.0209F, -2.2656F, -1.0571F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(20.2495F, -17.3474F, -0.8955F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-7.2505F, -17.3474F, -0.8955F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.degreeVec(-7.2505F, -17.3474F, -0.8955F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(25.0209F, -2.2656F, -1.0571F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("RightArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.1F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -1.4F, -1.1F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -1.4F, -1.1F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -3.4F, -1.1F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, -3.4F, -1.1F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.1F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(33.3674F, 12.9526F, 7.6308F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(31.0217F, 31.6091F, 21.2536F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(19.596F, 27.2915F, 18.3565F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.degreeVec(10.2922F, 35.2856F, 18.7223F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0833F, KeyframeAnimations.degreeVec(2.3412F, 37.2841F, 18.8138F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.degreeVec(2.3412F, 37.2841F, 18.8138F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(33.3674F, 12.9526F, 7.6308F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.5F, -2.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -2.23F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -4.5F, -2.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.posVec(0.1F, -3.5F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.5F, -2.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.5F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -3.5F, -2.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(9.33F, -20.1086F, -144.4286F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.degreeVec(1.8078F, -21.4689F, -123.9935F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-3.3394F, -9.9569F, -110.3108F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.degreeVec(-8.0201F, 1.7287F, -105.0891F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-9.0092F, 14.6284F, -106.3579F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-9.4183F, 1.8361F, -103.5872F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-13.9697F, -37.2999F, -92.5036F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.degreeVec(-13.9697F, -37.2999F, -92.5036F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(9.33F, -20.1086F, -144.4286F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-7.5423F, 1.6008F, -0.26F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.375F, KeyframeAnimations.posVec(-7.1234F, 3.3081F, -0.0141F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4583F, KeyframeAnimations.posVec(-7.1187F, 3.4674F, -0.0641F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.posVec(-7.6061F, 1.8392F, -0.434F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9583F, KeyframeAnimations.posVec(-7.7328F, -0.4088F, -0.5633F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0833F, KeyframeAnimations.posVec(-8.0144F, 1.8031F, -0.4062F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.posVec(-8.0144F, 1.8031F, -0.4062F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.posVec(-7.5423F, 1.6008F, -0.26F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Lantern",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -217.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -22.5F, -215.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5F, KeyframeAnimations.degreeVec(-1.9545F, -21.1194F, -207.3206F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.7917F, KeyframeAnimations.degreeVec(28.6156F, -14.9596F, -210.2896F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(21.7757F, 9.2951F, -215.4071F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.125F, KeyframeAnimations.degreeVec(22.7145F, -0.4561F, -192.003F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.2917F, KeyframeAnimations.degreeVec(12.4824F, -1.8854F, -196.0132F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -217.5F), AnimationChannel.Interpolations.LINEAR)))
			.build();
	public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(1.0F)
			.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, KeyframeAnimations.degreeVec(-102.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-107.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, KeyframeAnimations.degreeVec(-107.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-107.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.9167F, KeyframeAnimations.degreeVec(17.6417F, -7.151F, -2.2671F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("RightArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.7F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, -0.7F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.7F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, KeyframeAnimations.degreeVec(-12.5F, 42.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-14.9172F, 44.451F, -0.6063F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-14.9172F, 44.451F, -0.6063F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-14.9172F, 44.451F, -0.6063F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-11.4746F, 22.5208F, 5.5157F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-3.3226F, -11.8584F, 12.2114F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.9167F, KeyframeAnimations.degreeVec(23.789F, 6.4456F, 20.3152F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.9F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(11.4322F, -18.9696F, 26.8376F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.0833F, KeyframeAnimations.degreeVec(5.9797F, -37.7604F, 34.946F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.25F, KeyframeAnimations.degreeVec(79.4649F, -64.5566F, 3.866F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.6667F, KeyframeAnimations.degreeVec(79.4649F, -64.5566F, 3.866F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-5.7669F, -37.4651F, 14.6333F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-5.0493F, -25.0201F, 13.258F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(11.4322F, -18.9696F, 26.8376F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-1.3F, -3.2766F, 2.2943F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.0833F, KeyframeAnimations.posVec(-2.7F, -4.636F, 5.0325F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, KeyframeAnimations.posVec(-5.4F, -2.8629F, 8.4259F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.6667F, KeyframeAnimations.posVec(-5.4F, -2.8629F, 8.4259F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.8333F, KeyframeAnimations.posVec(0.6F, 0.1079F, 2.9636F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.0F, KeyframeAnimations.posVec(-1.3F, -3.2766F, 2.2943F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Lantern",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.4754F, 0.2899F, -37.6894F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.25F, KeyframeAnimations.degreeVec(21.7122F, 5.9597F, -51.603F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.5833F, KeyframeAnimations.degreeVec(18.7631F, 12.2841F, -69.0525F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.875F, KeyframeAnimations.degreeVec(33.8617F, 3.8882F, -47.0983F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.0F, KeyframeAnimations.degreeVec(22.4754F, 0.2899F, -37.6894F), AnimationChannel.Interpolations.LINEAR)))
			.build();
	public static final AnimationDefinition summon = AnimationDefinition.Builder.withLength(3.5F)
			.addAnimation("RightArm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, KeyframeAnimations.degreeVec(18.8817F, 6.7176F, -18.8818F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.5F, KeyframeAnimations.degreeVec(18.7588F, 1.9846F, -20.499F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(18.7696F, -2.7499F, -22.107F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(2.25F, KeyframeAnimations.degreeVec(26.2588F, 1.9846F, -20.499F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(3.0F, KeyframeAnimations.degreeVec(26.2588F, 1.9846F, -20.499F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.5305F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-121.7902F, -66.4037F, 133.5755F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.75F, KeyframeAnimations.degreeVec(-56.9695F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.degreeVec(-58.5246F, 20.3899F, 8.3785F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.375F, KeyframeAnimations.degreeVec(-69.803F, 19.0938F, 12.1038F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(-69.3538F, 18.6942F, 13.3701F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75F, KeyframeAnimations.degreeVec(-69.3538F, 18.6942F, 13.3701F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.2083F, KeyframeAnimations.degreeVec(20.6462F, 18.6942F, 13.3701F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.9583F, KeyframeAnimations.degreeVec(20.6462F, 18.6942F, 13.3701F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.1667F, KeyframeAnimations.degreeVec(13.8997F, 14.9537F, 9.3073F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.3333F, KeyframeAnimations.degreeVec(26.3487F, -4.3725F, 11.3896F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.4167F, KeyframeAnimations.degreeVec(30.8498F, -4.2644F, 21.2334F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.5F, KeyframeAnimations.degreeVec(25.5305F, 11.3126F, 5.3526F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("LeftArm",
					new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-0.8F, -1.3F, -2.1F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.25F, KeyframeAnimations.posVec(-0.8F, -1.3F, -3.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.5F, KeyframeAnimations.posVec(-0.8F, -1.3F, -2.1F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 47.5F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.75F, KeyframeAnimations.degreeVec(4.9954F, -0.2177F, -40.0093F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5F, KeyframeAnimations.degreeVec(4.8822F, 1.081F, -54.9537F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75F, KeyframeAnimations.degreeVec(4.8822F, 1.081F, -54.9537F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25F, KeyframeAnimations.degreeVec(4.8265F, -13.2405F, -63.678F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.0F, KeyframeAnimations.degreeVec(4.8265F, -13.2405F, -63.678F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 47.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Shovel", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-3.4159F, -4.5461F, 1.8914F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.75F, KeyframeAnimations.posVec(-1.42F, 6.45F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, KeyframeAnimations.posVec(-1.42F, 6.45F, -1.11F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.5F, KeyframeAnimations.posVec(-0.42F, 6.45F, -1.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.75F, KeyframeAnimations.posVec(-2.2708F, 7.134F, -0.7836F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(2.25F, KeyframeAnimations.posVec(-5.8376F, 0.7185F, -1.247F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, KeyframeAnimations.posVec(-5.8376F, 0.7185F, -1.247F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(3.5F, KeyframeAnimations.posVec(-3.4159F, -4.5461F, 1.8914F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Lantern",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -55.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("hat",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3.5F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("hat", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.build();
}