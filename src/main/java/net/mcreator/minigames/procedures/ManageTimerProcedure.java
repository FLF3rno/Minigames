package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ManageTimerProcedure {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event, event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null || world.isClientSide())
            return;

        if (MinigamesModVariables.MapVariables.get(world).ShowTimer) {
            MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
            
            _vars.timerTick += _vars.timerSpeed;

            if (_vars.timerSpeed > 0) {
                if (_vars.timerTick >= 20) {
                    _vars.timerSeconds += _vars.timerTick / 20;
                    _vars.timerTick %= 20;
                }
                if (_vars.timerSeconds >= 60) {
                    _vars.timerMinutes += _vars.timerSeconds / 60;
                    _vars.timerSeconds %= 60;
                }
                if (_vars.timerMinutes >= 60) {
                    _vars.timerHours += _vars.timerMinutes / 60;
                    _vars.timerMinutes %= 60;
                }
            } 
            else if (_vars.timerSpeed < 0) {
                if (_vars.timerTick < 0) {
                    if (_vars.timerSeconds > 0 || _vars.timerMinutes > 0 || _vars.timerHours > 0) {
                        _vars.timerSeconds--;
                        _vars.timerTick += 20;
                    } else {
                        _vars.timerTick = 0;
                    }
                }

                if (_vars.timerSeconds < 0) {
                    if (_vars.timerMinutes > 0 || _vars.timerHours > 0) {
                        _vars.timerMinutes--;
                        _vars.timerSeconds += 60;
                    } else {
                        _vars.timerSeconds = 0;
                    }
                }

                if (_vars.timerMinutes < 0) {
                    if (_vars.timerHours > 0) {
                        _vars.timerHours--;
                        _vars.timerMinutes += 60;
                    } else {
                        _vars.timerMinutes = 0;
                    }
                }
            }

            _vars.markSyncDirty();

            if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame && MinigamesModVariables.MapVariables.get(world).inGracePeriod) {
                if (_vars.timerSeconds % 2 == 0 && _vars.timerSeconds < 11 && _vars.timerMinutes == 0 && _vars.timerHours == 0) {
                    
                    if (_vars.timerTick == 0 && entity.level() instanceof ServerLevel _level && _level.getServer() != null) {
                        _level.getServer().getCommands().performPrefixedCommand(
                            new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), _level,
                                LevelBasedPermissionSet.OWNER, entity.getName().getString(), entity.getDisplayName(), _level.getServer(), entity), 
                            "playsound minigames:clock player @s ~ ~ ~ 1 1"
                        );
                    }

                    _vars.TimerColor = "FF3200";
                } else {
                    _vars.TimerColor = "E0E0E0";
                }
                _vars.markSyncDirty();
            }
        }
    }
}