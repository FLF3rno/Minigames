package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.photon_plugin.SummonFXBlockClient;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

public class ItemChipperEntityCollidesInTheBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double amount = 0;
		double wait = 0;
		Vec3 rng = Vec3.ZERO;
		if (entity instanceof Player) {
			if (!(entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MinigamesModMobEffects.ASCENDING))) {
				entity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), new Vec3(0.25, 0.05, 0.25));
				{
					Entity _ent = entity;
					if (_ent.level() instanceof ServerLevel _serverLevel) {
						_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 2);
					}
				}
			}
		}
		if (entity instanceof ItemEntity) {
			amount = GetItemAttributeProcedure.execute(entity instanceof ItemEntity _itemEnt ? _itemEnt.getItem() : ItemStack.EMPTY, "minigames:salvage_value");
			if (!entity.level().isClientSide())
				entity.discard();
			wait = 40;
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:grind_item")), SoundSource.BLOCKS, (float) 0.9, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:grind_item")), SoundSource.BLOCKS, (float) 0.9, 1, false);
				}
			}
			SummonFXBlockClient.create(x, y, z).name("grind").send();
			for (int index = 0; index < (int) (amount); index++) {
				MinigamesMod.queueServerWork((int) (Mth.nextInt(RandomSource.create(), 1, 55) + wait), () -> {
					SpawnCoinsFromTubeProcedure.execute(world);
				});
			}
		}
	}
}