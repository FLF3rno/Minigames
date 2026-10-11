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

public class GoldenTextureManager {
    private static final Identifier GOLD_MATERIAL_LOCATION = Identifier.fromNamespaceAndPath("minigames", "textures/entity/gold_material.png");
    private static final Map<Identifier, Identifier> CACHE = new ConcurrentHashMap<>();
    private static NativeImage goldMaterial = null;

    private static synchronized NativeImage getGoldMaterial() {
        if (goldMaterial == null) {
            try {
                Optional<Resource> res = Minecraft.getInstance().getResourceManager().getResource(GOLD_MATERIAL_LOCATION);
                if (res.isPresent()) {
                    try (InputStream is = res.get().open()) {
                        goldMaterial = NativeImage.read(is);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return goldMaterial;
    }

    public static Identifier getGoldenTexture(Identifier original) {
        if (original == null) {
            return GOLD_MATERIAL_LOCATION;
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
                return GOLD_MATERIAL_LOCATION;
            }

            int w = baseImage.getWidth();
            int h = baseImage.getHeight();
            NativeImage goldenImage = new NativeImage(w, h, true);
            NativeImage gold = getGoldMaterial();
            int gw = gold != null ? gold.getWidth() : 16;
            int gh = gold != null ? gold.getHeight() : 16;

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int basePixel = baseImage.getPixel(x, y);
                    int alpha = ARGB.alpha(basePixel);
                    if (alpha <= 5) {
                        goldenImage.setPixel(x, y, 0);
                    } else {
                        int br = ARGB.red(basePixel);
                        int bg = ARGB.green(basePixel);
                        int bb = ARGB.blue(basePixel);

                        int gr, gg, gb;
                        if (gold != null) {
                            int goldPixel = gold.getPixel(x % gw, y % gh);
                            gr = ARGB.red(goldPixel);
                            gg = ARGB.green(goldPixel);
                            gb = ARGB.blue(goldPixel);
                        } else {
                            gr = 245;
                            gg = 204;
                            gb = 39;
                        }

                        int finalR = Math.min(255, (int)(gr * 0.70f + br * 0.30f));
                        int finalG = Math.min(255, (int)(gg * 0.70f + bg * 0.30f));
                        int finalB = Math.min(255, (int)(gb * 0.70f + bb * 0.30f));

                        goldenImage.setPixel(x, y, ARGB.color(alpha, finalR, finalG, finalB));
                    }
                }
            }

            String sanitizedPath = ("golden/" + original.getNamespace() + "_" + original.getPath()).toLowerCase().replaceAll("[^a-z0-9_./-]", "_");
            Identifier dynamicId = Identifier.fromNamespaceAndPath("minigames", sanitizedPath);
            DynamicTexture dynamicTexture = new DynamicTexture(() -> "golden_" + original, goldenImage);
            Minecraft.getInstance().getTextureManager().register(dynamicId, dynamicTexture);
            CACHE.put(original, dynamicId);
            return dynamicId;
        } catch (Exception e) {
            return GOLD_MATERIAL_LOCATION;
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
