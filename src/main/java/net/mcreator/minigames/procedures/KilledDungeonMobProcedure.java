package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class KilledDungeonMobProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		ItemStack itemstack = ItemStack.EMPTY;
		double rng = 0;
		double rngTarget = 0;
		if (entity.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon")))) {
			rng = Mth.nextInt(RandomSource.create(), 1, 100);
			rngTarget = 1;
			if (sourceentity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MobEffects.LUCK)) {
				rngTarget = rngTarget + (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.LUCK) ? _livEnt.getEffect(MobEffects.LUCK).getAmplifier() : 0) + 1;
			}
			if (sourceentity instanceof LivingEntity _livEnt4 && _livEnt4.hasEffect(MobEffects.UNLUCK)) {
				rngTarget = rngTarget - ((sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.UNLUCK) ? _livEnt.getEffect(MobEffects.UNLUCK).getAmplifier() : 0) + 1);
			}
			if (rng <= rngTarget) {
				if ((RandomClassProcedure.execute()).equals("warrior")) {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.WARRIOR_ITEM_PEDESTAL.get().defaultBlockState(), 3);
				} else if ((RandomClassProcedure.execute()).equals("support")) {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.SUPPORT_ITEM_PEDESTAL.get().defaultBlockState(), 3);
				} else if ((RandomClassProcedure.execute()).equals("thief")) {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.THIEF_ITEM_PEDESTAL.get().defaultBlockState(), 3);
				}
			}
			if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.THE_FINISHER.get()) {
				MinigamesMod.queueServerWork(1, () -> {
					if (sourceentity instanceof Player _player)
						_player.getCooldowns().addCooldown((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), 0);
				});
			}
		}
	}
}