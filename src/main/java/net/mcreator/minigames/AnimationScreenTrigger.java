package net.mcreator.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class AnimationScreenTrigger {

    public static void startAnimation(int length, String animationType, float speed) {
        Minecraft.getInstance().execute(() -> {
            int x = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int y = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            AnimationManager manager = new AnimationManager(length, speed);

            if (animationType.equalsIgnoreCase("template")) {
                Identifier logo = Identifier.fromNamespaceAndPath("minigames", "textures/screens/logo");
                Identifier banner = Identifier.fromNamespaceAndPath("minigames", "textures/screens/banner.png");

                //static display from tick 0 to 40
                AnimationManager.displayStatic(0, 40, banner, 50, 50, 2, 0);

                //sliding movement from tick 20 to 80
                AnimationManager.displayMove(20, 80, logo, 0, 100, 200, 100, 1, "easeInOut", 0);

                //scaling effect from tick 50 to 100
                AnimationManager.displayScale(50, 100, logo, 150, 150, 0.5f, 2.0f, "easeOut", 1);
                AnimationManager.displayRotate(0, 40, logo, 50, 50, 0f, 180f, "linear", 1);
                // move, scale, AND rotate all at the same time:
                AnimationManager.displayTransform(
                        0, 50, logo,
                        0, 0, 100, 100,         // Move from (0,0) to (100,100)
                        0.5f, 2.0f,             // Scale from 0.5x to 2.0x
                        0f, 360f,               // Spin a full circle (0 to 360 degrees)
                        "easeInOut", 5          // Interpolation & Layer
                );

                //spin in place:
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

            AnimationOverlay.addManager(manager);
        });
    }
}