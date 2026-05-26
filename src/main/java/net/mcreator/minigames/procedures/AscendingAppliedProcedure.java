package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

import net.mcreator.minigames.network.PlayPlayerAnimationMessage;
import net.mcreator.minigames.ModDataAttachments;
import net.mcreator.minigames.init.MinigamesModSounds;

public class AscendingAppliedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        
        if (entity instanceof Player) {
            if (entity.level().isClientSide()) {
                CompoundTag data = entity.getPersistentData();
                data.putString("PlayerCurrentAnimation", "minigames:ascend");
                data.putBoolean("OverrideCurrentAnimation", true);
                data.putBoolean("FirstPersonAnimation", false);
            } else {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "minigames:ascend", true, false));
            }
        }

        double startX = entity.getX();
        double startY = entity.getY();
        double startZ = entity.getZ();

        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(startX, startY, startZ);
        while (checkPos.getY() > entity.level().getMinY() && entity.level().getBlockState(checkPos).isAir()) {
            checkPos.move(0, -1, 0);
        }

        double groundY = checkPos.getY() + 1.0;
        if (groundY > startY) {
            groundY = startY;
        }

        ModDataAttachments.BeamData beamData = new ModDataAttachments.BeamData(true, startX, groundY, startZ);
        entity.setData(ModDataAttachments.BEAM_DATA, beamData);

        if (!entity.level().isClientSide()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.syncData(ModDataAttachments.BEAM_DATA.get());
            }
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), 
                MinigamesModSounds.ASCENDING.get(), SoundSource.PLAYERS, 2.0f, 1.0f);
        }

        entity.getPersistentData().putDouble("immobileX", startX);
        entity.getPersistentData().putDouble("immobileZ", startZ);
    }
}