package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.TeammateHealthSync;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(Dist.CLIENT)
public class TeammateOverlayOverlay {
    private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/teammateoverlay.png");
    private static final Identifier HEART_CONTAINER = Identifier.withDefaultNamespace("hud/heart/container");
    private static final Identifier HEART_FULL = Identifier.withDefaultNamespace("hud/heart/full");
    private static final Identifier HEART_HALF = Identifier.withDefaultNamespace("hud/heart/half");
    private static final Identifier HEART_FULL_BLINKING = Identifier.withDefaultNamespace("hud/heart/full_blinking");
    private static final Identifier HEART_HALF_BLINKING = Identifier.withDefaultNamespace("hud/heart/half_blinking");
    private static final Identifier HEART_POISONED_FULL = Identifier.withDefaultNamespace("hud/heart/poisoned_full");
    private static final Identifier HEART_POISONED_HALF = Identifier.withDefaultNamespace("hud/heart/poisoned_half");
    private static final Identifier HEART_POISONED_FULL_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking");
    private static final Identifier HEART_POISONED_HALF_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking");
    private static final Identifier HEART_WITHERED_FULL = Identifier.withDefaultNamespace("hud/heart/withered_full");
    private static final Identifier HEART_WITHERED_HALF = Identifier.withDefaultNamespace("hud/heart/withered_half");
    private static final Identifier HEART_WITHERED_FULL_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_full_blinking");
    private static final Identifier HEART_WITHERED_HALF_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_half_blinking");
    private static final Identifier HEART_ABSORBING_FULL = Identifier.withDefaultNamespace("hud/heart/absorbing_full");
    private static final Identifier HEART_ABSORBING_HALF = Identifier.withDefaultNamespace("hud/heart/absorbing_half");
    private static final int DAMAGE_FLASH_DURATION_TICKS = 20;
    private static final int HEAL_FLASH_DURATION_TICKS = 10;
    private static final int FLASH_INTERVAL_TICKS = 3;
    private static final int ROW_X = 0;
    private static final int ROW_Y = 3;
    private static final int ROW_WIDTH = 141;
    private static final int ROW_HEIGHT = 48;
    private static final int ROW_SPACING = 2;
    private static final int NAME_Y = 15;
    private static final int CLASS_Y = 25;
    private static final int HEAD_SIZE = 8;
    private static final int HEAD_GAP = 4;
    private static final int HEALTH_BAR_X = 12;
    private static final int HEALTH_BAR_Y = 36;
    private static final int HEART_SIZE = 9;
    private static final int BASE_HEART_ICONS = 10;
    private static final int MAX_HEART_ICONS = 13;
    private static final int HEALTH_BAR_WIDTH = BASE_HEART_ICONS * HEART_SIZE;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFF;
    private static final int UNKNOWN_CLASS_COLOR = 0xAAAAAA;
    private static final int WARRIOR_COLOR = 0xFF5555;
    private static final int THIEF_COLOR = 0xFFAA00;
    private static final int SUPPORT_COLOR = 0x55FFFF;
    private static final int MAGE_COLOR = 0xFF55FF;
    private static final int DEAD_TEXT_COLOR = 0xFF808080;
    private static final int ASCENDING_TEXT_COLOR = 0xFFFFE059; // matches the beam (1.0, 0.88, 0.35)
    private static final Map<Integer, FlashState> FLASH_STATES = new ConcurrentHashMap<>();

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player self = minecraft.player;
        if (self == null || self.level() == null) {
            return;
        }
        if (!MinigamesModVariables.MapVariables.get(self.level()).playingDungeons) {
            return;
        }

        int rowIndex = 0;
        for (Player teammate : self.level().players()) {
            if (teammate == self) {
                continue;
            }

            int rowY = ROW_Y + rowIndex * (ROW_HEIGHT + ROW_SPACING);
            event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, ROW_X, rowY, 0, 0, ROW_WIDTH, ROW_HEIGHT, ROW_WIDTH, ROW_HEIGHT);

