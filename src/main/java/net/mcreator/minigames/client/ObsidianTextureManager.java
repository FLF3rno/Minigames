package net.mcreator.minigames.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.ARGB;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ObsidianTextureManager {
    private static final Identifier OBSIDIAN_MATERIAL_LOCATION = Identifier.fromNamespaceAndPath("minigames", "textures/entity/obsidian_material.png");
    private static final Identifier VANILLA_OBSIDIAN_LOCATION = Identifier.fromNamespaceAndPath("minecraft", "textures/block/obsidian.png");
    private static final Map<Identifier, Identifier> CACHE = new ConcurrentHashMap<>();
    private static NativeImage obsidianMaterial = null;

    private static synchronized NativeImage getObsidianMaterial() {
        if (obsidianMaterial == null) {
            try {
                Optional<Resource> res = Minecraft.getInstance().getResourceManager().getResource(OBSIDIAN_MATERIAL_LOCATION);
                if (!res.isPresent()) {
                    res = Minecraft.getInstance().getResourceManager().getResource(VANILLA_OBSIDIAN_LOCATION);
                }
                if (res.isPresent()) {
                    try (InputStream is = res.get().open()) {
                        obsidianMaterial = NativeImage.read(is);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return obsidianMaterial;
    }

    public static Identifier getObsidianTexture(Identifier original) {
        if (original == null) {
            return OBSIDIAN_MATERIAL_LOCATION;
        }
        Identifier cached = CACHE.get(original);
        if (cached != null) {
            return cached;
        }

        NativeImage baseImage = null;
        boolean mustCloseBase = false;
        try {
            AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(original);
            if (tex instanceof DynamicTexture dt && dt.getPixels() != null && !dt.getPixels().isClosed()) {
                baseImage = dt.getPixels();
            } else {
                String path = original.getPath();
                String normPath = path;
                if (!normPath.startsWith("textures/")) {
                    normPath = "textures/" + normPath;
                }
                if (!normPath.endsWith(".png")) {
                    normPath = normPath + ".png";
                }
                Identifier fixed = Identifier.fromNamespaceAndPath(original.getNamespace(), normPath);

                AbstractTexture fixedTex = Minecraft.getInstance().getTextureManager().getTexture(fixed);
                if (fixedTex instanceof DynamicTexture dt && dt.getPixels() != null && !dt.getPixels().isClosed()) {
                    baseImage = dt.getPixels();
                } else {
                    Optional<Resource> resOpt = Minecraft.getInstance().getResourceManager().getResource(original);
                    if (!resOpt.isPresent()) {
                        resOpt = Minecraft.getInstance().getResourceManager().getResource(fixed);
                    }
                    if (!resOpt.isPresent() && !original.getPath().endsWith(".png")) {
                        Identifier withPng = Identifier.fromNamespaceAndPath(original.getNamespace(), original.getPath() + ".png");
                        resOpt = Minecraft.getInstance().getResourceManager().getResource(withPng);
                    }
                    if (resOpt.isPresent()) {
                        InputStream is = resOpt.get().open();
                        baseImage = NativeImage.read(is);
                        mustCloseBase = true;
                    }
                }
            }

            if (baseImage == null) {
                // If the base image is not resolved, fallback to the obsidian material directly
                return OBSIDIAN_MATERIAL_LOCATION;
            }

            int w = baseImage.getWidth();
            int h = baseImage.getHeight();
            NativeImage obsidianImage = new NativeImage(w, h, true);
            NativeImage obsidian = getObsidianMaterial();
            int ow = obsidian != null ? obsidian.getWidth() : 16;
            int oh = obsidian != null ? obsidian.getHeight() : 16;

            boolean isPlayerSkin = (w == 64 && (h == 64 || h == 32));

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int basePixel = baseImage.getPixel(x, y);
                    int alpha = ARGB.alpha(basePixel);

                    // Ensure player's core body boxes are 100% solid obsidian with zero transparent gaps
                    boolean isBaseBodyPixel = false;
                    if (isPlayerSkin) {
                        // Head base: (0, 0) to (31, 15)
                        // Body base: (16, 16) to (39, 31)
                        // Right arm base: (40, 16) to (55, 31)
                        // Right leg base: (0, 16) to (15, 31)
                        // Left leg base: (16, 48) to (31, 63)
                        // Left arm base: (32, 48) to (47, 63)
                        if ((x < 32 && y < 16)
                                || (x >= 16 && x < 40 && y >= 16 && y < 32)
                                || (x >= 40 && x < 56 && y >= 16 && y < 32)
                                || (x < 16 && y >= 16 && y < 32)
                                || (h == 64 && x >= 16 && x < 32 && y >= 48 && y < 64)
                                || (h == 64 && x >= 32 && x < 48 && y >= 48 && y < 64)) {
                            isBaseBodyPixel = true;
                        }
                    }

                    if (alpha <= 5 && !isBaseBodyPixel) {
                        obsidianImage.setPixel(x, y, 0);
                    } else {
                        int or, og, ob;
                        if (obsidian != null) {
                            int obsPixel = obsidian.getPixel(x % ow, y % oh);
                            or = ARGB.red(obsPixel);
                            og = ARGB.green(obsPixel);
                            ob = ARGB.blue(obsPixel);
                        } else {
                            or = 20;
                            og = 18;
                            ob = 29;
                        }

                        // Pure solid obsidian block material with 100% opacity (no skin bleed-through, no transparency)
                        obsidianImage.setPixel(x, y, ARGB.color(255, or, og, ob));
                    }
                }
            }

            String sanitizedPath = ("obsidian/" + original.getNamespace() + "_" + original.getPath()).toLowerCase().replaceAll("[^a-z0-9_./-]", "_");
            Identifier dynamicId = Identifier.fromNamespaceAndPath("minigames", sanitizedPath);
            DynamicTexture dynamicTexture = new DynamicTexture(() -> "obsidian_" + original, obsidianImage);
            Minecraft.getInstance().getTextureManager().register(dynamicId, dynamicTexture);
            CACHE.put(original, dynamicId);
            return dynamicId;
        } catch (Exception e) {
            return OBSIDIAN_MATERIAL_LOCATION;
        } finally {
            if (mustCloseBase && baseImage != null) {
                try {
                    baseImage.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void clearCache() {
        CACHE.clear();
    }
}
