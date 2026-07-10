package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.entity.CandleheadEntity;
import net.mcreator.minigames.entity.VolcanicSpewEntity;
import net.mcreator.minigames.init.MinigamesModEntities;

public class CandleheadOnEntityTickUpdateProcedure {
    public static void execute(Entity entity) {
        if (entity == null) return;

        Level world = entity.level(); 
        double x = entity.getX();     
        double y = entity.getY();
        double z = entity.getZ();

        if (entity instanceof CandleheadEntity _datEntL0 && _datEntL0.getEntityData().get(CandleheadEntity.DATA_ready)) {
            if (!world.isClientSide() && entity.level().getServer() != null) {
                entity.level().getServer().getCommands().performPrefixedCommand(
                    new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world instanceof ServerLevel ? (ServerLevel) world : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), 
                    "particle minecraft:flame ~ ~2.5 ~ 0.1 0.1 0.1 0 3 normal @a"
                );
            }

            // attack code
            if (entity instanceof LivingEntity _livEnt2 && _livEnt2.swinging) {
                if (entity instanceof CandleheadEntity _datEntSetL)
                    _datEntSetL.getEntityData().set(CandleheadEntity.DATA_ready, false);
                if (entity instanceof CandleheadEntity _datEntSetI)
                    _datEntSetI.getEntityData().set(CandleheadEntity.DATA_COOLDOWN, 120);

                if (!world.isClientSide()) {
                    world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                }

                if (world instanceof ServerLevel projectileLevel) {
                    for (int index0 = 0; index0 < 30; index0++) {
                        VolcanicSpewEntity _entityToSpawn = new VolcanicSpewEntity(
                            MinigamesModEntities.VOLCANIC_SPEW.get(), 
                            projectileLevel
                        );
                        _entityToSpawn.setOwner(entity);
                        _entityToSpawn.setPos(x, y + entity.getEyeHeight(), z);
                        _entityToSpawn.shoot(0, 1, 0, 0.5F, 50F);
                        _entityToSpawn.pickup = AbstractArrow.Pickup.DISALLOWED;
                        projectileLevel.addFreshEntity(_entityToSpawn);
                    }
                }
            }
        } else if ((entity instanceof CandleheadEntity _datEntI ? _datEntI.getEntityData().get(CandleheadEntity.DATA_COOLDOWN) : 0) > 0) {
            if (entity instanceof CandleheadEntity _datEntSetI)
                _datEntSetI.getEntityData().set(CandleheadEntity.DATA_COOLDOWN, 
                (int) ((entity instanceof CandleheadEntity _datEntI ? _datEntI.getEntityData().get(CandleheadEntity.DATA_COOLDOWN) : 0) - 1));
        } else {
            if (entity instanceof CandleheadEntity _datEntSetL)
                _datEntSetL.getEntityData().set(CandleheadEntity.DATA_ready, true);
        }
    }
}