            int playerNameColor = getPlayerNameColor(teammate);
            ClassDisplay classDisplay = getClassDisplay(teammate.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon);
            String playerName = teammate.getGameProfile().name();
            String classLabel = classDisplay.label();
            drawCenteredHeader(event, teammate, playerName, classLabel, withFullAlpha(playerNameColor), withFullAlpha(classDisplay.color()), rowY);
            drawHealthBar(event, teammate, rowY);
            rowIndex++;
        }
    }

    private static void drawCenteredHeader(RenderGuiEvent.Pre event, Player teammate, String playerName, String classLabel, int nameColor, int classColor, int rowY) {
        Minecraft minecraft = Minecraft.getInstance();
        int nameWidth = minecraft.font.width(playerName);
        int classWidth = minecraft.font.width(classLabel);
        int nameRowWidth = HEAD_SIZE + HEAD_GAP + nameWidth;
        int centerX = HEALTH_BAR_X + HEALTH_BAR_WIDTH / 2;
        int nameRowX = centerX - nameRowWidth / 2;
        int classX = centerX - classWidth / 2;

        int headX = nameRowX;
        int nameX = headX + HEAD_SIZE + HEAD_GAP;
        int headY = rowY + ((NAME_Y + CLASS_Y - HEAD_SIZE) / 2) - ROW_Y - 1;

        PlayerFaceExtractor.extractRenderState(event.getGuiGraphics(), getPlayerSkin(teammate), headX, headY, HEAD_SIZE);
        event.getGuiGraphics().text(minecraft.font, Component.literal(playerName), nameX, rowY + (NAME_Y - ROW_Y), nameColor, false);
        event.getGuiGraphics().text(minecraft.font, Component.literal(classLabel), classX, rowY + (CLASS_Y - ROW_Y), classColor, false);
    }

    private static PlayerSkin getPlayerSkin(Player player) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            return clientPlayer.getSkin();
        }
        return DefaultPlayerSkin.get(player.getUUID());
    }

    private static void drawHealthBar(RenderGuiEvent.Pre event, Player teammate, int rowY) {
        Minecraft minecraft = Minecraft.getInstance();
        int heartsX = HEALTH_BAR_X;
        int heartsY = rowY + (HEALTH_BAR_Y - ROW_Y);

        if (teammate.hasEffect(MinigamesModMobEffects.ASCENDING)) {
            String text = "Ascending!!!";
            int centerX = HEALTH_BAR_X + HEALTH_BAR_WIDTH / 2;
            int textX = centerX - minecraft.font.width(text) / 2;
            event.getGuiGraphics().text(minecraft.font, Component.literal(text), textX, heartsY, ASCENDING_TEXT_COLOR, false);
            FLASH_STATES.remove(teammate.getId());
            return;
        }

        if (teammate.isSpectator()) {
            String deadText = "Dead";
            int centerX = HEALTH_BAR_X + HEALTH_BAR_WIDTH / 2;
            int textX = centerX - minecraft.font.width(deadText) / 2;
            event.getGuiGraphics().text(minecraft.font, Component.literal(deadText), textX, heartsY, DEAD_TEXT_COLOR, false);
            FLASH_STATES.remove(teammate.getId());
            return;
        }
        TeammateHealthSync.HealthSnapshot snapshot = TeammateHealthSync.get(teammate)
                .orElseGet(() -> new TeammateHealthSync.HealthSnapshot(teammate.getHealth(), teammate.getMaxHealth(), teammate.getAbsorptionAmount()));

        long nowTick = teammate.level().getGameTime();
        FlashState flashState = FLASH_STATES.compute(teammate.getId(), (id, state) -> updateFlashState(state, snapshot, nowTick));
        boolean shouldFlash = flashState != null && nowTick < flashState.flashUntilTick() && ((flashState.flashUntilTick() - nowTick) / FLASH_INTERVAL_TICKS) % 2L == 1L;
        if (snapshot.hurtFlashTicks() > 0 && (snapshot.hurtFlashTicks() / 2) % 2 == 0) {
            shouldFlash = true;
        }

        int maxHalfHearts = Math.max(2, (int) Math.ceil(Math.max(1.0F, snapshot.maxHealth())));
        int healthHalfHearts = Math.max(0, Math.min(maxHalfHearts, (int) Math.ceil(Math.max(0.0F, snapshot.health()))));
        int absorptionHalfHearts = Math.max(0, (int) Math.ceil(Math.max(0.0F, snapshot.absorption())));
        int totalIcons = Math.min(MAX_HEART_ICONS, Math.max(1, (int) Math.ceil((maxHalfHearts + absorptionHalfHearts) / 2.0)));

        Identifier fullHeart = getNormalHeartFull(snapshot, shouldFlash);
        Identifier halfHeart = getNormalHeartHalf(snapshot, shouldFlash);

        for (int i = 0; i < totalIcons; i++) {
            int x = heartsX + i * HEART_SIZE;
            event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, HEART_CONTAINER, x, heartsY, HEART_SIZE, HEART_SIZE);

            int slotStart = i * 2;
            int healthInSlot = Math.max(0, Math.min(2, healthHalfHearts - slotStart));
            if (healthInSlot >= 2) {
                event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, fullHeart, x, heartsY, HEART_SIZE, HEART_SIZE);
            } else if (healthInSlot == 1) {
                event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, halfHeart, x, heartsY, HEART_SIZE, HEART_SIZE);
            }

            int absorbInSlot = Math.max(0, Math.min(2, absorptionHalfHearts - Math.max(0, slotStart - maxHalfHearts)));
            if (slotStart >= maxHalfHearts && absorbInSlot > 0) {
                event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, absorbInSlot >= 2 ? HEART_ABSORBING_FULL : HEART_ABSORBING_HALF, x, heartsY, HEART_SIZE, HEART_SIZE);
            }
        }
    }

    private static FlashState updateFlashState(FlashState state, TeammateHealthSync.HealthSnapshot snapshot, long nowTick) {
        if (state == null) {
            return new FlashState(snapshot.health(), nowTick, snapshot.hurtFlashTicks());
        }
        long flashUntilTick = state.flashUntilTick();
        if (snapshot.health() < state.lastHealth()) {
            flashUntilTick = nowTick + DAMAGE_FLASH_DURATION_TICKS;
        } else if (snapshot.health() > state.lastHealth()) {
            flashUntilTick = nowTick + HEAL_FLASH_DURATION_TICKS;
        }
        if (snapshot.hurtFlashTicks() > state.lastHurtTicks()) {
            flashUntilTick = Math.max(flashUntilTick, nowTick + DAMAGE_FLASH_DURATION_TICKS);
        }
        return new FlashState(snapshot.health(), flashUntilTick, snapshot.hurtFlashTicks());
    }

    private static Identifier getNormalHeartFull(TeammateHealthSync.HealthSnapshot snapshot, boolean flashing) {
        if (snapshot.withered()) {
            return flashing ? HEART_WITHERED_FULL_BLINKING : HEART_WITHERED_FULL;
        }
        if (snapshot.poisoned()) {
            return flashing ? HEART_POISONED_FULL_BLINKING : HEART_POISONED_FULL;
        }
        // Both harmful and non-harmful use the same heart texture, just simplified the logic
        return flashing ? HEART_FULL_BLINKING : HEART_FULL;
    }

    private static Identifier getNormalHeartHalf(TeammateHealthSync.HealthSnapshot snapshot, boolean flashing) {
        if (snapshot.withered()) {
            return flashing ? HEART_WITHERED_HALF_BLINKING : HEART_WITHERED_HALF;
        }
        if (snapshot.poisoned()) {
            return flashing ? HEART_POISONED_HALF_BLINKING : HEART_POISONED_HALF;
        }
        // Both harmful and non-harmful use the same heart texture, just simplified the logic
        return flashing ? HEART_HALF_BLINKING : HEART_HALF;
    }

    private static int getPlayerNameColor(Player player) {
        String color = player.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
        if (color == null || !color.matches("^#?[0-9a-fA-F]{6}$"))
            return DEFAULT_TEXT_COLOR;
        String normalized = color.startsWith("#") ? color.substring(1) : color;
        return Integer.parseInt(normalized, 16);
    }

    private static int withFullAlpha(int color) {
        return (color & 0x00FFFFFF) | 0xFF000000;
    }

    private static ClassDisplay getClassDisplay(String rawClassName) {
        String className = rawClassName == null ? "" : rawClassName.trim().toLowerCase();
        return switch (className) {
            case "warrior" -> new ClassDisplay("Warrior", WARRIOR_COLOR);
            case "thief" -> new ClassDisplay("Thief", THIEF_COLOR);
            case "support" -> new ClassDisplay("Support", SUPPORT_COLOR);
            case "mage" -> new ClassDisplay("Mage", MAGE_COLOR);
            default -> new ClassDisplay("No Class", UNKNOWN_CLASS_COLOR);
        };
    }

    private record ClassDisplay(String label, int color) {
    }

    private record FlashState(float lastHealth, long flashUntilTick, int lastHurtTicks) {
    }
}