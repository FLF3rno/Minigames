package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.minigames.procedures.PriestOnEntityTickUpdateProcedure;
import net.mcreator.minigames.entity.PriestEntity;
import net.mcreator.minigames.client.model.animations.priestAnimation;
import net.mcreator.minigames.client.model.Modelpriest;

import java.util.Map;

import org.joml.Quaternionf;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.mojang.blaze3d.vertex.PoseStack;

public class PriestRenderer extends MobRenderer<PriestEntity, LivingEntityRenderState, Modelpriest> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/priest.png");

	public PriestRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelpriest.LAYER_LOCATION)), 0f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/priest_emissive.png");
			final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor);
			}
		});
	}

	@Override

	public boolean shouldRender(PriestEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ, float partialTicks) {
		return true;
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(PriestEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		updatePriestFx(entity, partialTicks);
	}

	private static void updatePriestFx(PriestEntity entity, float partialTicks) {
		int activeTick = entity.getEntityData().get(PriestEntity.DATA_activeTick);
		int targetId = entity.getEntityData().get(PriestEntity.DATA_target_id);
		if (!entity.isAlive() || activeTick <= 74 || targetId < 0) {
			entity.clientVisualP3 = null;
			return;
		}
		Entity healTarget = entity.level().getEntity(targetId);
		if (healTarget == null || !healTarget.isAlive()) {
			entity.clientVisualP3 = null;
			return;
		}
		Vec3 entityPos = entity.getPosition(partialTicks);
		Vec3 eyePos = entity.getEyePosition(partialTicks);
		Vec3 targetPos = healTarget.getPosition(partialTicks);
		Vec3 look = entity.getLookAngle();
		Vec3 forward = new Vec3(look.x, 0, look.z);
		forward = forward.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 1) : forward.normalize();
		double smoothTick = activeTick + partialTicks;
		double pearlY = entityPos.y + 1.25 + PriestOnEntityTickUpdateProcedure.getPearlAnimOffsetY(smoothTick);
		Vec3 P0 = new Vec3(entityPos.x, pearlY, entityPos.z).add(forward.scale(0.875));
		Vec3 targetCenter = new Vec3(targetPos.x, targetPos.y + healTarget.getBbHeight() * 0.5, targetPos.z);
		if (entity.clientVisualP3 == null) {
			entity.clientVisualP3 = targetCenter;
		} else {
			entity.clientVisualP3 = entity.clientVisualP3.lerp(targetCenter, 0.25);
		}
		double rawDist = P0.distanceTo(entity.clientVisualP3);
		Vec3 toTargetInit = entity.clientVisualP3.subtract(P0);
		Vec3 toTargetHInit = new Vec3(toTargetInit.x, 0, toTargetInit.z);
		Vec3 inDirInit = toTargetHInit.lengthSqr() < 1.0E-4 ? forward : toTargetHInit.normalize();
		double targetRadius = (healTarget.getBbWidth() * 0.5) + 0.15;
		Vec3 P3 = rawDist > targetRadius + 0.5 ? entity.clientVisualP3.subtract(inDirInit.scale(targetRadius)) : entity.clientVisualP3;
		double dist = P0.distanceTo(P3);
		if (dist <= 0.5) {
			return;
		}
		double lead = Math.min(3.5, Math.max(1.2, dist * 0.35));
		Vec3 P1 = P0.add(forward.scale(lead));
		Vec3 toTarget = P3.subtract(P1);
		Vec3 targetIn = new Vec3(toTarget.x, 0, toTarget.z);
		targetIn = targetIn.lengthSqr() < 1.0E-4 ? forward : targetIn.normalize();
		Vec3 P2 = P3.subtract(targetIn.scale(lead * 0.6));
		int cycleTicks = 40;
		double t = ((smoothTick - 75.0) % cycleTicks) / cycleTicks;
		if (t < 0)
			t += 1.0;
		double u = 1.0 - t;
		double tt = t * t;
		double uu = u * u;
		double uuu = uu * u;
		double ttt = tt * t;
		Vec3 pt = P0.scale(uuu).add(P1.scale(3 * uu * t)).add(P2.scale(3 * u * tt)).add(P3.scale(ttt));
		Vec3 tangent = P1.subtract(P0).scale(3 * uu).add(P2.subtract(P1).scale(6 * u * t)).add(P3.subtract(P2).scale(3 * tt));
		if (tangent.lengthSqr() < 1.0E-6) {
			tangent = P3.subtract(P0);
		}
		Vec3 dir = tangent.normalize();
		double hDist = Math.hypot(dir.x, dir.z);
		double rotY = Math.toDegrees(Math.atan2(-dir.z, dir.x));
		double rotZ = -Math.toDegrees(Math.atan2(hDist, dir.y));
		Vec3 relOffset = pt.subtract(eyePos);
		var activeEffects = EntityEffectExecutor.CACHE.get(entity);
		if (activeEffects != null && !activeEffects.isEmpty()) {
			Identifier priestFx = Identifier.parse("photon:priest");
			for (var effect : activeEffects) {
				if (effect != null && effect.getFx() != null && priestFx.equals(effect.getFx().getFxLocation())) {
					effect.setOffset(relOffset.x, relOffset.y, relOffset.z);
					effect.setRotation(0, rotY, rotZ);
					var runtime = effect.getRuntime();
					if (runtime != null && runtime.isValid()) {
						runtime.getRoot().updateRotation(new Quaternionf().rotationXYZ(0f, (float) Math.toRadians(rotY), (float) Math.toRadians(rotZ)));
					}
				}
			}
		}
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		PriestEntity entity = (PriestEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "priest")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelpriest {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(priestAnimation.spawn);
			this.keyframeAnimation1 = safeBake(priestAnimation.idle);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(root);
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(root);
			}
		}

		@Override
		public void setupAnim(LivingEntityRenderState state) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			PriestEntity entity = state.getRenderData(ENTITY_KEY);
			if (entity != null) {
				this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
				this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
			}
			super.setupAnim(state);
		}
	}

	public static final ContextKey<PriestEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:priest_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(PriestRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}