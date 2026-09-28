package net.mcreator.minigames.client;

import com.google.common.reflect.TypeToken;
import net.mcreator.minigames.CustomFireHelper;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.renderstate.AvatarRenderStateModifier;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class CustomFireClientEvents {

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
		Class rawRenderer = EntityRenderer.class;
		event.registerEntityModifier(rawRenderer, (entity, state) -> {
			int color = CustomFireHelper.getFireColor((Entity) entity);
			if (color != 0) {
				((EntityRenderState) state).setRenderData(CustomFireHelper.FIRE_COLOR_KEY, color);
			}
		});

		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
		}, (entity, state) -> {
			int color = CustomFireHelper.getFireColor(entity);
			if (color != 0) {
				state.setRenderData(CustomFireHelper.FIRE_COLOR_KEY, color);
			}
		});

		event.registerAvatarEntityModifier(new AvatarRenderStateModifier() {
			@Override
			public <T extends Avatar & ClientAvatarEntity> void accept(T avatar, AvatarRenderState renderState) {
				if (avatar instanceof Player player) {
					int color = CustomFireHelper.getFireColor(player);
					if (color != 0) {
						renderState.setRenderData(CustomFireHelper.FIRE_COLOR_KEY, color);
					}
				}
			}
		});
	}

	@SubscribeEvent
	public static void onRenderScreenOverlay(RenderBlockScreenEffectEvent event) {
		if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
			Player player = event.getPlayer();
			int color = CustomFireHelper.getFireColor(player);
			boolean lowFire = CustomFireHelper.isLowFire();
			if (color != 0 || lowFire) {
				CustomFireHelper.renderFirstPersonFire(event.getPoseStack(), event.getBufferSource(), event.getSprites(), color);
				event.setCanceled(true);
			}
		}
	}
}
