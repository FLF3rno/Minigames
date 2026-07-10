package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModParticleTypes;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

import java.util.Comparator;

public class ExplodeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity OwnedBy, boolean DamagePlayers, boolean OwnerImmune, double ExplosionDamage, double ExplosionKnockback, double ExplosionSize, String ExplosionType) {
		if (OwnedBy == null || ExplosionType == null)
			return;
		Entity origin = null;
		boolean canDamagePlayers = false;
		boolean ownerImmune = false;
		Vec3 Dcoords = Vec3.ZERO;
		double damage = 0;
		double knockback = 0;
		double length = 0;
		double yOffset = 0;
		String type = "";
		damage = ExplosionDamage;
		knockback = ExplosionKnockback;
		canDamagePlayers = DamagePlayers;
		origin = OwnedBy;
		ownerImmune = OwnerImmune;
		type = ExplosionType;
		if ((type.substring(0, 1)).equals("$")) {
			type = type.substring(1);
		}
		if ((type).equals("normal")) {
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, (int) (ExplosionSize * 15), (ExplosionSize / 5), (ExplosionSize / 5), (ExplosionSize / 5), 0.1);
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.SMOKE, x, y, z, (int) (ExplosionSize * 15), (ExplosionSize / 5), (ExplosionSize / 5), (ExplosionSize / 5), 0.3);
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.WHITE_SMOKE, x, y, z, (int) (ExplosionSize * 15), (ExplosionSize / 5), (ExplosionSize / 5), (ExplosionSize / 5), 0.3);
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/playsound minecraft:entity.generic.explode player @a ~ ~ ~ 0.8 0.8");
		} else if ((type).equals("red")) {
			if (world instanceof ServerLevel _level)
				_level.sendParticles((SimpleParticleType) (MinigamesModParticleTypes.RED_EXPLOSION.get()), x, y, z, (int) (ExplosionSize * 10), (ExplosionSize / 5), (ExplosionSize / 5), (ExplosionSize / 5), 0.1);
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/playsound minigames:red_explosion player @a ~ ~ ~ 0.4 1");
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(ExplosionSize / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (origin == entityiterator) {
					if (!ownerImmune) {
						{
							Entity _ent = entityiterator;
							if (_ent.level() instanceof ServerLevel _serverLevel) {
								_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.EXPLOSION)),
										(float) (damage * (1 - Math.min(20,
												Math.max((entityiterator instanceof LivingEntity _livingEntity7 && _livingEntity7.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity7.getAttribute(Attributes.ARMOR).getValue() : 0) / 5,
														(entityiterator instanceof LivingEntity _livingEntity8 && _livingEntity8.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity8.getAttribute(Attributes.ARMOR).getValue() : 0)
																- (4 * damage) / ((entityiterator instanceof LivingEntity _livingEntity9 && _livingEntity9.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS)
																		? _livingEntity9.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()
																		: 0) + 8)))
												/ 25)));
							}
						}
					}
				} else if (entityiterator instanceof ServerPlayer || entityiterator instanceof Player) {
					if (canDamagePlayers) {
						{
							Entity _ent = entityiterator;
							if (_ent.level() instanceof ServerLevel _serverLevel) {
								_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.EXPLOSION)),
										(float) (damage * (1 - Math.min(20,
												Math.max((entityiterator instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity14.getAttribute(Attributes.ARMOR).getValue() : 0) / 5,
														(entityiterator instanceof LivingEntity _livingEntity15 && _livingEntity15.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity15.getAttribute(Attributes.ARMOR).getValue() : 0)
																- (4 * damage) / ((entityiterator instanceof LivingEntity _livingEntity16 && _livingEntity16.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS)
																		? _livingEntity16.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()
																		: 0) + 8)))
												/ 25)));
							}
						}
					}
				} else {
					{
						Entity _ent = entityiterator;
						if (_ent.level() instanceof ServerLevel _serverLevel) {
							_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.EXPLOSION)),
									(float) (damage * (1 - Math.min(20,
											Math.max((entityiterator instanceof LivingEntity _livingEntity19 && _livingEntity19.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity19.getAttribute(Attributes.ARMOR).getValue() : 0) / 5,
													(entityiterator instanceof LivingEntity _livingEntity20 && _livingEntity20.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity20.getAttribute(Attributes.ARMOR).getValue() : 0)
															- (4 * damage) / ((entityiterator instanceof LivingEntity _livingEntity21 && _livingEntity21.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS)
																	? _livingEntity21.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()
																	: 0) + 8)))
											/ 25)));
						}
					}
				}
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(ExplosionSize / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				Dcoords = new Vec3((entityiterator.getX() - x), (entityiterator.getY() - y), (entityiterator.getZ() - z));
				length = Math.sqrt(Dcoords.x() * Dcoords.x() + Dcoords.y() * Dcoords.y() + Dcoords.z() * Dcoords.z());
				if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
					{
						MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.performKnockback = new Vec3(((Dcoords.x() / length) * ExplosionKnockback), ((Dcoords.y() / length) * ExplosionKnockback + 1), ((Dcoords.z() / length) * ExplosionKnockback));
						_vars.markSyncDirty();
					}
				} else {
					entityiterator.setDeltaMovement(new Vec3(((Dcoords.x() / length) * ExplosionKnockback), ((Dcoords.y() / length) * ExplosionKnockback + 1), ((Dcoords.z() / length) * ExplosionKnockback)));
				}
			}
		}
		if (CheckRelicProcedure.execute(OwnedBy, new ItemStack(MinigamesModItems.CLUSTER_BOMB.get())) && !(type.substring(0, 1)).equals("$")) {
			MinigamesMod.queueServerWork((int) GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(OwnedBy, new ItemStack(MinigamesModItems.CLUSTER_BOMB.get())), "minigames:ability_cooldown"), () -> {
				ExplodeProcedure.execute(world, x, y, z, OwnedBy, DamagePlayers, OwnerImmune,
						ExplosionDamage * (GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(OwnedBy, new ItemStack(MinigamesModItems.CLUSTER_BOMB.get())), "minigames:effect_potency") / 100),
						ExplosionKnockback * (GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(OwnedBy, new ItemStack(MinigamesModItems.CLUSTER_BOMB.get())), "minigames:effect_potency") / 100),
						ExplosionSize * (GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(OwnedBy, new ItemStack(MinigamesModItems.CLUSTER_BOMB.get())), "minigames:effect_potency") / 100), "$" + ExplosionType);
			});
		}
	}
}