package net.mcreator.minigames.photon_plugin;

import com.lowdragmc.photon.command.BlockEffectCommand;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.List;

public final class SummonFXBlockServer {
    private static final Logger LOGGER = LogUtils.getLogger();

    private SummonFXBlockServer() {
    }

    private static Identifier resolveEffectLocation(String name) {
        if (name == null || name.isEmpty()) return null;
        return name.indexOf(':') >= 0 ? Identifier.tryParse(name) : Identifier.tryParse("photon:" + name);
    }

    public static EffectBuilder create(double x, double y, double z) {
        return new EffectBuilder(BlockPos.containing(x, y, z));
    }

    public static EffectBuilder create(BlockPos pos) {
        return new EffectBuilder(pos);
    }

    public static class EffectBuilder {
        private final BlockPos pos;
        private String effectName;
        private Vec3 offset = Vec3.ZERO;
        private Vec3 rotation = Vec3.ZERO;
        private Vec3 scale = new Vec3(1, 1, 1);
        private int delay = 0;
        private boolean forcedDeath = false;
        private boolean allowMulti = false;
        private boolean checkState = false;
        private Integer colorR, colorG, colorB;
        private Integer duration;

        protected EffectBuilder(BlockPos pos) {
            this.pos = pos;
        }

        public EffectBuilder name(String name) {
            this.effectName = name;
            return this;
        }

        public EffectBuilder offset(double x, double y, double z) {
            this.offset = new Vec3(x, y, z);
            return this;
        }

        public EffectBuilder rotation(double x, double y, double z) {
            this.rotation = new Vec3(x, y, z);
            return this;
        }

        public EffectBuilder scale(double x, double y, double z) {
            this.scale = new Vec3(Math.max(0, x), Math.max(0, y), Math.max(0, z));
            return this;
        }

        public EffectBuilder delay(int delayTicks) {
            this.delay = Math.max(0, delayTicks);
            return this;
        }

        public EffectBuilder forcedDeath(boolean forcedDeath) {
            this.forcedDeath = forcedDeath;
            return this;
        }

        public EffectBuilder allowMulti(boolean allowMulti) {
            this.allowMulti = allowMulti;
            return this;
        }

        public EffectBuilder checkState(boolean checkState) {
            this.checkState = checkState;
            return this;
        }

        public EffectBuilder color(double r, double g, double b) {
            this.colorR = Math.clamp((int) Math.round(r), 0, 255);
            this.colorG = Math.clamp((int) Math.round(g), 0, 255);
            this.colorB = Math.clamp((int) Math.round(b), 0, 255);
            return this;
        }

        public EffectBuilder color(int argb) {
            this.colorR = (argb >> 16) & 0xFF;
            this.colorG = (argb >> 8) & 0xFF;
            this.colorB = argb & 0xFF;
            return this;
        }

        public EffectBuilder duration(double ticks) {
            this.duration = Math.max(1, (int) Math.round(ticks));
            return this;
        }

        public EffectBuilder duration(int ticks) {
            this.duration = Math.max(1, ticks);
            return this;
        }

        public void send() {
            try {
                if (this.effectName == null) {
                    this.effectName = "";
                }
                if (this.effectName.isEmpty()) {
                    LOGGER.warn("[SummonFXBlockServer] Effect name not defined (use .name()). Continuing with an empty effect name.");
                }
                if (this.pos == null) {
                    LOGGER.warn("[SummonFXBlockServer] No valid position provided for effect '{}'.", this.effectName);
                    return;
                }

                ServerEffectScheduler.queueServerWork(1, () -> {
                    try {
                        Identifier fxLocation = resolveEffectLocation(this.effectName);
                        if (fxLocation == null) {
                            LOGGER.warn("[SummonFXBlockServer] Invalid effect name '{}' could not be parsed into an Identifier.", this.effectName);
                            return;
                        }

                        var effectPacket = new BlockEffectCommand();
                        effectPacket.setLocation(fxLocation);
                        effectPacket.setPos(this.pos);
                        effectPacket.setOffset(this.offset);
                        effectPacket.setRotation(this.rotation);
                        effectPacket.setScale(this.scale);
                        effectPacket.setDelay(this.delay);
                        effectPacket.setForcedDeath(this.forcedDeath);
                        effectPacket.setAllowMulti(this.allowMulti);
                        effectPacket.setCheckState(this.checkState);
                        PacketDistributor.sendToAllPlayers(effectPacket);

                        if (this.colorR != null || this.duration != null) {
                            var modifyPacket = new ModifyBlockEffectPacket();
                            modifyPacket.setPositions(List.of(this.pos));
                            modifyPacket.setName(this.effectName);
                            if (this.colorR != null) {
                                modifyPacket.setColor(this.colorR, this.colorG, this.colorB);
                            }
                            if (this.duration != null) {
                                modifyPacket.setDuration(this.duration);
                            }
                            PacketDistributor.sendToAllPlayers(modifyPacket);
                        }
                    } catch (Exception e) {
                        LOGGER.error("[SummonFXBlockServer] Critical error while sending packet for effect '{}': {}", this.effectName, e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("[SummonFXBlockServer] Failed to send packet for effect '{}'. Cause: {}", this.effectName, e.getMessage(), e);
            }
        }
    }
}
