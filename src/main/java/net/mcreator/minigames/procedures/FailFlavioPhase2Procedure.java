package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class FailFlavioPhase2Procedure {
	public static void execute(LevelAccessor world) {
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				Entity _ent = entityiterator;
				if (_ent.level() instanceof ServerLevel _serverLevel) {
					_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:self_damage")))),
							(float) (40 / ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) * 1.2)));
				}
			}
		}
	}
}