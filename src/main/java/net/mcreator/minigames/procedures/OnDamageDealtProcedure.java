package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class OnDamageDealtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, damagesource, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		double damage = 0;
		Entity target = null;
		ArrayList<Object> entitiesinrange = new ArrayList<>();
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.BLESSED) && !damagesource.is(TagKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minecraft:bypasses_cooldown")))) {
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount(0);
			}
		} else if (MinigamesModVariables.MapVariables.get(world).playingDungeons && (entity instanceof Player || entity instanceof ServerPlayer) && (sourceentity instanceof Player || sourceentity instanceof ServerPlayer)) {
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount(0);
			}
		} else {
			damage = amount * (1 + (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("forged", 0) * 0.01
					+ (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("glitched", 0) * 0.01);
			if (sourceentity instanceof LivingEntity _livEnt10 && _livEnt10.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST)) {
				damage = damage + damage * (((sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_BOOST).getAmplifier() : 0) + 1) / 100d);
			}
			if (IsBackstabProcedure.execute(sourceentity, entity) && CanBackstabProcedure.execute(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.player.attack.crit")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.player.attack.crit")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				damage = damage * GetItemAttributeProcedure.execute(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY, "minigames:extra_damage");
				ApplyEffectProcedure.execute(entity, true, 1, GetItemAttributeProcedure.execute(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY, "minigames:effect_length"), "minigames:bleed");
			}
			if (!damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:decay_dmg")))) {
				if (entity.getPersistentData().getBooleanOr("parasiteScytheActive", false)) {
					entity.getPersistentData().putBoolean("parasiteScytheActive", false);
					damage = damage - damage * (GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PARASITE_SCYTHE.get()), "minigames:effect_potency") / 100);
					ApplyEffectProcedure.execute(entity, true, damage / (GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PARASITE_SCYTHE.get()), "minigames:effect_length") / 20),
							GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PARASITE_SCYTHE.get()), "minigames:effect_length"), "minigames:decay");
					damage = 0;
				}
			}
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount((float) damage);
			}
			if (!damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:zap")))) {
				if (CheckRelicProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.PLUG.get()))) {
					entitiesinrange.clear();
					for (Entity entityiterator : world.getEntities(entity,
							new AABB((x - GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range")),
									(y - GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range")),
									(z - GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range")),
									(x + GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range")),
									(y + GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range")),
									(z + GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:ability_range"))))) {
						if (!(entityiterator instanceof Player) && entityiterator instanceof AgeableMob && MinigamesModVariables.MapVariables.get(world).currentRoomID == entityiterator.getPersistentData().getDoubleOr("DataID", 0)) {
							entitiesinrange.add(entityiterator);
						}
					}
					if (!entitiesinrange.isEmpty()) {
						target = entitiesinrange.get(Mth.nextInt(RandomSource.create(), 0, (int) (entitiesinrange.size() - 1))) instanceof Entity _entity29 ? _entity29 : null;
						{
							Entity _ent = target;
							if (_ent.level() instanceof ServerLevel _serverLevel) {
								_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:zap")))),
										(float) (damage * (GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.PLUG.get()), "minigames:extra_damage") / 100)));
							}
						}
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"playsound minigames:zap player @a ~ ~ ~ 1 1.5");
						RenderBeamProcedure.execute(target, entity, 2, 10, Identifier.fromNamespaceAndPath("minigames", "textures/entities/zap.png"));
					}
				}
			}
		}
	}
}