package net.mcreator.minigames;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

public class AnimationScreenTrigger {

    public static void startAnimation(int length, String animationType, float speed) {
        Minecraft.getInstance().execute(() -> {
            int x = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int y = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            AnimationManager manager = new AnimationManager(length, speed);

            Entity p1 = null;
            Entity p2 = null;
            Entity p3 = null;
            Entity p4 = null;
            for (Entity entityiterator : new ArrayList<>(Minecraft.getInstance().level.players())) {
                if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1) {
                    p1 = entityiterator;
                } else if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 2) {
                    p2 = entityiterator;
                } else if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 3) {
                    p3 = entityiterator;
                } else if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 4) {
                    p4 = entityiterator;
                }
            }
            Identifier MATCHA_FONT =
                    Identifier.fromNamespaceAndPath(
                            "minigames",
                            "matcha_mint"
                    );

            if (animationType.equalsIgnoreCase("template")) {
                Identifier logo = Identifier.fromNamespaceAndPath("minigames", "textures/screens/logo");

                    AnimationManager.displayTransform(
                        0, 50, Component.literal("Take no damage").withStyle(Style.EMPTY.withColor(0xFFFFFFFF)),
                        0, 0, 100, 100,         // Move from (0,0) to (100,100)
                        0.5f, 2.0f,             // Scale from 0.5x to 2.0x
                        0f, 360f,               // Spin a full circle (0 to 360 degrees)
                        "easeInOut", 5          // Interpolation & Layer
                );

                AnimationManager.displayRotate(0, 40, logo, 50, 50, 0f, 180f, "linear", 1);
            }
            else if (animationType.equalsIgnoreCase("crown")) {
                int offsetX = 0;
                int offsetY = -20;
                int centerX = (x / 2) - 8 + offsetX;
                int centerY = (y / 2) - 8 + offsetY;
                Identifier crown = Identifier.fromNamespaceAndPath("minigames", "textures/animation/crown/crown.png");
                Identifier crownSlash = Identifier.fromNamespaceAndPath("minigames", "textures/animation/crown/crown_slashed.png");
                Identifier crownCrack = Identifier.fromNamespaceAndPath("minigames", "textures/animation/crown/crown_cracked.png");
                Identifier sword = Identifier.fromNamespaceAndPath("minigames", "textures/animation/crown/diamond_sword.png");

                AnimationManager.displayTransform(0, 50, crown,
                        centerX + 20, centerY - 40, centerX - 40, centerY - 70,
                        3f, 3f,
                        0f, -17f,
                        "easeOut", 3
                );
                AnimationManager.displayTransform(0, 47, sword,
                        centerX + 40, centerY - 30, centerX + 50, centerY - 40,
                        3f, 3f,
                        -30f, 6f,
                        "easeOut", 4
                );
                AnimationManager.displayTransform(47, 50, sword,
                        centerX + 50, centerY - 40, centerX - 40, centerY - 70,
                        3f, 3f,
                        6f, -70f,
                        "linear", 4
                );
                AnimationManager.displayStatic(50, 60, crownSlash, centerX - 40, centerY - 70, 3f, -17f, 3);
                AnimationManager.displayStatic(50, 60, sword, centerX - 40, centerY - 70, 3f, -70f, 4);
                AnimationManager.playSound(50, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:pvp_swordshit")), 1.0f, 2f);
                AnimationManager.playSound(60, SoundEvents.ITEM_BREAK, 0.7f, 1.0f);
                AnimationManager.displayTransform(60, 100, crownCrack,
                        centerX - 40, centerY - 70, centerX, centerY - 30,
                        3f, 3f,
                        -17f, -17f,
                        "linear", 3
                );
                AnimationManager.displayTransform(60, 100, sword,
                        centerX - 40, centerY - 70, centerX - 80, centerY - 130,
                        3f, 3f,
                        -70f, -130f,
                        "easeOut", 2
                );
            }
            else if (animationType.equalsIgnoreCase("roguelike_boss")) {

                Identifier bossTexture = Identifier.fromNamespaceAndPath("minigames", "textures/animation/roguelike/boss/" + MinigamesModVariables.MapVariables.get(Minecraft.getInstance().level).bossName + ".png");
                AnimationManager.displayColor(0, length, 0xFF000000, 0);

                if (p4 != null) {
                    AnimationManager.displayEntity(
                            0, length, p1,
                            100, 145, 100, 145,
                            50f, 50f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p2,
                            100, 245, 100, 245,
                            50f, 50f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p3,
                            100, 345, 100, 345,
                            50f, 50f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p4,
                            100, 445, 100, 445,
                            50f, 50f,
                            180f, 180f,
                            "linear", 5
                    );
                } else if (p3 != null) {
                    AnimationManager.displayEntity(
                            0, length, p1,
                            100, 165, 100, 165,
                            65f, 65f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p2,
                            100, 295, 100, 295,
                            65f, 65f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p3,
                            100, 435, 100, 435,
                            65f, 65f,
                            180f, 180f,
                            "linear", 5
                    );
                } else if (p2 != null) {
                    AnimationManager.displayEntity(
                            0, length, p1,
                            100, 265, 100, 265,
                            80f, 80f,
                            180f, 180f,
                            "linear", 5
                    );
                    AnimationManager.displayEntity(
                            0, length, p2,
                            100, 435, 100, 435,
                            80f, 80f,
                            180f, 180f,
                            "linear", 5
                    );
                } else {
                    AnimationManager.displayEntity(
                            0, length, p1,
                            100, 315, 100, 315,
                            90f, 90f,
                            180f, 180f,
                            "linear", 5
                    );
                }

                AnimationManager.displayTransform(
                        0, length, Component.literal("VS")
                                .withStyle(Style.EMPTY
                                        .withColor(0xFFF1F3BE)
                                        .withFont(new FontDescription.Resource(MATCHA_FONT))
                                ),
                        x / 2 - 48, y / 2, x / 2 - 48, y / 2, 0.5f, 0.5f,
                        0f, 0f, "easeInOut", 6
                );
                int bossCenterX = x / 2 + 60;
                int bossCenterY = (int) (y / 1.65);

                Component bossName = Component.literal(
                        MinigamesModVariables.MapVariables
                                .get(Minecraft.getInstance().level)
                                .bossName).withStyle(style -> style
                        .withColor(0xFFF1F3BE)
                        .withFont(
                                new FontDescription.Resource(MATCHA_FONT))
                );

                AnimationManager.displayCenteredTextWrapped(
                        0, length,
                        bossName,
                        bossCenterX,
                        bossCenterY,
                        700,
                        0.3f, 0.3f,
                        0f, 0f,
                        "easeInOut",
                        6
                );


                Component playerName = p1.getDisplayName().copy().withStyle(
                        style -> style
                                .withColor(0xFFF1F3BE)
                                .withFont(new FontDescription.Resource(MATCHA_FONT))
                );

                AnimationManager.displayCenteredText(
                        0, length,
                        playerName,
                        x / 2 - 68,
                        (int) (y / 2.7),
                        0.3f, 0.3f,
                        0f, 0f,
                        "easeInOut",
                        6
                );
                if (p2 != null) {
                    playerName = p2.getDisplayName().copy().withStyle(
                            style -> style
                                    .withColor(0xFFF1F3BE)
                                    .withFont(new FontDescription.Resource(MATCHA_FONT))
                    );

                    AnimationManager.displayCenteredText(
                            0, length,
                            playerName,
                            x / 2 - 68,
                            (int) (y / 2.9),
                            0.3f, 0.3f,
                            0f, 0f,
                            "easeInOut",
                            6
                    );
                }
                if (p3 != null) {
                    playerName = p3.getDisplayName().copy().withStyle(
                            style -> style
                                    .withColor(0xFFF1F3BE)
                                    .withFont(new FontDescription.Resource(MATCHA_FONT))
                    );

                    AnimationManager.displayCenteredText(
                            0, length,
                            playerName,
                            x / 2 - 68,
                            (int) (y / 3.1),
                            0.3f, 0.3f,
                            0f, 0f,
                            "easeInOut",
                            6
                    );
                }
                if (p4 != null) {
                    playerName = p4.getDisplayName().copy().withStyle(
                            style -> style
                                    .withColor(0xFFF1F3BE)
                                    .withFont(new FontDescription.Resource(MATCHA_FONT))
                    );

                    AnimationManager.displayCenteredText(
                            0, length,
                            playerName,
                            x / 2 - 68,
                            (int) (y / 3.3),
                            0.3f, 0.3f,
                            0f, 0f,
                            "easeInOut",
                            6
                    );
                }

                AnimationManager.displayTransform(
                        0, length,
                        bossTexture,
                        512, 512,
                        x - 275 , y /4,
                        x - 275, y / 4,
                        0.5f, 0.5f,
                        0f, 0f,
                        "linear",
                        10
                );

            }
            else if (animationType.equalsIgnoreCase("fade_in")) {
                AnimationManager.fadeIn(0, 20, 0x000000, 10);
            }
            else if (animationType.equalsIgnoreCase("fade_out")) {
                AnimationManager.fadeOut(0, 20, 0x000000, 10);
            }
            else if (animationType.equalsIgnoreCase("fade_in_ascend")) {
                Identifier square = Identifier.fromNamespaceAndPath("minigames", "textures/screens/square.png");

                AnimationManager.fadeIn(60, 140, 0x000000, 0);
                AnimationManager.displayColor(140, length, 0xFF000000, 0);
                // da 180 text per 5s e poi inizia l'azione
                Component ASCEND = Component.literal("ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND ASCEND")
                        .withStyle(style -> style
                        .withColor(0xFFF1F3BE)
                        //.withFont(new FontDescription.Resource(MATCHA_FONT))
                );
                Component GET_TOP = Component.literal("GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP GET TO THE TOP ")
                        .withStyle(style -> style
                        .withColor(0xFFF1F3BE)
                        //.withFont(new FontDescription.Resource(MATCHA_FONT))
                        );
                float lineamount = 8f;
                for (float iteration = -lineamount; iteration < lineamount * 2 + 1; iteration ++) {
                    if (iteration % 2 == 0) {
                        AnimationManager.displayCenteredTextWrappedScroll(
                                180, length,
                                ASCEND,
                                x / 2, (int) (y - y * (iteration / lineamount)),
                                4000,
                                5f, 5f,
                                30f, 30f,
                                "linear",
                                0.4f, 6
                        );
                    } else {
                        AnimationManager.displayCenteredTextWrappedScroll(
                                180, length,
                                GET_TOP,
                                x / 2, (int) (y - y * (iteration / lineamount)),
                                4000,
                                5f, 5f,
                                30f, 30f,
                                "linear",
                                -0.2f, 6
                        );
                    }
                }
                int color = 0xFFFFFFFF;
                AnimationManager.displayTransform(
                        240, length,
                        square,
                        48, 48,
                        x / 2, y / 2,
                        x / 2, y / 2,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        color,
                        color,
                        "linear",
                        10
                );
            }
            AnimationOverlay.addManager(manager);
        });
    }
}