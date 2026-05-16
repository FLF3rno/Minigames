/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.minigames.network.VoteYesMessage;
import net.mcreator.minigames.network.VoteNoMessage;
import net.mcreator.minigames.network.DashMessage;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModKeyMappings {
	public static final KeyMapping DASH = new KeyMapping("key.minigames.dash", GLFW.GLFW_KEY_F, "key.categories.movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPacketDistributor.sendToServer(new DashMessage(0, 0));
				DashMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping VOTE_YES = new KeyMapping("key.minigames.vote_yes", GLFW.GLFW_KEY_F9, "key.categories.vote") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPacketDistributor.sendToServer(new VoteYesMessage(0, 0));
				VoteYesMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping VOTE_NO = new KeyMapping("key.minigames.vote_no", GLFW.GLFW_KEY_F12, "key.categories.vote") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ClientPacketDistributor.sendToServer(new VoteNoMessage(0, 0));
				VoteNoMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(DASH);
		event.register(VOTE_YES);
		event.register(VOTE_NO);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				DASH.consumeClick();
				VOTE_YES.consumeClick();
				VOTE_NO.consumeClick();
			}
		}
	}
}