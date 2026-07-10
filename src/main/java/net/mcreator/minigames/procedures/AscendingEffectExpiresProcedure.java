package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.mcreator.minigames.network.PlayPlayerAnimationMessage;
import net.mcreator.minigames.network.MinigamesModVariables;

public class AscendingEffectExpiresProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;

        if (!entity.level().isClientSide() && entity.getServer() != null) {
            String command = "/execute if entity @a[nbt=!{active_effects:[{id:\"minigames:ascending\"}]}] run stopsound @a * minigames:ascending";
            String command2 = "/playsound minecraft:block.glass.break player @a ~ ~ ~ 2 0.7";
            entity.getServer().getCommands().performPrefixedCommand(
                new CommandSourceStack(CommandSource.NULL, entity.position(), Vec2.ZERO, (ServerLevel) entity.level(), 4, "", entity.getDisplayName(), entity.level().getServer(), null),
                command
            );
            entity.getServer().getCommands().performPrefixedCommand(
                new CommandSourceStack(CommandSource.NULL, entity.position(), Vec2.ZERO, (ServerLevel) entity.level(), 4, "", entity.getDisplayName(), entity.level().getServer(), null),
                command2
            );
        }

        if (entity instanceof Player) {
            if (entity.level().isClientSide()) {
                CompoundTag data = entity.getPersistentData();
                data.remove("PlayerCurrentAnimation");
                data.putBoolean("ResetPlayerAnimation", true);
            } else {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), 
                    new PlayPlayerAnimationMessage(entity.getId(), "", false, false));
            }
        }
        
        entity.getPersistentData().remove("immobileX");
        entity.getPersistentData().remove("immobileZ");
        entity.getPersistentData().remove("lockYRot");
        MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
        _vars.ascendingActive = false;
        if (_vars.ascendingTimer < 0) {
            _vars.ascendingTimer = 0;
        }
        _vars.markSyncDirty();

    }
}
