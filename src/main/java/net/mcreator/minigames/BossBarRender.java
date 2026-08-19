package net.mcreator.minigames;

import net.mcreator.minigames.network.MinigamesModVariables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.util.Locale;

@EventBusSubscriber(
		modid = "minigames",
		value = Dist.CLIENT
)
public class BossBarRender {

	private static final int DAMAGE_TICKS = 9;
	private static final int HEAL_TICKS = 12;

	private static float displayedHealth = 1.0f;
	private static float healthStart = 1.0f;
	private static float healthTarget = 1.0f;

	private static int healthAnimationTick = 0;
	private static int healthAnimationDuration = 0;

	private static int currentBossId = -1;

	private static final TagKey<EntityType<?>> DUNGEON_BOSS =
			TagKey.create(
					Registries.ENTITY_TYPE,
					Identifier.fromNamespaceAndPath(
							"minigames",
							"dungeon_boss"
					)
			);

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAboveAll(
				Identifier.fromNamespaceAndPath(
						"minigames",
						"boss_bar"
				),
				(GuiLayer) (graphics, deltaTracker) -> {
					renderBossBar(graphics);
				}
		);
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null || mc.player == null) {
			resetBar();
			return;
		}

		MinigamesModVariables.MapVariables vars =
				MinigamesModVariables.MapVariables.get(mc.level);

		if (!vars.showBossBar) {
			resetBar();
			return;
		}

		LivingEntity boss = getBoss();

		if (boss == null || !boss.isAlive()) {
			resetBar();
			return;
		}

		float newHealth = getHealthPercent(boss);

		if (currentBossId != boss.getId()) {
			currentBossId = boss.getId();

			displayedHealth = newHealth;
			healthStart = newHealth;
			healthTarget = newHealth;

			healthAnimationTick = 0;
			healthAnimationDuration = 0;

			return;
		}

		if (Math.abs(newHealth - healthTarget) > 0.0001f) {
			boolean damage = newHealth < healthTarget;

			healthStart = displayedHealth;
			healthTarget = newHealth;

			healthAnimationTick = 0;
			healthAnimationDuration =
					damage ? DAMAGE_TICKS : HEAL_TICKS;
		}

		if (healthAnimationTick < healthAnimationDuration) {
			healthAnimationTick++;

			float progress =
					healthAnimationTick
							/ (float) healthAnimationDuration;

			progress = Mth.clamp(progress, 0.0f, 1.0f);

			if (healthTarget < healthStart) {
				progress = 1.0f - (float) Math.pow(
						1.0f - progress,
						8.5f
				);
			} else {
				progress =
						progress * progress
								* (3.0f - 2.0f * progress);
			}

			displayedHealth = Mth.lerp(
					progress,
					healthStart,
					healthTarget
			);
		} else {
			displayedHealth = healthTarget;
		}
	}

	private static LivingEntity getBoss() {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null) {
			return null;
		}

		for (Entity entity : mc.level.entitiesForRendering()) {
			if (!(entity instanceof LivingEntity livingEntity)) {
				continue;
			}

			if (entity.getType()
					.builtInRegistryHolder()
					.is(DUNGEON_BOSS)) {

				return livingEntity;
			}
		}

		return null;
	}

	private static float getHealthPercent(LivingEntity boss) {
		float maxHealth = boss.getMaxHealth();

		if (maxHealth <= 0.0f) {
			return 0.0f;
		}

		return Mth.clamp(
				boss.getHealth() / maxHealth,
				0.0f,
				1.0f
		);
	}

	private static void resetBar() {
		currentBossId = -1;

		displayedHealth = 1.0f;
		healthStart = 1.0f;
		healthTarget = 1.0f;

		healthAnimationTick = 0;
		healthAnimationDuration = 0;
	}

	private static void renderBossBar(
			GuiGraphicsExtractor graphics) {

		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null || mc.player == null) {
			return;
		}

		MinigamesModVariables.MapVariables vars =
				MinigamesModVariables.MapVariables.get(mc.level);

		if (!vars.showBossBar) {
			return;
		}

		LivingEntity boss = getBoss();

		if (boss == null || !boss.isAlive()) {
			return;
		}

		if (vars.bossName == null || vars.bossName.isEmpty()) {
			return;
		}

		String textureName = vars.bossName
				.toLowerCase(Locale.ROOT)
				.replace(' ', '_');

		Identifier emptyTexture =
				Identifier.fromNamespaceAndPath(
						"minigames",
						"textures/gui/sprites/boss_bar/"
								+ textureName
								+ "_empty.png"
				);

		Identifier fullTexture =
				Identifier.fromNamespaceAndPath(
						"minigames",
						"textures/gui/sprites/boss_bar/"
								+ textureName
								+ ".png"
				);

		int screenWidth = graphics.guiWidth();

		int barWidth = 300;
		int barHeight = 32;

		int x = screenWidth / 2 - barWidth / 2;
		int y = 20;

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				emptyTexture,
				x,
				y,
				0,
				0,
				barWidth,
				barHeight,
				barWidth,
				barHeight
		);

		int healthWidth =
				(int) (barWidth * displayedHealth);

		if (healthWidth > 0) {
			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					fullTexture,
					x,
					y,
					0,
					0,
					healthWidth,
					barHeight,
					barWidth,
					barHeight
			);
		}

		Component bossName = Component.literal(vars.bossName)
				.withStyle(style -> style
						.withColor(0xFFF1F3BE)
						.withBold(true)
				);

		int nameWidth = mc.font.width(bossName);

		float nameScale = 1.0f;

		int nameX = (int)(
				screenWidth / 2.0f
						- (nameWidth * nameScale) / 2.0f
		);

		int nameY = y - mc.font.lineHeight - 4;

		graphics.pose().pushMatrix();

		graphics.pose().translate(
				nameX,
				nameY
		);

		graphics.pose().scale(
				nameScale,
				nameScale
		);

		graphics.text(
				mc.font,
                bossName,
				0,
				0,
				0xFFFFFFFF,
				true
		);

		graphics.pose().popMatrix();
	}
}