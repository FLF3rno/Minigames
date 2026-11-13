package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

public class IndividualUpgradePickedUpProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (itemstack.is(ItemTags.create(Identifier.parse("minigames:dungeon_shop")))) {
			if (MinigamesModItems.SLOT_UPGRADE.get() == itemstack.getItem()) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.playerSlots = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots + 1;
					_vars.markSyncDirty();
				}
			} else if (MinigamesModItems.EXTRA_DAMAGE_UPGRADE.get() == itemstack.getItem()) {
				if (entity instanceof LivingEntity _entity) {
					AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:extra_damage_shop"), 1.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
					if (!_entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(modifier.id())) {
						_entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(modifier);
					}
				}
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.experience_orb.pickup")), SoundSource.PLAYERS, (float) 0.9, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.experience_orb.pickup")), SoundSource.PLAYERS, (float) 0.9, 1, false);
				}
			}
		}
	}
}